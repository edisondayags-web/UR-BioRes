package com.saltech.urdocs.ui.screens
import com.saltech.urdocs.util.SecureScreen

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Picture
import android.graphics.Rect
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

/**
 * "Hybrid" Resume -- pinagsamang Chronological (work history by date)
 * at Project-based (project highlights) format, may maliit na 2x2 photo
 * sa tabi ng pangalan. Parehong "papel" + pinch-zoom + Download pattern
 * gaya ng Traditional/Chronological.
 */
data class HybridWorkEntry(
    val role: String = "",
    val company: String = "",
    val from: String = "",
    val to: String = "",
    val bullets: List<String> = List(3) { "" }
)

data class HybridProjectEntry(
    val name: String = "",
    val tech: String = "",
    val from: String = "",
    val to: String = "",
    val bullets: List<String> = List(3) { "" }
)

data class HybridResumeFields(
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val linkedin: String = "",
    val location: String = "",
    val summary: List<String> = List(4) { "" },
    val work: List<HybridWorkEntry> = List(3) { HybridWorkEntry() },
    val projects: List<HybridProjectEntry> = List(3) { HybridProjectEntry() },
    val eduDegree: String = "",
    val eduSchool: String = "",
    val eduLocation: String = "",
    val eduYear: String = "",
    val skillsProgramming: List<String> = List(3) { "" },
    val skillsFrameworks: List<String> = List(3) { "" },
    val skillsTools: List<String> = List(3) { "" },
    val certifications: List<String> = List(4) { "" },
    val interests: List<String> = List(4) { "" }
)

@Composable
fun HybridResumeScreen(
    processedSelfie: Bitmap? = null,
    onTakeSelfie: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    //SecureScreen()
    val paperWidthDp = 850.dp
    val paperHeightDp = 1350.dp

    var data by remember { mutableStateOf(HybridResumeFields()) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val context = LocalContext.current
    val picture = remember { Picture() }
    val coroutineScope = rememberCoroutineScope()

    var rawSource by remember { mutableStateOf<Bitmap?>(null) }
    var displaySelfie by remember { mutableStateOf<Bitmap?>(null) }
    var isProcessingPhoto by remember { mutableStateOf(false) }

    val uploadLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val loaded = com.saltech.urdocs.util.ImageUtils.loadBitmapFromUri(context, uri)
            if (loaded != null) rawSource = loaded
        }
    }

    LaunchedEffect(processedSelfie) {
        if (processedSelfie != null) rawSource = processedSelfie
    }

    LaunchedEffect(rawSource) {
        val raw = rawSource
        if (raw != null) {
            isProcessingPhoto = true
            val result = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Default) {
                try {
                    com.saltech.urdocs.ml.FaceCropHelper.cropTo2x2WithFaceBox(raw)
                } catch (e: Exception) {
                    null
                }
            }
            val finalBitmap = if (result != null) {
                try {
                    val whiteBg = com.saltech.urdocs.ml.BackgroundHelper.replaceWithWhiteBackground(result.first)
                    val balanced = com.saltech.urdocs.ml.WhiteBalanceHelper.grayWorldCorrect(whiteBg)
                    val leveled = com.saltech.urdocs.ml.SkinSmoothingHelper.studioClean(balanced)
                    val smoothed = com.saltech.urdocs.ml.SkinSmoothingHelper.frequencySeparationSmooth(leveled)
                    com.saltech.urdocs.ml.SharpeningHelper.unsharpMask(smoothed)
                } catch (e: Exception) {
                    result.first
                }
            } else {
                raw
            }
            displaySelfie = finalBitmap
            isProcessingPhoto = false
        }
    }

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

        BoxWithConstraints(
            modifier = Modifier.weight(1f).background(Color.Transparent)
        ) {
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

                // ===== HEADER: name/contact (left) + small photo (right) =====
                Row(verticalAlignment = Alignment.Top) {
                    Column(modifier = Modifier.weight(1f)) {
                        FieldLineH("NAME", data.name, bigLabel = true) { data = data.copy(name = it) }
                        Spacer(Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            ContactFieldH("📞", data.phone, Modifier.weight(1f)) { data = data.copy(phone = it) }
                            Text(" | ", color = Color.Black)
                            ContactFieldH("✉", data.email, Modifier.weight(1f)) { data = data.copy(email = it) }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            ContactFieldH("🔗", data.linkedin, Modifier.weight(1f)) { data = data.copy(linkedin = it) }
                            Text(" | ", color = Color.Black)
                            ContactFieldH("📍", data.location, Modifier.weight(1f)) { data = data.copy(location = it) }
                        }
                    }
                    Spacer(Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .size(80.dp, 96.dp)
                            .border(1.dp, Color.Black)
                            .clickable(enabled = displaySelfie == null) { onTakeSelfie() }
                    ) {
                        when {
                            isProcessingPhoto -> {
                                Text(
                                    "Processing...",
                                    fontSize = 8.sp,
                                    color = Color.Black,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                            displaySelfie != null -> {
                                Image(
                                    bitmap = displaySelfie!!.asImageBitmap(),
                                    contentDescription = "2x2 Photo",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            else -> {
                                Text("+", fontSize = 24.sp, color = Color.Black, modifier = Modifier.align(Alignment.Center))
                            }
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Spacer(Modifier.fillMaxWidth().height(1.dp).background(Color.Black))
                Spacer(Modifier.height(16.dp))

                // ===== PROFESSIONAL SUMMARY =====
                SectionHeaderH("📝", "PROFESSIONAL SUMMARY")
                MultiLineFieldH(data.summary) { idx, v ->
                    data = data.copy(summary = data.summary.toMutableList().also { it[idx] = v })
                }

                Spacer(Modifier.height(14.dp))

                // ===== WORK HISTORY =====
                SectionHeaderH("💼", "WORK HISTORY")
                data.work.forEachIndexed { i, entry ->
                    Row(modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
                        MiniFieldH("", entry.role, Modifier.weight(1f)) { v ->
                            data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(role = v) })
                        }
                        Spacer(Modifier.width(8.dp))
                        MiniFieldH("", entry.from, Modifier.width(60.dp)) { v ->
                            data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(from = v) })
                        }
                        Text(" - ", fontSize = 12.sp, color = Color.Black)
                        MiniFieldH("", entry.to, Modifier.width(60.dp)) { v ->
                            data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(to = v) })
                        }
                    }
                    MiniFieldH("", entry.company, Modifier.fillMaxWidth()) { v ->
                        data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(company = v) })
                    }
                    Text("(Position — Company/Partnership)", fontSize = 10.sp, fontStyle = FontStyle.Italic, color = Color.Black)
                    BulletLinesH(entry.bullets) { idx, v ->
                        val newBullets = entry.bullets.toMutableList().also { it[idx] = v }
                        data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(bullets = newBullets) })
                    }
                    if (i != data.work.lastIndex) Spacer(Modifier.height(12.dp))
                }

                Spacer(Modifier.height(14.dp))

                // ===== PROJECT HISTORY =====
                SectionHeaderH("📁", "PROJECT HISTORY")
                data.projects.forEachIndexed { i, entry ->
                    Row(modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
                        MiniFieldH("", entry.name, Modifier.weight(1f)) { v ->
                            data = data.copy(projects = data.projects.toMutableList().also { it[i] = entry.copy(name = v) })
                        }
                        Spacer(Modifier.width(8.dp))
                        MiniFieldH("", entry.from, Modifier.width(60.dp)) { v ->
                            data = data.copy(projects = data.projects.toMutableList().also { it[i] = entry.copy(from = v) })
                        }
                        Text(" - ", fontSize = 12.sp, color = Color.Black)
                        MiniFieldH("", entry.to, Modifier.width(60.dp)) { v ->
                            data = data.copy(projects = data.projects.toMutableList().also { it[i] = entry.copy(to = v) })
                        }
                    }
                    MiniFieldH("Technologies", entry.tech, Modifier.fillMaxWidth()) { v ->
                        data = data.copy(projects = data.projects.toMutableList().also { it[i] = entry.copy(tech = v) })
                    }
                    BulletLinesH(entry.bullets) { idx, v ->
                        val newBullets = entry.bullets.toMutableList().also { it[idx] = v }
                        data = data.copy(projects = data.projects.toMutableList().also { it[i] = entry.copy(bullets = newBullets) })
                    }
                    if (i != data.projects.lastIndex) Spacer(Modifier.height(12.dp))
                }

                Spacer(Modifier.height(14.dp))

                // ===== EDUCATION =====
                SectionHeaderH("🎓", "EDUCATION")
                Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                    MiniFieldH("", data.eduDegree, Modifier.weight(1f)) { data = data.copy(eduDegree = it) }
                    Spacer(Modifier.width(8.dp))
                    MiniFieldH("", data.eduYear, Modifier.width(90.dp)) { data = data.copy(eduYear = it) }
                }
                MiniFieldH("", data.eduSchool, Modifier.fillMaxWidth()) { data = data.copy(eduSchool = it) }
                Text("(Degree/Course — Year)", fontSize = 10.sp, fontStyle = FontStyle.Italic, color = Color.Black)
                MiniFieldH("", data.eduLocation, Modifier.fillMaxWidth()) { data = data.copy(eduLocation = it) }

                Spacer(Modifier.height(14.dp))

                // ===== SKILLS =====
                SectionHeaderH("⚙", "SKILLS")
                Text("Programming Languages:", fontSize = 12.sp, color = Color.Black, modifier = Modifier.padding(top = 4.dp))
                BulletLinesH(data.skillsProgramming) { idx, v ->
                    data = data.copy(skillsProgramming = data.skillsProgramming.toMutableList().also { it[idx] = v })
                }
                Text("Frameworks & Libraries:", fontSize = 12.sp, color = Color.Black, modifier = Modifier.padding(top = 8.dp))
                BulletLinesH(data.skillsFrameworks) { idx, v ->
                    data = data.copy(skillsFrameworks = data.skillsFrameworks.toMutableList().also { it[idx] = v })
                }
                Text("Tools & Platforms:", fontSize = 12.sp, color = Color.Black, modifier = Modifier.padding(top = 8.dp))
                BulletLinesH(data.skillsTools) { idx, v ->
                    data = data.copy(skillsTools = data.skillsTools.toMutableList().also { it[idx] = v })
                }

                Spacer(Modifier.height(14.dp))

                // ===== CERTIFICATIONS & INTERESTS (two columns) =====
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        SectionHeaderH("🏅", "CERTIFICATIONS & AWARDS")
                        BulletLinesH(data.certifications) { idx, v ->
                            data = data.copy(certifications = data.certifications.toMutableList().also { it[idx] = v })
                        }
                    }
                    Spacer(Modifier.width(24.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        SectionHeaderH("⭐", "INTERESTS")
                        BulletLinesH(data.interests) { idx, v ->
                            data = data.copy(interests = data.interests.toMutableList().also { it[idx] = v })
                        }
                    }
                }
            }
        }
        }

        // Upload + Download buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(24.dp))
                    .border(1.5.dp, Color(0xFF0B1530), androidx.compose.foundation.shape.RoundedCornerShape(24.dp))
                    .clickable { uploadLauncher.launch("image/*") }
                    .padding(horizontal = 18.dp, vertical = 10.dp)
            ) {
                Text("📤", fontSize = 16.sp)
                Spacer(Modifier.width(6.dp))
                Text("Upload", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                if (displaySelfie != null) {
                    Spacer(Modifier.width(12.dp))
                    Text("🔄 Retake", fontSize = 13.sp, color = Color(0xFF3B6FE0), modifier = Modifier.clickable { onTakeSelfie() })
                }
            }

            Button(
                onClick = {
                    fun doSave() {
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
                            saveBitmapToGalleryHybrid(context, bitmap)
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
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(24.dp))
                    .background(
                        androidx.compose.ui.graphics.Brush.horizontalGradient(
                            listOf(Color(0xFF3B6FE0), Color(0xFF1A1A1A), Color(0xFF0B1530))
                        )
                    )
            ) {
                Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)) {
                    Text("Download", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

private fun saveBitmapToGalleryHybrid(context: android.content.Context, bitmap: Bitmap) {
    val filename = "Resume_Hybrid_${System.currentTimeMillis()}.png"
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

private fun Modifier.bottomLineH(color: Color = Color.Black, thickness: Dp = 1.dp): Modifier =
    this.height(thickness).drawBehind { drawRect(color = color) }

@Composable
private fun SectionHeaderH(icon: String, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 4.dp)) {
        Text("$icon  ", fontSize = 14.sp)
        Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
    }
    Spacer(Modifier.fillMaxWidth().height(1.dp).background(Color.Black))
    Spacer(Modifier.height(6.dp))
}

@Composable
private fun FieldLineH(label: String, value: String, bigLabel: Boolean = false, onChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    Column(modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                "$label: ",
                fontSize = if (bigLabel) 20.sp else 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            BasicTextField(
                value = value, onValueChange = onChange,
                textStyle = TextStyle(fontSize = if (bigLabel) 20.sp else 12.sp, color = Color.Black),
                cursorBrush = SolidColor(Color.Black),
                interactionSource = interactionSource,
                modifier = Modifier.weight(1f).background(if (isFocused) Color(0xFFFFF3CD) else Color.Transparent)
            )
        }
        Spacer(Modifier.height(2.dp))
        Spacer(Modifier.fillMaxWidth().bottomLineH())
    }
}

@Composable
private fun ContactFieldH(icon: String, value: String, modifier: Modifier = Modifier, onChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text("$icon  ", fontSize = 12.sp)
            BasicTextField(
                value = value, onValueChange = onChange,
                textStyle = TextStyle(fontSize = 11.sp, color = Color.Black),
                cursorBrush = SolidColor(Color.Black),
                interactionSource = interactionSource,
                modifier = Modifier.weight(1f).background(if (isFocused) Color(0xFFFFF3CD) else Color.Transparent)
            )
        }
        Spacer(Modifier.height(2.dp))
        Spacer(Modifier.fillMaxWidth().bottomLineH())
    }
}

@Composable
private fun MiniFieldH(label: String, value: String, modifier: Modifier = Modifier, onChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.Bottom) {
            if (label.isNotEmpty()) Text("$label: ", fontSize = 12.sp, color = Color.Black)
            BasicTextField(
                value = value, onValueChange = onChange,
                textStyle = TextStyle(fontSize = 12.sp, color = Color.Black),
                cursorBrush = SolidColor(Color.Black),
                interactionSource = interactionSource,
                modifier = Modifier.weight(1f).background(if (isFocused) Color(0xFFFFF3CD) else Color.Transparent)
            )
        }
        Spacer(Modifier.height(2.dp))
        Spacer(Modifier.fillMaxWidth().bottomLineH())
    }
}

@Composable
private fun MultiLineFieldH(values: List<String>, onChange: (Int, String) -> Unit) {
    Column {
        values.forEachIndexed { i, v ->
            val interactionSource = remember { MutableInteractionSource() }
            val isFocused by interactionSource.collectIsFocusedAsState()
            BasicTextField(
                value = v, onValueChange = { onChange(i, it) },
                textStyle = TextStyle(fontSize = 12.sp, color = Color.Black),
                cursorBrush = SolidColor(Color.Black),
                interactionSource = interactionSource,
                modifier = Modifier.fillMaxWidth().background(if (isFocused) Color(0xFFFFF3CD) else Color.Transparent)
            )
            Spacer(Modifier.height(2.dp))
            Spacer(Modifier.fillMaxWidth().bottomLineH())
            Spacer(Modifier.height(4.dp))
        }
    }
}

@Composable
private fun BulletLinesH(values: List<String>, onChange: (Int, String) -> Unit) {
    Column {
        values.forEachIndexed { i, v ->
            val interactionSource = remember { MutableInteractionSource() }
            val isFocused by interactionSource.collectIsFocusedAsState()
            Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(top = 4.dp)) {
                Text("•  ", fontSize = 12.sp, color = Color.Black)
                BasicTextField(
                    value = v, onValueChange = { onChange(i, it) },
                    textStyle = TextStyle(fontSize = 12.sp, color = Color.Black),
                    cursorBrush = SolidColor(Color.Black),
                    interactionSource = interactionSource,
                    modifier = Modifier.weight(1f).background(if (isFocused) Color(0xFFFFF3CD) else Color.Transparent)
                )
            }
            Spacer(Modifier.height(2.dp))
            Spacer(Modifier.fillMaxWidth().padding(start = 16.dp).bottomLineH())
        }
    }
}
