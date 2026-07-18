package com.saltech.urdocs.ml

import android.graphics.Bitmap

/**
 * Ligtas, PUROng Kotlin/Android graphics na "auto levels" -- WALANG native
 * library (walang OpenCV), kaya WALANG panganib ng native crash. Ito ay
 * nag-a-auto-adjust ng brightness/contrast (histogram/contrast stretch --
 * totoong technique na ginagamit sa photo editing) para pantay ang ilaw,
 * nang hindi ginagalaw ang mukha o hugis.
 */
object SkinSmoothingHelper {
    fun studioClean(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        var minR = 255; var maxR = 0
        var minG = 255; var maxG = 0
        var minB = 255; var maxB = 0

        for (p in pixels) {
            val r = (p shr 16) and 0xFF
            val g = (p shr 8) and 0xFF
            val b = p and 0xFF
            if (r < minR) minR = r
            if (r > maxR) maxR = r
            if (g < minG) minG = g
            if (g > maxG) maxG = g
            if (b < minB) minB = b
            if (b > maxB) maxB = b
        }

        // 5% na "safety margin" para hindi masyadong sobra ang stretch
        val paddingR = ((maxR - minR) * 0.05f).toInt()
        val paddingG = ((maxG - minG) * 0.05f).toInt()
        val paddingB = ((maxB - minB) * 0.05f).toInt()
        val loR = (minR + paddingR).coerceIn(0, 255)
        val hiR = (maxR - paddingR).coerceIn(0, 255)
        val loG = (minG + paddingG).coerceIn(0, 255)
        val hiG = (maxG - paddingG).coerceIn(0, 255)
        val loB = (minB + paddingB).coerceIn(0, 255)
        val hiB = (maxB - paddingB).coerceIn(0, 255)

        val rangeR = (hiR - loR).coerceAtLeast(1)
        val rangeG = (hiG - loG).coerceAtLeast(1)
        val rangeB = (hiB - loB).coerceAtLeast(1)

        val result = IntArray(width * height)
        for (i in pixels.indices) {
            val p = pixels[i]
            val a = (p shr 24) and 0xFF
            var r = (p shr 16) and 0xFF
            var g = (p shr 8) and 0xFF
            var b = p and 0xFF

            r = (((r - loR) * 255) / rangeR).coerceIn(0, 255)
            g = (((g - loG) * 255) / rangeG).coerceIn(0, 255)
            b = (((b - loB) * 255) / rangeB).coerceIn(0, 255)

            result[i] = (a shl 24) or (r shl 16) or (g shl 8) or b
        }

        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        output.setPixels(result, 0, width, 0, 0, width, height)
        return output
    }
}
