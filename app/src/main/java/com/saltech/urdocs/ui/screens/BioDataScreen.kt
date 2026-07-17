package com.saltech.urdocs.ui.screens
// ⚠️ 1) PALITAN itong "package" line ng EXACT package niyo mula sa ORIGINAL Bio-Data file.
// ⚠️ 2) Yung function name na "BioDataScreen()" sa baba — gawin mong SAME sa pangalan
//        (at parameters, kung meron — navController, viewModel, etc.) ng ORIGINAL
//        composable niyo, para hindi masira yung tawag mula sa "Pumili ng Gagawin" menu.

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Picture
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

    // laki ng "papel" — pwede dagdagan ang height kung kulang pa sa fields niyo
    val paperWidthDp = 750.dp
    val paperHeightDp = 1250.dp

    var data by remember { mutableStateOf(BioDataFields()) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val context = LocalContext.current
    val picture = remember { Picture() }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFCFCFCF))
    ) {
        // fit-to-screen scale — dito nanggagaling yung "buong bond paper agad lalabas"
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
                .background(Color.White)
                .padding(24.dp)
                .drawWithCache {
                    // dito kinukuha yung "litrato" ng buong papel para sa Download button
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
                    Box(
                        modifier = Modifier
                            .size(90.dp, 110.dp)
                            .border(1.dp, Color.Black)
                            .clickable(enabled = processedSelfie == null) { onTakeSelfie() }
                    ) {
                        if (processedSelfie != null) {
                            androidx.compose.foundation.Image(
                                bitmap = processedSelfie.asImageBitmap(),
                                contentDescription = "2x2 Photo",
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Text(
                                "+",
                                fontSize = 32.sp,
                                color = Color.Black,
                                modifier = Modifier.align(Alignment.Center)
                            )
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

                // Date / Signature lines — kagaya ng standard PH bio-data template
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
                            modifier = Modifier.fillMaxWidth(),
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
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }

        // Download button — laging nakikita sa taas, hindi kasama sa zoom/pan
        Button(
            onClick = {
                val bitmap = Bitmap.createBitmap(
                    picture.width.coerceAtLeast(1),
                    picture.height.coerceAtLeast(1),
                    Bitmap.Config.ARGB_8888
                )
                val canvas = android.graphics.Canvas(bitmap)
                canvas.drawColor(android.graphics.Color.WHITE)
                canvas.drawPicture(picture)
                saveBitmapToGallery(context, bitmap)
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text("Download")
        }
    }
}

// I-se-save yung bitmap sa Photos/Gallery ng phone, sa loob ng "Pictures/URDocs" folder
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
    }
}

// Guhit na linya sa ilalim ng isang element — ginagamit bilang "sulatan" na underline
// sa mga blangko (field values) at sa Date/Signature sa dulo ng bio-data.
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
    Column(modifier = Modifier.fillMaxWidth().padding(top = 7.dp)) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text("$label: ", fontSize = 12.sp, color = Color.Black)
            BasicTextField(
                value = value,
                onValueChange = onChange,
                textStyle = TextStyle(fontSize = 12.sp, color = Color.Black),
                cursorBrush = SolidColor(Color.Black),
                modifier = Modifier.weight(1f)
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
    Row(modifier = Modifier.fillMaxWidth().padding(top = 7.dp)) {
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text("$label1: ", fontSize = 12.sp, color = Color.Black)
                BasicTextField(
                    value = value1,
                    onValueChange = onChange1,
                    textStyle = TextStyle(fontSize = 12.sp, color = Color.Black),
                    cursorBrush = SolidColor(Color.Black),
                    modifier = Modifier.weight(1f)
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
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(2.dp))
            Spacer(Modifier.fillMaxWidth().bottomLine())
        }
    }
}
