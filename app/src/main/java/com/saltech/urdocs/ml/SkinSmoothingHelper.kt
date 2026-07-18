package com.saltech.urdocs.ml

import android.graphics.Bitmap

/**
 * Ligtas na "auto levels" base sa LUMINANCE (hindi hiwalay bawat kulay),
 * para hindi magkaroon ng color cast/tint. Totoong technique sa photo
 * editing -- global contrast/exposure stretch, hindi ginagalaw ang mukha.
 */
object SkinSmoothingHelper {
    fun studioClean(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        var minL = 255
        var maxL = 0
        for (p in pixels) {
            val r = (p shr 16) and 0xFF
            val g = (p shr 8) and 0xFF
            val b = p and 0xFF
            val l = (0.299 * r + 0.587 * g + 0.114 * b).toInt()
            if (l < minL) minL = l
            if (l > maxL) maxL = l
        }

        val padding = ((maxL - minL) * 0.05f).toInt()
        val lo = (minL + padding).coerceIn(0, 255)
        val hi = (maxL - padding).coerceIn(0, 255)
        val range = (hi - lo).coerceAtLeast(1)

        val result = IntArray(width * height)
        for (i in pixels.indices) {
            val p = pixels[i]
            val a = (p shr 24) and 0xFF
            var r = (p shr 16) and 0xFF
            var g = (p shr 8) and 0xFF
            var b = p and 0xFF

            // PAREHONG lo/hi ang gamit sa lahat ng channel -- kaya
            // hindi nababago ang balanse ng kulay, ilaw/contrast lang.
            r = (((r - lo) * 255) / range).coerceIn(0, 255)
            g = (((g - lo) * 255) / range).coerceIn(0, 255)
            b = (((b - lo) * 255) / range).coerceIn(0, 255)

            result[i] = (a shl 24) or (r shl 16) or (g shl 8) or b
        }

        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        output.setPixels(result, 0, width, 0, 0, width, height)
        return output
    }
}
