package com.saltech.urdocs.ui.templates

import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.layer.drawLayer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy08 = Color(0xFF16305E)
private val Gold08 = Color(0xFFC9A227)

@Composable
private fun EditableText_08(
    value: String,
    placeholder: String,
    color: Color,
    fontSize: TextUnit,
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
fun ResumeTemplate08_PixelPerfect(
    userName: String = "",
    userTitle: String = "",
    avatarUri: String = "",
    contactPhone: String = "",
    contactEmail: String = "",
    contactAddress: String = "",
    contactWebsite: String = "",
    contactLinkedin: String = "",
    aboutMe: String = "",
    edu1Degree: String = "", edu1School: String = "", edu1Years: String = "",
    edu2Degree: String = "", edu2School: String = "", edu2Years: String = "",
    skills: List<String> = List(6) { "" },
    exp1Position: String = "", exp1Company: String = "", exp1Dates: String = "", exp1Desc: String = "",
    exp2Position: String = "", exp2Company: String = "", exp2Dates: String = "", exp2Desc: String = "",
    exp3Position: String = "", exp3Company: String = "", exp3Dates: String = "", exp3Desc: String = "",
    exp4Position: String = "", exp4Company: String = "", exp4Dates: String = "", exp4Desc: String = "",
    exp5Position: String = "", exp5Company: String = "", exp5Dates: String = "", exp5Desc: String = "",
    refName: String = "", refPositionCompany: String = "", refPhone: String = "", refEmail: String = "", refAvatarUri: String = "",
    ref2Name: String = "", ref2PositionCompany: String = "", ref2Phone: String = "", ref2Email: String = "", ref2AvatarUri: String = "",
    onFieldChange: (String, String) -> Unit = { _, _ -> },
    onHomeOverride: () -> Unit = {}
) {
    val graphicsLayer = androidx.compose.ui.graphics.rememberGraphicsLayer()
    val nameFontSize = autoShrinkNameFontSize(userName)

    Box(Modifier.fillMaxWidth()) {
        Box(Modifier.fillMaxWidth().background(Color(0xFF050505)).padding(0.dp)) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 700.dp)
                    .background(Color.White)
                    .border(1.dp, Gold08.copy(alpha = 0.4f), RoundedCornerShape(topStart = 14.dp, topEnd = 0.dp, bottomStart = 0.dp, bottomEnd = 14.dp))
                    .drawWithContent {
                        graphicsLayer.record { this@drawWithContent.drawContent() }
                        drawLayer(graphicsLayer)
                    }
            ) {
                // ===== Diagonal navy/gold ribbon header =====
                Box(Modifier.fillMaxWidth().height(110.dp)) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height
                        val navyPath = Path().apply {
                            moveTo(0f, 0f)
                            lineTo(w * 0.62f, 0f)
                            lineTo(0f, h * 0.9f)
                            close()
                        }
                        drawPath(navyPath, color = Navy08)
                        val goldPath = Path().apply {
                            moveTo(w * 0.62f, 0f)
                            lineTo(w * 0.68f, 0f)
                            lineTo(0f, h)
                            lineTo(0f, h * 0.9f)
                            close()
                        }
                        drawPath(goldPath, color = Gold08)
                    }
                    Box(Modifier.align(Alignment.TopEnd).padding(top = 14.dp, end = 16.dp)) {
                        SharedAvatarPicker(avatarUri, 70.dp, Gold08, userName) { onFieldChange("avatarUri", it) }
                    }
                }

                Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {

                    Column {
                        EditableText_08(userName, "Ex: Your Name", Navy08, nameFontSize, FontWeight.ExtraBold) { onFieldChange("fullName", it) }
                        Spacer(Modifier.height(2.dp))
                        EditableText_08(userTitle, "Ex: Professional Title", Color(0xFF6B6B6B), 10.sp) { onFieldChange("professionalTitle", it) }
                    }

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        // ===== LEFT COLUMN =====
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            SectionLabel_08("SKILLS")
                            val skillHints = listOf("Ex: Strategy", "Ex: Organisation", "Ex: Public Relations", "Ex: Customer Service", "Ex: Planning", "Ex: Negotiation")
                            skills.forEachIndexed { i, s ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(Modifier.size(4.dp).clip(CircleShape).background(Gold08))
                                    Spacer(Modifier.width(6.dp))
                                    EditableText_08(s, skillHints.getOrElse(i) { "Ex: Skill" }, Color(0xFF1C1C1C), 7.5.sp, modifier = Modifier.weight(1f)) { onFieldChange("skill${i + 1}", it) }
                                }
                            }

                            Spacer(Modifier.height(4.dp))
                            SectionLabel_08("EDUCATION")
                            IconTimelineEntry_08(Icons.Filled.School) {
                                EditableText_08(edu1Degree, "Ex: BS Information Technology", Color(0xFF1C1C1C), 7.5.sp, FontWeight.Bold) { onFieldChange("edu1Degree", it) }
                                EditableText_08(edu1School, "Ex: University Name", Color(0xFF6B6B6B), 7.sp) { onFieldChange("edu1School", it) }
                                EditableText_08(edu1Years, "Ex: 2018 - 2022", Gold08, 7.sp) { onFieldChange("edu1Years", it) }
                            }
                            IconTimelineEntry_08(Icons.Filled.School) {
                                EditableText_08(edu2Degree, "Ex: Senior High School", Color(0xFF1C1C1C), 7.5.sp, FontWeight.Bold) { onFieldChange("edu2Degree", it) }
                                EditableText_08(edu2School, "Ex: School Name", Color(0xFF6B6B6B), 7.sp) { onFieldChange("edu2School", it) }
                                EditableText_08(edu2Years, "Ex: 2016 - 2018", Gold08, 7.sp) { onFieldChange("edu2Years", it) }
                            }

                            Spacer(Modifier.height(4.dp))
                            SectionLabel_08("REFERENCES")
                            ReferenceBlock_08(refName, refPositionCompany, refPhone, refEmail, refAvatarUri,
                                { onFieldChange("refName", it) }, { onFieldChange("refPositionCompany", it) },
                                { onFieldChange("refPhone", it) }, { onFieldChange("refEmail", it) }, { onFieldChange("refAvatarUri", it) })
                            Spacer(Modifier.height(4.dp))
                            ReferenceBlock_08(ref2Name, ref2PositionCompany, ref2Phone, ref2Email, ref2AvatarUri,
                                { onFieldChange("ref2Name", it) }, { onFieldChange("ref2PositionCompany", it) },
                                { onFieldChange("ref2Phone", it) }, { onFieldChange("ref2Email", it) }, { onFieldChange("ref2AvatarUri", it) })

                            Spacer(Modifier.height(4.dp))
                            SectionLabel_08("CONTACT")
                            IconContactField_08(Icons.Filled.Phone, contactPhone, "Ex: 0912 345 6789") { onFieldChange("phone", it) }
                            IconContactField_08(Icons.Filled.Email, contactEmail, "Ex: youremail@email.com") { onFieldChange("email", it) }
                            IconContactField_08(Icons.Filled.Place, contactAddress, "Ex: City, Province") { onFieldChange("location", it) }
                            IconContactField_08(Icons.Filled.Language, contactWebsite, "Ex: www.yoursite.com") { onFieldChange("website", it) }
                        }

                        // ===== RIGHT COLUMN =====
                        Column(Modifier.weight(1.3f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            EditableText_08(aboutMe, "Ex: Dedicated professional with strong track record delivering results.", Color(0xFF1C1C1C), 8.sp) { onFieldChange("aboutMe", it) }

                            Spacer(Modifier.height(6.dp))
                            SectionLabel_08("WORK EXPERIENCE")
                            IconTimelineEntry_08(Icons.Filled.Work) {
                                EditableText_08(exp1Position, "Ex: Account Executive", Color(0xFF1C1C1C), 7.5.sp, FontWeight.Bold) { onFieldChange("exp1Position", it) }
                                EditableText_08(exp1Company, "Ex: Company Name", Navy08, 7.sp) { onFieldChange("exp1Company", it) }
                                EditableText_08(exp1Dates, "Ex: 2021 - Present", Gold08, 7.sp) { onFieldChange("exp1Dates", it) }
                                EditableText_08(exp1Desc, "Ex: Describe your role and key achievements.", Color(0xFF6B6B6B), 7.sp) { onFieldChange("exp1Desc", it) }
                            }
                            IconTimelineEntry_08(Icons.Filled.Work) {
                                EditableText_08(exp2Position, "Ex: Junior Account Executive", Color(0xFF1C1C1C), 7.5.sp, FontWeight.Bold) { onFieldChange("exp2Position", it) }
                                EditableText_08(exp2Company, "Ex: Company Name", Navy08, 7.sp) { onFieldChange("exp2Company", it) }
                                EditableText_08(exp2Dates, "Ex: 2016 - 2020", Gold08, 7.sp) { onFieldChange("exp2Dates", it) }
                                EditableText_08(exp2Desc, "Ex: Describe your role and key achievements.", Color(0xFF6B6B6B), 7.sp) { onFieldChange("exp2Desc", it) }
                            }
                            IconTimelineEntry_08(Icons.Filled.Work) {
                                EditableText_08(exp3Position, "Ex: Sales Assistant", Color(0xFF1C1C1C), 7.5.sp, FontWeight.Bold) { onFieldChange("exp3Position", it) }
                                EditableText_08(exp3Company, "Ex: Company Name", Navy08, 7.sp) { onFieldChange("exp3Company", it) }
                                EditableText_08(exp3Dates, "Ex: 2011 - 2015", Gold08, 7.sp) { onFieldChange("exp3Dates", it) }
                                EditableText_08(exp3Desc, "Ex: Describe your role and key achievements.", Color(0xFF6B6B6B), 7.sp) { onFieldChange("exp3Desc", it) }
                            }
                            IconTimelineEntry_08(Icons.Filled.Work) {
                                EditableText_08(exp4Position, "Ex: Customer Service Representative", Color(0xFF1C1C1C), 7.5.sp, FontWeight.Bold) { onFieldChange("exp4Position", it) }
                                EditableText_08(exp4Company, "Ex: Company Name", Navy08, 7.sp) { onFieldChange("exp4Company", it) }
                                EditableText_08(exp4Dates, "Ex: 2020 - 2021", Gold08, 7.sp) { onFieldChange("exp4Dates", it) }
                                EditableText_08(exp4Desc, "Ex: Describe your role and key achievements.", Color(0xFF6B6B6B), 7.sp) { onFieldChange("exp4Desc", it) }
                            }
                            IconTimelineEntry_08(Icons.Filled.Work) {
                                EditableText_08(exp5Position, "Ex: Sales Assistant", Color(0xFF1C1C1C), 7.5.sp, FontWeight.Bold) { onFieldChange("exp5Position", it) }
                                EditableText_08(exp5Company, "Ex: Retail Mart", Navy08, 7.sp) { onFieldChange("exp5Company", it) }
                                EditableText_08(exp5Dates, "Ex: May 2019 - Dec 2019", Gold08, 7.sp) { onFieldChange("exp5Dates", it) }
                                EditableText_08(exp5Desc, "Ex: Assisted customers and managed inventory.", Color(0xFF6B6B6B), 7.sp) { onFieldChange("exp5Desc", it) }
                            }
                        }
                    }

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("08", color = Navy08.copy(alpha = 0.4f), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                        Text("Corporate Diagonal", color = Color(0xFF6B6B6B).copy(alpha = 0.6f), fontSize = 7.sp)
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

@Composable
private fun SectionLabel_08(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text, color = Navy08, fontSize = 8.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
        Spacer(Modifier.width(6.dp))
        Box(Modifier.weight(1f).height(1.dp).background(Gold08.copy(alpha = 0.6f)))
    }
}

@Composable
private fun IconContactField_08(icon: ImageVector, value: String, placeholder: String, onValueChange: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.size(16.dp).clip(CircleShape).border(0.8.dp, Navy08.copy(alpha = 0.7f), CircleShape), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = Navy08, modifier = Modifier.size(9.dp))
        }
        Spacer(Modifier.width(6.dp))
        EditableText_08(value, placeholder, Color(0xFF1C1C1C), 7.sp, modifier = Modifier.weight(1f), onValueChange = onValueChange)
    }
}

@Composable
private fun IconTimelineEntry_08(icon: ImageVector, content: @Composable ColumnScope.() -> Unit) {
    Row(Modifier.fillMaxWidth()) {
        Box(Modifier.size(16.dp).clip(CircleShape).border(1.dp, Gold08, CircleShape).background(Navy08), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = Gold08, modifier = Modifier.size(9.dp))
        }
        Spacer(Modifier.width(7.dp))
        Column(verticalArrangement = Arrangement.spacedBy(1.dp), modifier = Modifier.weight(1f), content = content)
    }
}

@Composable
private fun ReferenceBlock_08(
    name: String, positionCompany: String, phone: String, email: String, avatarUri: String,
    onName: (String) -> Unit, onPosition: (String) -> Unit, onPhone: (String) -> Unit, onEmail: (String) -> Unit, onAvatarUri: (String) -> Unit
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        SharedAvatarPicker(avatarUri, 26.dp, Gold08, name, onAvatarUri)
        Spacer(Modifier.width(6.dp))
        Column(Modifier.weight(1f)) {
            EditableText_08(name, "Ex: Reference Name", Color(0xFF1C1C1C), 7.5.sp, FontWeight.Bold) { onName(it) }
            EditableText_08(positionCompany, "Ex: Position / Company", Color(0xFF6B6B6B), 6.5.sp) { onPosition(it) }
            EditableText_08(phone, "Ex: 0912 111 2222", Color(0xFF6B6B6B), 6.5.sp) { onPhone(it) }
            EditableText_08(email, "Ex: reference@email.com", Color(0xFF6B6B6B), 6.5.sp) { onEmail(it) }
        }
    }
}
