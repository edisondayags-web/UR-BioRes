package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ResumeTemplateFields(
    val fullName: String = "",
    val professionalTitle: String = "",
    val phone: String = "",
    val email: String = "",
    val location: String = "",
    val linkedin: String = "",
    val website: String = "",
    val aboutMe: String = "",
    val edu1Degree: String = "", val edu1School: String = "", val edu1Years: String = "",
    val edu2Degree: String = "", val edu2School: String = "", val edu2Years: String = "",
    val skills: String = "",
    val exp1Position: String = "", val exp1Company: String = "", val exp1Dates: String = "", val exp1Desc: String = "",
    val exp2Position: String = "", val exp2Company: String = "", val exp2Dates: String = "", val exp2Desc: String = "",
    val refName: String = "", val refPositionCompany: String = "", val refContact: String = ""
)

@Composable
fun ResumeTemplateFormScreen(
    templateName: String,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    var data by remember { mutableStateOf(ResumeTemplateFields()) }
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
                ResumeField("👤 Full Name", data.fullName, "Ex: Juan Dela Cruz") { data = data.copy(fullName = it) }
                ResumeField("💼 Professional Title", data.professionalTitle, "Ex: Marketing Specialist") { data = data.copy(professionalTitle = it) }

                SectionLabel("CONTACT")
                ResumeField("📞 Phone", data.phone, "Ex: 09123456789") { data = data.copy(phone = it) }
                ResumeField("📧 Email", data.email, "Ex: yourname@email.com") { data = data.copy(email = it) }
                ResumeField("📍 Location", data.location, "Ex: Quezon City, Philippines") { data = data.copy(location = it) }
                ResumeField("🔗 LinkedIn", data.linkedin, "Ex: linkedin.com/in/username") { data = data.copy(linkedin = it) }
                ResumeField("🌐 Website", data.website, "Ex: yourwebsite.com") { data = data.copy(website = it) }

                SectionLabel("ABOUT ME")
                ResumeField("📝 Summary", data.aboutMe, "Ex: Dedicated professional with 3 years of experience...", singleLine = false) { data = data.copy(aboutMe = it) }

                SectionLabel("EDUCATION")
                ResumeField("🎓 Degree / Major", data.edu1Degree, "Ex: BS Information Technology") { data = data.copy(edu1Degree = it) }
                ResumeField("🏫 University", data.edu1School, "Ex: University Name") { data = data.copy(edu1School = it) }
                ResumeField("📅 Years", data.edu1Years, "Ex: 2020 - 2024") { data = data.copy(edu1Years = it) }

                ResumeField("🎓 Degree / Major", data.edu2Degree, "Ex: (optional second entry)") { data = data.copy(edu2Degree = it) }
                ResumeField("🏫 University", data.edu2School, "Ex: University Name") { data = data.copy(edu2School = it) }
                ResumeField("📅 Years", data.edu2Years, "Ex: 2016 - 2018") { data = data.copy(edu2Years = it) }

                SectionLabel("SKILLS")
                ResumeField("⭐ Skills", data.skills, "Ex: Problem Solving, Communication, Teamwork", singleLine = false) { data = data.copy(skills = it) }

                SectionLabel("EXPERIENCE")
                ResumeField("💼 Job Position", data.exp1Position, "Ex: Sales Associate") { data = data.copy(exp1Position = it) }
                ResumeField("🏢 Company Name", data.exp1Company, "Ex: Company Name") { data = data.copy(exp1Company = it) }
                ResumeField("📅 Dates", data.exp1Dates, "Ex: 2022 - Present") { data = data.copy(exp1Dates = it) }
                ResumeField("📄 Description", data.exp1Desc, "Ex: Handled daily sales transactions...", singleLine = false) { data = data.copy(exp1Desc = it) }

                ResumeField("💼 Job Position", data.exp2Position, "Ex: (optional second entry)") { data = data.copy(exp2Position = it) }
                ResumeField("🏢 Company Name", data.exp2Company, "Ex: Company Name") { data = data.copy(exp2Company = it) }
                ResumeField("📅 Dates", data.exp2Dates, "Ex: 2020 - 2022") { data = data.copy(exp2Dates = it) }
                ResumeField("📄 Description", data.exp2Desc, "Ex: Assisted in inventory management...", singleLine = false) { data = data.copy(exp2Desc = it) }

                SectionLabel("REFERENCES")
                ResumeField("🧑 Reference Name", data.refName, "Ex: Maria Santos") { data = data.copy(refName = it) }
                ResumeField("🏢 Position / Company", data.refPositionCompany, "Ex: HR Manager, Company Name") { data = data.copy(refPositionCompany = it) }
                ResumeField("📞 Contact", data.refContact, "Ex: 09123456789") { data = data.copy(refContact = it) }

                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
    )
}

@Composable
private fun ResumeField(
    label: String,
    value: String,
    placeholder: String,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)) {
        Text(label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Spacer(Modifier.height(2.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.White.copy(alpha = 0.5f), fontSize = 13.sp) },
            singleLine = singleLine,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.Black.copy(alpha = 0.35f),
                unfocusedContainerColor = Color.Black.copy(alpha = 0.35f),
                focusedBorderColor = Color.White.copy(alpha = 0.6f),
                unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
