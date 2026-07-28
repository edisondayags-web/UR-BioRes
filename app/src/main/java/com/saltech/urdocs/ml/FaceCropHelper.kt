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

object FaceCropHelper {

    private val detector by lazy {
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .build()
        FaceDetection.getClient(options)
    }

    suspend fun cropTo2x2(bitmap: Bitmap): Bitmap = suspendCancellableCoroutine { cont ->
        val image = InputImage.fromBitmap(bitmap, 0)
        detector.process(image)
            .addOnSuccessListener { faces ->
                val face = faces.firstOrNull()
                if (face == null) {
                    cont.resume(centerCropSquare(bitmap))
                    return@addOnSuccessListener
                }
                cont.resume(cropAroundFace(bitmap, face.boundingBox))
            }
            .addOnFailureListener { e -> cont.resumeWithException(e) }
    }

    suspend fun cropTo2x2WithFaceBox(bitmap: Bitmap): Pair<Bitmap, Rect> =
        suspendCancellableCoroutine { cont ->
            val image = InputImage.fromBitmap(bitmap, 0)
            detector.process(image)
                .addOnSuccessListener { faces ->
                    val face = faces.firstOrNull()
                    if (face == null) {
                        val cropped = centerCropSquare(bitmap)
                        val side = cropped.width
                        val estimated = Rect(
                            (side * 0.30f).toInt(),
                            (side * 0.15f).toInt(),
                            (side * 0.70f).toInt(),
                            (side * 0.55f).toInt()
                        )
                        cont.resume(cropped to estimated)
                        return@addOnSuccessListener
                    }
                    val faceBox = face.boundingBox
                    val cropRect = computeCropRect(bitmap, faceBox)
                    val cropped = Bitmap.createBitmap(
                        bitmap, cropRect.left, cropRect.top,
                        cropRect.width(), cropRect.height()
                    )
                    val relativeFaceBox = Rect(
                        faceBox.left - cropRect.left,
                        faceBox.top - cropRect.top,
                        faceBox.right - cropRect.left,
                        faceBox.bottom - cropRect.top
                    )
                    cont.resume(cropped to relativeFaceBox)
                }
                .addOnFailureListener { e -> cont.resumeWithException(e) }
        }

    private fun computeCropRect(bitmap: Bitmap, faceBox: Rect): Rect {
        val faceHeight = faceBox.height()
        val paddingTop = (faceHeight * 0.45f).toInt()
        val paddingSides = (faceHeight * 0.35f).toInt()
        val paddingBottom = (faceHeight * 0.55f).toInt()

        var left = faceBox.left - paddingSides
        var top = faceBox.top - paddingTop
        var right = faceBox.right + paddingSides
        var bottom = faceBox.bottom + paddingBottom

        val boxWidth = right - left
        val boxHeight = bottom - top
        val side = max(boxWidth, boxHeight)
        val centerX = (left + right) / 2
        val centerY = (top + bottom) / 2

        left = centerX - side / 2
        top = centerY - side / 2
        right = left + side
        bottom = top + side

        left = max(0, left)
        top = max(0, top)
        right = min(bitmap.width, right)
        bottom = min(bitmap.height, bottom)

        val finalSide = min(right - left, bottom - top)
        if (finalSide <= 0) {
            val fallbackSide = min(bitmap.width, bitmap.height)
            val x = (bitmap.width - fallbackSide) / 2
            val y = (bitmap.height - fallbackSide) / 2
            return Rect(x, y, x + fallbackSide, y + fallbackSide)
        }
        return Rect(left, top, left + finalSide, top + finalSide)
    }

    private fun cropAroundFace(bitmap: Bitmap, faceBox: Rect): Bitmap {
        val cropRect = computeCropRect(bitmap, faceBox)
        return Bitmap.createBitmap(
            bitmap, cropRect.left, cropRect.top,
            cropRect.width(), cropRect.height()
        )
    }

    private fun centerCropSquare(bitmap: Bitmap): Bitmap {
        val side = min(bitmap.width, bitmap.height)
        val x = (bitmap.width - side) / 2
        val y = (bitmap.height - side) / 2
        return Bitmap.createBitmap(bitmap, x, y, side, side)
    }

    fun addPoloOverlay(croppedBitmap: Bitmap, faceBoxInCropped: Rect): Bitmap {
        val result = croppedBitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = android.graphics.Canvas(result)

        val faceWidth = faceBoxInCropped.width().toFloat()
        val faceCenterX = faceBoxInCropped.left + faceWidth / 2f
        val neckY = faceBoxInCropped.bottom - faceWidth * 0.05f
        val shoulderY = faceBoxInCropped.bottom + faceWidth * 0.55f
        val shoulderHalfWidth = faceWidth * 1.35f
        val neckHalfWidth = faceWidth * 0.32f

        val bottomY = result.height.toFloat()

        val shirtPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.WHITE
            isAntiAlias = true
            style = android.graphics.Paint.Style.FILL
        }
        val shirtPath = android.graphics.Path().apply {
            moveTo(faceCenterX - neckHalfWidth, neckY)
            lineTo(faceCenterX - shoulderHalfWidth, shoulderY)
            lineTo(faceCenterX - shoulderHalfWidth, bottomY)
            lineTo(faceCenterX + shoulderHalfWidth, bottomY)
            lineTo(faceCenterX + shoulderHalfWidth, shoulderY)
            lineTo(faceCenterX + neckHalfWidth, neckY)
            close()
        }
        canvas.drawPath(shirtPath, shirtPaint)

        val collarPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.rgb(235, 235, 235)
            isAntiAlias = true
            style = android.graphics.Paint.Style.FILL
        }
        val collarDrop = faceWidth * 0.38f

        val leftCollar = android.graphics.Path().apply {
            moveTo(faceCenterX - neckHalfWidth, neckY)
            lineTo(faceCenterX, neckY + collarDrop)
            lineTo(faceCenterX - neckHalfWidth * 0.35f, neckY)
            close()
        }
        canvas.drawPath(leftCollar, collarPaint)

        val rightCollar = android.graphics.Path().apply {
            moveTo(faceCenterX + neckHalfWidth, neckY)
            lineTo(faceCenterX, neckY + collarDrop)
            lineTo(faceCenterX + neckHalfWidth * 0.35f, neckY)
            close()
        }
        canvas.drawPath(rightCollar, collarPaint)

        val outlinePaint = android.graphics.Paint().apply {
            color = android.graphics.Color.rgb(210, 210, 210)
            isAntiAlias = true
            style = android.graphics.Paint.Style.STROKE
            strokeWidth = faceWidth * 0.015f
        }
        canvas.drawPath(shirtPath, outlinePaint)

        return result
    }

    fun addFormalAttireOverlay(croppedBitmap: Bitmap, faceBoxInCropped: Rect): Bitmap {
        val result = croppedBitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = android.graphics.Canvas(result)

        val faceWidth = faceBoxInCropped.width().toFloat()
        val faceCenterX = faceBoxInCropped.left + faceWidth / 2f
        val neckY = faceBoxInCropped.bottom - faceWidth * 0.05f
        val shoulderY = faceBoxInCropped.bottom + faceWidth * 0.55f
        val shoulderHalfWidth = faceWidth * 1.35f
        val neckHalfWidth = faceWidth * 0.32f
        val bottomY = result.height.toFloat()

        val shirtPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.WHITE
            isAntiAlias = true
            style = android.graphics.Paint.Style.FILL
        }
        val shirtPath = android.graphics.Path().apply {
            moveTo(faceCenterX - neckHalfWidth, neckY)
            lineTo(faceCenterX - shoulderHalfWidth, shoulderY)
            lineTo(faceCenterX - shoulderHalfWidth, bottomY)
            lineTo(faceCenterX + shoulderHalfWidth, bottomY)
            lineTo(faceCenterX + shoulderHalfWidth, shoulderY)
            lineTo(faceCenterX + neckHalfWidth, neckY)
            close()
        }
        canvas.drawPath(shirtPath, shirtPaint)

        val blazerPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.rgb(28, 28, 32)
            isAntiAlias = true
            style = android.graphics.Paint.Style.FILL
        }
        val vDepth = faceWidth * 1.05f
        val lapelWidth = neckHalfWidth * 1.15f

        val leftBlazer = android.graphics.Path().apply {
            moveTo(faceCenterX - shoulderHalfWidth, shoulderY)
            lineTo(faceCenterX - shoulderHalfWidth, bottomY)
            lineTo(faceCenterX, bottomY)
            lineTo(faceCenterX, shoulderY + vDepth)
            lineTo(faceCenterX - lapelWidth, shoulderY + vDepth * 0.25f)
            close()
        }
        canvas.drawPath(leftBlazer, blazerPaint)

        val rightBlazer = android.graphics.Path().apply {
            moveTo(faceCenterX + shoulderHalfWidth, shoulderY)
            lineTo(faceCenterX + shoulderHalfWidth, bottomY)
            lineTo(faceCenterX, bottomY)
            lineTo(faceCenterX, shoulderY + vDepth)
            lineTo(faceCenterX + lapelWidth, shoulderY + vDepth * 0.25f)
            close()
        }
        canvas.drawPath(rightBlazer, blazerPaint)

        val collarPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.rgb(235, 235, 235)
            isAntiAlias = true
            style = android.graphics.Paint.Style.FILL
        }
        val collarDrop = faceWidth * 0.38f
        val leftCollar = android.graphics.Path().apply {
            moveTo(faceCenterX - neckHalfWidth, neckY)
            lineTo(faceCenterX, neckY + collarDrop)
            lineTo(faceCenterX - neckHalfWidth * 0.35f, neckY)
            close()
        }
        canvas.drawPath(leftCollar, collarPaint)

        val rightCollar = android.graphics.Path().apply {
            moveTo(faceCenterX + neckHalfWidth, neckY)
            lineTo(faceCenterX, neckY + collarDrop)
            lineTo(faceCenterX + neckHalfWidth * 0.35f, neckY)
            close()
        }
        canvas.drawPath(rightCollar, collarPaint)

        val outlinePaint = android.graphics.Paint().apply {
            color = android.graphics.Color.rgb(10, 10, 12)
            isAntiAlias = true
            style = android.graphics.Paint.Style.STROKE
            strokeWidth = faceWidth * 0.015f
        }
        canvas.drawPath(leftBlazer, outlinePaint)
        canvas.drawPath(rightBlazer, outlinePaint)

        return result
    }
}
