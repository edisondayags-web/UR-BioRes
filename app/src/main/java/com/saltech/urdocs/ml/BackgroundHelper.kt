package com.saltech.urdocs.ml

import android.graphics.Bitmap
import android.graphics.Color
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions
import com.google.mlkit.vision.segmentation.Segmentation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * On-device selfie segmentation (TFLite-based, Google ML Kit) para palitan
 * ng puting background yung selfie -- karaniwang requirement sa 2x2 ID photo
 * dito sa Pilipinas (SSS, Pag-IBIG, atbp).
 *
 * Gumagamit ng SOFT alpha blend base sa confidence value (hindi hard cutoff)
 * para makinis ang gilid ng buhok/balikat -- katulad ng totoong studio backdrop,
 * hindi pixelated/jagged na edges.
 */
object BackgroundHelper {

    private val segmenter by lazy {
        val options = SelfieSegmenterOptions.Builder()
            .setDetectorMode(SelfieSegmenterOptions.SINGLE_IMAGE_MODE)
            .build()
        Segmentation.getClient(options)
    }

    suspend fun replaceWithWhiteBackground(bitmap: Bitmap): Bitmap =
        suspendCancellableCoroutine { cont ->
            val image = InputImage.fromBitmap(bitmap, 0)
            segmenter.process(image)
                .addOnSuccessListener { mask ->
                    val buffer = mask.buffer
                    val maskWidth = mask.width
                    val maskHeight = mask.height

                    val output = Bitmap.createBitmap(
                        bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888
                    )

                    val scaleX = maskWidth.toFloat() / bitmap.width
                    val scaleY = maskHeight.toFloat() / bitmap.height

                    // I-store muna lahat ng confidence sa array para magamit
                    // ulit sa feathering (soft edge) sa halip na hard 0.5 cutoff.
                    val confidences = FloatArray(maskWidth * maskHeight)
                    buffer.rewind()
                    for (i in confidences.indices) {
                        confidences[i] = buffer.float
                    }

                    for (y in 0 until bitmap.height) {
                        for (x in 0 until bitmap.width) {
                            val mx = (x * scaleX).toInt().coerceIn(0, maskWidth - 1)
                            val my = (y * scaleY).toInt().coerceIn(0, maskHeight - 1)
                            val confidence = confidences[my * maskWidth + mx]

                            val srcPixel = bitmap.getPixel(x, y)
                            val srcR = (srcPixel shr 16) and 0xFF
                            val srcG = (srcPixel shr 8) and 0xFF
                            val srcB = srcPixel and 0xFF

                            // Soft blend: kapag malapit sa 1.0 (tao) -- gamitin
                            // yung orihinal; kapag malapit sa 0.0 (bg) -- puti.
                            // Sa pagitan (edges ng buhok/balikat), i-blend nang
                            // makinis para walang jagged/pixelated na gilid.
                            val alpha = confidence.coerceIn(0f, 1f)
                            val r = (srcR * alpha + 255 * (1 - alpha)).toInt().coerceIn(0, 255)
                            val g = (srcG * alpha + 255 * (1 - alpha)).toInt().coerceIn(0, 255)
                            val b = (srcB * alpha + 255 * (1 - alpha)).toInt().coerceIn(0, 255)

                            output.setPixel(x, y, Color.rgb(r, g, b))
                        }
                    }
                    cont.resume(output)
                }
                .addOnFailureListener { e -> cont.resumeWithException(e) }
        }
}
