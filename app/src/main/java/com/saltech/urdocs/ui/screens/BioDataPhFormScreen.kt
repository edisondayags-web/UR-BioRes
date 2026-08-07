package com.saltech.urdocs.ui.screens

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Picture
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.draw.clip

data class BioDataPhFormFields(
    val positionDesired: String = "",
    val date: String = "",
    val name: String = "",
    val gender: String = "",
    val cityAddress: String = "",
    val provincialAddress: String = "",
    val email: String = "",
    val cellphone: String = "",
    val dob: String = "",
    val placeOfBirth: String = "",
    val civilStatus: String = "",
    val citizenship: String = "",
    val height: String = "",
    val weight: String = "",
    val religion: String = "",
    val spouse: String = "",
    val spouseOccupation: String = "",
    val child1Name: String = "", val child1Dob: String = "", val child1Occ: String = "",
    val child2Name: String = "", val child2Dob: String = "", val child2Occ: String = "",
    val child3Name: String = "", val child3Dob: String = "",
    val fathersName: String = "",
    val mothersName: String = "",
    val language: String = "",
    val emergencyContact: String = "",
    val emergencyAddress: String = "",
    val elementary: String = "", val elementaryYear: String = "",
    val highSchool: String = "", val highSchoolYear: String = "",
    val college: String = "", val collegeYear: String = "",
    val certificate: String = "",
    val degreeReceived: String = "",
    val specialSkills: String = "",
    val emp1Company: String = "", val emp1Position: String = "", val emp1From: String = "", val emp1To: String = "",
    val emp2Company: String = "", val emp2Position: String = "", val emp2From: String = "", val emp2To: String = "",
    val ref1Name: String = "", val ref1Position: String = "", val ref1Company: String = "", val ref1Contact: String = "",
    val ref2Name: String = "", val ref2Position: String = "", val ref2Company: String = "", val ref2Contact: String = "",
    val resCertNo: String = "",
    val issuedAt: String = "",
    val issuedOn: String = "",
    val sss: String = "",
    val tin: String = "",
    val pagIbig: String = "",
    val nbiNo: String = "",
    val passportNo: String = ""
)

@Composable
fun BioDataPhFormScreen(
    processedSelfie: Bitmap? = null,
    onTakeSelfie: () -> Unit = {}
) {
    val paperWidthDp = 850.dp
    val paperHeightDp = 1250.dp

    val context = LocalContext.current
    var data by remember { mutableStateOf(BioDataPhFormFields()) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val picture = remember { Picture() }
    val coroutineScope = rememberCoroutineScope()
    var uploadedPhoto by remember { mutableStateOf<Bitmap?>(null) }
    val displaySelfie = uploadedPhoto ?: processedSelfie
    val uploadLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val loaded = com.saltech.urdocs.util.ImageUtils.loadBitmapFromUri(context, uri)
            if (loaded != null) uploadedPhoto = loaded
        }
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

        BoxWithConstraints(modifier = Modifier.weight(1f)) {
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
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(verticalAlignment = Alignment.Top) {
                        Text(
                            "BIO DATA",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.weight(1f)
                        )
                        Box(
                            modifier = Modifier
                                .size(110.dp, 110.dp)
                                .border(1.dp, Color.Black)
                                .clickable(enabled = displaySelfie == null) { onTakeSelfie() }
                        ) {
                            if (displaySelfie != null) {
                                androidx.compose.foundation.Image(
                                    bitmap = displaySelfie.asImageBitmap(),
                                    contentDescription = "Photo",
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Text("+", fontSize = 28.sp, color = Color.Black, modifier = Modifier.align(Alignment.Center))
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                    PhSectionHeader("PERSONAL DATA")

                    PhTwoCol("Position Desired", data.positionDesired, { data = data.copy(positionDesired = it) },
                        "Date", data.date) { data = data.copy(date = it) }
                    PhTwoCol("Name", data.name, { data = data.copy(name = it) },
                        "Gender", data.gender) { data = data.copy(gender = it) }
                    PhFieldLine("City Address", data.cityAddress) { data = data.copy(cityAddress = it) }
                    PhFieldLine("Provincial Address", data.provincialAddress) { data = data.copy(provincialAddress = it) }
                    PhTwoCol("E-mail Address", data.email, { data = data.copy(email = it) },
                        "Cellphone", data.cellphone) { data = data.copy(cellphone = it) }
                    PhTwoCol("Date of Birth", data.dob, { data = data.copy(dob = it) },
                        "Birth of Place", data.placeOfBirth) { data = data.copy(placeOfBirth = it) }
                    PhTwoCol("Civil Status", data.civilStatus, { data = data.copy(civilStatus = it) },
                        "Citizenship", data.citizenship) { data = data.copy(citizenship = it) }
                    PhTwoCol("Height", data.height, { data = data.copy(height = it) },
                        "Weight", data.weight) { data = data.copy(weight = it) }
                    PhFieldLine("Religion", data.religion) { data = data.copy(religion = it) }
                    PhTwoCol("Spouse", data.spouse, { data = data.copy(spouse = it) },
                        "Occupation", data.spouseOccupation) { data = data.copy(spouseOccupation = it) }

                    PhTwoCol("Name of Children", data.child1Name, { data = data.copy(child1Name = it) },
                        "Date of Birth", data.child1Dob) { data = data.copy(child1Dob = it) }
                    PhTwoCol("", data.child2Name, { data = data.copy(child2Name = it) },
                        "Date of Birth", data.child2Dob) { data = data.copy(child2Dob = it) }
                    PhTwoCol("", data.child3Name, { data = data.copy(child3Name = it) },
                        "Occupation", data.child1Occ) { data = data.copy(child1Occ = it) }
                    PhFieldLine("Occupation", data.child2Occ) { data = data.copy(child2Occ = it) }

                    PhFieldLine("Father's Name", data.fathersName) { data = data.copy(fathersName = it) }
                    PhFieldLine("Mother's Name", data.mothersName) { data = data.copy(mothersName = it) }
                    PhFieldLine("Language or dialect spoken and written", data.language) { data = data.copy(language = it) }
                    PhFieldLine("Person to be contacted in case of emergency", data.emergencyContact) { data = data.copy(emergencyContact = it) }
                    PhFieldLine("His or Her address and telephone", data.emergencyAddress) { data = data.copy(emergencyAddress = it) }

                    Spacer(Modifier.height(12.dp))
                    PhSectionHeader("EDUCATIONAL BACKGROUND")

                    PhTwoCol("Elementary", data.elementary, { data = data.copy(elementary = it) },
                        "Year Graduated", data.elementaryYear) { data = data.copy(elementaryYear = it) }
                    PhTwoCol("High School", data.highSchool, { data = data.copy(highSchool = it) },
                        "Year Graduated", data.highSchoolYear) { data = data.copy(highSchoolYear = it) }
                    PhTwoCol("College", data.college, { data = data.copy(college = it) },
                        "Year Graduated", data.collegeYear) { data = data.copy(collegeYear = it) }
                    PhFieldLine("Certificate", data.certificate) { data = data.copy(certificate = it) }
                    PhFieldLine("Degree Received", data.degreeReceived) { data = data.copy(degreeReceived = it) }
                    PhFieldLine("Special Skills", data.specialSkills) { data = data.copy(specialSkills = it) }

                    Spacer(Modifier.height(12.dp))
                    PhSectionHeader("EMPLOYMENT BACKGROUND")

                    PhFieldLine("Company Name", data.emp1Company) { data = data.copy(emp1Company = it) }
                    PhTwoCol("Position", data.emp1Position, { data = data.copy(emp1Position = it) },
                        "From", data.emp1From) { data = data.copy(emp1From = it) }
                    PhFieldLine("To", data.emp1To) { data = data.copy(emp1To = it) }

                    Spacer(Modifier.height(6.dp))
                    PhFieldLine("Company Name", data.emp2Company) { data = data.copy(emp2Company = it) }
                    PhTwoCol("Position", data.emp2Position, { data = data.copy(emp2Position = it) },
                        "From", data.emp2From) { data = data.copy(emp2From = it) }
                    PhFieldLine("To", data.emp2To) { data = data.copy(emp2To = it) }

                    Spacer(Modifier.height(12.dp))
                    PhSectionHeader("CHARACTER REFERENCE")

                    PhTwoCol("Name", data.ref1Name, { data = data.copy(ref1Name = it) },
                        "Company", data.ref1Company) { data = data.copy(ref1Company = it) }
                    PhTwoCol("Position", data.ref1Position, { data = data.copy(ref1Position = it) },
                        "Contact No.", data.ref1Contact) { data = data.copy(ref1Contact = it) }

                    Spacer(Modifier.height(6.dp))
                    PhTwoCol("Name", data.ref2Name, { data = data.copy(ref2Name = it) },
                        "Company", data.ref2Company) { data = data.copy(ref2Company = it) }
                    PhTwoCol("Position", data.ref2Position, { data = data.copy(ref2Position = it) },
                        "Contact No.", data.ref2Contact) { data = data.copy(ref2Contact = it) }

                    Spacer(Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            PhFieldLine("Res. Cert. No.", data.resCertNo) { data = data.copy(resCertNo = it) }
                            PhFieldLine("Issued at", data.issuedAt) { data = data.copy(issuedAt = it) }
                            PhFieldLine("Issued on", data.issuedOn) { data = data.copy(issuedOn = it) }
                            PhFieldLine("SSS", data.sss) { data = data.copy(sss = it) }
                            PhFieldLine("TIN", data.tin) { data = data.copy(tin = it) }
                            PhFieldLine("PAG-IBIG", data.pagIbig) { data = data.copy(pagIbig = it) }
                            PhFieldLine("NBI No.", data.nbiNo) { data = data.copy(nbiNo = it) }
                            PhFieldLine("Passport No.", data.passportNo) { data = data.copy(passportNo = it) }
                        }
                        Spacer(Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "I here certify that the above information is true and correct as to the " +
                                    "best of my knowledge and belief. I also understand that any " +
                                    "misinterpretation will be sufficient reason for withdrawal of an " +
                                    "offer or subsequent dismissal if employed.",
                                fontSize = 11.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(Modifier.height(28.dp))
                    Spacer(
                        Modifier
                            .fillMaxWidth(0.6f)
                            .align(Alignment.CenterHorizontally)
                            .phBottomLine()
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Applicant's Signature",
                        fontSize = 11.sp,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
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
                            .clip(RoundedCornerShape(24.dp))
                            .border(1.5.dp, Color(0xFF0B1530), RoundedCornerShape(24.dp))
                            .clickable { uploadLauncher.launch("image/*") }
                            .padding(horizontal = 18.dp, vertical = 10.dp)
                    ) {
                        Text("\uD83D\uDCE4", fontSize = 16.sp)
                        Spacer(Modifier.width(6.dp))
                        Text("Upload", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
                                savePhFormBitmapToGallery(context, bitmap)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                Brush.horizontalGradient(
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

private fun savePhFormBitmapToGallery(context: android.content.Context, bitmap: Bitmap) {
    val filename = "BioData_PHForm_${System.currentTimeMillis()}.png"
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

private fun Modifier.phBottomLine(
    color: Color = Color.Black,
    thickness: Dp = 1.dp
): Modifier = this
    .height(thickness)
    .drawBehind { drawRect(color = color) }

@Composable
private fun PhSectionHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(vertical = 5.dp, horizontal = 8.dp)
    ) {
        Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
    Spacer(Modifier.height(4.dp))
}

@Composable
private fun PhFieldLine(label: String, value: String, onChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    Column(modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
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
        Spacer(Modifier.fillMaxWidth().phBottomLine())
    }
}

@Composable
private fun PhTwoCol(
    label1: String, value1: String, onChange1: (String) -> Unit,
    label2: String, value2: String, onChange2: (String) -> Unit
) {
    val focus1 = remember { MutableInteractionSource() }
    val isFocused1 by focus1.collectIsFocusedAsState()
    val focus2 = remember { MutableInteractionSource() }
    val isFocused2 by focus2.collectIsFocusedAsState()
    Row(modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.Bottom) {
                if (label1.isNotEmpty()) Text("$label1: ", fontSize = 12.sp, color = Color.Black)
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
            Spacer(Modifier.fillMaxWidth().phBottomLine())
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
            Spacer(Modifier.fillMaxWidth().phBottomLine())
        }
    }
}
