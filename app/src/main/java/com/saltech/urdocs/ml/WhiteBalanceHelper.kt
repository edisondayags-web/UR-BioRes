package com.saltech.urdocs.ml

import android.graphics.Bitmap

/**
 * Gray-World Auto White Balance -- ang pinakasimple at pinaka-ginagamit na
 * white balance algorithm sa totoong photo editing software (kasama sa
 * Photoshop "Auto Color", GIMP, atbp).
 *
 * Konsepto: sa isang normal/di-may-kulay-tint na litrato, ang AVERAGE ng
 * red, green, at blue channels ay dapat magkapareho (gray). Kung may color
 * cast (hal. sobrang dilaw dahil sa fluorescent light, o sobrang asul dahil
 * sa lilim), hindi magkapareho ang average -- kaya inaayos natin ang bawat
 * channel para magkapareho sila, na siyang nag-aalis ng color tint.
 */
object WhiteBalanceHelper {

    fun grayWorldCorrect(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        var sumR = 0L
        var sumG = 0L
        var sumB = 0L
        for (p in pixels) {
            sumR += (p shr 16) and 0xFF
            sumG += (p shr 8) and 0xFF
            sumB += p and 0xFF
        }

        val avgR = sumR.toDouble() / pixels.size
        val avgG = sumG.toDouble() / pixels.size
        val avgB = sumB.toDouble() / pixels.size

        // Ang gray target ay ang average ng tatlong channel -- ito ang
        // dapat maging pantay na "neutral gray" na antas.
        val gray = (avgR + avgG + avgB) / 3.0

        // Gain per channel: kung masyadong mataas ang isang channel
        // (hal. red dahil sa dilaw na tint), babawasan natin ang gain nito
        // papunta sa neutral. Naka-clamp para hindi sumobra ang correction.
        val gainR = (gray / avgR.coerceAtLeast(1.0)).coerceIn(0.7, 1.4)
        val gainG = (gray / avgG.coerceAtLeast(1.0)).coerceIn(0.7, 1.4)
        val gainB = (gray / avgB.coerceAtLeast(1.0)).coerceIn(0.7, 1.4)

        val result = IntArray(pixels.size)
        for (i in pixels.indices) {
            val p = pixels[i]
            val a = (p shr 24) and 0xFF
            val r = (((p shr 16) and 0xFF) * gainR).toInt().coerceIn(0, 255)
            val g = (((p shr 8) and 0xFF) * gainG).toInt().coerceIn(0, 255)
            val b = ((p and 0xFF) * gainB).toInt().coerceIn(0, 255)
            result[i] = (a shl 24) or (r shl 16) or (g shl 8) or b
        }

        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        output.setPixels(result, 0, width, 0, 0, width, height)
        return output
    }
}
