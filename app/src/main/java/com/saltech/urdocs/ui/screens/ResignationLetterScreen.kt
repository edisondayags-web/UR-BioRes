package com.saltech.urdocs.ui.screens
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

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
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

private val RLBlue = Color(0xFF1D3FB5)

private const val DEFAULT_BODY_TEXT =
    "I have truly valued the opportunities for growth and development\n" +
    "that I have gained during my time here. I am grateful for the support,\n" +
    "guidance, and encouragement I have received from you and the entire\n" +
    "team.\n\n" +
    "I will do my best to ensure a smooth transition by completing my\n" +
    "assigned tasks and assisting in the turnover process before my last day.\n\n" +
    "Thank you once again for the experience and for everything I have\n" +
    "learned during my tenure."

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun ResignationLetterScreen() {
    val paperWidthDp = 850.dp
    val paperHeightDp = 1600.dp
    val context = LocalContext.current
    var interstitialAd by remember { mutableStateOf<InterstitialAd?>(null) }
    LaunchedEffect(Unit) {
        InterstitialAd.load(
            context,
            "ca-app-pub-3134240485602899/5274307709",
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }
            }
        )
    }

    // ===== Per-field state (typing in one field only recomposes that field) =====
    var dateVal by remember { mutableStateOf("") }
    var to1Val by remember { mutableStateOf("") }
    var to2Val by remember { mutableStateOf("") }
    var positionVal by remember { mutableStateOf("") }
    var effectiveDateVal by remember { mutableStateOf("") }
    var nameVal by remember { mutableStateOf("") }
    var employeeIdVal by remember { mutableStateOf("") }
    var departmentVal by remember { mutableStateOf("") }
    var signatureVal by remember { mutableStateOf("") }

    var offset by remember { mutableStateOf(Offset.Zero) }
    val picture = remember { Picture() }
    val coroutineScope = rememberCoroutineScope()

    // Edit mode - lets user freely rewrite the body paragraph (add/remove lines, translate, etc.)
    var isEditMode by remember { mutableStateOf(false) }
    var bodyText by remember { mutableStateOf(DEFAULT_BODY_TEXT) }

    // Plain mode - toggles between the blue floral design and a plain white/black letter
    // (for strict offices/government that won't accept a "designed" letter)
    var isPlainMode by remember { mutableStateOf(false) }

    val textColor = if (isPlainMode) Color.Black else RLBlue
    val bodyFontFamily = if (isPlainMode) FontFamily.Default else FontFamily.Serif
    val bodyFontStyle = if (isPlainMode) FontStyle.Normal else FontStyle.Italic

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
                            if (isEditMode) {
                                // Skip Picture recording while typing - no lag. Recording only matters on Download.
                                this@onDrawWithContent.drawContent()
                            } else {
                                val pictureCanvas = androidx.compose.ui.graphics.Canvas(picture.beginRecording(w, h))
                                draw(this, this.layoutDirection, pictureCanvas, this.size) {
                                    this@onDrawWithContent.drawContent()
                                }
                                picture.endRecording()
                                drawIntoCanvas { canvas -> canvas.nativeCanvas.drawPicture(picture) }
                            }
                        }
                    }
                    .background(Color.White)
            ) {
                // Bond paper background (blue floral border) - only shown in design mode
                if (!isPlainMode) {
                    Image(
                        painter = painterResource(R.drawable.bond_paper_blank),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 70.dp, vertical = 55.dp)
                ) {
                    Spacer(Modifier.height(60.dp))

                    // Title - FROZEN, not editable, big first letter per word
                    if (isPlainMode) {
    // Plain mode - simple, uniform, formal heading (no decorative caps)
    Text(
        text = "RESIGNATION LETTER",
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        letterSpacing = 1.sp,
        color = textColor,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
} else {
    // Design mode - decorative small-caps style
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
        color = textColor,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
                    }

                    Spacer(Modifier.height(20.dp))

                    // ===== TOP FLEX SPACE - auto balances with bottom =====
                    Spacer(Modifier.weight(1f))

                    // Date
                    RLField("Date", dateVal, fieldWidth = 130.dp, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { dateVal = it }

                    Spacer(Modifier.height(35.dp))

                    RLPlainText("To,", color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())
                    RLPlainText("The Manager / Principal", color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())
                    Spacer(Modifier.height(10.dp))
                    RLUnderline(to1Val, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { to1Val = it }
                    Spacer(Modifier.height(10.dp))
                    RLUnderline(to2Val, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { to2Val = it }

                    Spacer(Modifier.height(16.dp))

                    // Subject line - bold label, normal rest
                    FlowRow(modifier = Modifier.fillMaxWidth()) {
                        RLWord("Subject:", bold = true, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())
                        RLWord("Resignation Letter", color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())
                    }

                    Spacer(Modifier.height(14.dp))

                    RLPlainText("Respected Sir/Madam,", color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())

                    Spacer(Modifier.height(14.dp))

                    // Flowing paragraph with two inline blanks - wraps like real text
                    FlowRow(modifier = Modifier.fillMaxWidth()) {
                        "I am writing to formally tender my resignation from my position"
                            .split(" ").forEach { RLWord(it, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) }
                    }

                    FlowRow(modifier = Modifier.fillMaxWidth()) {
                        RLWord("as", color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())
                        RLInlineField(positionVal, 180.dp, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { positionVal = it }
                        RLWord("effective", color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())
                        RLInlineField(effectiveDateVal, 180.dp, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { effectiveDateVal = it }
                        RLWord(".", color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())
                    }

                    FlowRow(modifier = Modifier.fillMaxWidth()) {
                        "Please accept this letter as my official notice of resignation in accordance"
                            .split(" ").forEach { RLWord(it, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) }
                    }

                    FlowRow(modifier = Modifier.fillMaxWidth()) {
                        "with the company's policy."
                            .split(" ").forEach { RLWord(it, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) }
                    }

                    Spacer(Modifier.height(16.dp))

                    // ===== EDITABLE BODY - user can freely rewrite, translate, add/remove lines =====
                    if (isEditMode) {
                        BasicTextField(
                            value = bodyText,
                            onValueChange = { bodyText = it },
                            textStyle = TextStyle(
                                fontFamily = bodyFontFamily,
                                fontStyle = bodyFontStyle,
                                fontWeight = FontWeight.Medium,
                                fontSize = 18.sp,
                                color = textColor,
                                textAlign = TextAlign.Justify,
                                lineHeight = 27.sp
                            ),
                            cursorBrush = SolidColor(textColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFEFF3FF))
                                .padding(8.dp)
                        )
                    } else {
                        RLParagraph(bodyText, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())
                    }

                    Spacer(Modifier.height(45.dp))

                    Box(modifier = Modifier.padding(start = 60.dp)) {
                        RLPlainText("Yours sincerely,", color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())
                    }

                    Spacer(Modifier.height(50.dp))

                    Column(modifier = Modifier.padding(start = 100.dp)) {
                        RLField("Name", nameVal, fieldWidth = 160.dp, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { nameVal = it }
                        Spacer(Modifier.height(8.dp))
                        RLField("Employee ID", employeeIdVal, fieldWidth = 130.dp, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { employeeIdVal = it }
                        Spacer(Modifier.height(8.dp))
                        RLField("Department", departmentVal, fieldWidth = 130.dp, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { departmentVal = it }
                        Spacer(Modifier.height(8.dp))
                        RLField("Signature", signatureVal, fieldWidth = 130.dp, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { signatureVal = it }
                    }

                    // ===== BOTTOM FLEX SPACE - same weight as top, keeps content centered =====
                    Spacer(Modifier.weight(1f))
                }
            }

            // Edit + Download + Plain buttons in a row
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { isEditMode = !isEditMode },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = RLBlue)
                ) {
                    Text(if (isEditMode) "Done" else "Edit", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Button(
                onClick = {
                    isEditMode = false // exit edit mode so no highlight box shows + Picture recording turns back on
                    scale = fitScale
                    offset = Offset.Zero
                    val activity = context as? android.app.Activity
                    fun proceedDownload() {
                        coroutineScope.launch {
                            delay(100)
                            withContext(Dispatchers.Default) {
                                tmap(
                                    picture.width.coerceAtLeast(1),
                                    picture.height.coerceAtLeast(1),
                                    Bitmap.Config.ARGB_8888
                                )
                                val canvas = android.graphics.Canvas(bitmap)
                                canvas.drawColor(android.graphics.Color.WHITE)
                                canvas.drawPicture(picture)
                                saveResignationToGallery(context, bitmap)
                            }
                        }
                    }
                    if (activity != null && interstitialAd != null) {
                        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                interstitialAd = null
                                proceedDownload()
                            }
                        }
                        interstitialAd?.show(activity)
                    } else {
                        proceedDownload()
                    }
                },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = RLBlue)
                ) {
                    Text("Download", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { isPlainMode = !isPlainMode },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = if (isPlainMode) Color.DarkGray else RLBlue
                    )
                ) {
                    Text(if (isPlainMode) "Design" else "Plain", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ---------- Reusable pieces ----------
// All accept optional color/fontFamily/italic so Plain mode can override the look
// without needing separate duplicate composables.

@Composable
private fun RLPlainText(
    text: String,
    bold: Boolean = false,
    color: Color = RLBlue,
    fontFamily: FontFamily = FontFamily.Serif,
    italic: Boolean = true
) {
    Text(
        text,
        fontFamily = fontFamily,
        fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
        fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
        fontSize = 16.sp,
        color = color,
        modifier = Modifier.padding(vertical = 2.dp)
    )
}

/** One paragraph that wraps naturally like the bond paper, with slight justification feel. */
@Composable
private fun RLParagraph(
    text: String,
    color: Color = RLBlue,
    fontFamily: FontFamily = FontFamily.Serif,
    italic: Boolean = true
) {
    Text(
        text,
        fontFamily = fontFamily,
        fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        color = color,
        textAlign = TextAlign.Justify,
        lineHeight = 27.sp,
        modifier = Modifier.fillMaxWidth()
    )
}

/** A single word rendered inside a FlowRow so it wraps like normal text. */
@Composable
private fun RLWord(
    word: String,
    bold: Boolean = false,
    color: Color = RLBlue,
    fontFamily: FontFamily = FontFamily.Serif,
    italic: Boolean = true
) {
    Text(
        "$word ",
        fontFamily = fontFamily,
        fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
        fontWeight = if (bold) FontWeight.Bold else FontWeight.Medium,
        fontSize = 18.sp,
        color = color
    )
}

/** Editable blank that sits inline inside a FlowRow, same size/spacing as the words around it. */
@Composable
private fun RLInlineField(
    value: String,
    width: androidx.compose.ui.unit.Dp,
    color: Color = RLBlue,
    fontFamily: FontFamily = FontFamily.Serif,
    italic: Boolean = true,
    onChange: (String) -> Unit
) {
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
                    drawLine(color, Offset(0f, y), Offset(size.width, y), strokeWidth = 1.dp.toPx())
                }
            }
    ) {
        BasicTextField(
            value = value, onValueChange = onChange,
            textStyle = TextStyle(
                fontFamily = fontFamily,
                fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
                fontSize = 20.sp,
                color = color
            ),
            cursorBrush = SolidColor(color),
            interactionSource = interactionSource,
            modifier = Modifier.widthIn(min = width)
        )
    }
}

/** Underline-only blank line (for "To," lines with no label). */
@Composable
private fun RLUnderline(
    value: String,
    color: Color = RLBlue,
    fontFamily: FontFamily = FontFamily.Serif,
    italic: Boolean = true,
    onChange: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    BasicTextField(
        value = value, onValueChange = onChange,
        textStyle = TextStyle(
            fontFamily = fontFamily,
            fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
            fontSize = 15.sp,
            color = color
        ),
        cursorBrush = SolidColor(color),
        interactionSource = interactionSource,
        modifier = Modifier
            .width(400.dp)
            .background(if (isFocused) Color(0xFFEFF3FF) else Color.Transparent)
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    val y = size.height - 2.dp.toPx()
                    drawLine(color, Offset(0f, y), Offset(size.width, y), strokeWidth = 1.dp.toPx())
                }
            }
    )
}

/** "Label: ___________" row like Date / Name / Employee ID / Department / Signature. */
@Composable
private fun RLField(
    label: String,
    value: String,
    fieldWidth: androidx.compose.ui.unit.Dp,
    color: Color = RLBlue,
    fontFamily: FontFamily = FontFamily.Serif,
    italic: Boolean = true,
    onChange: (String) -> Unit
) {
    Row(verticalAlignment = Alignment.Bottom) {
        RLPlainText("$label: ", bold = true, color = color, fontFamily = fontFamily, italic = italic)
        RLInlineField(value, fieldWidth, color = color, fontFamily = fontFamily, italic = italic, onChange = onChange)
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
