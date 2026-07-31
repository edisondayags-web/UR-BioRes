package com.saltech.urdocs.ui.screens

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Picture
import android.os.Build
import android.provider.MediaStore
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
import com.saltech.urdocs.model.LetterType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val GLBlue = Color(0xFF1D3FB5)

/** Title, subject line, salutation and default body per letter type. */
private data class LetterContent(
    val title: String,
    val subject: String,
    val salutation: String,
    val defaultBody: String,
    val fileNamePrefix: String
)

private fun contentFor(type: LetterType): LetterContent = when (type) {
    LetterType.EXCUSE -> LetterContent(
        title = "EXCUSE LETTER",
        subject = "Excuse Letter",
        salutation = "Dear Sir/Madam,",
        defaultBody =
            "I am writing to formally explain my absence and to request your\n" +
            "kind understanding regarding this matter.\n\n" +
            "I sincerely apologize for any inconvenience this may have caused\n" +
            "and assure you that I will make up for any missed work or\n" +
            "responsibilities upon my return.\n\n" +
            "Thank you for your understanding and consideration.",
        fileNamePrefix = "Excuse"
    )
    LetterType.GOVT_SSS -> LetterContent(
        title = "SSS LETTER/REQUEST",
        subject = "SSS Request",
        salutation = "Dear Sir/Madam,",
        defaultBody =
            "I am writing to formally request assistance regarding my SSS\n" +
            "records/account, specifically for the concern stated below.\n\n" +
            "I would greatly appreciate your help in processing this request at\n" +
            "your earliest convenience. Please let me know if any additional\n" +
            "documents or information are needed.\n\n" +
            "Thank you for your time and assistance.",
        fileNamePrefix = "SSS"
    )
    LetterType.GOVT_PAGIBIG -> LetterContent(
        title = "PAG-IBIG LETTER/REQUEST",
        subject = "Pag-IBIG Request",
        salutation = "Dear Sir/Madam,",
        defaultBody =
            "I am writing to formally request assistance regarding my Pag-IBIG\n" +
            "membership/account, specifically for the concern stated below.\n\n" +
            "I would greatly appreciate your help in processing this request at\n" +
            "your earliest convenience. Please let me know if any additional\n" +
            "documents or information are needed.\n\n" +
            "Thank you for your time and assistance.",
        fileNamePrefix = "PagIBIG"
    )
    LetterType.APPLICATION -> LetterContent(
        title = "APPLICATION LETTER",
        subject = "Application",
        salutation = "Dear Sir/Madam,",
        defaultBody =
            "I am writing to formally apply for the position/opportunity stated\n" +
            "above. I believe that my skills and experience make me a strong\n" +
            "candidate, and I am eager to contribute to your organization.\n\n" +
            "I have attached my credentials for your review and would welcome\n" +
            "the opportunity to discuss my application further.\n\n" +
            "Thank you for considering my application.",
        fileNamePrefix = "Application"
    )
    LetterType.AUTHORIZATION -> LetterContent(
        title = "AUTHORIZATION LETTER",
        subject = "Authorization",
        salutation = "To Whom It May Concern,",
        defaultBody =
            "I am writing to authorize the person named below to act on my\n" +
            "behalf regarding the matter stated in this letter, in the event that\n" +
            "I am unable to attend or process it personally.\n\n" +
            "This authorization is valid for the purpose stated and I take full\n" +
            "responsibility for the actions taken on my behalf.\n\n" +
            "Thank you for your assistance in this matter.",
        fileNamePrefix = "Authorization"
    )
    LetterType.REFERRAL -> LetterContent(
        title = "REFERRAL LETTER",
        subject = "Referral",
        salutation = "Dear Sir/Madam,",
        defaultBody =
            "I am writing to formally recommend the person named below for the\n" +
            "opportunity/position stated in this letter.\n\n" +
            "Based on my experience working with them, I am confident that\n" +
            "they will be a valuable addition and will perform their\n" +
            "responsibilities well.\n\n" +
            "Please feel free to reach out should you need further information.",
        fileNamePrefix = "Referral"
    )
    LetterType.FOLLOW_UP -> LetterContent(
        title = "FOLLOW-UP LETTER",
        subject = "Follow-up",
        salutation = "Dear Sir/Madam,",
        defaultBody =
            "I am writing to kindly follow up on my previous request/application\n" +
            "regarding the matter stated above.\n\n" +
            "I understand that you may be busy, and I appreciate your time. I\n" +
            "would just like to check on the status and see if there is any\n" +
            "additional information needed from my end.\n\n" +
            "Thank you for your attention to this matter.",
        fileNamePrefix = "FollowUp"
    )
    LetterType.THANK_YOU -> LetterContent(
        title = "THANK YOU LETTER",
        subject = "Thank You",
        salutation = "Dear Sir/Madam,",
        defaultBody =
            "I am writing to express my sincere gratitude for the opportunity,\n" +
            "support, or assistance you have given me.\n\n" +
            "Your kindness and generosity have meant a lot to me, and I truly\n" +
            "appreciate everything you have done.\n\n" +
            "Thank you once again from the bottom of my heart.",
        fileNamePrefix = "ThankYou"
    )
    LetterType.JOB_OFFER -> LetterContent(
        title = "JOB OFFER LETTER",
        subject = "Job Offer",
        salutation = "Dear",
        defaultBody =
            "We are pleased to formally offer you the position stated above.\n" +
            "We were impressed by your qualifications and believe you will be\n" +
            "a great addition to our team.\n\n" +
            "Please review the details of this offer and let us know your\n" +
            "decision at your earliest convenience.\n\n" +
            "We look forward to having you on board.",
        fileNamePrefix = "JobOffer"
    )
    LetterType.SALARY_INCREASE -> LetterContent(
        title = "SALARY INCREASE REQUEST",
        subject = "Salary Increase Request",
        salutation = "Dear Sir/Madam,",
        defaultBody =
            "I am writing to formally request a review of my current salary,\n" +
            "given my contributions and responsibilities since I started this\n" +
            "position.\n\n" +
            "I believe this adjustment would fairly reflect my performance and\n" +
            "continued commitment to the company.\n\n" +
            "I would appreciate the opportunity to discuss this further at your\n" +
            "convenience.",
        fileNamePrefix = "SalaryIncrease"
    )
    LetterType.COMPLAINT -> LetterContent(
        title = "COMPLAINT LETTER",
        subject = "Complaint",
        salutation = "Dear Sir/Madam,",
        defaultBody =
            "I am writing to formally raise a concern regarding the matter\n" +
            "stated above.\n\n" +
            "I would appreciate it if this could be looked into and resolved at\n" +
            "the earliest possible time. Please let me know if you need any\n" +
            "further details from my end.\n\n" +
            "Thank you for your attention to this matter.",
        fileNamePrefix = "Complaint"
    )
    LetterType.BRGY_CITY_REQUEST -> LetterContent(
        title = "BARANGAY/CITY REQUEST LETTER",
        subject = "Request Letter",
        salutation = "Dear Sir/Madam,",
        defaultBody =
            "I am writing to formally request assistance regarding the matter\n" +
            "stated above, for our barangay/city.\n\n" +
            "I would greatly appreciate your kind consideration and support in\n" +
            "addressing this request at your earliest convenience.\n\n" +
            "Thank you for your time and assistance.",
        fileNamePrefix = "BrgyCity"
    )
    LetterType.SCHOLARSHIP -> LetterContent(
        title = "SCHOLARSHIP APPLICATION LETTER",
        subject = "Scholarship Application",
        salutation = "Dear Sir/Madam,",
        defaultBody =
            "I am writing to formally apply for the scholarship program stated\n" +
            "above. I believe that this opportunity would greatly help me\n" +
            "continue my studies and achieve my academic goals.\n\n" +
            "I have attached my credentials for your review and would be\n" +
            "grateful for the chance to be considered.\n\n" +
            "Thank you for your time and consideration.",
        fileNamePrefix = "Scholarship"
    )
    LetterType.OJT_INTERNSHIP -> LetterContent(
        title = "OJT/INTERNSHIP LETTER",
        subject = "OJT/Internship Application",
        salutation = "Dear Sir/Madam,",
        defaultBody =
            "I am writing to formally apply for an On-the-Job Training/Internship\n" +
            "opportunity at your company, as required by my course/program.\n\n" +
            "I am eager to apply my academic knowledge in a practical setting\n" +
            "and would welcome the opportunity to learn from your team.\n\n" +
            "Thank you for considering my application.",
        fileNamePrefix = "OJT"
    )
    LetterType.OTHERS_REQUEST -> LetterContent(
        title = "REQUEST LETTER",
        subject = "Request",
        salutation = "Dear Sir/Madam,",
        defaultBody =
            "I am writing to formally request your assistance regarding the\n" +
            "matter stated above.\n\n" +
            "I would greatly appreciate your kind consideration and support at\n" +
            "your earliest convenience.\n\n" +
            "Thank you for your time and assistance.",
        fileNamePrefix = "Request"
    )
    LetterType.CUSTOM -> LetterContent(
        title = "LETTER",
        subject = "Letter",
        salutation = "Dear Sir/Madam,",
        defaultBody =
            "I am writing to formally address the matter stated above.\n\n" +
            "Please feel free to edit this letter to fit your specific needs.\n\n" +
            "Thank you for your time and consideration.",
        fileNamePrefix = "Custom"
    )
    else -> LetterContent(
        title = "LETTER",
        subject = "Letter",
        salutation = "Dear Sir/Madam,",
        defaultBody = "I am writing to formally address the matter stated above.",
        fileNamePrefix = "Letter"
    )
}

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun GenericLetterScreen(letterType: LetterType) {
    val content = remember(letterType) { contentFor(letterType) }
    val paperWidthDp = 850.dp
    val paperHeightDp = 1600.dp
    val context = LocalContext.current

    var dateVal by remember { mutableStateOf("") }
    var to1Val by remember { mutableStateOf("") }
    var to2Val by remember { mutableStateOf("") }
    var nameVal by remember { mutableStateOf("") }
    var employeeIdVal by remember { mutableStateOf("") }
    var departmentVal by remember { mutableStateOf("") }
    var signatureVal by remember { mutableStateOf("") }

    var offset by remember { mutableStateOf(Offset.Zero) }
    val picture = remember { Picture() }
    val coroutineScope = rememberCoroutineScope()

    var isEditMode by remember { mutableStateOf(false) }
    var bodyText by remember(letterType) { mutableStateOf(content.defaultBody) }

    var isPlainMode by remember { mutableStateOf(false) }

    val textColor = if (isPlainMode) Color.Black else GLBlue
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

                    if (isPlainMode) {
                        Text(
                            text = content.title,
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
                                content.title.forEachIndexed { i, c ->
                                    val isFirstOfWord = i == 0 || content.title[i - 1] == ' '
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
                    Spacer(Modifier.weight(1f))

                    GLField("Date", dateVal, fieldWidth = 130.dp, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { dateVal = it }

                    Spacer(Modifier.height(35.dp))

                    GLPlainText("To,", color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())
                    Spacer(Modifier.height(10.dp))
                    GLUnderline(to1Val, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { to1Val = it }
                    Spacer(Modifier.height(10.dp))
                    GLUnderline(to2Val, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { to2Val = it }

                    Spacer(Modifier.height(16.dp))

                    FlowRow(modifier = Modifier.fillMaxWidth()) {
                        GLWord("Subject:", bold = true, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())
                        GLWord(content.subject, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())
                    }

                    Spacer(Modifier.height(14.dp))

                    GLPlainText(content.salutation, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())

                    Spacer(Modifier.height(16.dp))

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
                        GLParagraph(bodyText, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())
                    }

                    Spacer(Modifier.height(45.dp))

                    Box(modifier = Modifier.padding(start = 60.dp)) {
                        GLPlainText("Yours sincerely,", color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())
                    }

                    Spacer(Modifier.height(50.dp))

                    Column(modifier = Modifier.padding(start = 100.dp)) {
                        GLField("Name", nameVal, fieldWidth = 160.dp, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { nameVal = it }
                        Spacer(Modifier.height(8.dp))
                        GLField("Employee ID", employeeIdVal, fieldWidth = 130.dp, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { employeeIdVal = it }
                        Spacer(Modifier.height(8.dp))
                        GLField("Department", departmentVal, fieldWidth = 130.dp, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { departmentVal = it }
                        Spacer(Modifier.height(8.dp))
                        GLField("Signature", signatureVal, fieldWidth = 130.dp, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { signatureVal = it }
                    }

                    Spacer(Modifier.weight(1f))
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { isEditMode = !isEditMode },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = GLBlue)
                ) {
                    Text(if (isEditMode) "Done" else "Edit", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        isEditMode = false
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
                            saveGenericLetterToGallery(context, bitmap, content.fileNamePrefix)
                        }
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = GLBlue)
                ) {
                    Text("Download", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { isPlainMode = !isPlainMode },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = if (isPlainMode) Color.DarkGray else GLBlue
                    )
                ) {
                    Text(if (isPlainMode) "Design" else "Plain", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ---------- Reusable pieces (same pattern as ResignationLetterScreen/LeaveLetterScreen) ----------

@Composable
private fun GLPlainText(
    text: String,
    bold: Boolean = false,
    color: Color = GLBlue,
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
private fun GLParagraph(
    text: String,
    color: Color = GLBlue,
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
private fun GLWord(
    word: String,
    bold: Boolean = false,
    color: Color = GLBlue,
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
private fun GLInlineField(
    value: String,
    width: androidx.compose.ui.unit.Dp,
    color: Color = GLBlue,
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
private fun GLUnderline(
    value: String,
    color: Color = GLBlue,
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
private fun GLField(
    label: String,
    value: String,
    fieldWidth: androidx.compose.ui.unit.Dp,
    color: Color = GLBlue,
    fontFamily: FontFamily = FontFamily.Serif,
    italic: Boolean = true,
    onChange: (String) -> Unit
) {
    Row(verticalAlignment = Alignment.Bottom) {
        GLPlainText("$label: ", bold = true, color = color, fontFamily = fontFamily, italic = italic)
        GLInlineField(value, fieldWidth, color = color, fontFamily = fontFamily, italic = italic, onChange = onChange)
    }
}

private fun saveGenericLetterToGallery(context: android.content.Context, bitmap: Bitmap, prefix: String) {
    val filename = "${prefix}_${System.currentTimeMillis()}.png"
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
