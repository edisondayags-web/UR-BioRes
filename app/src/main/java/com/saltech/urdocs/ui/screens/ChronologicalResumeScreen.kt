package com.saltech.urdocs.ui.screens
import com.saltech.urdocs.util.SecureScreen

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Picture
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.saltech.urdocs.R
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString

/**
 * "Chronological" na Resume -- ATS-friendly single-header + two-column style.
 * JOB TITLE (malaki) sa taas, pangalan sa ibaba, contact row (phone | email | location | linkedin),
 * Professional Summary + Work Experience + Education sa kaliwa (mas malapad),
 * Soft Skills + Technical Skills + Languages + Interests sa kanan (mas makitid).
 * May prefilled guide text (editable) para hindi blangko ang preview.
 */
data class ChronoWorkEntry(
    val role: String = "",
    val company: String = "",
    val from: String = "",
    val to: String = "",
    val bullets: List<String> = List(3) { "" }
)

data class ChronologicalResumeFields(
    val name: String = "Fanny Cooper",
    val jobTitle: String = "Sales Representative",
    val phone: String = "123 456 7890",
    val email: String = "fannycooper@email.com",
    val location: String = "Chicago",
    val linkedin: String = "linkedin.com/in/fannycooper",
    val summary: String = "With a background in marketing and sales, and a Master's degree in Business Development, I bring a strong combination of field experience and client relationship management. Through two roles in direct sales, I have developed the ability to prospect, understand customer needs, negotiate effectively, and build long-term client loyalty. Autonomous, detail-oriented, and results-driven, I am eager to contribute to your company's commercial growth through a personalized and performance-focused approach.",
    val work: List<ChronoWorkEntry> = listOf(
        ChronoWorkEntry(
            role = "Sales Assistant",
            company = "Mod'Style Boutique, Chicago",
            from = "2019",
            to = "2021",
            bullets = listOf(
                "In a direct BtoC sales context, I adapted my prospecting and closing techniques to each customer profile, which allowed me to exceed 110% of monthly objectives for 10 consecutive months.",
                "Faced with a need to improve customer follow-up, I designed and structured a personalized reminder file, which generated a 30% increase in the revisit rate in 6 months.",
                "Responsible for integrating two new recruits, I trained them in sales techniques and product pitches, promoting their autonomy and rapid growth in skills."
            )
        ),
        ChronoWorkEntry(
            role = "Bartender",
            company = "Le Patio, Chicago",
            from = "2013",
            to = "2018",
            bullets = listOf(
                "Analyzed customer preferences by age group and tailored beverage recommendations accordingly, resulting in a 25% increase in sales.",
                "Built strong relationships with regular customers by creating authentic, personalized interactions, driving long-term loyalty and repeat business.",
                "Maintained fast, high-quality service during peak hours by effectively managing pressure and prioritizing tasks to ensure customer satisfaction."
            )
        )
    ),
    val eduDegree: String = "Bachelor Negotiation and Digitalization of Customer Relations",
    val eduSchool: String = "Lycée Gustave Eiffel – Chicago",
    val eduYear: String = "Class of 2018",
    val softSkills: List<String> = listOf(
        "Analytical thinking", "Adaptability", "Resilience under pressure",
        "Strong communication skills", "Problem-solving mindset", "Self-discipline and time management"
    ),
    val technicalSkills: List<String> = listOf("Google Analytics", "Google Ads", "Meta Ads Manager", "HubSpot"),
    val languages: List<String> = listOf("French: Full professional proficiency", "Spanish: Professional proficiency"),
    val interests: List<String> = listOf("Hiking – Helps me develop endurance, consistency, and perseverance, qualities that are essential in sales roles.")
)

@Composable
fun ChronologicalResumeScreen() {
    //SecureScreen()
    val paperWidthDp = 850.dp
    val paperHeightDp = 1100.dp

    var data by remember { mutableStateOf(ChronologicalResumeFields()) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var showPaymentDialog by remember { mutableStateOf(false) }
    var hasPaid by remember { mutableStateOf(false) }
    val picture = remember { Picture() }
    val coroutineScope = rememberCoroutineScope()

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

    Box(modifier = Modifier.fillMaxSize()) {
        PremiumWaveBackground()
        Column(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints(
            modifier = Modifier.weight(1f).background(Color.Transparent)
        ) {
        val fitScale = minOf(maxWidth / paperWidthDp, maxHeight / paperHeightDp)
            var scale by remember { mutableStateOf(fitScale) }

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
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
                .clipToBounds()
                .drawWithCache {
                    val width = this.size.width.toInt().coerceAtLeast(1)
                    val height = this.size.height.toInt().coerceAtLeast(1)
                    onDrawWithContent {
                        val pictureCanvas = androidx.compose.ui.graphics.Canvas(
                            picture.beginRecording(width, height)
                        )
                        draw(this, this.layoutDirection, pictureCanvas, this.size) {
                            this@onDrawWithContent.drawContent()
                        }
                        picture.endRecording()
                        drawIntoCanvas { canvas -> canvas.nativeCanvas.drawPicture(picture) }
                    }
                }
                .border(1.dp, Color.Black)
                .background(Color.White)
                .padding(28.dp)
              ) {
            Column(modifier = Modifier.fillMaxSize()) {

                // ===== HEADER (centered) — job title malaki sa taas, pangalan sa ibaba =====
                BasicTextFieldCentered(
                    value = data.jobTitle,
                    fontSize = 26.sp,
                    bold = true,
                    letterSpacing = 1.sp,
                    placeholder = "PROFESSIONAL TITLE",
                    onChange = { data = data.copy(jobTitle = it) }
                )
                Spacer(Modifier.height(4.dp))
                BasicTextFieldCentered(
                    value = data.name,
                    fontSize = 15.sp,
                    bold = false,
                    placeholder = "YOUR NAME",
                    onChange = { data = data.copy(name = it) }
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ContactFieldInline("📞", data.phone) { data = data.copy(phone = it) }
                    Text("   •   ", fontSize = 12.sp, color = Color.Black)
                    ContactFieldInline("📧", data.email) { data = data.copy(email = it) }
                    Text("   •   ", fontSize = 12.sp, color = Color.Black)
                    ContactFieldInline("📍", data.location) { data = data.copy(location = it) }
                    Text("   •   ", fontSize = 12.sp, color = Color.Black)
                    ContactFieldInline("🔗", data.linkedin) { data = data.copy(linkedin = it) }
                }
                Spacer(Modifier.height(10.dp))
                Spacer(Modifier.fillMaxWidth().height(1.dp).background(Color.Black))
                Spacer(Modifier.height(16.dp))

                // ===== TWO COLUMNS =====
                Row(modifier = Modifier.weight(1f)) {
                    // LEFT COLUMN (wider) — summary, experience, education
                    Column(modifier = Modifier.weight(2f)) {
                        SectionHeader2("", "PROFESSIONAL SUMMARY")
                        ParagraphField(data.summary) { data = data.copy(summary = it) }

                        Spacer(Modifier.height(14.dp))
                        SectionHeader2("", "PROFESSIONAL EXPERIENCE")
                        data.work.forEachIndexed { i, entry ->
                            Row(modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
                                MiniField2("", entry.company, Modifier.weight(1f)) { v ->
                                    data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(company = v) })
                                }
                                MiniField2("", entry.from, Modifier.width(60.dp)) { v ->
                                    data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(from = v) })
                                }
                                Text(" - ", fontSize = 13.sp, color = Color.Black)
                                MiniField2("", entry.to, Modifier.width(60.dp)) { v ->
                                    data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(to = v) })
                                }
                            }
                            MiniField2("", entry.role, Modifier.fillMaxWidth()) { v ->
                                data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(role = v) })
                            }
                            BulletLines2(entry.bullets) { idx, v ->
                                val newBullets = entry.bullets.toMutableList().also { it[idx] = v }
                                data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(bullets = newBullets) })
                            }
                            if (i != data.work.lastIndex) Spacer(Modifier.height(12.dp))
                        }

                        Spacer(Modifier.height(14.dp))
                        SectionHeader2("", "EDUCATION")
                        EduLine(data.eduSchool, bold = false) { data = data.copy(eduSchool = it) }
                        EduLine(data.eduDegree, bold = true) { data = data.copy(eduDegree = it) }
                        EduLine(data.eduYear, bold = false) { data = data.copy(eduYear = it) }
                    }

                    Spacer(Modifier.width(20.dp))

                    // RIGHT COLUMN (narrower) — soft skills, technical skills, languages, interests
                    Column(modifier = Modifier.weight(1f)) {
                        SectionHeader2("", "SOFT SKILLS")
                        BulletLines2(data.softSkills) { idx, v ->
                            data = data.copy(softSkills = data.softSkills.toMutableList().also { it[idx] = v })
                        }

                        Spacer(Modifier.height(14.dp))
                        SectionHeader2("", "TECHNICAL SKILLS")
                        BulletLines2(data.technicalSkills) { idx, v ->
                            data = data.copy(technicalSkills = data.technicalSkills.toMutableList().also { it[idx] = v })
                        }

                        Spacer(Modifier.height(14.dp))
                        SectionHeader2("", "LANGUAGES")
                        BulletLines2(data.languages) { idx, v ->
                            data = data.copy(languages = data.languages.toMutableList().also { it[idx] = v })
                        }

                        Spacer(Modifier.height(14.dp))
                        SectionHeader2("", "INTERESTS")
                        BulletLines2(data.interests) { idx, v ->
                            data = data.copy(interests = data.interests.toMutableList().also { it[idx] = v })
                        }
                    }
                }
            }
        }

        // Download button -- laging nakikita, hindi kasama sa zoom/pan, nasa baba na (para sa ads sa taas)
        Button(
            onClick = {
                fun doSave() {
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
                        saveBitmapToGalleryChrono(context, bitmap)
                    }
                }
                val activity = context as? android.app.Activity
               if (activity != null && interstitialAd != null) {
                    interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            interstitialAd = null
                            doSave()
                        }
                    }
                    interstitialAd?.show(activity)
                } else {
                    doSave()
               }
            },
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .wrapContentWidth()
                .heightIn(min = 52.dp)
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(50))
                .background(
                    androidx.compose.ui.graphics.Brush.horizontalGradient(
                        listOf(Color(0xFF3B6FE0), Color(0xFF1A1A1A), Color(0xFF0B1530))
                    )
                )
        ) { Text("Download", color = Color.White, fontWeight = FontWeight.Bold) }
    }
    }
    }
}

private fun saveBitmapToGalleryChrono(context: android.content.Context, bitmap: Bitmap) {
    val filename = "Resume_Chronological_${System.currentTimeMillis()}.png"
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

// ================== Reusable "papel" pieces ==================

private fun Modifier.bottomLine2(color: Color = Color.Black, thickness: Dp = 1.dp): Modifier =
    this.height(thickness).drawBehind { drawRect(color = color) }

@Composable
private fun SectionHeader2(icon: String, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 4.dp)) {
        if (icon.isNotEmpty()) Text("$icon  ", fontSize = 15.sp)
        Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black, letterSpacing = 0.5.sp)
    }
    Spacer(Modifier.fillMaxWidth().height(1.dp).background(Color.Black))
    Spacer(Modifier.height(6.dp))
}

@Composable
private fun BasicTextFieldCentered(
    value: String,
    fontSize: androidx.compose.ui.unit.TextUnit,
    bold: Boolean,
    letterSpacing: androidx.compose.ui.unit.TextUnit = 0.sp,
    placeholder: String,
    onChange: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        BasicTextField(
            value = value, onValueChange = onChange,
            textStyle = TextStyle(
                fontSize = fontSize,
                fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
                color = Color.Black,
                textAlign = TextAlign.Center,
                letterSpacing = letterSpacing
            ),
            cursorBrush = SolidColor(Color.Black),
            interactionSource = interactionSource,
            modifier = Modifier.fillMaxWidth(0.9f).background(if (isFocused) Color(0xFFFFF3CD) else Color.Transparent)
        )
    }
}

@Composable
private fun ContactFieldInline(icon: String, value: String, onChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("$icon ", fontSize = 12.sp)
        BasicTextField(
            value = value, onValueChange = onChange,
            textStyle = TextStyle(fontSize = 12.sp, color = Color.Black),
            cursorBrush = SolidColor(Color.Black),
            interactionSource = interactionSource,
            modifier = Modifier.width(180.dp).background(if (isFocused) Color(0xFFFFF3CD) else Color.Transparent)
        )
    }
}

@Composable
private fun ParagraphField(value: String, onChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    BasicTextField(
        value = value, onValueChange = onChange,
        textStyle = TextStyle(fontSize = 13.5.sp, color = Color.Black, lineHeight = 19.sp),
        cursorBrush = SolidColor(Color.Black),
        interactionSource = interactionSource,
        modifier = Modifier.fillMaxWidth().background(if (isFocused) Color(0xFFFFF3CD) else Color.Transparent)
    )
}

@Composable
private fun EduLine(value: String, bold: Boolean, onChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    BasicTextField(
        value = value, onValueChange = onChange,
        textStyle = TextStyle(
            fontSize = 13.sp,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            color = Color.Black
        ),
        cursorBrush = SolidColor(Color.Black),
        interactionSource = interactionSource,
        modifier = Modifier.fillMaxWidth().padding(top = 4.dp).background(if (isFocused) Color(0xFFFFF3CD) else Color.Transparent)
    )
}

@Composable
private fun MiniField2(label: String, value: String, modifier: Modifier = Modifier, onChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.Bottom) {
            if (label.isNotEmpty()) Text("$label: ", fontSize = 14.sp, color = Color.Black)
            BasicTextField(
                value = value, onValueChange = onChange,
                textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black),
                cursorBrush = SolidColor(Color.Black),
                interactionSource = interactionSource,
                modifier = Modifier.weight(1f).background(if (isFocused) Color(0xFFFFF3CD) else Color.Transparent)
            )
        }
    }
}

@Composable
private fun BulletLines2(values: List<String>, onChange: (Int, String) -> Unit) {
    Column {
        values.forEachIndexed { i, v ->
            val interactionSource = remember { MutableInteractionSource() }
            val isFocused by interactionSource.collectIsFocusedAsState()
            Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(top = 4.dp)) {
                Text("•  ", fontSize = 13.sp, color = Color.Black)
                BasicTextField(
                    value = v, onValueChange = { onChange(i, it) },
                    textStyle = TextStyle(fontSize = 12.5.sp, color = Color.Black, lineHeight = 17.sp),
                    cursorBrush = SolidColor(Color.Black),
                    interactionSource = interactionSource,
                    modifier = Modifier.weight(1f).background(if (isFocused) Color(0xFFFFF3CD) else Color.Transparent)
                )
            }
        }
    }
}
