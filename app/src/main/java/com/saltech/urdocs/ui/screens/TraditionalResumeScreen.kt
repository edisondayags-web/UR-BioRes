package com.saltech.urdocs.ui.screens

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Picture
import android.graphics.Rect
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.alpha
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

data class TraditionalResumeFields(
    val fullName: String = "",
    val phone: String = "",
    val email: String = "",
    val address: String = "",
    val dob: String = "",
    val gender: String = "",
    val civilStatus: String = "",
    val objective: String = "",
    val eduCourse: String = "",
    val eduSchool: String = "",
    val eduYear: String = "",
    val eduAddress: String = "",
    val skills: List<String> = List(6) { "" },
    val languages: List<String> = List(3) { "" },
    val ref1Name: String = "", val ref1Position: String = "", val ref1Company: String = "", val ref1Contact: String = "",
    val ref2Name: String = "", val ref2Position: String = "", val ref2Company: String = "", val ref2Contact: String = "",
    val work: List<WorkEntry> = List(3) { WorkEntry() },
    val trainings: List<String> = List(4) { "" },
    val certifications: List<String> = List(3) { "" }
)

data class WorkEntry(
    val jobTitle: String = "",
    val company: String = "",
    val address: String = "",
    val durationFrom: String = "",
    val durationTo: String = "",
    val duties: List<String> = List(4) { "" }
)

@Composable
fun TraditionalResumeScreen(
    processedSelfie: Bitmap? = null,
    onTakeSelfie: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val paperWidthDp = 850.dp
    val paperHeightDp = 1250.dp

    val context = LocalContext.current
    var data by remember { mutableStateOf(TraditionalResumeFields()) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    // rawSource = pinagmulan ng litrato -- camera (processedSelfie) o Upload.
    var rawSource by remember { mutableStateOf<Bitmap?>(null) }
    var displaySelfie by remember { mutableStateOf<Bitmap?>(null) }
    var isProcessingPhoto by remember { mutableStateOf(false) }

    // ---------- Polo overlay choice: state para sa pending crop habang naghihintay ng consent ----------
    var poloChoicePending by remember { mutableStateOf<Pair<Bitmap, Rect>?>(null) }

    LaunchedEffect(processedSelfie) {
        if (processedSelfie != null) rawSource = processedSelfie
    }

    val uploadLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val loaded = com.saltech.urdocs.util.ImageUtils.loadBitmapFromUri(context, uri)
            if (loaded != null) rawSource = loaded
        }
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
            isProcessingPhoto = false
            if (result != null) {
                finishProcessing(result.first, result.second, addPolo = false)
            } else {
                displaySelfie = raw
            }
        }
    }

    fun finishProcessing(cropped: Bitmap, faceBox: Rect, addPolo: Boolean) {
        val scope = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Default)
        scope.launch {
            isProcessingPhoto = true
            val result = try {
                val withPolo = if (addPolo) {
                    com.saltech.urdocs.ml.FaceCropHelper.addFormalAttireOverlay(cropped, faceBox)
                } else {
                    cropped
                }
                val whiteBg = com.saltech.urdocs.ml.BackgroundHelper.replaceWithWhiteBackground(withPolo)
                com.saltech.urdocs.ml.SkinSmoothingHelper.studioClean(whiteBg)
            } catch (e: Exception) {
                cropped
            }
            displaySelfie = result
            isProcessingPhoto = false
        }
    }

    // ---------- Consent dialog: may polo o yun na lang ----------
    poloChoicePending?.let { (cropped, faceBox) ->
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Lagyan ng formal attire?") },
            text = {
                Text("Gusto mo bang lagyan ng formal attire (blazer/coat) ang litrato mo, o gamitin na lang ang litrato mo mismo?")
            },
            confirmButton = {
                TextButton(onClick = {
                    poloChoicePending = null
                    finishProcessing(cropped, faceBox, addPolo = true)
                }) {
                    Text("Oo, lagyan ng formal attire")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    poloChoicePending = null
                    finishProcessing(cropped, faceBox, addPolo = false)
                }) {
                    Text("Hindi, ito na")
                }
            }
        )
    }

    val picture = remember { Picture() }
    val coroutineScope = rememberCoroutineScope()

    var interstitialAd by remember { mutableStateOf<com.google.android.gms.ads.interstitial.InterstitialAd?>(null) }
    LaunchedEffect(Unit) {
        com.google.android.gms.ads.interstitial.InterstitialAd.load(
            context,
            "ca-app-pub-3134240485602899/5274307709",
            AdRequest.Builder().build(),
            object : com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: com.google.android.gms.ads.interstitial.InterstitialAd) {
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
                .background(Color.White)
                .padding(28.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    "R E S U M E",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text("•", color = Color.Black, fontSize = 14.sp)
                    HorizontalDivider(modifier = Modifier.weight(1f).padding(horizontal = 6.dp), thickness = 1.dp, color = Color.Black)
                    Text("◇", color = Color.Black, fontSize = 12.sp)
                    HorizontalDivider(modifier = Modifier.weight(1f).padding(horizontal = 6.dp), thickness = 1.dp, color = Color.Black)
                    Text("•", color = Color.Black, fontSize = 14.sp)
                }
                Spacer(Modifier.height(18.dp))

                Row {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(140.dp, 175.dp)
                                .border(1.dp, Color.Black)
                                .clickable(enabled = displaySelfie == null) { onTakeSelfie() }
                        ) {
                            when {
                                isProcessingPhoto -> {
                                    Text(
                                        "Processing...",
                                        fontSize = 9.sp,
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
                                    Text("+", fontSize = 32.sp, color = Color.Black, modifier = Modifier.align(Alignment.Center))
                                }
                            }
                        }
                    }
                    Spacer(Modifier.width(20.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        FieldLine("FULL NAME", data.fullName, bold = true) { data = data.copy(fullName = it) }
                        IconFieldLine("📞", "Phone No.", data.phone) { data = data.copy(phone = it) }
                        IconFieldLine("✉", "Email Address", data.email) { data = data.copy(email = it) }
                        IconFieldLine("📍", "Address", data.address) { data = data.copy(address = it) }
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 7.dp)) {
                            MiniField("Date of Birth", data.dob, Modifier.weight(1f)) { data = data.copy(dob = it) }
                            Spacer(Modifier.width(8.dp))
                            MiniField("Gender", data.gender, Modifier.weight(1f)) { data = data.copy(gender = it) }
                            Spacer(Modifier.width(8.dp))
                            MiniField("Civil Status", data.civilStatus, Modifier.weight(1f)) { data = data.copy(civilStatus = it) }
                        }
                    }
                }

                Spacer(Modifier.height(6.dp))
                Spacer(Modifier.fillMaxWidth().height(2.dp).background(Color.Black))
                Spacer(Modifier.height(16.dp))

                Row(modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.weight(1f)) {
                        SectionHeader("👤", "OBJECTIVE")
                        MultiLineField(data.objective, lines = 3) { data = data.copy(objective = it) }

                        Spacer(Modifier.height(14.dp))
                        SectionHeader("🎓", "EDUCATION")
                        FieldLine("Course/Degree", data.eduCourse) { data = data.copy(eduCourse = it) }
                        FieldLine("School", data.eduSchool) { data = data.copy(eduSchool = it) }
                        FieldLine("Year Graduated", data.eduYear) { data = data.copy(eduYear = it) }
                        FieldLine("Address", data.eduAddress) { data = data.copy(eduAddress = it) }

                        Spacer(Modifier.height(14.dp))
                        SectionHeader("⚙", "SKILLS")
                        BulletLines(data.skills) { idx, v ->
                            data = data.copy(skills = data.skills.toMutableList().also { it[idx] = v })
                        }

                        Spacer(Modifier.height(14.dp))
                        SectionHeader("💬", "LANGUAGES")
                        BulletLines(data.languages) { idx, v ->
                            data = data.copy(languages = data.languages.toMutableList().also { it[idx] = v })
                        }

                        Spacer(Modifier.height(14.dp))
                        SectionHeader("👥", "REFERENCES")
                        RefEntry(data.ref1Name, data.ref1Position, data.ref1Company, data.ref1Contact,
                            onName = { data = data.copy(ref1Name = it) },
                            onPosition = { data = data.copy(ref1Position = it) },
                            onCompany = { data = data.copy(ref1Company = it) },
                            onContact = { data = data.copy(ref1Contact = it) })
                        Spacer(Modifier.height(8.dp))
                        RefEntry(data.ref2Name, data.ref2Position, data.ref2Company, data.ref2Contact,
                            onName = { data = data.copy(ref2Name = it) },
                            onPosition = { data = data.copy(ref2Position = it) },
                            onCompany = { data = data.copy(ref2Company = it) },
                            onContact = { data = data.copy(ref2Contact = it) })
                    }

                    Spacer(Modifier.width(24.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        SectionHeader("💼", "WORK EXPERIENCE")
                        data.work.forEachIndexed { i, entry ->
                            FieldLine("Job Title", entry.jobTitle) { v ->
                                data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(jobTitle = v) })
                            }
                            FieldLine("Company", entry.company) { v ->
                                data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(company = v) })
                            }
                            FieldLine("Address", entry.address) { v ->
                                data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(address = v) })
                            }
                            Row(modifier = Modifier.fillMaxWidth().padding(top = 7.dp)) {
                                Text("Duration:", fontSize = 12.sp, color = Color.Black)
                                Spacer(Modifier.width(6.dp))
                                MiniField("", entry.durationFrom, Modifier.weight(1f)) { v ->
                                    data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(durationFrom = v) })
                                }
                                Text(" to ", fontSize = 12.sp, color = Color.Black)
                                MiniField("", entry.durationTo, Modifier.weight(1f)) { v ->
                                    data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(durationTo = v) })
                                }
                            }
                            Spacer(Modifier.height(4.dp))
                            Text("Duties and Responsibilities:", fontSize = 12.sp, color = Color.Black)
                            BulletLines(entry.duties) { idx, v ->
                                val newDuties = entry.duties.toMutableList().also { it[idx] = v }
                                data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(duties = newDuties) })
                            }
                            if (i != data.work.lastIndex) Spacer(Modifier.height(12.dp))
                        }

                        Spacer(Modifier.height(14.dp))
                        SectionHeader("🏅", "TRAININGS / SEMINARS ATTENDED")
                        BulletLines(data.trainings) { idx, v ->
                            data = data.copy(trainings = data.trainings.toMutableList().also { it[idx] = v })
                        }

                        Spacer(Modifier.height(14.dp))
                        SectionHeader("📜", "CERTIFICATIONS / LICENSES")
                        BulletLines(data.certifications) { idx, v ->
                            data = data.copy(certifications = data.certifications.toMutableList().also { it[idx] = v })
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
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
                            saveBitmapToGalleryTraditional(context, bitmap)
                        }
                    }
                    val activity = context as? android.app.Activity
                    if (activity != null && interstitialAd != null) {
                        interstitialAd?.fullScreenContentCallback = object : com.google.android.gms.ads.FullScreenContentCallback() {
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
}
private fun saveBitmapToGalleryTraditional(context: android.content.Context, bitmap: Bitmap) {
    val filename = "Resume_Traditional_${System.currentTimeMillis()}.png"
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

private fun Modifier.bottomLine(color: Color = Color.Black, thickness: Dp = 1.dp): Modifier =
    this.height(thickness).drawBehind { drawRect(color = color) }

@Composable
private fun SectionHeader(icon: String, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 4.dp)) {
        Text("$icon  ", fontSize = 14.sp)
        Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
    }
    Spacer(Modifier.fillMaxWidth().height(1.dp).background(Color.Black))
    Spacer(Modifier.height(6.dp))
}

@Composable
private fun FieldLine(label: String, value: String, bold: Boolean = false, onChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    Column(modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text("$label: ", fontSize = 12.sp, fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal, color = Color.Black)
            BasicTextField(
                value = value, onValueChange = onChange,
                textStyle = TextStyle(fontSize = 12.sp, color = Color.Black),
                cursorBrush = SolidColor(Color.Black),
                interactionSource = interactionSource,
                modifier = Modifier.weight(1f).background(if (isFocused) Color(0xFFFFF3CD) else Color.Transparent)
            )
        }
        Spacer(Modifier.height(2.dp))
        Spacer(Modifier.fillMaxWidth().bottomLine())
    }
}

@Composable
private fun IconFieldLine(icon: String, label: String, value: String, onChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    Column(modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text("$icon  $label: ", fontSize = 12.sp, color = Color.Black)
            BasicTextField(
                value = value, onValueChange = onChange,
                textStyle = TextStyle(fontSize = 12.sp, color = Color.Black),
                cursorBrush = SolidColor(Color.Black),
                interactionSource = interactionSource,
                modifier = Modifier.weight(1f).background(if (isFocused) Color(0xFFFFF3CD) else Color.Transparent)
            )
        }
        Spacer(Modifier.height(2.dp))
        Spacer(Modifier.fillMaxWidth().bottomLine())
    }
}

@Composable
private fun MiniField(label: String, value: String, modifier: Modifier = Modifier, onChange: (String) -> Unit) {
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
        Spacer(Modifier.fillMaxWidth().bottomLine())
    }
}

@Composable
private fun MultiLineField(value: String, lines: Int, onChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    Column {
        BasicTextField(
            value = value, onValueChange = onChange,
            textStyle = TextStyle(fontSize = 12.sp, color = Color.Black),
            cursorBrush = SolidColor(Color.Black),
            interactionSource = interactionSource,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = (lines * 18).dp)
                .background(if (isFocused) Color(0xFFFFF3CD) else Color.Transparent)
        )
        repeat(lines) {
            Spacer(Modifier.height(14.dp))
            Spacer(Modifier.fillMaxWidth().bottomLine())
        }
    }
}

@Composable
private fun BulletLines(values: List<String>, onChange: (Int, String) -> Unit) {
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
            Spacer(Modifier.fillMaxWidth().padding(start = 16.dp).bottomLine())
        }
    }
}

@Composable
private fun RefEntry(
    name: String, position: String, company: String, contact: String,
    onName: (String) -> Unit, onPosition: (String) -> Unit, onCompany: (String) -> Unit, onContact: (String) -> Unit
) {
    Column {
        FieldLine("Name", name, onChange = onName)
        FieldLine("Position", position, onChange = onPosition)
        FieldLine("Company", company, onChange = onCompany)
        FieldLine("Contact No.", contact, onChange = onContact)
    }
}
