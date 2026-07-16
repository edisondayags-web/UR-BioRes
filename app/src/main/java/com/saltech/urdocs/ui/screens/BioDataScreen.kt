package com.saltech.urdocs.ui.screens

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saltech.urdocs.util.BioDataFull
import com.saltech.urdocs.util.DocumentRenderer
import com.saltech.urdocs.util.GallerySaver

@Composable
private fun BField(label: String, value: String, modifier: Modifier = Modifier, onChange: (String) -> Unit) {
    Column(modifier = modifier) {
        Text(label, color = Color.Black, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
        TextField(
            value = value, onValueChange = onChange,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White, unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                focusedIndicatorColor = Color.Black, unfocusedIndicatorColor = Color.Gray
            ),
            singleLine = true
        )
    }
}

@Composable
private fun SectionLabel(text: String) {
    Spacer(modifier = Modifier.height(14.dp))
    Text(text, color = Color.Black, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(6.dp))
}

@Composable
fun BioDataScreen(
    processedSelfie: Bitmap? = null,
    onTakeSelfie: () -> Unit
) {
    val context = LocalContext.current

    var fullName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var currentAddress by remember { mutableStateOf("") }
    var permanentAddress by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("") }
    var telephone by remember { mutableStateOf("") }
    var civilStatus by remember { mutableStateOf("") }
    var cellphone by remember { mutableStateOf("") }
    var placeOfBirth by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var citizenship by remember { mutableStateOf("Filipino") }
    var weight by remember { mutableStateOf("") }
    var religion by remember { mutableStateOf("") }
    var fatherName by remember { mutableStateOf("") }
    var fatherOccupation by remember { mutableStateOf("") }
    var motherName by remember { mutableStateOf("") }
    var motherOccupation by remember { mutableStateOf("") }
    var language by remember { mutableStateOf("") }
    var emergencyContact by remember { mutableStateOf("") }
    var emergencyAddress by remember { mutableStateOf("") }
    var emergencyContactNo by remember { mutableStateOf("") }
    var elementary by remember { mutableStateOf("") }
    var elementaryYear by remember { mutableStateOf("") }
    var highSchool by remember { mutableStateOf("") }
    var highSchoolYear by remember { mutableStateOf("") }
    var college by remember { mutableStateOf("") }
    var collegeYear by remember { mutableStateOf("") }

    var previewBitmap by remember { mutableStateOf<Bitmap?>(null) }

    if (previewBitmap != null) {
        Column(modifier = Modifier.fillMaxSize().background(Color.Black).verticalScroll(rememberScrollState()).padding(16.dp)) {
            Text("👁️ Preview", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            Image(bitmap = previewBitmap!!.asImageBitmap(), contentDescription = "Bio-Data preview", modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val saved = GallerySaver.saveBitmap(context, previewBitmap!!, "BioData_${System.currentTimeMillis()}")
                    Toast.makeText(context, if (saved) "Na-save sa Gallery (Pictures/UR Docs)!" else "Hindi na-save, subukan ulit.", Toast.LENGTH_LONG).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("⬇️ I-download / I-save sa Gallery") }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = { previewBitmap = null }, modifier = Modifier.fillMaxWidth()) { Text("✏️ Bumalik sa Form") }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("BIO-DATA", color = Color.Black, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))

        Box(modifier = Modifier.size(120.dp).border(2.dp, Color.Black)) {
            if (processedSelfie != null) {
                Image(bitmap = processedSelfie.asImageBitmap(), contentDescription = "2x2 Photo", modifier = Modifier.fillMaxSize())
            } else {
                Text("2x2 PICTURE", color = Color.Black, modifier = Modifier.padding(8.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(onClick = onTakeSelfie, modifier = Modifier.fillMaxWidth()) {
            Text(if (processedSelfie == null) "📸 Kumuha ng 2x2 Selfie" else "📸 Palitan ang Selfie")
        }

        SectionLabel("PERSONAL DATA")
        BField("Name", fullName) { fullName = it }
        BField("Gender", gender) { gender = it }
        BField("Date of Birth", birthDate) { birthDate = it }
        BField("Current Address", currentAddress) { currentAddress = it }
        BField("Permanent Address", permanentAddress) { permanentAddress = it }
        Row {
            BField("Age", age, modifier = Modifier.weight(1f)) { age = it }
            Spacer(modifier = Modifier.width(8.dp))
            BField("Date", date, modifier = Modifier.weight(1f)) { date = it }
        }
        Row {
            BField("Occupation", occupation, modifier = Modifier.weight(1f)) { occupation = it }
            Spacer(modifier = Modifier.width(8.dp))
            BField("Telephone", telephone, modifier = Modifier.weight(1f)) { telephone = it }
        }
        Row {
            BField("Civil Status", civilStatus, modifier = Modifier.weight(1f)) { civilStatus = it }
            Spacer(modifier = Modifier.width(8.dp))
            BField("Cellphone", cellphone, modifier = Modifier.weight(1f)) { cellphone = it }
        }
        Row {
            BField("Place of Birth", placeOfBirth, modifier = Modifier.weight(1f)) { placeOfBirth = it }
            Spacer(modifier = Modifier.width(8.dp))
            BField("Email", email, modifier = Modifier.weight(1f)) { email = it }
        }
        Row {
            BField("Height", height, modifier = Modifier.weight(1f)) { height = it }
            Spacer(modifier = Modifier.width(8.dp))
            BField("Citizenship", citizenship, modifier = Modifier.weight(1f)) { citizenship = it }
        }
        Row {
            BField("Weight", weight, modifier = Modifier.weight(1f)) { weight = it }
            Spacer(modifier = Modifier.width(8.dp))
            BField("Religion", religion, modifier = Modifier.weight(1f)) { religion = it }
        }
        Row {
            BField("Father's Name", fatherName, modifier = Modifier.weight(1f)) { fatherName = it }
            Spacer(modifier = Modifier.width(8.dp))
            BField("Occupation", fatherOccupation, modifier = Modifier.weight(1f)) { fatherOccupation = it }
        }
        Row {
            BField("Mother's Name", motherName, modifier = Modifier.weight(1f)) { motherName = it }
            Spacer(modifier = Modifier.width(8.dp))
            BField("Occupation", motherOccupation, modifier = Modifier.weight(1f)) { motherOccupation = it }
        }
        BField("Language or Dialect Spoken", language) { language = it }
        BField("Person to be Contacted (Emergency)", emergencyContact) { emergencyContact = it }
        Row {
            BField("Address", emergencyAddress, modifier = Modifier.weight(1f)) { emergencyAddress = it }
            Spacer(modifier = Modifier.width(8.dp))
            BField("Contact No.", emergencyContactNo, modifier = Modifier.weight(1f)) { emergencyContactNo = it }
        }

        SectionLabel("EDUCATIONAL BACKGROUND")
        Row {
            BField("Elementary", elementary, modifier = Modifier.weight(1f)) { elementary = it }
            Spacer(modifier = Modifier.width(8.dp))
            BField("Year Graduated", elementaryYear, modifier = Modifier.weight(1f)) { elementaryYear = it }
        }
        Row {
            BField("High School", highSchool, modifier = Modifier.weight(1f)) { highSchool = it }
            Spacer(modifier = Modifier.width(8.dp))
            BField("Year Graduated", highSchoolYear, modifier = Modifier.weight(1f)) { highSchoolYear = it }
        }
        Row {
            BField("College", college, modifier = Modifier.weight(1f)) { college = it }
            Spacer(modifier = Modifier.width(8.dp))
            BField("Year Graduated", collegeYear, modifier = Modifier.weight(1f)) { collegeYear = it }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                if (fullName.isBlank()) {
                    Toast.makeText(context, "Lagyan muna ng Name.", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                val data = BioDataFull(
                    fullName = fullName, gender = gender, birthDate = birthDate,
                    currentAddress = currentAddress, permanentAddress = permanentAddress,
                    age = age, date = date, occupation = occupation, telephone = telephone,
                    civilStatus = civilStatus, cellphone = cellphone, placeOfBirth = placeOfBirth,
                    email = email, height = height, citizenship = citizenship, weight = weight,
                    religion = religion, fatherName = fatherName, fatherOccupation = fatherOccupation,
                    motherName = motherName, motherOccupation = motherOccupation, language = language,
                    emergencyContact = emergencyContact, emergencyAddress = emergencyAddress,
                    emergencyContactNo = emergencyContactNo, elementary = elementary,
                    elementaryYear = elementaryYear, highSchool = highSchool,
                    highSchoolYear = highSchoolYear, college = college, collegeYear = collegeYear
                )
                previewBitmap = DocumentRenderer.renderBioData(data, processedSelfie)
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("✅ Tapos na") }
    }
}
