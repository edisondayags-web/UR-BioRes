package com.saltech.urdocs.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface

/**
 * Ginagamit para i-load ang litrato na na-UPLOAD (galing sa Gallery), hindi
 * lang camera capture. Ito ang nagpapaayos din sa "pagiba" ng ilang uploaded
 * photos (EXIF rotation) para hindi paikot ang lumalabas.
 */
object ImageUtils {
    fun downscaleIfLarge(bitmap: android.graphics.Bitmap, maxDimension: Int): android.graphics.Bitmap {
        val w = bitmap.width
        val h = bitmap.height
        val largest = maxOf(w, h)
        if (largest <= maxDimension) return bitmap
        val scale = maxDimension.toFloat() / largest
        val newW = (w * scale).toInt()
        val newH = (h * scale).toInt()
        return android.graphics.Bitmap.createScaledBitmap(bitmap, newW, newH, true)
    }

    fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
        val resolver = context.contentResolver
        val bitmap = resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it) } ?: return null

        val rotationDegrees = try {
            resolver.openInputStream(uri)?.use { stream ->
                val exif = ExifInterface(stream)
                when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> 90
                    ExifInterface.ORIENTATION_ROTATE_180 -> 180
                    ExifInterface.ORIENTATION_ROTATE_270 -> 270
                    else -> 0
                }
            } ?: 0
        } catch (e: Exception) {
            0
        }

        return if (rotationDegrees != 0) {
            val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } else {
            bitmap
        }
    }
}
