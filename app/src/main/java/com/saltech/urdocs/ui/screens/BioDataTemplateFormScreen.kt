package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class BioDataTemplateFields(
    val fullName: String = "",
    val dob: String = "",
    val placeOfBirth: String = "",
    val civilStatus: String = "",
    val nationality: String = "",
    val religion: String = "",
    val contactNo: String = "",
    val email: String = "",
    val currentAddress: String = "",
    val eduLevel: String = "",
    val eduSchool: String = "",
    val eduYearGraduated: String = "",
    val workCompany: String = "",
    val workPosition: String = "",
    val workInclusiveDates: String = "",
    val skills: String = "",
    val references: String = ""
)

@Composable
fun BioDataTemplateFormScreen(
    templateName: String,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    var data by remember { mutableStateOf(BioDataTemplateFields()) }
    val resId = remember(templateName) {
        context.resources.getIdentifier(templateName, "drawable", context.packageName)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (resId != 0) {
            Image(
                painter = painterResource(id = resId),
                contentDescription = templateName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                TemplateField("Full Name", data.fullName) { data = data.copy(fullName = it) }
                TemplateField("Date of Birth", data.dob) { data = data.copy(dob = it) }
                TemplateField("Place of Birth", data.placeOfBirth) { data = data.copy(placeOfBirth = it) }
                TemplateField("Civil Status", data.civilStatus) { data = data.copy(civilStatus = it) }
                TemplateField("Nationality", data.nationality) { data = data.copy(nationality = it) }
                TemplateField("Religion", data.religion) { data = data.copy(religion = it) }
                TemplateField("Contact No.", data.contactNo) { data = data.copy(contactNo = it) }
                TemplateField("Email Address", data.email) { data = data.copy(email = it) }
                TemplateField("Current Address", data.currentAddress) { data = data.copy(currentAddress = it) }
                Text("EDUCATIONAL ATTAINMENT", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.padding(top = 8.dp))
                TemplateField("Level", data.eduLevel) { data = data.copy(eduLevel = it) }
                TemplateField("School", data.eduSchool) { data = data.copy(eduSchool = it) }
                TemplateField("Year Graduated", data.eduYearGraduated) { data = data.copy(eduYearGraduated = it) }

                Text("WORK EXPERIENCE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.padding(top = 8.dp))
                TemplateField("Company / Organization", data.workCompany) { data = data.copy(workCompany = it) }
                TemplateField("Position", data.workPosition) { data = data.copy(workPosition = it) }
                TemplateField("Inclusive Dates", data.workInclusiveDates) { data = data.copy(workInclusiveDates = it) }
                TemplateField("Skills", data.skills) { data = data.copy(skills = it) }
                TemplateField("References", data.references) { data = data.copy(references = it) }
                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun TemplateField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Text(label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.35f))
                .padding(10.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                cursorBrush = androidx.compose.ui.graphics.SolidColor(Color.White),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
