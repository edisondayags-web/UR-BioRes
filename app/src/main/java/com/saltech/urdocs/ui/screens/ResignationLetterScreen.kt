package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

/**
 * ============================================================================
 * 100% code, walang kailangang image asset.
 * Yung mga flowers ay hand-drawn gamit Canvas (vector circles/petals) para
 * i-approximate yung blue watercolor flower clusters sa corners.
 * Font: swap LetterSerif sa isang Google Font (EB Garamond / Cormorant) para
 * mas eksaktong tugma sa small-caps drop-cap look ng "RESIGNATION LETTER".
 * ============================================================================
 */

private val LetterBlue = Color(0xFF1D3FB5)
private val LetterBlueDark = Color(0xFF16309C)
private val LeafGreen = Color(0xFF3E7D5A)
private val LeafGreenLight = Color(0xFF6FAE85)
private val FlowerWhite = Color(0xFFF3F6FB)
private val FlowerYellow = Color(0xFFE8C24A)

private val LetterSerif = FontFamily.Serif

@Composable
fun ResignationLetterScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            LetterBorderFrame {
                LetterContent()
            }
        }
    }
}

@Composable
private fun LetterBorderFrame(content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {

        // Double-line border + corner diamonds
        Canvas(modifier = Modifier.matchParentSize()) {
            val inset1 = 8.dp.toPx()
            val inset2 = 16.dp.toPx()

            drawRoundRect(
                color = LetterBlueDark,
                topLeft = Offset(inset1, inset1),
                size = Size(size.width - inset1 * 2, size.height - inset1 * 2),
                cornerRadius = CornerRadius(2.dp.toPx()),
                style = Stroke(width = 2.dp.toPx())
            )
            drawRoundRect(
                color = LetterBlueDark,
                topLeft = Offset(inset2, inset2),
                size = Size(size.width - inset2 * 2, size.height - inset2 * 2),
                cornerRadius = CornerRadius(2.dp.toPx()),
                style = Stroke(width = 1.dp.toPx())
            )

            // Small diamond ornaments at all 4 corners (on the outer line)
            val d = 5.dp.toPx()
            listOf(
                Offset(inset1, inset1),
                Offset(size.width - inset1, inset1),
                Offset(inset1, size.height - inset1),
                Offset(size.width - inset1, size.height - inset1)
            ).forEach { c ->
                drawCircle(color = LetterBlueDark, radius = d / 2.2f, center = c)
            }
        }

        // Flower clusters, top-right and bottom-left, drawn with vectors
        FlowerCluster(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(170.dp),
            mirrored = false
        )
        FlowerCluster(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(170.dp),
            mirrored = true
        )

        Box(modifier = Modifier.padding(36.dp)) {
            content()
        }
    }
}

/** Stylized blue/white watercolor-ish flower cluster with leaves, drawn on a Canvas. */
@Composable
private fun FlowerCluster(modifier: Modifier = Modifier, mirrored: Boolean) {
    Canvas(modifier = modifier) {
        rotate(if (mirrored) 180f else 0f) {
            val w = size.width
            val h = size.height

            // Leaves trailing down/along the corner
            val leafPositions = listOf(
                Offset(w * 0.15f, h * 0.55f) to 40f,
                Offset(w * 0.05f, h * 0.75f) to 20f,
                Offset(w * 0.25f, h * 0.85f) to 60f,
                Offset(w * 0.35f, h * 0.35f) to 100f,
                Offset(w * 0.55f, h * 0.15f) to 130f
            )
            leafPositions.forEach { (pos, angleDeg) ->
                drawLeaf(pos, 26f, angleDeg, LeafGreen)
                drawLeaf(pos + Offset(6f, 6f), 18f, angleDeg + 30f, LeafGreenLight)
            }

            // Flower 1 — big, top-right area
            drawFlower(center = Offset(w * 0.62f, h * 0.28f), radius = 34f, petalColor = LetterBlue, coreColor = FlowerYellow)
            // Flower 2 — white, slightly smaller, overlapping
            drawFlower(center = Offset(w * 0.40f, h * 0.18f), radius = 26f, petalColor = FlowerWhite, coreColor = FlowerYellow, outline = LetterBlue)
            // Flower 3 — small blue accent lower down
            drawFlower(center = Offset(w * 0.18f, h * 0.62f), radius = 20f, petalColor = LetterBlue, coreColor = FlowerYellow)
            // Small berries / buds
            listOf(
                Offset(w * 0.30f, h * 0.70f),
                Offset(w * 0.10f, h * 0.45f),
                Offset(w * 0.48f, h * 0.40f)
            ).forEach { p ->
                drawCircle(color = LetterBlue, radius = 7f, center = p)
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawFlower(
    center: Offset,
    radius: Float,
    petalColor: Color,
    coreColor: Color,
    outline: Color? = null
) {
    val petalCount = 6
    for (i in 0 until petalCount) {
        val angle = (360f / petalCount) * i
        val rad = Math.toRadians(angle.toDouble())
        val petalCenter = Offset(
            center.x + (radius * 0.65f) * cos(rad).toFloat(),
            center.y + (radius * 0.65f) * sin(rad).toFloat()
        )
        drawCircle(color = petalColor, radius = radius * 0.55f, center = petalCenter)
        if (outline != null) {
            drawCircle(
                color = outline,
                radius = radius * 0.55f,
                center = petalCenter,
                style = Stroke(width = 1.5f)
            )
        }
    }
    drawCircle(color = coreColor, radius = radius * 0.32f, center = center)
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawLeaf(
    tip: Offset,
    length: Float,
    angleDeg: Float,
    color: Color
) {
    val rad = Math.toRadians(angleDeg.toDouble())
    val base = Offset(
        tip.x - length * cos(rad).toFloat(),
        tip.y - length * sin(rad).toFloat()
    )
    val mid = Offset((tip.x + base.x) / 2, (tip.y + base.y) / 2)
    drawCircle(color = color, radius = length * 0.35f, center = mid)
}

@Composable
private fun LetterContent() {
    Column(modifier = Modifier.fillMaxWidth()) {

        DropCapTitle()
        Spacer(modifier = Modifier.height(6.dp))
        FlourishDivider()
        Spacer(modifier = Modifier.height(28.dp))

        BlankLineField(label = "Date:", minWidth = 150.dp)

        Spacer(modifier = Modifier.height(20.dp))
        LetterBodyText("To,")
        LetterBodyText("The Manager / Principal")
        Spacer(modifier = Modifier.height(6.dp))
        UnderlineOnlyField()
        UnderlineOnlyField()

        Spacer(modifier = Modifier.height(20.dp))
        SubjectLine()

        Spacer(modifier = Modifier.height(20.dp))
        LetterBodyText("Respected Sir/Madam,")

        Spacer(modifier = Modifier.height(18.dp))
        ResignationParagraphWithBlanks()

        Spacer(modifier = Modifier.height(16.dp))
        LetterBodyText(
            "I have truly valued the opportunities for growth and development " +
                "that I have gained during my time here. I am grateful for the support, " +
                "guidance, and encouragement I have received from you and the entire team."
        )

        Spacer(modifier = Modifier.height(16.dp))
        LetterBodyText(
            "I will do my best to ensure a smooth transition by completing my " +
                "assigned tasks and assisting in the turnover process before my last day."
        )

        Spacer(modifier = Modifier.height(16.dp))
        LetterBodyText(
            "Thank you once again for the experience and for everything I have learned during my tenure."
        )

        Spacer(modifier = Modifier.height(36.dp))
        LetterBodyText("Yours sincerely,")

        Spacer(modifier = Modifier.height(36.dp))
        BlankLineField(label = "Name:", minWidth = 170.dp)
        Spacer(modifier = Modifier.height(10.dp))
        BlankLineField(label = "Employee ID:", minWidth = 150.dp)
        Spacer(modifier = Modifier.height(10.dp))
        BlankLineField(label = "Department:", minWidth = 140.dp)
        Spacer(modifier = Modifier.height(10.dp))
        BlankLineField(label = "Signature:", minWidth = 150.dp)

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun DropCapTitle() {
    val title = buildAnnotatedString {
        withStyle(SpanStyle(fontSize = 36.sp, fontWeight = FontWeight.Bold)) { append("R") }
        withStyle(SpanStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)) {
            append("ESIGNATION ")
        }
        withStyle(SpanStyle(fontSize = 36.sp, fontWeight = FontWeight.Bold)) { append("L") }
        withStyle(SpanStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)) {
            append("ETTER")
        }
    }
    Text(
        text = title,
        color = LetterBlue,
        fontFamily = LetterSerif,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun FlourishDivider() {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Divider(color = LetterBlue, thickness = 1.dp, modifier = Modifier.weight(1f))
        Text("  ❖ ⟡ ❖  ", color = LetterBlue, fontSize = 15.sp, fontFamily = LetterSerif)
        Divider(color = LetterBlue, thickness = 1.dp, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun BlankLineField(label: String, minWidth: Dp) {
    var text by remember { mutableStateOf("") }
    Row(verticalAlignment = Alignment.Bottom) {
        Text(
            text = label,
            color = LetterBlue,
            fontFamily = LetterSerif,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        UnderlineTextField(value = text, onValueChange = { text = it }, minWidth = minWidth)
    }
}

@Composable
private fun UnderlineOnlyField() {
    var text by remember { mutableStateOf("") }
    UnderlineTextField(
        value = text,
        onValueChange = { text = it },
        minWidth = 260.dp,
        modifier = Modifier.padding(vertical = 6.dp)
    )
}

/**
 * The core "blank line" field:
 *  - EMPTY  -> reserves [minWidth] so it visually looks like a blank line.
 *  - TYPED  -> width collapses to exactly fit the typed text (no reserved
 *              space), so whatever comes right after it sits close, like a
 *              normal sentence — no more leftover gap after short words.
 * The underline is drawn in both cases.
 */
@Composable
private fun UnderlineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    minWidth: Dp,
    modifier: Modifier = Modifier
) {
    val widthModifier = if (value.isEmpty()) Modifier.width(minWidth) else Modifier
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = TextStyle(
            color = LetterBlue,
            fontFamily = LetterSerif,
            fontStyle = FontStyle.Italic,
            fontSize = 16.sp
        ),
        cursorBrush = androidx.compose.ui.graphics.SolidColor(LetterBlue),
        modifier = modifier
            .then(widthModifier)
            .drawBehind {
                // The underline itself — always drawn, empty or not.
                drawLine(
                    color = LetterBlue,
                    start = Offset(0f, size.height + 2.dp.toPx()),
                    end = Offset(size.width, size.height + 2.dp.toPx()),
                    strokeWidth = 1.dp.toPx()
                )
            }
            .padding(bottom = 2.dp)
    )
}

@Composable
private fun SubjectLine() {
    Row {
        Text("Subject: ", color = LetterBlue, fontFamily = LetterSerif, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic, fontSize = 16.sp)
        Text("Resignation Letter", color = LetterBlue, fontFamily = LetterSerif, fontStyle = FontStyle.Italic, fontSize = 16.sp)
    }
}

@Composable
private fun ResignationParagraphWithBlanks() {
    var position by remember { mutableStateOf("") }
    var effectiveDate by remember { mutableStateOf("") }

    Column {
        Text(
            "I am writing to formally tender my resignation from my position as",
            color = LetterBlue, fontFamily = LetterSerif, fontStyle = FontStyle.Italic, fontSize = 16.sp, lineHeight = 24.sp
        )
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxWidth()
        ) {
            UnderlineTextField(value = position, onValueChange = { position = it }, minWidth = 140.dp)
            Text(", effective ", color = LetterBlue, fontFamily = LetterSerif, fontStyle = FontStyle.Italic, fontSize = 16.sp)
            UnderlineTextField(value = effectiveDate, onValueChange = { effectiveDate = it }, minWidth = 140.dp)
            Text(".", color = LetterBlue, fontFamily = LetterSerif, fontStyle = FontStyle.Italic, fontSize = 16.sp)
        }
        Text(
            "Please accept this letter as my official notice of resignation in accordance with the company's policy.",
            color = LetterBlue, fontFamily = LetterSerif, fontStyle = FontStyle.Italic, fontSize = 16.sp, lineHeight = 24.sp
        )
    }
}

@Composable
private fun LetterBodyText(text: String) {
    Text(
        text = text,
        color = LetterBlue,
        fontFamily = LetterSerif,
        fontStyle = FontStyle.Italic,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        modifier = Modifier.fillMaxWidth()
    )
}
