path = "app/src/main/java/com/saltech/urdocs/ui/screens/LetterAssistantScreen.kt"
with open(path, "r", encoding="utf-8") as f:
    content = f.read()

old_imports = "import androidx.compose.ui.draw.clip\n"
new_imports = (
    "import androidx.compose.ui.draw.clip\n"
    "import androidx.compose.ui.draw.rotate\n"
    "import androidx.compose.ui.draw.scale\n"
    "import androidx.compose.foundation.Canvas\n"
    "import androidx.compose.ui.graphics.drawscope.Stroke\n"
    "import androidx.compose.ui.graphics.StrokeCap\n"
)
assert old_imports in content, "IMPORTS NOT FOUND"
content = content.replace(old_imports, new_imports, 1)

old_item = """            if (isTyping) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AssistantAvatar()
                        Spacer(Modifier.width(10.dp))
                        Box(
                            modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(UrBubbleDark).padding(14.dp)
                        ) {
                            TypingDots()
                        }
                    }
                }
            }"""

new_item = """            if (isTyping) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AssistantAvatar()
                        Spacer(Modifier.width(10.dp))
                        Box(
                            modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(UrBubbleDark).padding(14.dp)
                        ) {
                            ThinkingIndicator()
                        }
                    }
                }
            }"""

assert old_item in content, "OLD ITEM NOT FOUND"
content = content.replace(old_item, new_item, 1)

old_fun = """@Composable
private fun TypingDots() {"""

new_fun = """@Composable
private fun ThinkingIndicator() {
    val transition = rememberInfiniteTransition(label = "thinking")

    val ringRotation by transition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = androidx.compose.animation.core.tween(2200, easing = androidx.compose.animation.core.LinearEasing)
        ), label = "ringRotation"
    )

    val pulse by transition.animateFloat(
        initialValue = 0.85f, targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = androidx.compose.animation.core.tween(900, easing = androidx.compose.animation.core.FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "pulse"
    )

    val textAlpha by transition.animateFloat(
        initialValue = 0.35f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = androidx.compose.animation.core.tween(800),
            repeatMode = RepeatMode.Reverse
        ), label = "textAlpha"
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(22.dp)
                .background(Brush.verticalGradient(listOf(Color(0xFF0A3D1F), Color.Black)))
        )
        Spacer(Modifier.width(8.dp))
        Column {
            Text("Thinking...", color = UrGray.copy(alpha = textAlpha), fontSize = 12.sp)
            Spacer(Modifier.height(4.dp))
            TypingDots()
        }
        Spacer(Modifier.width(10.dp))
        Box(
            modifier = Modifier.size(30.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.matchParentSize().rotate(ringRotation)) {
                val stroke = 2.dp.toPx()
                drawArc(
                    color = UrPink,
                    startAngle = 0f, sweepAngle = 110f,
                    useCenter = false,
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
                drawArc(
                    color = UrGreen,
                    startAngle = 180f, sweepAngle = 110f,
                    useCenter = false,
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
            }
            Icon(
                Icons.Filled.Psychology,
                contentDescription = null,
                tint = UrGreen,
                modifier = Modifier.size(16.dp).scale(pulse)
            )
        }
    }
}

@Composable
private fun TypingDots() {"""

assert old_fun in content, "OLD FUN NOT FOUND"
content = content.replace(old_fun, new_fun, 1)

with open(path, "w", encoding="utf-8") as f:
    f.write(content)

print("DONE")
