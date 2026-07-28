package com.saltech.urdocs.ml

import android.graphics.Bitmap

/**
 * Dalawang klase ng "studio" enhancement:
 *
 * 1. studioClean() -- luminance-based auto levels/contrast stretch.
 *    Hindi ito totoong skin smoothing, exposure/contrast lang.
 *
 * 2. frequencySeparationSmooth() -- TOTOONG skin smoothing technique na
 *    ginagamit ng mga propesyonal na retoucher: hinahati ang litrato sa
 *    LOW FREQUENCY (kulay/tone, blurred) at HIGH FREQUENCY (texture/detalye).
 *    Pinapantay ang blotches sa low frequency habang buo pa rin ang texture
 *    (pores, fine lines) sa high frequency -- kaya hindi "plastic" ang resulta.
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

            r = (((r - lo) * 255) / range).coerceIn(0, 255)
            g = (((g - lo) * 255) / range).coerceIn(0, 255)
            b = (((b - lo) * 255) / range).coerceIn(0, 255)

            result[i] = (a shl 24) or (r shl 16) or (g shl 8) or b
        }

        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        output.setPixels(result, 0, width, 0, 0, width, height)
        return output
    }

    /**
     * @param radius laki ng blur window (mas malaki = mas malutong ang blotches
     *   na naaalis, pero mas mabagal). 6-10 ang magandang starting point.
     * @param strength 0.0 = walang smoothing (parehas sa orihinal), 1.0 = pinaka-
     *   malakas na smoothing (posibleng masyadong makinis/plastic). 0.5-0.7 ang
     *   natural na resulta.
     */
    fun frequencySeparationSmooth(
        bitmap: Bitmap,
        radius: Int = 8,
        strength: Float = 0.6f
    ): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        // LOW FREQUENCY layer: box blur (3-pass box blur ~= Gaussian blur,
        // pero mas mabilis kalkulahin sa CPU).
        val low = boxBlur(pixels, width, height, radius)

        val result = IntArray(width * height)
        for (i in pixels.indices) {
            val orig = pixels[i]
            val a = (orig shr 24) and 0xFF
            val or_ = (orig shr 16) and 0xFF
            val og = (orig shr 8) and 0xFF
            val ob = orig and 0xFF

            val lp = low[i]
            val lr = (lp shr 16) and 0xFF
            val lg = (lp shr 8) and 0xFF
            val lb = lp and 0xFF

            // HIGH FREQUENCY (texture/detalye) = orig - low, centered sa 0
            val hr = or_ - lr
            val hg = og - lg
            val hb = ob - lb

            // Pagsasama: low frequency (pinantay na kulay/tone) + natitirang
            // detalye base sa 'strength'. Mas mataas ang strength, mas
            // maraming detalye ang na-a-absorb papunta sa smooth na base.
            val outR = (lr + hr * (1 - strength)).toInt().coerceIn(0, 255)
            val outG = (lg + hg * (1 - strength)).toInt().coerceIn(0, 255)
            val outB = (lb + hb * (1 - strength)).toInt().coerceIn(0, 255)

            result[i] = (a shl 24) or (outR shl 16) or (outG shl 8) or outB
        }

        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        output.setPixels(result, 0, width, 0, 0, width, height)
        return output
    }

    /** Two-pass (horizontal + vertical) box blur gamit ang sliding window sum -- O(n), mabilis. */
    private fun boxBlur(pixels: IntArray, width: Int, height: Int, radius: Int): IntArray {
        val temp = IntArray(width * height)
        val result = IntArray(width * height)
        val windowSize = radius * 2 + 1

        for (y in 0 until height) {
            var rSum = 0; var gSum = 0; var bSum = 0
            for (x in -radius..radius) {
                val xi = x.coerceIn(0, width - 1)
                val p = pixels[y * width + xi]
                rSum += (p shr 16) and 0xFF
                gSum += (p shr 8) and 0xFF
                bSum += p and 0xFF
            }
            for (x in 0 until width) {
                temp[y * width + x] = ((rSum / windowSize) shl 16) or ((gSum / windowSize) shl 8) or (bSum / windowSize)
                val xOutIdx = (x - radius).coerceIn(0, width - 1)
                val xInIdx = (x + radius + 1).coerceIn(0, width - 1)
                val pOut = pixels[y * width + xOutIdx]
                val pIn = pixels[y * width + xInIdx]
                rSum += ((pIn shr 16) and 0xFF) - ((pOut shr 16) and 0xFF)
                gSum += ((pIn shr 8) and 0xFF) - ((pOut shr 8) and 0xFF)
                bSum += (pIn and 0xFF) - (pOut and 0xFF)
            }
        }

        for (x in 0 until width) {
            var rSum = 0; var gSum = 0; var bSum = 0
            for (y in -radius..radius) {
                val yi = y.coerceIn(0, height - 1)
                val p = temp[yi * width + x]
                rSum += (p shr 16) and 0xFF
                gSum += (p shr 8) and 0xFF
                bSum += p and 0xFF
            }
            for (y in 0 until height) {
                result[y * width + x] = (0xFF shl 24) or ((rSum / windowSize) shl 16) or ((gSum / windowSize) shl 8) or (bSum / windowSize)
                val yOutIdx = (y - radius).coerceIn(0, height - 1)
                val yInIdx = (y + radius + 1).coerceIn(0, height - 1)
                val pOut = temp[yOutIdx * width + x]
                val pIn = temp[yInIdx * width + x]
                rSum += ((pIn shr 16) and 0xFF) - ((pOut shr 16) and 0xFF)
                gSum += ((pIn shr 8) and 0xFF) - ((pOut shr 8) and 0xFF)
                bSum += (pIn and 0xFF) - (pOut and 0xFF)
            }
        }
        return result
    }
}
