package com.saltech.urdocs.ml

import android.graphics.Bitmap
import android.graphics.Color
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions
import com.google.mlkit.vision.segmentation.Segmentation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

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

                    val rawConfidences = FloatArray(maskWidth * maskHeight)
                    buffer.rewind()
                    for (i in rawConfidences.indices) {
                        rawConfidences[i] = buffer.float
                    }

                    val confidences = blurConfidenceMask(rawConfidences, maskWidth, maskHeight, radius = 3)

                    for (y in 0 until bitmap.height) {
                        for (x in 0 until bitmap.width) {
                            val mx = (x * scaleX).toInt().coerceIn(0, maskWidth - 1)
                            val my = (y * scaleY).toInt().coerceIn(0, maskHeight - 1)
                            val confidence = confidences[my * maskWidth + mx]

                            val srcPixel = bitmap.getPixel(x, y)
                            val srcR = (srcPixel shr 16) and 0xFF
                            val srcG = (srcPixel shr 8) and 0xFF
                            val srcB = srcPixel and 0xFF

                            val alpha = sharpenAlpha(confidence)
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

    /** Itulak papuntang 0 o 1 ang gitnang confidence values; gradient lang sa totoong edge. */
    private fun sharpenAlpha(confidence: Float): Float {
        val t = ((confidence - 0.3f) / 0.4f).coerceIn(0f, 1f)
        return t * t * (3f - 2f * t)
    }

    private fun blurConfidenceMask(mask: FloatArray, width: Int, height: Int, radius: Int): FloatArray {
        val temp = FloatArray(width * height)
        val result = FloatArray(width * height)
        val windowSize = (radius * 2 + 1).toFloat()

        for (y in 0 until height) {
            var sum = 0f
            for (x in -radius..radius) {
                val xi = x.coerceIn(0, width - 1)
                sum += mask[y * width + xi]
            }
            for (x in 0 until width) {
                temp[y * width + x] = sum / windowSize
                val xOutIdx = (x - radius).coerceIn(0, width - 1)
                val xInIdx = (x + radius + 1).coerceIn(0, width - 1)
                sum += mask[y * width + xInIdx] - mask[y * width + xOutIdx]
            }
        }

        for (x in 0 until width) {
            var sum = 0f
            for (y in -radius..radius) {
                val yi = y.coerceIn(0, height - 1)
                sum += temp[yi * width + x]
            }
            for (y in 0 until height) {
                result[y * width + x] = sum / windowSize
                val yOutIdx = (y - radius).coerceIn(0, height - 1)
                val yInIdx = (y + radius + 1).coerceIn(0, height - 1)
                sum += temp[yInIdx * width + x] - temp[yOutIdx * width + x]
            }
        }
        return result
    }
}
