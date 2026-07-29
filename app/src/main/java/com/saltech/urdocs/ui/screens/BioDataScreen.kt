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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.saltech.urdocs.R
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

data class BioDataFields(
    val name: String = "",
    val gender: String = "",
    val dob: String = "",
    val currentAddress: String = "",
    val permanentAddress: String = "",
    val age: String = "",
    val date: String = "",
    val occupation: String = "",
    val telephone: String = "",
    val civilStatus: String = "",
    val cellphone: String = "",
    val placeOfBirth: String = "",
    val email: String = "",
    val height: String = "",
    val citizenship: String = "",
    val weight: String = "",
    val religion: String = "",
    val fathersName: String = "",
    val fathersOccupation: String = "",
    val mothersName: String = "",
    val mothersOccupation: String = "",
    val language: String = "",
    val emergencyContact: String = "",
    val emergencyAddress: String = "",
    val emergencyContactNo: String = "",
    val elementary: String = "",
    val elementaryYear: String = "",
    val highSchool: String = "",
    val highSchoolYear: String = "",
    val college: String = "",
    val collegeYear: String = ""
)

@Composable
fun BioDataScreen(
    processedSelfie: android.graphics.Bitmap? = null,
    onTakeSelfie: () -> Unit = {}
) {
    SecureScreen()
    val paperWidthDp = 750.dp
    val paperHeightDp = 1250.dp

    val context = LocalContext.current
    var data by remember { mutableStateOf(BioDataFields()) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    var rawSource by remember { mutableStateOf<Bitmap?>(null) }
    var displaySelfie by remember { mutableStateOf<Bitmap?>(null) }
    var isProcessingPhoto by remember { mutableStateOf(false) }

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
                val balanced = com.saltech.urdocs.ml.WhiteBalanceHelper.grayWorldCorrect(whiteBg)
                val leveled = com.saltech.urdocs.ml.SkinSmoothingHelper.studioClean(balanced)
                val smoothed = com.saltech.urdocs.ml.SkinSmoothingHelper.frequencySeparationSmooth(leveled)
                com.saltech.urdocs.ml.SharpeningHelper.unsharpMask(smoothed)
            } catch (e: Exception) {
                cropped
            }
            displaySelfie = result
            isProcessingPhoto = false
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
            modifier = Modifier
                .weight(1f)
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
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
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
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        "BIO-DATA",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(180.dp, 180.dp)
                                .border(1.dp, Color.Black)
                                .clickable(enabled = displaySelfie == null) { onTakeSelfie() }
                        ) {
                            when {
                                isProcessingPhoto -> {
                                    Text(
                                        "Processing...",
                                        fontSize = 10.sp,
                                        color = Color.Black,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                                displaySelfie != null -> {
                                    androidx.compose.foundation.Image(
                                        bitmap = displaySelfie!!.asImageBitmap(),
                                        contentDescription = "2x2 Photo",
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                else -> {
                                    Text(
                                        "+",
                                        fontSize = 32.sp,
                                        color = Color.Black,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))
                Text("PERSONAL DATA", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)

                FieldLine("Name", data.name) { data = data.copy(name = it) }
                FieldLine("Gender", data.gender) { data = data.copy(gender = it) }
                FieldLine("Date of Birth", data.dob) { data = data.copy(dob = it) }
                FieldLine("Current Address", data.currentAddress) { data = data.copy(currentAddress = it) }
                FieldLine("Permanent Address", data.permanentAddress) { data = data.copy(permanentAddress = it) }

                TwoCol("Age", data.age, { data = data.copy(age = it) },
                    "Date", data.date) { data = data.copy(date = it) }
                TwoCol("Occupation", data.occupation, { data = data.copy(occupation = it) },
                    "Telephone", data.telephone) { data = data.copy(telephone = it) }
                TwoCol("Civil Status", data.civilStatus, { data = data.copy(civilStatus = it) },
                    "Cellphone", data.cellphone) { data = data.copy(cellphone = it) }
                TwoCol("Place of Birth", data.placeOfBirth, { data = data.copy(placeOfBirth = it) },
                    "Email", data.email) { data = data.copy(email = it) }
                TwoCol("Height", data.height, { data = data.copy(height = it) },
                    "Citizenship", data.citizenship) { data = data.copy(citizenship = it) }
                TwoCol("Weight", data.weight, { data = data.copy(weight = it) },
                    "Religion", data.religion) { data = data.copy(religion = it) }
                TwoCol("Father's Name", data.fathersName, { data = data.copy(fathersName = it) },
                    "Occupation", data.fathersOccupation) { data = data.copy(fathersOccupation = it) }
                TwoCol("Mother's Name", data.mothersName, { data = data.copy(mothersName = it) },
                    "Occupation", data.mothersOccupation) { data = data.copy(mothersOccupation = it) }

                FieldLine("Language or dialect spoken", data.language) { data = data.copy(language = it) }
                FieldLine("Person to be contacted in case of emergency", data.emergencyContact) { data = data.copy(emergencyContact = it) }
                TwoCol("Address", data.emergencyAddress, { data = data.copy(emergencyAddress = it) },
                    "Contact No.", data.emergencyContactNo) { data = data.copy(emergencyContactNo = it) }

                Spacer(Modifier.height(12.dp))
                Text("EDUCATIONAL BACKGROUND", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                TwoCol("Elementary", data.elementary, { data = data.copy(elementary = it) },
                    "Year Graduated", data.elementaryYear) { data = data.copy(elementaryYear = it) }
                TwoCol("High School", data.highSchool, { data = data.copy(highSchool = it) },
                    "Year Graduated", data.highSchoolYear) { data = data.copy(highSchoolYear = it) }
                TwoCol("College", data.college, { data = data.copy(college = it) },
                    "Year Graduated", data.collegeYear) { data = data.copy(collegeYear = it) }

                Spacer(Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Black)
                        .padding(12.dp)
                ) {
                    Text(
                        "I here certify that the above information is true and correct to the " +
                            "best of my knowledge and belief. I also understand that any " +
                            "misinterpretation will be considered reason for withdrawal of an " +
                            "offer or subsequent dismissal if employed.",
                        fontSize = 11.sp,
                        color = Color.Black
                    )
                }

                Spacer(Modifier.height(28.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Spacer(
                            Modifier
                                .fillMaxWidth()
                                .padding(end = 20.dp)
                                .bottomLine()
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Date",
                            fontSize = 11.sp,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Spacer(
                            Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp)
                                .bottomLine()
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Signature",
                            fontSize = 11.sp,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Black)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                                saveBitmapToGallery(context, bitmap)
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
    }
}
private fun saveBitmapToGallery(context: android.content.Context, bitmap: Bitmap) {
    val filename = "BioData_${System.currentTimeMillis()}.png"
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
        resolver.openOutputStream(it)?.use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        android.widget.Toast.makeText(context, "see your gellery luv🩵", android.widget.Toast.LENGTH_LONG).show()
    } ?: run {
        android.widget.Toast.makeText(context, "Hindi na-download, subukan ulit.", android.widget.Toast.LENGTH_LONG).show()
    }
}

private fun Modifier.bottomLine(
    color: Color = Color.Black,
    thickness: Dp = 1.dp
): Modifier = this
    .height(thickness)
    .drawBehind {
        drawRect(color = color)
    }

@Composable
private fun FieldLine(label: String, value: String, onChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    Column(modifier = Modifier.fillMaxWidth().padding(top = 7.dp)) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text("$label: ", fontSize = 12.sp, color = Color.Black)
            BasicTextField(
                value = value,
                onValueChange = onChange,
                textStyle = TextStyle(fontSize = 12.sp, color = Color.Black),
                cursorBrush = SolidColor(Color.Black),
                interactionSource = interactionSource,
                modifier = Modifier
                    .weight(1f)
                    .background(if (isFocused) Color(0xFFFFF3CD) else Color.Transparent)
            )
        }
        Spacer(Modifier.height(2.dp))
        Spacer(Modifier.fillMaxWidth().bottomLine())
    }
}

@Composable
private fun TwoCol(
    label1: String, value1: String, onChange1: (String) -> Unit,
    label2: String, value2: String, onChange2: (String) -> Unit
) {
    val focus1 = remember { MutableInteractionSource() }
    val isFocused1 by focus1.collectIsFocusedAsState()
    val focus2 = remember { MutableInteractionSource() }
    val isFocused2 by focus2.collectIsFocusedAsState()
    Row(modifier = Modifier.fillMaxWidth().padding(top = 7.dp)) {
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text("$label1: ", fontSize = 12.sp, color = Color.Black)
                BasicTextField(
                    value = value1,
                    onValueChange = onChange1,
                    textStyle = TextStyle(fontSize = 12.sp, color = Color.Black),
                    cursorBrush = SolidColor(Color.Black),
                    interactionSource = focus1,
                    modifier = Modifier
                        .weight(1f)
                        .background(if (isFocused1) Color(0xFFFFF3CD) else Color.Transparent)
                )
            }
            Spacer(Modifier.height(2.dp))
            Spacer(Modifier.fillMaxWidth().bottomLine())
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text("$label2: ", fontSize = 12.sp, color = Color.Black)
                BasicTextField(
                    value = value2,
                    onValueChange = onChange2,
                    textStyle = TextStyle(fontSize = 12.sp, color = Color.Black),
                    cursorBrush = SolidColor(Color.Black),
                    interactionSource = focus2,
                    modifier = Modifier
                        .weight(1f)
                        .background(if (isFocused2) Color(0xFFFFF3CD) else Color.Transparent)
                )
            }
            Spacer(Modifier.height(2.dp))
            Spacer(Modifier.fillMaxWidth().bottomLine())
        }
    }
}

