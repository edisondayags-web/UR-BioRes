package com.saltech.urdocs.ui.screens
// ⚠️ 1) PALITAN itong "package" line ng EXACT package niyo mula sa ORIGINAL Bio-Data file.
// ⚠️ 2) Yung function name na "BioDataScreen()" sa baba — gawin mong SAME sa pangalan
//        (at parameters, kung meron — navController, viewModel, etc.) ng ORIGINAL
//        composable niyo, para hindi masira yung tawag mula sa "Pumili ng Gagawin" menu.

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.graphicsLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.asImageBitmap
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
    val paperWidthDp = 600.dp
    val paperHeightDp = 900.dp

    var data by remember { mutableStateOf(BioDataFields()) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    // TODO: ikonekta dito yung resulta ng existing 2x2 ML Kit selfie capture niyo
    // var photoBitmap: android.graphics.Bitmap? by remember { mutableStateOf(null) }

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
                .then(
    Modifier.graphicsLayer(
        scaleX = scale,
        scaleY = scale,
        translationX = offset.x,
        translationY = offset.y
    )
)
                )
                .width(paperWidthDp)
                .height(paperHeightDp)
                .background(Color.White)
                .padding(24.dp)
        ) {
            Column(Modifier.fillMaxSize()) {

                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        "BIO-DATA",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .size(90.dp, 110.dp)
                            .border(1.dp, Color.Black)
                    ) {
                        if (processedSelfie != null) {
    androidx.compose.foundation.Image(
        bitmap = processedSelfie.asImageBitmap(),
        contentDescription = "2x2 Photo",
        modifier = Modifier.fillMaxSize()
    )
}
                    }
                }

                Spacer(Modifier.height(10.dp))
                Text("PERSONAL DATA", fontWeight = FontWeight.Bold, fontSize = 15.sp)

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
                Text("EDUCATIONAL BACKGROUND", fontWeight = FontWeight.Bold, fontSize = 15.sp)
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
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun FieldLine(label: String, value: String, onChange: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 7.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Text("$label: ", fontSize = 12.sp)
        BasicTextField(
            value = value,
            onValueChange = onChange,
            textStyle = TextStyle(fontSize = 12.sp),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TwoCol(
    label1: String, value1: String, onChange1: (String) -> Unit,
    label2: String, value2: String, onChange2: (String) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth().padding(top = 7.dp)) {
        Row(Modifier.weight(1f), verticalAlignment = Alignment.Bottom) {
            Text("$label1: ", fontSize = 12.sp)
            BasicTextField(
                value = value1,
                onValueChange = onChange1,
                textStyle = TextStyle(fontSize = 12.sp),
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.width(10.dp))
        Row(Modifier.weight(1f), verticalAlignment = Alignment.Bottom) {
            Text("$label2: ", fontSize = 12.sp)
            BasicTextField(
                value = value2,
                onValueChange = onChange2,
                textStyle = TextStyle(fontSize = 12.sp),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

