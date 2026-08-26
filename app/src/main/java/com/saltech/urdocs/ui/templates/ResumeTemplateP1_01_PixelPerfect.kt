package com.saltech.urdocs.ui.templates

import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.layer.drawLayer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

// ===== PACKAGE 1 - TEMPLATE 01 ("Anime Avatar / Sidebar Timeline") v2 =====
// Adds: permanent "(Ex: ...)" hint line under every field, 3-bullet experience
// descriptions, PREFERENCES sidebar section, ADDITIONAL INFORMATION section,
// tighter spacing + smaller fonts so everything fits.

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
                placeholder, color = color.copy(alpha = 0.45f), fontSize = fontSize, fontWeight = fontWeight,
                fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal, maxLines = 3
            )
        }
        BasicTextField(
            value = value, onValueChange = onValueChange,
            textStyle = TextStyle(color = color, fontSize = fontSize, fontWeight = fontWeight, fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal),
            cursorBrush = SolidColor(color), maxLines = 3, modifier = Modifier.fillMaxWidth()
        )
    }
}

/** Value line + a permanent small gray "(Ex: ...)" hint underneath (always visible, like the reference image). */
@Composable
private fun FieldWithHint_P1(
    value: String, hint: String, color: Color, fontSize: TextUnit,
    fontWeight: FontWeight? = null, italic: Boolean = false,
    modifier: Modifier = Modifier, onValueChange: (String) -> Unit
) {
    EditableText_P1(value, "Ex: $hint", color, fontSize, fontWeight, italic, modifier, onValueChange)
}

@Composable
private fun SidebarHeader_P1(text: String) {
    Box(Modifier.fillMaxWidth().background(Color.Black.copy(alpha = 0.35f)).padding(vertical = 5.dp, horizontal = 8.dp)) {
        Text(text, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 9.5.sp, letterSpacing = 0.5.sp)
    }
}
@Composable
private fun SidebarLabeledField_P1(icon: ImageVector, label: String, value: String, hint: String, accent: Color, onValueChange: (String) -> Unit) {
    Column(Modifier.fillMaxWidth()) {
        Text(label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 7.5.sp)
        Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
            Box(Modifier.size(16.dp).clip(CircleShape).background(accent), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = Color(0xFF12203D), modifier = Modifier.size(9.dp))
            }
            Spacer(Modifier.width(7.dp))
            FieldWithHint_P1(value, hint, Color.White, 7.5.sp, modifier = Modifier.weight(1f), onValueChange = onValueChange)
        }
    }
}

@Composable
private fun SidebarIconField_P1(icon: ImageVector, value: String, hint: String, accent: Color, onValueChange: (String) -> Unit) {
    Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.size(16.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = Color(0xFF12203D), modifier = Modifier.size(9.dp))
        }
        Spacer(Modifier.width(7.dp))
        FieldWithHint_P1(value, hint, Color.White, 7.5.sp, modifier = Modifier.weight(1f), onValueChange = onValueChange)
    }
}

@Composable
private fun SidebarCheckField_P1(value: String, hint: String, accent: Color, onValueChange: (String) -> Unit) {
    Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.size(13.dp).clip(CircleShape).background(accent), contentAlignment = Alignment.Center) {
            Icon(Icons.Filled.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(8.dp))
        }
        Spacer(Modifier.width(7.dp))
        FieldWithHint_P1(value, hint, Color.White, 7.5.sp, modifier = Modifier.weight(1f), onValueChange = onValueChange)
    }
}

@Composable
private fun DiamondDivider_P1(value: String, hint: String, accent: Color, onValueChange: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.weight(1f).height(1.dp).background(accent.copy(alpha = 0.6f)))
        Text("  ◆  ", color = accent, fontSize = 9.sp)
        EditableText_P1(value, "Ex: $hint", Color(0xFF222222), 10.sp, FontWeight.Medium, modifier = Modifier.widthIn(max = 220.dp), onValueChange = onValueChange)
        Text("  ◆  ", color = accent, fontSize = 9.sp)
        Box(Modifier.weight(1f).height(1.dp).background(accent.copy(alpha = 0.6f)))
    }
}

@Composable
private fun SectionHeader_P1(text: String) {
    Column(Modifier.fillMaxWidth()) {
        Text(text, color = Color(0xFF12203D), fontWeight = FontWeight.Black, fontSize = 11.sp, letterSpacing = 0.2.sp)
        Spacer(Modifier.height(2.dp))
        Box(Modifier.fillMaxWidth().height(1.2.dp).background(Color(0xFF12203D).copy(alpha = 0.25f)))
    }
}

@Composable
private fun BulletHint_P1(value: String, hint: String, color: Color, onValueChange: (String) -> Unit) {
    Row(Modifier.fillMaxWidth()) {
        Text("•  ", color = color, fontSize = 7.5.sp)
        FieldWithHint_P1(value, hint, color, 7.5.sp, modifier = Modifier.weight(1f), onValueChange = onValueChange)
    }
}

@Composable
private fun ExperienceEntry_P1(
    years: String, onYears: (String) -> Unit,
    position: String, onPosition: (String) -> Unit,
    company: String, onCompany: (String) -> Unit,
    desc1: String, onDesc1: (String) -> Unit,
    desc2: String, onDesc2: (String) -> Unit,
    desc3: String, onDesc3: (String) -> Unit,
    accent: Color
) {
    Row(Modifier.fillMaxWidth()) {
        Column(Modifier.width(46.dp)) {
            EditableText_P1(years, "2023-2024", Color(0xFF12203D), 8.5.sp, FontWeight.Bold, modifier = Modifier.fillMaxWidth(), onValueChange = onYears)
        }
        Column(Modifier.width(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.size(7.dp).clip(CircleShape).background(accent))
            Box(Modifier.width(1.dp).weight(1f).background(accent.copy(alpha = 0.3f)))
        }
        Spacer(Modifier.width(7.dp))
        Column(Modifier.weight(1f).padding(bottom = 6.dp), verticalArrangement = Arrangement.spacedBy(1.dp)) {
            FieldWithHint_P1(position, "Job Position", Color(0xFF12203D), 9.5.sp, FontWeight.Bold, onValueChange = onPosition)
            FieldWithHint_P1(company, "Company Name", Color(0xFF444444), 8.sp, italic = true, onValueChange = onCompany)
            Spacer(Modifier.height(3.dp))
            BulletHint_P1(desc1, "Describe your responsibilities and achievements.", Color(0xFF333333), onDesc1)
            BulletHint_P1(desc2, "Describe your responsibilities and achievements.", Color(0xFF333333), onDesc2)
            BulletHint_P1(desc3, "Describe your responsibilities and achievements.", Color(0xFF333333), onDesc3)
        }
    }
}

@Composable
private fun ReferenceBlock_P1(
    name: String, positionCompany: String, phone: String, email: String, avatarUri: String, accent: Color, modifier: Modifier,
    onName: (String) -> Unit, onPosition: (String) -> Unit, onPhone: (String) -> Unit, onEmail: (String) -> Unit, onAvatarUri: (String) -> Unit
) {
    Row(modifier, verticalAlignment = Alignment.Top) {
        SharedAvatarPicker(avatarUri, 28.dp, accent, name, onAvatarUri)
        Spacer(Modifier.width(7.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(1.dp)) {
            FieldWithHint_P1(name, "Full Name", Color(0xFF12203D), 8.sp, FontWeight.Bold, onValueChange = onName)
            FieldWithHint_P1(positionCompany, "Position / Relation", Color(0xFF555555), 7.sp, onValueChange = onPosition)
            FieldWithHint_P1(phone, "Contact Number", Color(0xFF555555), 7.sp, onValueChange = onPhone)
            FieldWithHint_P1(email, "Email Address", Color(0xFF555555), 7.sp, onValueChange = onEmail)
        }
    }
}

@Composable
private fun AdditionalInfoRow_P1(icon: ImageVector, label: String, value: String, hint: String, accent: Color, onValueChange: (String) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.Top) {
        Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(15.dp).padding(top = 1.dp))
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f)) {
            Text(label, color = Color(0xFF12203D), fontWeight = FontWeight.Bold, fontSize = 9.sp)
            FieldWithHint_P1(value, hint, Color(0xFF333333), 8.sp, onValueChange = onValueChange)
        }
    }
    Box(Modifier.fillMaxWidth().height(0.6.dp).background(Color(0xFF12203D).copy(alpha = 0.12f)))
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
    exp1Position: String = "", exp1Company: String = "", exp1Dates: String = "", exp1Desc: String = "", exp1Desc2: String = "", exp1Desc3: String = "",
    exp2Position: String = "", exp2Company: String = "", exp2Dates: String = "", exp2Desc: String = "", exp2Desc2: String = "", exp2Desc3: String = "",
    exp3Position: String = "", exp3Company: String = "", exp3Dates: String = "", exp3Desc: String = "", exp3Desc2: String = "", exp3Desc3: String = "",
    refName: String = "", refPositionCompany: String = "", refPhone: String = "", refEmail: String = "", refAvatarUri: String = "",
    ref2Name: String = "", ref2PositionCompany: String = "", ref2Phone: String = "", ref2Email: String = "", ref2AvatarUri: String = "",
    workSetup: String = "", workSchedule: String = "", preferredRole: String = "",
    prefLocations: String = "", availability: String = "", languages: String = "",
    certifications: String = "", hobbies: String = "", careerGoal: String = "", strengths: String = "", otherInfo: String = "",
    onFieldChange: (String, String) -> Unit = { _, _ -> },
    onHomeOverride: () -> Unit = {}
) {
    val accent = Color(0xFF4FC3F7)
    val sidebarBg = Color(0xFF12203D)
    val graphicsLayer = androidx.compose.ui.graphics.rememberGraphicsLayer()
    val nameFontSize = autoShrinkNameFontSize(userName)

    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier.fillMaxSize().background(Color.White).verticalScroll(rememberScrollState())
                .drawWithContent { graphicsLayer.record { this@drawWithContent.drawContent() }; drawLayer(graphicsLayer) }
        ) {
            Row(Modifier.defaultMinSize(minHeight = 900.dp).height(IntrinsicSize.Max)) {

                // ===== LEFT SIDEBAR =====
                Column(
                    Modifier.width(150.dp).fillMaxHeight().background(sidebarBg).padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(11.dp)
                ) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        SharedAvatarPicker(avatarUri, 78.dp, accent, userName) { onFieldChange("avatarUri", it) }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        SidebarHeader_P1("CONTACT")
                        SidebarIconField_P1(Icons.Filled.Phone, contactPhone, "09123456789", accent) { onFieldChange("phone", it) }
                        SidebarIconField_P1(Icons.Filled.Email, contactEmail, "yourname@gmail.com", accent) { onFieldChange("email", it) }
                        SidebarIconField_P1(Icons.Filled.Place, contactAddress, "City, Country", accent) { onFieldChange("location", it) }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        SidebarHeader_P1("EDUCATION")
                        FieldWithHint_P1(edu1Degree, "Bachelor of Science in Information Technology", Color.White, 8.sp, FontWeight.Bold) { onFieldChange("edu1Degree", it) }
                        FieldWithHint_P1(edu1School, "University Name", Color.White.copy(alpha = 0.85f), 7.sp) { onFieldChange("edu1School", it) }
                        FieldWithHint_P1(edu1Years, "20XX-20XX", accent, 7.sp) { onFieldChange("edu1Years", it) }
                        Spacer(Modifier.height(3.dp))
                        FieldWithHint_P1(edu2Degree, "Senior High School", Color.White, 8.sp, FontWeight.Bold) { onFieldChange("edu2Degree", it) }
                        FieldWithHint_P1(edu2School, "School Name", Color.White.copy(alpha = 0.85f), 7.sp) { onFieldChange("edu2School", it) }
                        FieldWithHint_P1(edu2Years, "Year Graduated", accent, 7.sp) { onFieldChange("edu2Years", it) }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        SidebarHeader_P1("SKILLS")
                        val skillHints = listOf("Skill name", "Skill name", "Skill name", "Skill name", "Skill name", "Skill name")
                        skills.take(6).forEachIndexed { i, s ->
                            SidebarCheckField_P1(s, skillHints.getOrElse(i) { "Skill name" }, accent) { onFieldChange("skill${i + 1}", it) }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        SidebarHeader_P1("WEBSITE / LINKS")
                        SidebarCheckField_P1(contactWebsite, "https://yourwebsite.com", accent) { onFieldChange("website", it) }
                        SidebarCheckField_P1(contactLinkedin, "linkedin.com/in/yourname", accent) { onFieldChange("linkedin", it) }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        SidebarHeader_P1("PREFERENCES")
                        SidebarLabeledField_P1(Icons.Filled.Work, "Work Setup", workSetup, "Flexible / Remote / On-site", accent) { onFieldChange("workSetup", it) }
                        SidebarLabeledField_P1(Icons.Filled.DateRange, "Work Schedule", workSchedule, "Full-time / Part-time / Freelance", accent) { onFieldChange("workSchedule", it) }
                        SidebarLabeledField_P1(Icons.Filled.Person, "Preferred Role", preferredRole, "Software Developer / Designer / VA", accent) { onFieldChange("preferredRole", it) }
                        SidebarLabeledField_P1(Icons.Filled.Place, "Locations", prefLocations, "Bukidnon or Remote / Philippines", accent) { onFieldChange("prefLocations", it) }
                        SidebarLabeledField_P1(Icons.Filled.AccessTime, "Availability", availability, "Immediately / Within 2 Weeks", accent) { onFieldChange("availability", it) }
                        SidebarLabeledField_P1(Icons.Filled.Public, "Languages", languages, "English (Fluent) / Filipino (Native)", accent) { onFieldChange("languages", it) }
                    }
                }

                // ===== RIGHT CONTENT =====
                Column(Modifier.weight(1f).padding(horizontal = 14.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                    Column {
                        EditableText_P1(userName, "Your Name", Color(0xFF12203D), nameFontSize, FontWeight.Black) { onFieldChange("fullName", it) }
                        Spacer(Modifier.height(5.dp))
                        DiamondDivider_P1(userTitle, "Ex: Software Developer", accent) { onFieldChange("professionalTitle", it) }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        SectionHeader_P1("ABOUT ME")
                        FieldWithHint_P1(aboutMe, "Tell something about yourself, your goals, and what makes you a great fit for the position.", Color(0xFF333333), 8.sp) { onFieldChange("aboutMe", it) }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        SectionHeader_P1("PROFESSIONAL EXPERIENCE")
                        Spacer(Modifier.height(3.dp))
                        ExperienceEntry_P1(exp1Dates, { onFieldChange("exp1Dates", it) }, exp1Position, { onFieldChange("exp1Position", it) },
                            exp1Company, { onFieldChange("exp1Company", it) },
                            exp1Desc, { onFieldChange("exp1Desc", it) }, exp1Desc2, { onFieldChange("exp1Desc2", it) }, exp1Desc3, { onFieldChange("exp1Desc3", it) }, accent)
                        ExperienceEntry_P1(exp2Dates, { onFieldChange("exp2Dates", it) }, exp2Position, { onFieldChange("exp2Position", it) },
                            exp2Company, { onFieldChange("exp2Company", it) },
                            exp2Desc, { onFieldChange("exp2Desc", it) }, exp2Desc2, { onFieldChange("exp2Desc2", it) }, exp2Desc3, { onFieldChange("exp2Desc3", it) }, accent)
                        ExperienceEntry_P1(exp3Dates, { onFieldChange("exp3Dates", it) }, exp3Position, { onFieldChange("exp3Position", it) },
                            exp3Company, { onFieldChange("exp3Company", it) },
                            exp3Desc, { onFieldChange("exp3Desc", it) }, exp3Desc2, { onFieldChange("exp3Desc2", it) }, exp3Desc3, { onFieldChange("exp3Desc3", it) }, accent)
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        SectionHeader_P1("REFERENCES")
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            ReferenceBlock_P1(refName, refPositionCompany, refPhone, refEmail, refAvatarUri, accent, Modifier.weight(1f),
                                { onFieldChange("refName", it) }, { onFieldChange("refPositionCompany", it) },
                                { onFieldChange("refPhone", it) }, { onFieldChange("refEmail", it) }, { onFieldChange("refAvatarUri", it) })
                            ReferenceBlock_P1(ref2Name, ref2PositionCompany, ref2Phone, ref2Email, ref2AvatarUri, accent, Modifier.weight(1f),
                                { onFieldChange("ref2Name", it) }, { onFieldChange("ref2PositionCompany", it) },
                                { onFieldChange("ref2Phone", it) }, { onFieldChange("ref2Email", it) }, { onFieldChange("ref2AvatarUri", it) })
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        SectionHeader_P1("ADDITIONAL INFORMATION")
                        Spacer(Modifier.height(2.dp))
                        AdditionalInfoRow_P1(Icons.Filled.EmojiEvents, "Certifications", certifications, "List your certifications or trainings here.", accent) { onFieldChange("certifications", it) }
                        AdditionalInfoRow_P1(Icons.Filled.Favorite, "Hobbies", hobbies, "What are your hobbies or interests?", accent) { onFieldChange("hobbies", it) }
                        AdditionalInfoRow_P1(Icons.Filled.Flag, "Career Goal", careerGoal, "Your short-term or long-term career goal.", accent) { onFieldChange("careerGoal", it) }
                        AdditionalInfoRow_P1(Icons.Filled.Person, "Strengths", strengths, "Your key strengths and qualities.", accent) { onFieldChange("strengths", it) }
                        AdditionalInfoRow_P1(Icons.Filled.Info, "Other Information", otherInfo, "Any other information that may help your application.", accent) { onFieldChange("otherInfo", it) }
                    }
                }
            }
        }

        com.saltech.urdocs.ui.templates.TemplateExportMenu(
            graphicsLayer, "resume_$userName", onHome = onHomeOverride,
            modifier = Modifier.align(Alignment.TopEnd).padding(top = 4.dp, end = 12.dp)
        )
    }
}

