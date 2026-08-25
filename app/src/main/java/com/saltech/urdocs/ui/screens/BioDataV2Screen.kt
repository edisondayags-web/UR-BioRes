package com.saltech.urdocs.ui.screens

import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdRequest
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Picture
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ---------- Field data (blank by default - user fills everything in) ----------

data class BioDataV2Fields(
    val positionDesired: String = "", val date: String = "",
    val name: String = "", val nickname: String = "",
    val dob: String = "", val placeOfBirth: String = "", val age: String = "",
    val gender: String = "", val height: String = "", val weight: String = "",
    val email: String = "", val contactNo: String = "",
    val religion: String = "", val citizenship: String = "", val civilStatus: String = "",
    val spouseName: String = "", val spouseOccupation: String = "",

    val fathersName: String = "", val fathersOcc: String = "",
    val mothersName: String = "", val mothersOcc: String = "",
    val emergencyContact: String = "", val relationship: String = "",
    val emergencyContactNo: String = "", val emergencyAddress: String = "",

    val presentAddress: String = "", val permanentAddress: String = "",

    val edu1Level: String = "High School", val edu1School: String = "", val edu1Year: String = "",
    val edu2Level: String = "Bachelor's Degree", val edu2School: String = "", val edu2Year: String = "",

    val lang1: String = "", val lang2: String = "", val lang3: String = "",
    val skill1: String = "", val skill2: String = "", val skill3: String = "",
    val hobby1: String = "", val hobby2: String = "", val hobby3: String = "",

    val emp1Company: String = "", val emp1Position: String = "", val emp1From: String = "", val emp1To: String = "",
    val emp2Company: String = "", val emp2Position: String = "", val emp2From: String = "", val emp2To: String = "",

    val ref1Name: String = "", val ref1Position: String = "", val ref1Company: String = "", val ref1Contact: String = "",
    val ref2Name: String = "", val ref2Position: String = "", val ref2Company: String = "", val ref2Contact: String = "",

    val declarantName: String = "",
    val place: String = ""
)

/** isBlack = true -> black accent (boxed section bars). false -> blue accent (thin-line style). */
@Composable
fun BioDataV2Screen(
    isBlack: Boolean,
    processedSelfie: Bitmap? = null,
) {
    val accent = if (isBlack) Color.Black else Color(0xFF1D6FE0)
    val paperWidthDp = 850.dp
    val paperHeightDp = 1250.dp

    val context = LocalContext.current
    var data by remember { mutableStateOf(BioDataV2Fields()) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val picture = remember { Picture() }
    val coroutineScope = rememberCoroutineScope()

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
            val fitScaleX = maxWidth / paperWidthDp
            val fitScaleY = maxHeight / paperHeightDp
            var zoomFactor by remember { mutableStateOf(1f) }

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            zoomFactor = (zoomFactor * zoom).coerceIn(1f, 4f)
                            offset = if (zoomFactor <= 1f) Offset.Zero else offset + pan
                        }
                    }
                    .graphicsLayer(
                        scaleX = fitScaleX * zoomFactor,
                        scaleY = fitScaleY * zoomFactor,
                        translationX = offset.x,
                        translationY = offset.y
                    )
                    .requiredWidth(paperWidthDp)
                    .requiredHeight(paperHeightDp)
                    .drawWithCache {
                        val width = size.width.toInt().coerceAtLeast(1)
                        val height = size.height.toInt().coerceAtLeast(1)
                        onDrawWithContent {
                            val pictureCanvas = androidx.compose.ui.graphics.Canvas(picture.beginRecording(width, height))
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
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "BIO-DATA",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.align(Alignment.Center)
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(90.dp, 100.dp)
                                .clip(RoundedCornerShape(topEnd = 18.dp))
                                .border(1.dp, accent, RoundedCornerShape(topEnd = 18.dp))
                        ) {
                            if (processedSelfie != null) {
                                Image(
                                    bitmap = processedSelfie.asImageBitmap(),
                                    contentDescription = "Photo",
                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Text("+", fontSize = 24.sp, color = accent, modifier = Modifier.align(Alignment.Center))
                            }
                        }
                    }

                    Spacer(Modifier.height(14.dp))
                    V2Header("PERSONAL INFORMATION", accent, isBlack)
                    V2Two("Position Desired", data.positionDesired, { data = data.copy(positionDesired = it) }, "Date", data.date) { data = data.copy(date = it) }
                    V2Two("Name", data.name, { data = data.copy(name = it) }, "Nickname", data.nickname) { data = data.copy(nickname = it) }
                    V2Three("Date of Birth", data.dob, { data = data.copy(dob = it) }, "Place of Birth", data.placeOfBirth, { data = data.copy(placeOfBirth = it) }, "Age", data.age) { data = data.copy(age = it) }
                    V2Three("Gender", data.gender, { data = data.copy(gender = it) }, "Height", data.height, { data = data.copy(height = it) }, "Weight", data.weight) { data = data.copy(weight = it) }
                    V2Two("Email", data.email, { data = data.copy(email = it) }, "Contact No", data.contactNo) { data = data.copy(contactNo = it) }
                    V2Three("Religion", data.religion, { data = data.copy(religion = it) }, "Citizenship", data.citizenship, { data = data.copy(citizenship = it) }, "Civil Status", data.civilStatus) { data = data.copy(civilStatus = it) }
                    V2Two("Spouse name", data.spouseName, { data = data.copy(spouseName = it) }, "Spouse Occupation", data.spouseOccupation) { data = data.copy(spouseOccupation = it) }

                    Spacer(Modifier.height(10.dp))
                    V2Header("FAMILY BACKGROUND", accent, isBlack)
                    V2Two("Father's Name", data.fathersName, { data = data.copy(fathersName = it) }, "Occupation", data.fathersOcc) { data = data.copy(fathersOcc = it) }
                    V2Two("Mother's Name", data.mothersName, { data = data.copy(mothersName = it) }, "Occupation", data.mothersOcc) { data = data.copy(mothersOcc = it) }
                    V2Field("Person to be contacted in case of Emergency", data.emergencyContact) { data = data.copy(emergencyContact = it) }
                    V2Two("Relationship", data.relationship, { data = data.copy(relationship = it) }, "Contact No", data.emergencyContactNo) { data = data.copy(emergencyContactNo = it) }
                    V2Field("Address", data.emergencyAddress) { data = data.copy(emergencyAddress = it) }

                    Spacer(Modifier.height(10.dp))
                    V2Header("COMMUNICATION DETAILS", accent, isBlack)
                    Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                        Column(Modifier.weight(1f)) {
                            Text("Present Address", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            Spacer(Modifier.height(4.dp))
                            BasicTextField(
                                value = data.presentAddress, onValueChange = { data = data.copy(presentAddress = it) },
                                textStyle = TextStyle(fontSize = 11.sp, color = Color.Black),
                                cursorBrush = SolidColor(Color.Black),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(2.dp))
                            Spacer(Modifier.fillMaxWidth().v2Line(accent))
                        }
                        Spacer(Modifier.width(16.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Permanent Address", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            Spacer(Modifier.height(4.dp))
                            BasicTextField(
                                value = data.permanentAddress, onValueChange = { data = data.copy(permanentAddress = it) },
                                textStyle = TextStyle(fontSize = 11.sp, color = Color.Black),
                                cursorBrush = SolidColor(Color.Black),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(2.dp))
                            Spacer(Modifier.fillMaxWidth().v2Line(accent))
                        }
                    }

                    Spacer(Modifier.height(10.dp))
                    V2Header("EDUCATIONAL BACKGROUND", accent, isBlack)
                    Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                        Text("Level of Education", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.weight(1f))
                        Text("School / College / University name", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.weight(1.6f))
                        Text("Year Graduated", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.weight(1f))
                    }
                    V2EduRow(data.edu1Level, data.edu1School, { data = data.copy(edu1School = it) }, data.edu1Year) { data = data.copy(edu1Year = it) }
                    V2EduRow(data.edu2Level, data.edu2School, { data = data.copy(edu2School = it) }, data.edu2Year) { data = data.copy(edu2Year = it) }

                    Spacer(Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.weight(1f)) {
                            V2Header("LANGUAGE KNOWN", accent, isBlack)
                            V2Bullet(data.lang1) { data = data.copy(lang1 = it) }
                            V2Bullet(data.lang2) { data = data.copy(lang2 = it) }
                            V2Bullet(data.lang3) { data = data.copy(lang3 = it) }
                        }
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            V2Header("SKILLS", accent, isBlack)
                            V2Bullet(data.skill1) { data = data.copy(skill1 = it) }
                            V2Bullet(data.skill2) { data = data.copy(skill2 = it) }
                            V2Bullet(data.skill3) { data = data.copy(skill3 = it) }
                        }
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            V2Header("HOBBIES", accent, isBlack)
                            V2Bullet(data.hobby1) { data = data.copy(hobby1 = it) }
                            V2Bullet(data.hobby2) { data = data.copy(hobby2 = it) }
                            V2Bullet(data.hobby3) { data = data.copy(hobby3 = it) }
                        }
                    }

                    Spacer(Modifier.height(10.dp))
                    V2Header("EMPLOYMENT RECORD", accent, isBlack)
                    V2Field("Company Name", data.emp1Company) { data = data.copy(emp1Company = it) }
                    V2Three("Position", data.emp1Position, { data = data.copy(emp1Position = it) }, "From", data.emp1From, { data = data.copy(emp1From = it) }, "To", data.emp1To) { data = data.copy(emp1To = it) }
                    Spacer(Modifier.height(6.dp))
                    V2Field("Company Name", data.emp2Company) { data = data.copy(emp2Company = it) }
                    V2Three("Position", data.emp2Position, { data = data.copy(emp2Position = it) }, "From", data.emp2From, { data = data.copy(emp2From = it) }, "To", data.emp2To) { data = data.copy(emp2To = it) }

                    Spacer(Modifier.height(10.dp))
                    V2Header("CHARACTER REFERENCES", accent, isBlack)
                    V2Two("Name", data.ref1Name, { data = data.copy(ref1Name = it) }, "Company", data.ref1Company) { data = data.copy(ref1Company = it) }
                    V2Two("Position", data.ref1Position, { data = data.copy(ref1Position = it) }, "Contact No", data.ref1Contact) { data = data.copy(ref1Contact = it) }
                    Spacer(Modifier.height(6.dp))
                    V2Two("Name", data.ref2Name, { data = data.copy(ref2Name = it) }, "Company", data.ref2Company) { data = data.copy(ref2Company = it) }
                    V2Two("Position", data.ref2Position, { data = data.copy(ref2Position = it) }, "Contact No", data.ref2Contact) { data = data.copy(ref2Contact = it) }

                    Spacer(Modifier.height(10.dp))
                    V2Header("DECLARATION", accent, isBlack)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("I ", fontSize = 11.sp, color = Color.Black)
                        V2InlineField(data.declarantName, 160.dp) { data = data.copy(declarantName = it) }
                        Text(
                            ", hereby declare that the above mentioned information is correct to the best of my knowledge. If found any information are wrong or false during the further next process, You can reject my application without any Notice.",
                            fontSize = 11.sp, color = Color.Black
                        )
                    }

                    Spacer(Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.weight(1f)) {
                            Text("Place: ", fontSize = 11.sp, color = Color.Black)
                            V2InlineField(data.place, 140.dp) { data = data.copy(place = it) }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Spacer(Modifier.fillMaxWidth().v2Line(accent))
                            Text("Signature", fontSize = 10.sp, color = Color.Black, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                        }
                    }

                    Spacer(Modifier.weight(1f))
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
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            zoomFactor = 1f
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
                                saveBioDataV2ToGallery(context, bitmap, if (isBlack) "BioDataBlack" else "BioDataBlue")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                Brush.horizontalGradient(listOf(accent, Color(0xFF1A1A1A), Color(0xFF0B1530)))
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

private fun saveBioDataV2ToGallery(context: android.content.Context, bitmap: Bitmap, prefix: String) {
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

private fun Modifier.v2Line(color: Color, thickness: Dp = 1.dp): Modifier =
    this.height(thickness).drawBehind { drawRect(color = color) }

@Composable
private fun V2Header(title: String, accent: Color, isBlack: Boolean) {
    if (isBlack) {
        Box(
            modifier = Modifier.fillMaxWidth().background(accent).padding(vertical = 5.dp, horizontal = 8.dp)
        ) {
            Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    } else {
        Column {
            Text(title, color = accent, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(Modifier.height(2.dp))
            Spacer(Modifier.fillMaxWidth().v2Line(accent, 2.dp))
        }
    }
    Spacer(Modifier.height(6.dp))
}

@Composable
private fun V2InlineField(value: String, width: Dp, onChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    Box(
        modifier = Modifier
            .padding(horizontal = 2.dp)
            .background(if (isFocused) Color(0xFFFFF3CD) else Color.Transparent)
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    val y = size.height - 1.dp.toPx()
                    drawLine(Color.Black, Offset(0f, y), Offset(size.width, y), strokeWidth = 1.dp.toPx())
                }
            }
    ) {
        BasicTextField(
            value = value, onValueChange = onChange,
            textStyle = TextStyle(fontSize = 11.sp, color = Color.Black),
            cursorBrush = SolidColor(Color.Black),
            interactionSource = interactionSource,
            modifier = Modifier.widthIn(min = width)
        )
    }
}

@Composable
private fun V2Field(label: String, value: String, onChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    Column(modifier = Modifier.fillMaxWidth().padding(top = 5.dp)) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text("$label: ", fontSize = 11.sp, color = Color.Black)
            BasicTextField(
                value = value, onValueChange = onChange,
                textStyle = TextStyle(fontSize = 11.sp, color = Color.Black),
                cursorBrush = SolidColor(Color.Black),
                interactionSource = interactionSource,
                modifier = Modifier.weight(1f).background(if (isFocused) Color(0xFFFFF3CD) else Color.Transparent)
            )
        }
        Spacer(Modifier.height(2.dp))
        Spacer(Modifier.fillMaxWidth().v2Line(Color.Black))
    }
}

@Composable
private fun V2Two(l1: String, v1: String, c1: (String) -> Unit, l2: String, v2: String, c2: (String) -> Unit) {
    val f1 = remember { MutableInteractionSource() }; val fo1 by f1.collectIsFocusedAsState()
    val f2 = remember { MutableInteractionSource() }; val fo2 by f2.collectIsFocusedAsState()
    Row(modifier = Modifier.fillMaxWidth().padding(top = 5.dp)) {
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text("$l1: ", fontSize = 11.sp, color = Color.Black)
                BasicTextField(v1, c1, textStyle = TextStyle(fontSize = 11.sp, color = Color.Black), cursorBrush = SolidColor(Color.Black), interactionSource = f1,
                    modifier = Modifier.weight(1f).background(if (fo1) Color(0xFFFFF3CD) else Color.Transparent))
            }
            Spacer(Modifier.height(2.dp)); Spacer(Modifier.fillMaxWidth().v2Line(Color.Black))
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text("$l2: ", fontSize = 11.sp, color = Color.Black)
                BasicTextField(v2, c2, textStyle = TextStyle(fontSize = 11.sp, color = Color.Black), cursorBrush = SolidColor(Color.Black), interactionSource = f2,
                    modifier = Modifier.weight(1f).background(if (fo2) Color(0xFFFFF3CD) else Color.Transparent))
            }
            Spacer(Modifier.height(2.dp)); Spacer(Modifier.fillMaxWidth().v2Line(Color.Black))
        }
    }
}

@Composable
private fun V2Three(
    l1: String, v1: String, c1: (String) -> Unit,
    l2: String, v2: String, c2: (String) -> Unit,
    l3: String, v3: String, c3: (String) -> Unit
) {
    val f1 = remember { MutableInteractionSource() }; val fo1 by f1.collectIsFocusedAsState()
    val f2 = remember { MutableInteractionSource() }; val fo2 by f2.collectIsFocusedAsState()
    val f3 = remember { MutableInteractionSource() }; val fo3 by f3.collectIsFocusedAsState()
    Row(modifier = Modifier.fillMaxWidth().padding(top = 5.dp)) {
        listOf(Triple(l1, v1, c1 to f1 to fo1), Triple(l2, v2, c2 to f2 to fo2), Triple(l3, v3, c3 to f3 to fo3)).forEachIndexed { i, (label, value, rest) ->
            val (cb, fi) = rest.first
            val focused = rest.second
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("$label: ", fontSize = 11.sp, color = Color.Black)
                    BasicTextField(value, cb, textStyle = TextStyle(fontSize = 11.sp, color = Color.Black), cursorBrush = SolidColor(Color.Black), interactionSource = fi,
                        modifier = Modifier.weight(1f).background(if (focused) Color(0xFFFFF3CD) else Color.Transparent))
                }
                Spacer(Modifier.height(2.dp)); Spacer(Modifier.fillMaxWidth().v2Line(Color.Black))
            }
            if (i != 2) Spacer(Modifier.width(8.dp))
        }
    }
}

@Composable
private fun V2Bullet(value: String, onChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    Row(modifier = Modifier.fillMaxWidth().padding(top = 3.dp), verticalAlignment = Alignment.Bottom) {
        Text("• ", fontSize = 11.sp, color = Color.Black)
        BasicTextField(
            value = value, onValueChange = onChange,
            textStyle = TextStyle(fontSize = 11.sp, color = Color.Black),
            cursorBrush = SolidColor(Color.Black),
            interactionSource = interactionSource,
            modifier = Modifier.weight(1f).background(if (isFocused) Color(0xFFFFF3CD) else Color.Transparent)
        )
    }
}

@Composable
private fun V2EduRow(level: String, school: String, onSchool: (String) -> Unit, year: String, onYear: (String) -> Unit) {
    val f1 = remember { MutableInteractionSource() }; val fo1 by f1.collectIsFocusedAsState()
    val f2 = remember { MutableInteractionSource() }; val fo2 by f2.collectIsFocusedAsState()
    Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), verticalAlignment = Alignment.Bottom) {
        Text(level, fontSize = 11.sp, color = Color.Black, modifier = Modifier.weight(1f))
        BasicTextField(school, onSchool, textStyle = TextStyle(fontSize = 11.sp, color = Color.Black), cursorBrush = SolidColor(Color.Black), interactionSource = f1,
            modifier = Modifier.weight(1.6f).background(if (fo1) Color(0xFFFFF3CD) else Color.Transparent))
        BasicTextField(year, onYear, textStyle = TextStyle(fontSize = 11.sp, color = Color.Black), cursorBrush = SolidColor(Color.Black), interactionSource = f2,
            modifier = Modifier.weight(1f).background(if (fo2) Color(0xFFFFF3CD) else Color.Transparent))
    }
    Spacer(Modifier.height(2.dp))
    Spacer(Modifier.fillMaxWidth().v2Line(Color.Black))
}
