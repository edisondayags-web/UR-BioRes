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

                    buffer.rewind()
                    for (y in 0 until maskHeight) {
                        for (x in 0 until maskWidth) {
                            // confidence na "foreground" (person) papalapit sa 1.0
                            val confidence = buffer.float
                            val srcPixel = bitmap.getPixel(x, y)
                            val pixel = if (confidence > 0.5f) {
                                srcPixel
                            } else {
                                Color.WHITE
                            }
                            output.setPixel(x, y, pixel)
                        }
                    }
                    cont.resume(output)
                }
                .addOnFailureListener { e -> cont.resumeWithException(e) }
        }
}
