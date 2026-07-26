package com.saltech.urdocs.ui.components

import android.graphics.Paint
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp

private val MatrixGreen = Color(0xFF39FF6A)
private const val MATRIX_CHARS = "01ABCDEFGHIJKLMNOPQRSTUVWXYZ\$%#@*+-/\\<>"

@Composable
fun MatrixRainBackground(
    modifier: Modifier = Modifier,
    color: Color = MatrixGreen,
    alpha: Float = 0.35f
) {
    val transition = rememberInfiniteTransition(label = "matrixRain")
    val time by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing)
        ),
        label = "matrixTime"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val fontSizePx = 14.dp.toPx()
        val columnWidth = fontSizePx
        val columns = (size.width / columnWidth).toInt() + 1

        val paint = Paint().apply {
            this.color = color.copy(alpha = alpha).toArgb()
            textSize = fontSizePx
            isAntiAlias = true
        }

        drawContext.canvas.nativeCanvas.apply {
            for (col in 0 until columns) {
                val seed = col * 9973
                val speed = 40f + (seed % 60)
                val startOffset = (seed % 2000).toFloat()
                val colHeight = size.height + fontSizePx * 20
                val y = ((time * speed + startOffset) % colHeight) - fontSizePx * 20

                val x = col * columnWidth
                for (i in 0 until 18) {
                    val charY = y - i * fontSizePx
                    if (charY in -fontSizePx..size.height) {
                        val ch = MATRIX_CHARS[(seed + i * 31) % MATRIX_CHARS.length]
                        val fadeAlpha = (alpha * (1f - i / 18f)).coerceIn(0f, alpha)
                        paint.alpha = (fadeAlpha * 255).toInt().coerceIn(0, 255)
                        drawText(ch.toString(), x, charY, paint)
                    }
                }
            }
        }
    }
}
