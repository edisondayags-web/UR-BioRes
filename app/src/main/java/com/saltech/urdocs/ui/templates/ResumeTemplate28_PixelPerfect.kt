// STEP 2-6: KUMPLETO NA (Contact, Education, Skills, Experience, References)
// Ito na yung FINAL na buong ResumeTemplate28_PixelPerfect.kt
// PAPALITAN nito yung Step 1 file — buong laman ng file, hindi na dagdag lang
//
// Path: ~/UR-BioRes/app/src/main/java/com/saltech/urdocs/ui/templates/ResumeTemplate28_PixelPerfect.kt

package com.saltech.urdocs.ui.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PKG1_ACCENT = Color(0xFFD4AF37)
private val PKG1_BG = Color.White
private val PKG1_TEXT = Color(0xFF2B2B2B)

@Composable
private fun EditableText_28(
    value: String,
    placeholder: String,
    color: Color,
    fontSize: androidx.compose.ui.unit.TextUnit,
    fontWeight: FontWeight? = null,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    Box(modifier = modifier) {
        if (value.isEmpty()) {
            Text(placeholder, color = color.copy(alpha = 0.45f), fontSize = fontSize, fontWeight = fontWeight, maxLines = 1)
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(color = color, fontSize = fontSize, fontWeight = fontWeight),
            cursorBrush = SolidColor(color),
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SectionLabel_28(text: String, accent: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text, color = accent, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
        Spacer(Modifier.width(6.dp))
        Box(Modifier.weight(1f).height(1.dp).background(accent.copy(alpha = 0.6f)))
        Box(Modifier.size(6.dp).clip(CircleShape).background(accent))
    }
}

@Composable
private fun IconContactField_28(icon: ImageVector, value: String, placeholder: String, accent: Color, onValueChange: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Box(Modifier.size(22.dp).clip(CircleShape).border(1.dp, accent.copy(alpha = 0.7f), CircleShape), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(12.dp))
        }
        Spacer(Modifier.width(8.dp))
        EditableText_28(value, placeholder, PKG1_TEXT, 10.sp, modifier = Modifier.weight(1f), onValueChange = onValueChange)
    }
}

@Composable
private fun IconTimelineEntry_28(icon: ImageVector, accent: Color, content: @Composable ColumnScope.() -> Unit) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Box(Modifier.size(22.dp).clip(CircleShape).background(Color.Black), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(12.dp))
        }
        Spacer(Modifier.width(8.dp))
        Column(verticalArrangement = Arrangement.spacedBy(1.dp), modifier = Modifier.weight(1f), content = content)
    }
}

@Composable
private fun ReferenceBlock_28(
    name: String, positionCompany: String, phone: String, email: String, accent: Color, modifier: Modifier,
    onName: (String) -> Unit, onPosition: (String) -> Unit, onPhone: (String) -> Unit, onEmail: (String) -> Unit
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(36.dp).clip(CircleShape).border(1.5.dp, accent, CircleShape))
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            EditableText_28(name, "Ex: Reference Name", PKG1_TEXT, 10.sp, FontWeight.Bold) { onName(it) }
            EditableText_28(positionCompany, "Ex: Position / Company", PKG1_TEXT.copy(alpha = 0.7f), 9.sp) { onPosition(it) }
            IconContactField_28(Icons.Filled.Phone, phone, "Ex: 0912 111 2222", accent) { onPhone(it) }
            IconContactField_28(Icons.Filled.Email, email, "Ex: reference@email.com", accent) { onEmail(it) }
        }
    }
}

@Composable
fun ResumeTemplate28_PixelPerfect(
    userName: String = "",
    avatarUri: String = "",
    aboutMe: String = "",
    contactPhone: String = "",
    contactEmail: String = "",
    contactAddress: String = "",
    contactLinkedin: String = "",
    contactWebsite: String = "",
    edu1Degree: String = "", edu1School: String = "", edu1Years: String = "",
    edu2Degree: String = "", edu2School: String = "", edu2Years: String = "",
    skills: List<String> = List(6) { "" },
    exp1Position: String = "", exp1Company: String = "", exp1Dates: String = "", exp1Desc: String = "",
    exp2Position: String = "", exp2Company: String = "", exp2Dates: String = "", exp2Desc: String = "",
    exp3Position: String = "", exp3Company: String = "", exp3Dates: String = "", exp3Desc: String = "",
    refName: String = "", refPositionCompany: String = "", refPhone: String = "", refEmail: String = "",
    ref2Name: String = "", ref2PositionCompany: String = "", ref2Phone: String = "", ref2Email: String = "",
    onFieldChange: (String, String) -> Unit = { _, _ -> },
    onHomeOverride: () -> Unit = {}
) {
    val graphicsLayer = rememberGraphicsLayer()

    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxSize()
                .background(PKG1_BG)
                .verticalScroll(rememberScrollState())
                .drawWithContent {
                    graphicsLayer.record { this@drawWithContent.drawContent() }
                    drawLayer(graphicsLayer)
                }
        ) {
            Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

                // ===== HEADER: AVATAR + NAME =====
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(90.dp)) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .border(2.dp, PKG1_ACCENT, CircleShape)
                                .clickable { onFieldChange("avatarTapped", "true") }
                        )
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .align(Alignment.BottomEnd)
                                .clip(CircleShape)
                                .background(PKG1_ACCENT)
                                .border(2.dp, Color.Black, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = "Upload Photo", tint = Color.Black, modifier = Modifier.size(16.dp))
                        }
                    }
                    Spacer(Modifier.width(16.dp))
                    EditableText_28(userName, "Ex: Your Name", PKG1_ACCENT, 24.sp, FontWeight.Bold, modifier = Modifier.weight(1f)) {
                        onFieldChange("fullName", it)
                    }
                }

                // ===== ABOUT ME =====
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    SectionLabel_28("ABOUT ME", PKG1_ACCENT)
                    EditableText_28(aboutMe, "Ex: Short summary about yourself", PKG1_TEXT, 10.sp) { onFieldChange("aboutMe", it) }
                }

                // ===== CONTACT + EDUCATION (side by side) =====
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        SectionLabel_28("CONTACT", PKG1_ACCENT)
                        IconContactField_28(Icons.Filled.Phone, contactPhone, "Ex: 0912 345 6789", PKG1_ACCENT) { onFieldChange("phone", it) }
                        IconContactField_28(Icons.Filled.Email, contactEmail, "Ex: youremail@email.com", PKG1_ACCENT) { onFieldChange("email", it) }
                        IconContactField_28(Icons.Filled.Place, contactAddress, "Ex: City, Province", PKG1_ACCENT) { onFieldChange("location", it) }
                        IconContactField_28(Icons.Filled.Language, contactWebsite, "Ex: linkedin.com/in/you", PKG1_ACCENT) { onFieldChange("linkedin", it) }
                    }
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        SectionLabel_28("EDUCATION", PKG1_ACCENT)
                        IconTimelineEntry_28(Icons.Filled.School, PKG1_ACCENT) {
                            EditableText_28(edu1Degree, "Ex: BS Information Technology", PKG1_TEXT, 10.sp, FontWeight.Bold) { onFieldChange("edu1Degree", it) }
                            EditableText_28(edu1School, "Ex: University Name", PKG1_TEXT, 9.sp) { onFieldChange("edu1School", it) }
                            EditableText_28(edu1Years, "Ex: 2018 - 2022", PKG1_ACCENT, 9.sp) { onFieldChange("edu1Years", it) }
                        }
                        IconTimelineEntry_28(Icons.Filled.School, PKG1_ACCENT) {
                            EditableText_28(edu2Degree, "Ex: Senior High School", PKG1_TEXT, 10.sp, FontWeight.Bold) { onFieldChange("edu2Degree", it) }
                            EditableText_28(edu2School, "Ex: School Name", PKG1_TEXT, 9.sp) { onFieldChange("edu2School", it) }
                            EditableText_28(edu2Years, "Ex: 2016 - 2018", PKG1_ACCENT, 9.sp) { onFieldChange("edu2Years", it) }
                        }
                    }
                }

                // ===== SKILLS =====
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    SectionLabel_28("SKILLS", PKG1_ACCENT)
                    val skillHints = listOf("Ex: Problem Solving", "Ex: Communication", "Ex: Teamwork", "Ex: Leadership", "Ex: Time Mgmt", "Ex: Creativity")
                    skills.forEachIndexed { i, s ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                            Box(Modifier.size(6.dp).clip(CircleShape).background(PKG1_ACCENT))
                            Spacer(Modifier.width(8.dp))
                            EditableText_28(s, skillHints.getOrElse(i) { "Ex: Skill" }, PKG1_TEXT, 10.sp, modifier = Modifier.weight(1f)) {
                                onFieldChange("skill${i + 1}", it)
                            }
                        }
                    }
                }

                // ===== EXPERIENCE =====
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    SectionLabel_28("EXPERIENCE", PKG1_ACCENT)
                    IconTimelineEntry_28(Icons.Filled.Work, PKG1_ACCENT) {
                        EditableText_28(exp1Position, "Ex: IT Support Specialist", PKG1_TEXT, 10.sp, FontWeight.Bold) { onFieldChange("exp1Position", it) }
                        EditableText_28(exp1Company, "Ex: ABC Company", PKG1_TEXT, 9.sp) { onFieldChange("exp1Company", it) }
                        EditableText_28(exp1Dates, "Ex: Jan 2023 - Present", PKG1_ACCENT, 9.sp) { onFieldChange("exp1Dates", it) }
                        EditableText_28(exp1Desc, "Ex: Provided technical support and resolved issues.", PKG1_TEXT.copy(alpha = 0.8f), 9.sp) { onFieldChange("exp1Desc", it) }
                    }
                    IconTimelineEntry_28(Icons.Filled.Work, PKG1_ACCENT) {
                        EditableText_28(exp2Position, "Ex: Web Developer", PKG1_TEXT, 10.sp, FontWeight.Bold) { onFieldChange("exp2Position", it) }
                        EditableText_28(exp2Company, "Ex: XYZ Solutions", PKG1_TEXT, 9.sp) { onFieldChange("exp2Company", it) }
                        EditableText_28(exp2Dates, "Ex: Jun 2021 - Dec 2022", PKG1_ACCENT, 9.sp) { onFieldChange("exp2Dates", it) }
                        EditableText_28(exp2Desc, "Ex: Developed and maintained client websites.", PKG1_TEXT.copy(alpha = 0.8f), 9.sp) { onFieldChange("exp2Desc", it) }
                    }
                    IconTimelineEntry_28(Icons.Filled.Work, PKG1_ACCENT) {
                        EditableText_28(exp3Position, "Ex: On-the-Job Trainee", PKG1_TEXT, 10.sp, FontWeight.Bold) { onFieldChange("exp3Position", it) }
                        EditableText_28(exp3Company, "Ex: TechSoft Inc.", PKG1_TEXT, 9.sp) { onFieldChange("exp3Company", it) }
                        EditableText_28(exp3Dates, "Ex: Jan 2021 - Apr 2021", PKG1_ACCENT, 9.sp) { onFieldChange("exp3Dates", it) }
                        EditableText_28(exp3Desc, "Ex: Assisted in system maintenance and documentation.", PKG1_TEXT.copy(alpha = 0.8f), 9.sp) { onFieldChange("exp3Desc", it) }
                    }
                }

                // ===== REFERENCES =====
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    SectionLabel_28("REFERENCES", PKG1_ACCENT)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        ReferenceBlock_28(refName, refPositionCompany, refPhone, refEmail, PKG1_ACCENT, Modifier.weight(1f),
                            { onFieldChange("refName", it) }, { onFieldChange("refPositionCompany", it) },
                            { onFieldChange("refPhone", it) }, { onFieldChange("refEmail", it) })
                        ReferenceBlock_28(ref2Name, ref2PositionCompany, ref2Phone, ref2Email, PKG1_ACCENT, Modifier.weight(1f),
                            { onFieldChange("ref2Name", it) }, { onFieldChange("ref2PositionCompany", it) },
                            { onFieldChange("ref2Phone", it) }, { onFieldChange("ref2Email", it) })
                    }
                }
            }
        }

        com.saltech.urdocs.ui.templates.TemplateExportMenu(
            graphicsLayer,
            "resume_$userName",
            onHome = onHomeOverride,
            modifier = Modifier.align(Alignment.TopEnd).padding(top = 4.dp, end = 12.dp)
        )
    }
}

// IDAGDAG MO 'TO SA DULO NG ResumeTemplate28_PixelPerfect.kt (huwag palitan, IDAGDAG lang sa ilalim)
// Corrected version - tumutugma na sa tunay na ResumeTemplateFields fields

@Composable
fun ResumeTemplate28Screen(
    data: ResumeTemplateFields,
    onDataChange: (ResumeTemplateFields) -> Unit,
    onBack: () -> Unit
) {
    ResumeTemplate28_PixelPerfect(
        userName = data.fullName,
        avatarUri = data.avatarUri,
        aboutMe = data.aboutMe,
        contactPhone = data.phone,
        contactEmail = data.email,
        contactAddress = data.location,
        contactLinkedin = data.linkedin,
        contactWebsite = data.website,
        edu1Degree = data.edu1Degree, edu1School = data.edu1School, edu1Years = data.edu1Years,
        edu2Degree = data.edu2Degree, edu2School = data.edu2School, edu2Years = data.edu2Years,
        skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
        exp1Position = data.exp1Position, exp1Company = data.exp1Company, exp1Dates = data.exp1Dates, exp1Desc = data.exp1Desc,
        exp2Position = data.exp2Position, exp2Company = data.exp2Company, exp2Dates = data.exp2Dates, exp2Desc = data.exp2Desc,
        exp3Position = data.exp3Position, exp3Company = data.exp3Company, exp3Dates = data.exp3Dates, exp3Desc = data.exp3Desc,
        refName = data.refName, refPositionCompany = data.refPositionCompany, refPhone = data.refContact, refEmail = data.refEmail,
        ref2Name = data.ref2Name, ref2PositionCompany = data.ref2PositionCompany, ref2Phone = data.ref2Contact, ref2Email = data.ref2Email,
        onFieldChange = { field, value ->
            onDataChange(
                when (field) {
                    "fullName" -> data.copy(fullName = value)
                    "avatarTapped" -> data // TODO: image picker, walang laman muna
                    "aboutMe" -> data.copy(aboutMe = value)
                    "phone" -> data.copy(phone = value)
                    "email" -> data.copy(email = value)
                    "location" -> data.copy(location = value)
                    "linkedin" -> data.copy(linkedin = value)
                    "website" -> data.copy(website = value)
                    "edu1Degree" -> data.copy(edu1Degree = value)
                    "edu1School" -> data.copy(edu1School = value)
                    "edu1Years" -> data.copy(edu1Years = value)
                    "edu2Degree" -> data.copy(edu2Degree = value)
                    "edu2School" -> data.copy(edu2School = value)
                    "edu2Years" -> data.copy(edu2Years = value)
                    "skill1" -> data.copy(skill1 = value)
                    "skill2" -> data.copy(skill2 = value)
                    "skill3" -> data.copy(skill3 = value)
                    "skill4" -> data.copy(skill4 = value)
                    "skill5" -> data.copy(skill5 = value)
                    "skill6" -> data.copy(skill6 = value)
                    "exp1Position" -> data.copy(exp1Position = value)
                    "exp1Company" -> data.copy(exp1Company = value)
                    "exp1Dates" -> data.copy(exp1Dates = value)
                    "exp1Desc" -> data.copy(exp1Desc = value)
                    "exp2Position" -> data.copy(exp2Position = value)
                    "exp2Company" -> data.copy(exp2Company = value)
                    "exp2Dates" -> data.copy(exp2Dates = value)
                    "exp2Desc" -> data.copy(exp2Desc = value)
                    "exp3Position" -> data.copy(exp3Position = value)
                    "exp3Company" -> data.copy(exp3Company = value)
                    "exp3Dates" -> data.copy(exp3Dates = value)
                    "exp3Desc" -> data.copy(exp3Desc = value)
                    "refName" -> data.copy(refName = value)
                    "refPositionCompany" -> data.copy(refPositionCompany = value)
                    "refPhone" -> data.copy(refContact = value)
                    "refEmail" -> data.copy(refEmail = value)
                    "ref2Name" -> data.copy(ref2Name = value)
                    "ref2PositionCompany" -> data.copy(ref2PositionCompany = value)
                    "ref2Phone" -> data.copy(ref2Contact = value)
                    "ref2Email" -> data.copy(ref2Email = value)
                    else -> data
                }
            )
        },
        onHomeOverride = onBack
    )
}
