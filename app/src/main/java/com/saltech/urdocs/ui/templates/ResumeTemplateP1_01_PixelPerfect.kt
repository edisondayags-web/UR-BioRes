package com.saltech.urdocs.ui.templates

import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.layer.drawLayer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ===== PACKAGE 1 - TEMPLATE 01 ("Anime Avatar / Sidebar Timeline") =====
// Same data model + field-wiring pattern as ResumeTemplate01_PixelPerfect (Package 2),
// different layout: dark sidebar (contact/education/skills/language) on the left,
// big name header + year-timeline experience + references on the right.

@Composable
private fun EditableText_P1(
    value: String,
    placeholder: String,
    color: Color,
    fontSize: TextUnit,
    fontWeight: FontWeight? = null,
    italic: Boolean = false,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    Box(modifier = modifier) {
        if (value.isEmpty()) {
            Text(
                placeholder,
                color = color.copy(alpha = 0.45f),
                fontSize = fontSize,
                fontWeight = fontWeight,
                fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
                maxLines = 3
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                color = color,
                fontSize = fontSize,
                fontWeight = fontWeight,
                fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal
            ),
            cursorBrush = SolidColor(color),
            maxLines = 3,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SidebarHeader_P1(text: String, sidebarBg: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.35f))
            .padding(vertical = 6.dp, horizontal = 8.dp)
    ) {
        Text(text, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 10.sp, letterSpacing = 0.5.sp)
    }
}

@Composable
private fun SidebarIconRow_P1(icon: ImageVector, value: String, placeholder: String, accent: Color, onValueChange: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.size(18.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = Color(0xFF12203D), modifier = Modifier.size(10.dp))
        }
        Spacer(Modifier.width(8.dp))
        EditableText_P1(value, placeholder, Color.White, 8.sp, modifier = Modifier.weight(1f), onValueChange = onValueChange)
    }
}

@Composable
private fun SidebarCheckItem_P1(value: String, placeholder: String, accent: Color, onValueChange: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.size(14.dp).clip(CircleShape).background(accent), contentAlignment = Alignment.Center) {
            Icon(Icons.Filled.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(9.dp))
        }
        Spacer(Modifier.width(8.dp))
        EditableText_P1(value, placeholder, Color.White, 8.sp, modifier = Modifier.weight(1f), onValueChange = onValueChange)
    }
}

@Composable
private fun DiamondDivider_P1(text: String, accent: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.weight(1f).height(1.dp).background(accent.copy(alpha = 0.6f)))
        Text("  ◆  ", color = accent, fontSize = 10.sp)
        Text(text.ifEmpty { "Professional Title" }, color = Color(0xFF222222), fontSize = 11.sp, fontWeight = FontWeight.Medium)
        Text("  ◆  ", color = accent, fontSize = 10.sp)
        Box(Modifier.weight(1f).height(1.dp).background(accent.copy(alpha = 0.6f)))
    }
}

@Composable
private fun SectionHeader_P1(text: String) {
    Column(Modifier.fillMaxWidth()) {
        Text(text, color = Color(0xFF12203D), fontWeight = FontWeight.Black, fontSize = 15.sp, letterSpacing = 0.5.sp)
        Spacer(Modifier.height(3.dp))
        Box(Modifier.fillMaxWidth().height(1.5.dp).background(Color(0xFF12203D).copy(alpha = 0.25f)))
    }
}

@Composable
private fun ExperienceEntry_P1(
    years: String, yearsPlaceholder: String, accent: Color, onYears: (String) -> Unit,
    position: String, positionPlaceholder: String, onPosition: (String) -> Unit,
    company: String, companyPlaceholder: String, onCompany: (String) -> Unit,
    desc: String, descPlaceholder: String, onDesc: (String) -> Unit
) {
    Row(Modifier.fillMaxWidth()) {
        Column(Modifier.width(52.dp)) {
            EditableText_P1(years, yearsPlaceholder, Color(0xFF12203D), 9.sp, FontWeight.Bold, modifier = Modifier.fillMaxWidth(), onValueChange = onYears)
        }
        Column(Modifier.width(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.size(8.dp).clip(CircleShape).background(accent))
            Box(Modifier.width(1.dp).weight(1f).background(accent.copy(alpha = 0.3f)))
        }
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f).padding(bottom = 12.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            EditableText_P1(position, positionPlaceholder, Color(0xFF12203D), 10.5.sp, FontWeight.Bold, onValueChange = onPosition)
            EditableText_P1(company, companyPlaceholder, Color(0xFF444444), 8.5.sp, italic = true, onValueChange = onCompany)
            Spacer(Modifier.height(2.dp))
            EditableText_P1(desc, descPlaceholder, Color(0xFF333333), 8.sp, onValueChange = onDesc)
        }
    }
}

@Composable
private fun ReferenceBlock_P1(
    name: String, positionCompany: String, phone: String, email: String, avatarUri: String, accent: Color, modifier: Modifier,
    onName: (String) -> Unit, onPosition: (String) -> Unit, onPhone: (String) -> Unit, onEmail: (String) -> Unit, onAvatarUri: (String) -> Unit
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        SharedAvatarPicker(avatarUri, 30.dp, accent, name, onAvatarUri)
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(1.dp)) {
            EditableText_P1(name, "Ex: Reference Name", Color(0xFF12203D), 8.5.sp, FontWeight.Bold, onValueChange = onName)
            EditableText_P1(positionCompany, "Ex: Position / Company", Color(0xFF555555), 7.5.sp, onValueChange = onPosition)
            EditableText_P1(phone, "Ex: 0912 345 6789", Color(0xFF555555), 7.5.sp, onValueChange = onPhone)
            EditableText_P1(email, "Ex: reference@email.com", Color(0xFF555555), 7.5.sp, onValueChange = onEmail)
        }
    }
}

@Composable
fun ResumeTemplateP1_01_PixelPerfect(
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
    val accent = Color(0xFF4FC3F7)
    val sidebarBg = Color(0xFF12203D)
    val graphicsLayer = androidx.compose.ui.graphics.rememberGraphicsLayer()
    val nameFontSize = autoShrinkNameFontSize(userName)

    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .drawWithContent {
                    graphicsLayer.record { this@drawWithContent.drawContent() }
                    drawLayer(graphicsLayer)
                }
        ) {
            Row(Modifier.defaultMinSize(minHeight = 900.dp)) {

                // ===== LEFT SIDEBAR =====
                Column(
                    Modifier
                        .width(140.dp)
                        .fillMaxHeight()
                        .background(sidebarBg)
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        SharedAvatarPicker(avatarUri, 84.dp, accent, userName) { onFieldChange("avatarUri", it) }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        SidebarHeader_P1("CONTACT", sidebarBg)
                        SidebarIconRow_P1(Icons.Filled.Phone, contactPhone, "Ex: 0912 345 6789", accent) { onFieldChange("phone", it) }
                        SidebarIconRow_P1(Icons.Filled.Email, contactEmail, "Ex: email@email.com", accent) { onFieldChange("email", it) }
                        SidebarIconRow_P1(Icons.Filled.Place, contactAddress, "Ex: City, Province", accent) { onFieldChange("location", it) }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        SidebarHeader_P1("EDUCATION", sidebarBg)
                        EditableText_P1(edu1Degree, "Ex: Bachelor's Degree", Color.White, 8.5.sp, FontWeight.Bold) { onFieldChange("edu1Degree", it) }
                        EditableText_P1(edu1School, "Ex: University Name", Color.White.copy(alpha = 0.8f), 7.5.sp) { onFieldChange("edu1School", it) }
                        EditableText_P1(edu1Years, "Ex: 2016 - 2020", accent, 7.5.sp) { onFieldChange("edu1Years", it) }
                        Spacer(Modifier.height(4.dp))
                        EditableText_P1(edu2Degree, "Ex: Second Degree", Color.White, 8.5.sp, FontWeight.Bold) { onFieldChange("edu2Degree", it) }
                        EditableText_P1(edu2School, "Ex: School Name", Color.White.copy(alpha = 0.8f), 7.5.sp) { onFieldChange("edu2School", it) }
                        EditableText_P1(edu2Years, "Ex: 2020 - 2023", accent, 7.5.sp) { onFieldChange("edu2Years", it) }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        SidebarHeader_P1("SKILLS", sidebarBg)
                        val skillHints = listOf("Ex: Sales Strategies", "Ex: Negotiation", "Ex: Problem-Solving", "Ex: Time Mgmt", "Ex: Presentation", "Ex: Networking")
                        skills.take(6).forEachIndexed { i, s ->
                            SidebarCheckItem_P1(s, skillHints.getOrElse(i) { "Ex: Skill" }, accent) { onFieldChange("skill${i + 1}", it) }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        SidebarHeader_P1("WEBSITE / LINKS", sidebarBg)
                        SidebarCheckItem_P1(contactWebsite, "Ex: www.yoursite.com", accent) { onFieldChange("website", it) }
                        SidebarCheckItem_P1(contactLinkedin, "Ex: linkedin.com/in/you", accent) { onFieldChange("linkedin", it) }
                    }
                }

                // ===== RIGHT CONTENT =====
                Column(
                    Modifier
                        .weight(1f)
                        .padding(horizontal = 18.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column {
                        EditableText_P1(userName, "Ex: Your Name", Color(0xFF12203D), nameFontSize, FontWeight.Black) { onFieldChange("fullName", it) }
                        Spacer(Modifier.height(6.dp))
                        DiamondDivider_P1(userTitle, accent)
                    }

                    if (aboutMe.isNotEmpty() || true) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            SectionHeader_P1("ABOUT ME")
                            EditableText_P1(aboutMe, "Ex: Dedicated and results-driven professional with strong communication skills.", Color(0xFF333333), 8.5.sp) { onFieldChange("aboutMe", it) }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        SectionHeader_P1("PROFESSIONAL EXPERIENCE")
                        Spacer(Modifier.height(4.dp))
                        ExperienceEntry_P1(exp1Dates, "2023-Present", accent, { onFieldChange("exp1Dates", it) },
                            exp1Position, "Ex: Inside Sales Representative", { onFieldChange("exp1Position", it) },
                            exp1Company, "Ex: Company Name | Address", { onFieldChange("exp1Company", it) },
                            exp1Desc, "Ex: Developed and executed strategies that increased results.", { onFieldChange("exp1Desc", it) })
                        ExperienceEntry_P1(exp2Dates, "2021-2023", accent, { onFieldChange("exp2Dates", it) },
                            exp2Position, "Ex: Sales Associate", { onFieldChange("exp2Position", it) },
                            exp2Company, "Ex: Company Name | Address", { onFieldChange("exp2Company", it) },
                            exp2Desc, "Ex: Prospected and qualified leads through calls and campaigns.", { onFieldChange("exp2Desc", it) })
                        ExperienceEntry_P1(exp3Dates, "2019-2021", accent, { onFieldChange("exp3Dates", it) },
                            exp3Position, "Ex: Sales Associate", { onFieldChange("exp3Position", it) },
                            exp3Company, "Ex: Company Name | Address", { onFieldChange("exp3Company", it) },
                            exp3Desc, "Ex: Provided excellent customer service and resolved inquiries.", { onFieldChange("exp3Desc", it) })
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        SectionHeader_P1("REFERENCES")
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                            ReferenceBlock_P1(refName, refPositionCompany, refPhone, refEmail, refAvatarUri, accent, Modifier.weight(1f),
                                { onFieldChange("refName", it) }, { onFieldChange("refPositionCompany", it) },
                                { onFieldChange("refPhone", it) }, { onFieldChange("refEmail", it) }, { onFieldChange("refAvatarUri", it) })
                            ReferenceBlock_P1(ref2Name, ref2PositionCompany, ref2Phone, ref2Email, ref2AvatarUri, accent, Modifier.weight(1f),
                                { onFieldChange("ref2Name", it) }, { onFieldChange("ref2PositionCompany", it) },
                                { onFieldChange("ref2Phone", it) }, { onFieldChange("ref2Email", it) }, { onFieldChange("ref2AvatarUri", it) })
                        }
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

