package com.saltech.urdocs.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saltech.urdocs.R
import kotlinx.coroutines.delay

private val PtBlue = Color(0xFF4C8DFF)
private val PtBlueDeep = Color(0xFF16255E)

/** Premium blue/black "thinking" indicator, ginagamit sa lahat ng chatbot screens. */
@Composable
fun PremiumThinkingIndicator(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "premiumThinking")

    val outerRotation by transition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(animation = tween(2600, easing = LinearEasing)),
        label = "outerRotation"
    )
    val innerRotation by transition.animateFloat(
        initialValue = 360f, targetValue = 0f,
        animationSpec = infiniteRepeatable(animation = tween(1800, easing = LinearEasing)),
        label = "innerRotation"
    )
    val pulse by transition.animateFloat(
        initialValue = 0.88f, targetValue = 1.12f,
        animationSpec = infiniteRepeatable(animation = tween(750, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "pulse"
    )
    val glow by transition.animateFloat(
        initialValue = 0.25f, targetValue = 0.65f,
        animationSpec = infiniteRepeatable(animation = tween(900, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "glow"
    )

    var dotCount by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(350)
            dotCount = (dotCount + 1) % 4
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .shadow(elevation = (glow * 20).dp, shape = CircleShape, ambientColor = PtBlue, spotColor = PtBlue),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.matchParentSize().rotate(outerRotation)) {
                val stroke = 2.2.dp.toPx()
                drawArc(color = PtBlue.copy(alpha = 0.9f), startAngle = 0f, sweepAngle = 130f, useCenter = false, style = Stroke(width = stroke, cap = StrokeCap.Round))
                drawArc(color = PtBlueDeep.copy(alpha = 0.6f), startAngle = 180f, sweepAngle = 130f, useCenter = false, style = Stroke(width = stroke, cap = StrokeCap.Round))
            }
            Canvas(modifier = Modifier.matchParentSize().padding(5.dp).rotate(innerRotation)) {
                val stroke = 1.4.dp.toPx()
                drawArc(color = PtBlue.copy(alpha = 0.5f), startAngle = 45f, sweepAngle = 90f, useCenter = false, style = Stroke(width = stroke, cap = StrokeCap.Round))
                drawArc(color = PtBlue.copy(alpha = 0.5f), startAngle = 225f, sweepAngle = 90f, useCenter = false, style = Stroke(width = stroke, cap = StrokeCap.Round))
            }
            Image(
                painter = painterResource(R.drawable.ic_brain_thinking),
                contentDescription = null,
                modifier = Modifier.size(18.dp).scale(pulse)
            )
        }
        Spacer(Modifier.width(10.dp))
        Text(
            text = "Thinking" + ".".repeat(dotCount),
            color = Color.White,
            fontSize = 13.sp
        )
    }
}
