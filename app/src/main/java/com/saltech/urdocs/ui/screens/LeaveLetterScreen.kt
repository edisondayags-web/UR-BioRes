package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Picture
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.viewinterop.AndroidView

private val LLBlue = Color(0xFF1D3FB5)

private const val DEFAULT_LEAVE_BODY_TEXT =
    "I am writing to formally request a leave of absence from my duties,\n" +
    "for the reasons stated below. I kindly ask for your understanding and\n" +
    "approval of this request.\n\n" +
    "I have made necessary arrangements to ensure that my responsibilities\n" +
    "will be properly covered during my absence, and I will do my best to\n" +
    "catch up on any pending tasks upon my return.\n\n" +
    "I would greatly appreciate your approval of this request. Please let\n" +
    "me know if you need any additional information or documentation."

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun LeaveLetterScreen(onBack: () -> Unit = {}) {
    val paperWidthDp = 850.dp
    val paperHeightDp = 1250.dp
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
    var recipientTitleVal by remember { mutableStateOf("The Manager / Principal") }
    var salutationVal by remember { mutableStateOf("Dear Sir/Madam,") }
    var subjectVal by remember { mutableStateOf("Leave Request") }
    var closingVal by remember { mutableStateOf("Yours sincerely,") }
    var startDateVal by remember { mutableStateOf("") }
    var endDateVal by remember { mutableStateOf("") }
    var numDaysVal by remember { mutableStateOf("") }
    var nameVal by remember { mutableStateOf("") }
    var employeeIdVal by remember { mutableStateOf("") }
    var departmentVal by remember { mutableStateOf("") }
    var signatureVal by remember { mutableStateOf("") }

    var offset by remember { mutableStateOf(Offset.Zero) }
    val picture = remember { Picture() }
    val coroutineScope = rememberCoroutineScope()

    // Edit mode - lets user freely rewrite the body paragraph (add/remove lines, translate, etc.)
    var isEditMode by remember { mutableStateOf(false) }
    var bodyText by remember { mutableStateOf(DEFAULT_LEAVE_BODY_TEXT) }

    // Plain mode - toggles between blue floral design and plain white/black letter
    var isPlainMode by remember { mutableStateOf(false) }
    var selectedBorderIndex by remember { mutableStateOf(0) }
    var showTemplateSelector by remember { mutableStateOf(false) }

    val textColor = if (isPlainMode) Color.Black else LLBlue
    val bodyFontFamily = if (isPlainMode) FontFamily.Default else FontFamily.Serif
    val bodyFontStyle = if (isPlainMode) FontStyle.Normal else FontStyle.Italic

    Box(modifier = Modifier.fillMaxSize()) {
        PremiumWaveBackground()
        Column(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                AdView(ctx).apply {
                    val displayMetrics = ctx.resources.displayMetrics
                    val adWidthPixels = displayMetrics.widthPixels.toFloat()
                    val density = displayMetrics.density
                    val adWidth = (adWidthPixels / density).toInt()
                    setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(ctx, adWidth))
                    adUnitId = "ca-app-pub-3134240485602899/5923255956"
                    loadAd(AdRequest.Builder().build())
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        BoxWithConstraints(modifier = Modifier.weight(1f).background(Color.Black)) {
            val fitScale = minOf(maxWidth / paperWidthDp, maxHeight / paperHeightDp)
            var scale by remember { mutableStateOf(fitScale) }

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
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
                    val ctx = LocalContext.current
                    val borderResId = if (selectedBorderIndex == 0) {
                        R.drawable.bond_paper_blank
                    } else {
                        val name = "letter_border_" + selectedBorderIndex.toString().padStart(2, '0')
                        ctx.resources.getIdentifier(name, "drawable", ctx.packageName).let { if (it != 0) it else R.drawable.bond_paper_blank }
                    }
                    Image(
                        painter = painterResource(borderResId),
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

                    // Title - FROZEN, not editable
                    if (isPlainMode) {
                        Text(
                            text = "LEAVE LETTER",
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp,
                            letterSpacing = 1.sp,
                            color = textColor,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text(
                            buildAnnotatedString {
                                "LEAVE LETTER".forEachIndexed { i, c ->
                                    val isFirstOfWord = i == 0 || "LEAVE LETTER"[i - 1] == ' '
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
                    LLField("Date", dateVal, fieldWidth = 130.dp, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { dateVal = it }

                    Spacer(Modifier.height(35.dp))

                    LLPlainText("To,", color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())
                    LLEditableText(recipientTitleVal, { recipientTitleVal = it }, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())
                    Spacer(Modifier.height(10.dp))
                    LLUnderline(to1Val, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { to1Val = it }
                    Spacer(Modifier.height(10.dp))
                    LLUnderline(to2Val, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { to2Val = it }

                    Spacer(Modifier.height(16.dp))

                    // Subject line
                    FlowRow(modifier = Modifier.fillMaxWidth()) {
                        LLWord("Subject:", bold = true, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())
                        LLInlineField(subjectVal, 220.dp, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { subjectVal = it }
                    }

                    Spacer(Modifier.height(14.dp))

                    LLEditableText(salutationVal, { salutationVal = it }, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())

                    Spacer(Modifier.height(14.dp))

                    // Leave dates line with inline blanks - wraps like real text
                    FlowRow(modifier = Modifier.fillMaxWidth()) {
                        "I would like to request a leave of absence from"
                            .split(" ").forEach { LLWord(it, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) }
                    }

                    FlowRow(modifier = Modifier.fillMaxWidth()) {
                        LLInlineField(startDateVal, 140.dp, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { startDateVal = it }
                        LLWord("to", color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())
                        LLInlineField(endDateVal, 140.dp, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { endDateVal = it }
                        LLWord(", for a total of", color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())
                        LLInlineField(numDaysVal, 60.dp, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { numDaysVal = it }
                        LLWord("day(s).", color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())
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
                        LLParagraph(bodyText, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())
                    }

                    Spacer(Modifier.height(45.dp))

                    Box(modifier = Modifier.padding(start = 60.dp)) {
                        LLEditableText(closingVal, { closingVal = it }, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())
                    }

                    Spacer(Modifier.height(50.dp))

                    Column(modifier = Modifier.padding(start = 100.dp)) {
                        LLField("Name", nameVal, fieldWidth = 160.dp, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { nameVal = it }
                        Spacer(Modifier.height(8.dp))
                        LLField("Employee ID", employeeIdVal, fieldWidth = 130.dp, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { employeeIdVal = it }
                        Spacer(Modifier.height(8.dp))
                        LLField("Department", departmentVal, fieldWidth = 130.dp, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { departmentVal = it }
                        Spacer(Modifier.height(8.dp))
                        LLField("Signature", signatureVal, fieldWidth = 130.dp, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { signatureVal = it }
                    }

                    // ===== BOTTOM FLEX SPACE =====
                    Spacer(Modifier.weight(1f))
                }
            }

            // Edit + Download + Plain buttons in a row
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Button(
                    onClick = { isEditMode = !isEditMode },
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = LLBlue)
                ) {
                    Text(if (isEditMode) "Done" else "Edit", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

            Button(
                onClick = {
                    isEditMode = false
                    scale = fitScale
                    offset = Offset.Zero
                    val activity = context as? android.app.Activity
                    fun proceedDownload() {
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
                            saveLeaveLetterToGallery(context, bitmap)
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
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = LLBlue)
            ) {
                Icon(Icons.Filled.Download, contentDescription = "Download", tint = Color.White)
            }

                Button(
                    onClick = { showTemplateSelector = true },
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = LLBlue)
                ) {
                    Text("More Templates", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
                Button(
                    onClick = { isPlainMode = !isPlainMode },
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = if (isPlainMode) Color.DarkGray else LLBlue
                    )
                ) {
                    Text(if (isPlainMode) "Design" else "Plain", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
        if (showTemplateSelector) {
            LetterTemplateSelectorScreen(
                onTemplateSelected = { idx ->
                    selectedBorderIndex = idx
                    showTemplateSelector = false
                },
                onBack = { showTemplateSelector = false }
            )
        }
    }
}

// ---------- Reusable pieces (same pattern as ResignationLetterScreen) ----------

@Composable
private fun LLPlainText(
    text: String,
    bold: Boolean = false,
    color: Color = LLBlue,
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

@Composable
private fun LLEditableText(
    value: String,
    onChange: (String) -> Unit,
    bold: Boolean = false,
    color: Color = LLBlue,
    fontFamily: FontFamily = FontFamily.Serif,
    italic: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    BasicTextField(
        value = value, onValueChange = onChange,
        textStyle = TextStyle(
            fontFamily = fontFamily,
            fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            fontSize = 16.sp,
            color = color
        ),
        cursorBrush = SolidColor(color),
        interactionSource = interactionSource,
        modifier = Modifier
            .background(if (isFocused) Color(0xFFEFF3FF) else Color.Transparent)
            .padding(vertical = 2.dp)
    )
}

@Composable
private fun LLParagraph(
    text: String,
    color: Color = LLBlue,
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

@Composable
private fun LLWord(
    word: String,
    bold: Boolean = false,
    color: Color = LLBlue,
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

@Composable
private fun LLInlineField(
    value: String,
    width: androidx.compose.ui.unit.Dp,
    color: Color = LLBlue,
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

@Composable
private fun LLUnderline(
    value: String,
    color: Color = LLBlue,
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

@Composable
private fun LLField(
    label: String,
    value: String,
    fieldWidth: androidx.compose.ui.unit.Dp,
    color: Color = LLBlue,
    fontFamily: FontFamily = FontFamily.Serif,
    italic: Boolean = true,
    onChange: (String) -> Unit
) {
    Row(verticalAlignment = Alignment.Bottom) {
        LLPlainText("$label: ", bold = true, color = color, fontFamily = fontFamily, italic = italic)
        LLInlineField(value, fieldWidth, color = color, fontFamily = fontFamily, italic = italic, onChange = onChange)
    }
}

private fun saveLeaveLetterToGallery(context: android.content.Context, bitmap: Bitmap) {
    val filename = "Leave_${System.currentTimeMillis()}.png"
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
