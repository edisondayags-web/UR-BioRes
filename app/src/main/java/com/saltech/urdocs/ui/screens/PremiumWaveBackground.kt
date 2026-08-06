package com.saltech.urdocs.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.sin

@Composable
fun PremiumWaveBackground(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave_anim")

    // Mabagal at tuloy-tuloy na pagdaloy ng wave (12 seconds bawat cycle)
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(modifier = modifier.fillMaxSize().background(Color.Black)) {
        val w = size.width
        val h = size.height

        // 1. Kumukutitap na maliliit na Particles (Tech Dust)
        val particleCount = 30
        for (i in 0 until particleCount) {
            val px = (w * ((i * 37) % 100) / 100f)
            val py = (h * ((i * 53) % 100) / 100f)
            val alpha = 0.10f + 0.30f * sin(phase + i).coerceIn(0f, 1f)
            drawCircle(
                color = Color.White.copy(alpha = alpha),
                radius = if (i % 3 == 0) 2.5f else 1.5f,
                center = Offset(px, py)
            )
        }

        // 2. Top-Left Blue Laser Wave Lines (Katulad ng sa screenshot)
        for (lineIndex in 0..5) {
            val path = Path()
            val startY = h * 0.02f + (lineIndex * 22f)
            path.moveTo(0f, startY)

            val step = w / 20f
            for (x in 0..20) {
                val currentX = x * step
                val sineVal = sin((currentX / w * 2.5 * Math.PI) + phase + (lineIndex * 0.3)).toFloat()
                val currentY = startY + (sineVal * 25f) + (currentX * 0.45f)
                path.lineTo(currentX, currentY)
            }

            drawPath(
                path = path,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF2A5CE0).copy(alpha = 0.75f - lineIndex * 0.1f),
                        Color(0xFF00D2FF).copy(alpha = 0.40f - lineIndex * 0.06f),
                        Color.Transparent
                    )
                ),
                style = Stroke(width = 2.8f - lineIndex * 0.3f)
            )
        }

        // 3. Bottom-Right Red/Pink Laser Wave Lines
        for (lineIndex in 0..5) {
            val path = Path()
            val startY = h * 0.72f + (lineIndex * 22f)
            path.moveTo(w, startY)

            val step = w / 20f
            for (x in 20 downTo 0) {
                val currentX = x * step
                val sineVal = sin((currentX / w * 2.5 * Math.PI) - phase + (lineIndex * 0.3)).toFloat()
                val currentY = startY + (sineVal * 30f) - ((w - currentX) * 0.40f)
                path.lineTo(currentX, currentY)
            }

            drawPath(
                path = path,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color(0xFFE0245E).copy(alpha = 0.40f - lineIndex * 0.06f),
                        Color(0xFFFF3366).copy(alpha = 0.75f - lineIndex * 0.1f)
                    )
                ),
                style = Stroke(width = 2.8f - lineIndex * 0.3f)
            )
        }
    }
}
