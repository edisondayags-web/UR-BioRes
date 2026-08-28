package com.saltech.urdocs.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.sin

/**
 * "More Templates" screen.
 * - Animated flowing gradient lines in the background (blue -> pink sweep)
 * - Package 1 / Package 2 pill selector with a glowing scan-line
 *   sweeping across whichever card is active
 *
 * NOTE: No resume template previews here on purpose — those are handled
 * elsewhere. This screen is purely the picker UI.
 */

private val NeonBlue = Color(0xFF3E7BFA)
private val NeonPink = Color(0xFFFF4FA3)
private val BgBlack = Color(0xFF05060A)

@Composable
fun MoreTemplatesScreen(
    onBack: () -> Unit,
    onPackageSelected: (Int) -> Unit
) {
    var selectedPackage by remember { mutableStateOf(1) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgBlack)
    ) {
        AnimatedBackgroundLines(modifier = Modifier.matchParentSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }

            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "More Templates ",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Text("💙", fontSize = 22.sp)
            }
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Choose your perfect resume template, Luv. 💙",
                color = Color(0xFFB0B0C0),
                fontSize = 14.sp
            )

            Spacer(Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                PackagePillCard(
                    label = "Package 1",
                    crownColor = NeonBlue,
                    glowColor = NeonBlue,
                    selected = selectedPackage == 1,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedPackage = 1
                        onPackageSelected(1)
                    }
                )
                PackagePillCard(
                    label = "Package 2",
                    crownColor = NeonPink,
                    glowColor = NeonPink,
                    selected = selectedPackage == 2,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedPackage = 2
                        onPackageSelected(2)
                    }
                )
            }

            // Rest of the screen (template list, etc.) is intentionally
            // left out here — plug in your existing templates UI below.
        }
    }
}

/**
 * A single "Package" pill card with a rounded glowing border and an
 * animated scan-line that sweeps top-to-bottom only when selected.
 */
@Composable
private fun PackagePillCard(
    label: String,
    crownColor: Color,
    glowColor: Color,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val infinite = rememberInfiniteTransition(label = "scan")
    val scanProgress by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scanProgress"
    )

    val borderAlpha by infinite.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "borderAlpha"
    )

    Box(
        modifier = modifier
            .height(64.dp)
            .clip(RoundedCornerShape(50))
            .background(Color(0xFF0B0D14))
            .border(
                width = 1.5.dp,
                color = glowColor.copy(alpha = if (selected) borderAlpha else 0.35f),
                shape = RoundedCornerShape(50)
            )
            .clickable { onClick() }
    ) {
        // Scan-line glow, clipped to the pill shape
        if (selected) {
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(50))
            ) {
                val bandWidth = size.width * 0.35f
                val x = (size.width + bandWidth) * scanProgress - bandWidth
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            glowColor.copy(alpha = 0.35f),
                            Color.Transparent
                        ),
                        startX = x,
                        endX = x + bandWidth
                    ),
                    topLeft = Offset.Zero,
                    size = Size(size.width, size.height)
                )
            }
        }

        Row(
            modifier = Modifier
                .matchParentSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("👑", fontSize = 18.sp)
            Spacer(Modifier.width(8.dp))
            Text(
                text = label,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

/**
 * Flowing gradient "wave" lines drifting across the background,
 * blue on the left sweeping into pink on the right — matching the
 * dark hero background used across the app's intro/format screens.
 */
@Composable
private fun AnimatedBackgroundLines(modifier: Modifier = Modifier) {
    val infinite = rememberInfiniteTransition(label = "bgLines")
    val phase by infinite.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 9000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(modifier = modifier) {
        val lineCount = 5
        val amplitude = size.height * 0.05f

        for (i in 0 until lineCount) {
            val yBase = size.height * (0.08f + i * 0.05f)
            val colorMix = i / (lineCount - 1f)
            val lineColor = lerpColor(NeonBlue, NeonPink, colorMix).copy(alpha = 0.5f)

            val path = androidx.compose.ui.graphics.Path()
            val steps = 60
            for (s in 0..steps) {
                val t = s / steps.toFloat()
                val x = t * size.width
                val y = yBase + amplitude * sin(phase + t * 4f + i)
                if (s == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }

            drawPath(
                path = path,
                color = lineColor,
                style = Stroke(width = 2.dp.toPx())
            )
        }
    }
}

private fun lerpColor(start: Color, end: Color, fraction: Float): Color {
    return Color(
        red = start.red + (end.red - start.red) * fraction,
        green = start.green + (end.green - start.green) * fraction,
        blue = start.blue + (end.blue - start.blue) * fraction,
        alpha = 1f
    )
}
