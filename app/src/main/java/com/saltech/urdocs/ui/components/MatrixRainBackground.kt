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
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min

private val MatrixGreen = Color(0xFFFFFFFF)
private const val CYCLE_CHARS = "&xv"

@Composable
fun MatrixRainBackground(
    modifier: Modifier = Modifier,
    color: Color = MatrixGreen,
    alpha: Float = 0.25f
) {
    val transition = rememberInfiniteTransition(label = "matrixCycle")
    val time by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000 * 1000, easing = LinearEasing)
        ),
        label = "matrixTime"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val fontSizePx = 8.dp.toPx()
        val cellSize = fontSizePx * 0.9f
        val cols = (size.width / cellSize).toInt() + 1
        val rows = (size.height / cellSize).toInt() + 1
        val centerX = cols / 2f
        val centerY = rows / 2f
        val maxDist = hypot(centerX, centerY)

        val paint = Paint().apply {
            this.color = color.toArgb()
            textSize = fontSizePx
            isAntiAlias = true
        }

        drawContext.canvas.nativeCanvas.apply {
            for (row in 0 until rows) {
                for (col in 0 until cols) {
                    val dist = hypot(col - centerX, row - centerY)
                    val delaySec = (dist / max(maxDist, 1f)) * 1.2f
                    val elapsed = time - delaySec
                    if (elapsed < 0) continue

                    val cyclePos = elapsed / 3f
                    val idx = cyclePos.toInt() % CYCLE_CHARS.length
                    val nextIdx = (idx + 1) % CYCLE_CHARS.length
                    val frac = cyclePos - cyclePos.toInt()

                    val fadeAlpha = if (frac < 0.5f) 1f - (frac * 2f) else (frac - 0.5f) * 2f
                    val ch = if (frac < 0.5f) CYCLE_CHARS[idx] else CYCLE_CHARS[nextIdx]

                    val entryFade = min(1f, elapsed)
                    val finalAlpha = (alpha * (0.4f + 0.6f * fadeAlpha) * entryFade).coerceIn(0f, alpha)
                    paint.alpha = (finalAlpha * 255).toInt().coerceIn(0, 255)

                    val x = col * cellSize
                    val y = row * cellSize
                    drawText(ch.toString(), x, y, paint)
                }
            }
        }
    }
}
