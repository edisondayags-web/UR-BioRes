package com.saltech.urdocs.ui.screens

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Picture
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saltech.urdocs.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalConfiguration

private val RLBlue = Color(0xFF1D3FB5)

data class ResignationFields(
    val date: String = "",
    val to1: String = "",
    val to2: String = "",
    val position: String = "",
    val effectiveDate: String = "",
    val name: String = "",
    val employeeId: String = "",
    val department: String = "",
    val signature: String = ""
)

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun ResignationLetterScreen() {
    val paperWidthDp = 850.dp
    val paperHeightDp = 1780.dp
    val context = LocalContext.current
    var data by remember { mutableStateOf(ResignationFields()) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val picture = remember { Picture() }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints(modifier = Modifier.weight(1f).background(Color.Black)) {
            val fitScale = minOf(maxWidth / paperWidthDp, maxHeight / paperHeightDp)
            var scale by remember { mutableStateOf(fitScale) }

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceIn(fitScale, 4f)
                            offset = if (scale <= fitScale) Offset.Zero else offset + pan
                        }
                    }
                    .graphicsLayer(
                        scaleX = scale, scaleY = scale,
                        translationX = offset.x, translationY = offset.y
                    )
                    .requiredWidth(paperWidthDp)
                    .requiredHeight(paperHeightDp)
                    .drawWithCache {
                        val w = size.width.toInt().coerceAtLeast(1)
                        val h = size.height.toInt().coerceAtLeast(1)
                        onDrawWithContent {
                            val pictureCanvas = androidx.compose.ui.graphics.Canvas(picture.beginRecording(w, h))
                            draw(this, this.layoutDirection, pictureCanvas, this.size) {
                                this@onDrawWithContent.drawContent()
                            }
                            picture.endRecording()
                            drawIntoCanvas { canvas -> canvas.nativeCanvas.drawPicture(picture) }
                        }
                    }
                    .background(Color.White)
            ) {
                // Bond paper background (flowers + border) — already in your drawable
                Image(
                    painter = painterResource(R.drawable.resignation_flowers),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 70.dp, vertical = 55.dp)
                ) {
                    // Title - big first letter per word, like a small-caps heading
                    Text(
                        buildAnnotatedString {
                            "RESIGNATION LETTER".forEachIndexed { i, c ->
                                val isFirstOfWord = i == 0 || "RESIGNATION LETTER"[i - 1] == ' '
                                withStyle(SpanStyle(fontSize = if (isFirstOfWord) 44.sp else 30.sp)) {
                                    append(c)
                                }
                            }
                        },
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = RLBlue,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(70.dp))

// Date
RLField("Date", data.date, fieldWidth = 130.dp) { data = data.copy(date = it) }

                    Spacer(Modifier.height(35.dp))

                    RLPlainText("To,")
                    RLPlainText("The Manager / Principal")
                    Spacer(Modifier.height(10.dp))
                    RLUnderline(data.to1) { data = data.copy(to1 = it) }
                    Spacer(Modifier.height(10.dp))
                    RLUnderline(data.to2) { data = data.copy(to2 = it) }

                    Spacer(Modifier.height(16.dp))

                    // Subject line - bold label, normal rest
                    FlowRow(modifier = Modifier.fillMaxWidth()) {
                        RLWord("Subject:", bold = true)
                        RLWord("Resignation Letter")
                    }

                    Spacer(Modifier.height(14.dp))

                    RLPlainText("Respected Sir/Madam,")

                    Spacer(Modifier.height(14.dp))

                    // Flowing paragraph with two inline blanks - wraps like real text
                    FlowRow(modifier = Modifier.fillMaxWidth()) {
    "I am writing to formally tender my resignation from my position"
        .split(" ").forEach { RLWord(it) }
}

FlowRow(modifier = Modifier.fillMaxWidth()) {
    RLWord("as")
    RLInlineField(data.position, 180.dp) { data = data.copy(position = it) }
    RLWord(", effective")
    RLInlineField(data.effectiveDate, 180.dp) { data = data.copy(effectiveDate = it) }
    RLWord(".")
}

FlowRow(modifier = Modifier.fillMaxWidth()) {
    "Please accept this letter as my official notice of resignation in accordance"
        .split(" ").forEach { RLWord(it) }
}

FlowRow(modifier = Modifier.fillMaxWidth()) {
    "with the company's policy."
        .split(" ").forEach { RLWord(it) }
}

                    Spacer(Modifier.height(16.dp))

                        RLParagraph(
    "I have truly valued the opportunities for growth and development\nthat I have gained during my time here. I am grateful for the support,\nguidance, and encouragement I have received from you and the entire\nteam."
)

                    Spacer(Modifier.height(14.dp))

                        RLParagraph(
    "I will do my best to ensure a smooth transition by completing my\nassigned tasks and assisting in the turnover process before my last day."
)

                    Spacer(Modifier.height(14.dp))

                    RLParagraph(
    "Thank you once again for the experience and for everything I have\nlearned during my tenure."
)

                    Spacer(Modifier.height(45.dp))

                  Spacer(Modifier.padding(start = 60.dp)) // hindi ito gagana mag-isa, tignan sa baba

Spacer(Modifier.height(30.dp))

Box(modifier = Modifier.padding(start = 60.dp)) {
    RLPlainText("Yours sincerely,")
}

Spacer(Modifier.height(50.dp))

Column(modifier = Modifier.padding(start = 100.dp)) {
    RLField("Name", data.name, fieldWidth = 160.dp) { data = data.copy(name = it) }
    Spacer(Modifier.height(8.dp))
    RLField("Employee ID", data.employeeId, fieldWidth = 130.dp) { data = data.copy(employeeId = it) }
    Spacer(Modifier.height(8.dp))
    RLField("Department", data.department, fieldWidth = 130.dp) { data = data.copy(department = it) }
    Spacer(Modifier.height(8.dp))
    RLField("Signature", data.signature, fieldWidth = 130.dp) { data = data.copy(signature = it) }
}
            }
            }

            Button(
                onClick = {
                    scale = fitScale
                    offset = Offset.Zero
                    coroutineScope.launch {
                        delay(100)
                        val bitmap = Bitmap.createBitmap(
                            picture.width.coerceAtLeast(1),
                            picture.height.coerceAtLeast(1),
                            Bitmap.Config.ARGB_8888
                        )
                        val canvas = android.graphics.Canvas(bitmap)
                        canvas.drawColor(android.graphics.Color.WHITE)
                        canvas.drawPicture(picture)
                        saveResignationToGallery(context, bitmap)
                    }
                },
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = RLBlue),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) { Text("Download", color = Color.White, fontWeight = FontWeight.Bold) }
        }
    }
}

// ---------- Reusable pieces ----------

@Composable
private fun RLPlainText(text: String, bold: Boolean = false) {
    Text(
        text,
        fontFamily = FontFamily.Serif,
        fontStyle = FontStyle.Italic,
        fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
        fontSize = 16.sp,
        color = RLBlue,
        modifier = Modifier.padding(vertical = 2.dp)
    )
}

/** One paragraph that wraps naturally like the bond paper, with slight justification feel. */
@Composable
private fun RLParagraph(text: String) {
    Text(
        text,
        fontFamily = FontFamily.Serif,
        fontStyle = FontStyle.Italic,
        fontSize = 18.sp,
        color = RLBlue,
        textAlign = TextAlign.Justify,
        lineHeight = 27.sp,
        modifier = Modifier.fillMaxWidth()
    )
}

/** A single word rendered inside a FlowRow so it wraps like normal text. */
@Composable
private fun RLWord(word: String, bold: Boolean = false) {
    Text(
        "$word ",
        fontFamily = FontFamily.Serif,
        fontStyle = FontStyle.Italic,
        fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
        fontSize = 18.sp,
        color = RLBlue
    )
}

/** Editable blank that sits inline inside a FlowRow, same size/spacing as the words around it. */
@Composable
private fun RLInlineField(value: String, width: androidx.compose.ui.unit.Dp, onChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    Box(
        modifier = Modifier
            .padding(end = 4.dp)
            .background(if (isFocused) Color(0xFFEFF3FF) else Color.Transparent)
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    val y = size.height - 2.dp.toPx()
                    drawLine(RLBlue, Offset(0f, y), Offset(size.width, y), strokeWidth = 1.dp.toPx())
                }
            }
    ) {
        BasicTextField(
            value = value, onValueChange = onChange,
            textStyle = TextStyle(fontFamily = FontFamily.Serif, fontStyle = FontStyle.Italic, fontSize = 20.sp, color = RLBlue),
            cursorBrush = SolidColor(RLBlue),
            interactionSource = interactionSource,
            modifier = Modifier.widthIn(min = width)
        )
    }
}

/** Underline-only blank line (for "To," lines with no label). */
@Composable
private fun RLUnderline(value: String, onChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    BasicTextField(
        value = value, onValueChange = onChange,
        textStyle = TextStyle(fontFamily = FontFamily.Serif, fontStyle = FontStyle.Italic, fontSize = 15.sp, color = RLBlue),
        cursorBrush = SolidColor(RLBlue),
        interactionSource = interactionSource,
        modifier = Modifier
            .width(400.dp)   // ✅ dati fillMaxWidth(), ngayon fixed
            .background(if (isFocused) Color(0xFFEFF3FF) else Color.Transparent)
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    val y = size.height - 2.dp.toPx()
                    drawLine(RLBlue, Offset(0f, y), Offset(size.width, y), strokeWidth = 1.dp.toPx())
                }
            }
    )
}

/** "Label: ___________" row like Date / Name / Employee ID / Department / Signature. */
@Composable
private fun RLField(label: String, value: String, fieldWidth: androidx.compose.ui.unit.Dp, onChange: (String) -> Unit) {
    Row(verticalAlignment = Alignment.Bottom) {
        RLPlainText("$label: ", bold = true)
        RLInlineField(value, fieldWidth, onChange)
    }
}

private fun saveResignationToGallery(context: android.content.Context, bitmap: Bitmap) {
    val filename = "Resignation_${System.currentTimeMillis()}.png"
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/URDocs")
        }
    }
    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    uri?.let {
        resolver.openOutputStream(it)?.use { out -> bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) }
        android.widget.Toast.makeText(context, "see your gallery luv🩵", android.widget.Toast.LENGTH_LONG).show()
    } ?: run {
        android.widget.Toast.makeText(context, "Hindi na-download, subukan ulit.", android.widget.Toast.LENGTH_LONG).show()
    }
}
