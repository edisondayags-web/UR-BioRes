package com.saltech.urdocs.ui.screens

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saltech.urdocs.util.BioDataFull
import com.saltech.urdocs.util.DocumentRenderer
import com.saltech.urdocs.util.GallerySaver

@Composable
private fun BField(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value, onValueChange = onChange,
        label = { Text(label) }, modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
private fun SectionLabel(text: String) {
    Spacer(modifier = Modifier.height(8.dp))
    Text(text, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
    Spacer(modifier = Modifier.height(8.dp))
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
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            Text("👁️ Preview", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            Image(
                bitmap = previewBitmap!!.asImageBitmap(),
                contentDescription = "Bio-Data preview",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val saved = GallerySaver.saveBitmap(context, previewBitmap!!, "BioData_${System.currentTimeMillis()}")
                    Toast.makeText(
                        context,
                        if (saved) "Na-save sa Gallery (Pictures/UR Docs)!" else "Hindi na-save, subukan ulit.",
                        Toast.LENGTH_LONG
                    ).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("⬇️ I-download / I-save sa Gallery") }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = { previewBitmap = null }, modifier = Modifier.fillMaxWidth()) {
                Text("✏️ Bumalik sa Form")
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("📝 Bio-Data Maker", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (processedSelfie != null) {
            Image(bitmap = processedSelfie.asImageBitmap(), contentDescription = "2x2 ID Photo", modifier = Modifier.size(120.dp))
            Spacer(modifier = Modifier.height(8.dp))
        }
        OutlinedButton(onClick = onTakeSelfie, modifier = Modifier.fillMaxWidth()) {
            Text(if (processedSelfie == null) "📸 Kumuha ng 2x2 Selfie" else "📸 Palitan ang Selfie")
        }

        SectionLabel("Personal Data")
        BField("Buong Pangalan", fullName) { fullName = it }
        BField("Gender", gender) { gender = it }
        BField("Petsa ng Kapanganakan", birthDate) { birthDate = it }
        BField("Current Address", currentAddress) { currentAddress = it }
        BField("Permanent Address", permanentAddress) { permanentAddress = it }
        BField("Age", age) { age = it }
        BField("Date", date) { date = it }
        BField("Occupation", occupation) { occupation = it }
        BField("Telephone", telephone) { telephone = it }
        BField("Civil Status", civilStatus) { civilStatus = it }
        BField("Cellphone", cellphone) { cellphone = it }
        BField("Place of Birth", placeOfBirth) { placeOfBirth = it }
        BField("Email", email) { email = it }
        BField("Height", height) { height = it }
        BField("Citizenship", citizenship) { citizenship = it }
        BField("Weight", weight) { weight = it }
        BField("Religion", religion) { religion = it }

        SectionLabel("Family Background")
        BField("Father's Name", fatherName) { fatherName = it }
        BField("Father's Occupation", fatherOccupation) { fatherOccupation = it }
        BField("Mother's Name", motherName) { motherName = it }
        BField("Mother's Occupation", motherOccupation) { motherOccupation = it }
        BField("Language or Dialect Spoken", language) { language = it }

        SectionLabel("Emergency Contact")
        BField("Person to be Contacted", emergencyContact) { emergencyContact = it }
        BField("Address", emergencyAddress) { emergencyAddress = it }
        BField("Contact No.", emergencyContactNo) { emergencyContactNo = it }

        SectionLabel("Educational Background")
        BField("Elementary (School)", elementary) { elementary = it }
        BField("Elementary Year Graduated", elementaryYear) { elementaryYear = it }
        BField("High School (School)", highSchool) { highSchool = it }
        BField("High School Year Graduated", highSchoolYear) { highSchoolYear = it }
        BField("College (School)", college) { college = it }
        BField("College Year Graduated", collegeYear) { collegeYear = it }

        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (fullName.isBlank()) {
                    Toast.makeText(context, "Lagyan muna ng Buong Pangalan.", Toast.LENGTH_SHORT).show()
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
        ) {
            Text("✅ Tapos na")
        }
    }
}
