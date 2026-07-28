package com.saltech.urdocs.ml

import android.graphics.Bitmap

/**
 * Unsharp Mask -- ang standard na sharpening technique sa totoong photo
 * editing (Photoshop, Lightroom). Konsepto: gumawa ng blurred version ng
 * litrato, tapos i-subtract sa orihinal para makuha ang "edges" (mataas na
 * detalye). Idagdag ulit ang edges na ito sa orihinal nang paulit-ulit
 * (amplified) para tumingkad ang mga detalye -- mata, kilay, gilid ng buhok.
 */
object SharpeningHelper {

    /**
     * @param radius laki ng blur window na gagamitin bilang batayan.
     *   2-4 ang magandang starting point para sa face/ID photos.
     * @param amount lakas ng sharpening. 0.3-0.6 ang natural na resulta;
     *   mas mataas dito ay posibleng magdulot ng "halo" artifact sa gilid.
     */
    fun unsharpMask(bitmap: Bitmap, radius: Int = 3, amount: Float = 0.4f): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        val blurred = boxBlur(pixels, width, height, radius)

        val result = IntArray(pixels.size)
        for (i in pixels.indices) {
            val orig = pixels[i]
            val a = (orig shr 24) and 0xFF
            val or_ = (orig shr 16) and 0xFF
            val og = (orig shr 8) and 0xFF
            val ob = orig and 0xFF

            val bp = blurred[i]
            val br = (bp shr 16) and 0xFF
            val bg = (bp shr 8) and 0xFF
            val bb = bp and 0xFF

            // orig + (orig - blurred) * amount -- pinatingkad ang edges
            val r = (or_ + (or_ - br) * amount).toInt().coerceIn(0, 255)
            val g = (og + (og - bg) * amount).toInt().coerceIn(0, 255)
            val b = (ob + (ob - bb) * amount).toInt().coerceIn(0, 255)

            result[i] = (a shl 24) or (r shl 16) or (g shl 8) or b
        }

        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        output.setPixels(result, 0, width, 0, 0, width, height)
        return output
    }

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
