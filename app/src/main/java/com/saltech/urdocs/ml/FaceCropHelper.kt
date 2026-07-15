package com.saltech.urdocs.ml

import android.graphics.Bitmap
import android.graphics.Rect
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.max
import kotlin.math.min

/**
 * Gamit dito ang ML Kit Face Detection -- on-device, TFLite-based na model
 * ni Google (hindi mo na kailangan mag-bundle/mag-train ng sarili mong
 * .tflite file para makapag-detect ng mukha). Kapag kailangan mo talaga
 * ng custom TFLite model sa hinaharap (hal. specific na ID-photo scoring),
 * dito na lang dadagdag ng Interpreter-based loader.
 */
object FaceCropHelper {

    private val detector by lazy {
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .build()
        FaceDetection.getClient(options)
    }

    /**
     * Detects the primary face in [bitmap] and returns a bitmap cropped
     * to a square (2x2-style) framing centered on the face, with some
     * headroom above the head so it looks like a proper ID photo.
     */
    suspend fun cropTo2x2(bitmap: Bitmap): Bitmap = suspendCancellableCoroutine { cont ->
        val image = InputImage.fromBitmap(bitmap, 0)
        detector.process(image)
            .addOnSuccessListener { faces ->
                val face = faces.firstOrNull()
                if (face == null) {
                    // Walang na-detect na mukha -- ibalik na lang yung
                    // center-cropped square version bilang fallback.
                    cont.resume(centerCropSquare(bitmap))
                    return@addOnSuccessListener
                }
                cont.resume(cropAroundFace(bitmap, face.boundingBox))
            }
            .addOnFailureListener { e -> cont.resumeWithException(e) }
    }

    private fun cropAroundFace(bitmap: Bitmap, faceBox: Rect): Bitmap {
        // Idagdag ang padding: mas malaki sa taas (headroom), katamtaman sa gilid.
        val faceHeight = faceBox.height()
        val paddingTop = (faceHeight * 0.9f).toInt()
        val paddingSides = (faceHeight * 0.7f).toInt()
        val paddingBottom = (faceHeight * 1.1f).toInt()

        var left = faceBox.left - paddingSides
        var top = faceBox.top - paddingTop
        var right = faceBox.right + paddingSides
        var bottom = faceBox.bottom + paddingBottom

        // I-square ang crop box (2x2 ratio = 1:1).
        val boxWidth = right - left
        val boxHeight = bottom - top
        val side = max(boxWidth, boxHeight)
        val centerX = (left + right) / 2
        val centerY = (top + bottom) / 2

        left = centerX - side / 2
        top = centerY - side / 2
        right = left + side
        bottom = top + side

        // Clamp sa loob ng bitmap bounds.
        left = max(0, left)
        top = max(0, top)
        right = min(bitmap.width, right)
        bottom = min(bitmap.height, bottom)

        val finalSide = min(right - left, bottom - top)
        if (finalSide <= 0) return centerCropSquare(bitmap)

        return Bitmap.createBitmap(bitmap, left, top, finalSide, finalSide)
    }

    private fun centerCropSquare(bitmap: Bitmap): Bitmap {
        val side = min(bitmap.width, bitmap.height)
        val x = (bitmap.width - side) / 2
        val y = (bitmap.height - side) / 2
        return Bitmap.createBitmap(bitmap, x, y, side, side)
    }
}
