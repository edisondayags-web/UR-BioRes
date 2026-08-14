package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

private val T01White = Color(0xFFF5F5F5)
private val T01Gray = Color(0xFF6E6E6E)
private val T01Divider = Color(0xFF3A3A3A)
private val T01BarTrack = Color(0xFF2A2A2A)

// ===== DATA CLASS =====
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
    val skill1: String = "", val skill2: String = "", val skill3: String = "", 
    val skill4: String = "", val skill5: String = "", val skill6: String = "",
    val exp1Position: String = "", val exp1Company: String = "", val exp1Dates: String = "", val exp1Desc: String = "",
    val exp2Position: String = "", val exp2Company: String = "", val exp2Dates: String = "",
    val refName: String = "", val refPositionCompany: String = "", val refContact: String = ""
)

// ===== FORM SCREEN WRAPPER =====
@Composable
fun ResumeTemplateFormScreen(
    templateName: String,
    onBack: () -> Unit = {}
) {
    var data by remember { mutableStateOf(ResumeTemplateFields()) }
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF0D0D0D))) {
        IconButton(onClick = onBack) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
        }
        when (templateName) {
            "resume_template_01" -> ResumeTemplate01Screen(data, { data = it })
            "resume_template_02" -> ResumeTemplate02Screen(data, { data = it })
            "resume_template_03" -> ResumeTemplate03Screen(data, { data = it })
            "resume_template_04" -> ResumeTemplate04Screen(data, { data = it })
            "resume_template_05" -> ResumeTemplate05Screen(data, { data = it })
            "resume_template_06" -> ResumeTemplate06Screen(data, { data = it })
            "resume_template_07" -> ResumeTemplate07Screen(data, { data = it })
            "resume_template_08" -> ResumeTemplate08Screen(data, { data = it })
            "resume_template_09" -> ResumeTemplate09Screen(data, { data = it })
            "resume_template_10" -> ResumeTemplate10Screen(data, { data = it })
            "resume_template_11" -> ResumeTemplate11Screen(data, { data = it })
            "resume_template_12" -> ResumeTemplate12Screen(data, { data = it })
            "resume_template_13" -> ResumeTemplate13Screen(data, { data = it })
            "resume_template_14" -> ResumeTemplate14Screen(data, { data = it })
            "resume_template_15" -> ResumeTemplate15Screen(data, { data = it })
            "resume_template_16" -> ResumeTemplate16Screen(data, { data = it })
            "resume_template_17" -> ResumeTemplate17Screen(data, { data = it })
            "resume_template_18" -> ResumeTemplate18Screen(data, { data = it })
            "resume_template_19" -> ResumeTemplate19Screen(data, { data = it })
            "resume_template_20" -> ResumeTemplate20Screen(data, { data = it })
            else -> ResumeTemplate01Screen(data, { data = it })
        }
    }
}

// ===== INDIVIDUAL TEMPLATES (01 to 12) =====

@Composable
fun ResumeTemplate01Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit) {
    BaseResumeTemplateScreenV2(Color(0xFF0D0D0D), Color(0xFFD4AF37), Color.White, Color(0xFFAAAAAA), "01", true, data, onFieldChange)
}

@Composable
fun ResumeTemplate02Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit) {
    BaseResumeTemplateScreenV3(Color.White, Color(0xFF1B3358), Color(0xFF1B1B1B), Color(0xFF777777), "02", false, data, onFieldChange)
}

@Composable
fun ResumeTemplate03Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit) {
    BaseResumeTemplateScreenV2(Color(0xFF0D140D), Color(0xFF4CAF50), Color.White, Color(0xFFAAAAAA), "03", false, data, onFieldChange)
}

@Composable
fun ResumeTemplate04Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit) {
    BaseResumeTemplateScreenV2(Color(0xFFFBF3E7), Color(0xFFC9A227), Color(0xFF2B2B2B), Color(0xFF6E6E6E), "04", true, data, onFieldChange)
}

@Composable
fun ResumeTemplate05Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit) {
    BaseResumeTemplateScreenV2(Color(0xFF0F0A14), Color(0xFF9B6FE0), Color.White, Color(0xFFAAAAAA), "05", false, data, onFieldChange)
}

@Composable
fun ResumeTemplate06Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit) {
    BaseResumeTemplateScreenV4(Color.White, Color(0xFF2E7D6B), Color(0xFF1B1B1B), Color(0xFF777777), "06", false, data, onFieldChange)
}

@Composable
fun ResumeTemplate07Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit) {
    BaseResumeTemplateScreenV2(Color(0xFF0B1224), Color(0xFFD4AF37), Color.White, Color(0xFFAAAAAA), "07", false, data, onFieldChange)
}

@Composable
fun ResumeTemplate08Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit) {
    BaseResumeTemplateScreenV3(Color(0xFFF3F6FA), Color(0xFF2F4B7C), Color(0xFF1B1B1B), Color(0xFF777777), "08", true, data, onFieldChange)
}

@Composable
fun ResumeTemplate09Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit) {
    BaseResumeTemplateScreenV2(Color(0xFFFBF3E7), Color(0xFFC9A227), Color(0xFF2B2B2B), Color(0xFF6E6E6E), "09", false, data, onFieldChange)
}

@Composable
fun ResumeTemplate10Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit) {
    BaseResumeTemplateScreenV2(Color(0xFF0D0D0D), Color(0xFFCC2B2B), Color.White, Color(0xFFAAAAAA), "10", false, data, onFieldChange)
}

@Composable
fun ResumeTemplate11Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit) {
    BaseResumeTemplateScreenV4(Color.White, Color(0xFF2E5E3E), Color(0xFF1B1B1B), Color(0xFF777777), "11", false, data, onFieldChange)
}

@Composable
fun ResumeTemplate12Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit) {
    BaseResumeTemplateScreenV2(Color(0xFF14071A), Color(0xFFB744C4), Color.White, Color(0xFFAAAAAA), "12", true, data, onFieldChange)
}

// ===== REUSABLE BASE ENGINE & COMPONENTS =====

// ===== V2 TEMPLATES (13 to 20) - photo circle layout =====

@Composable
fun ResumeTemplate13Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit) {
    BaseResumeTemplateScreenV2(Color(0xFFFDF8F0), Color(0xFF6B8E4E), Color(0xFF2B2B2B), Color(0xFF6E6E6E), "13", true, data, onFieldChange)
}

@Composable
fun ResumeTemplate14Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit) {
    BaseResumeTemplateScreenV2(Color(0xFF1A0F2E), Color(0xFFB794F6), Color.White, Color(0xFFBBBBBB), "14", true, data, onFieldChange)
}

@Composable
fun ResumeTemplate15Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit) {
    BaseResumeTemplateScreenV3(Color.White, Color(0xFF1B3358), Color(0xFF1B1B1B), Color(0xFF777777), "15", false, data, onFieldChange)
}

@Composable
fun ResumeTemplate16Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit) {
    BaseResumeTemplateScreenV2(Color(0xFF0D0D0D), Color(0xFFD4AF37), Color.White, Color(0xFFAAAAAA), "16", false, data, onFieldChange)
}

@Composable
fun ResumeTemplate17Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit) {
    BaseResumeTemplateScreenV2(Color(0xFF071A1A), Color(0xFF33CCCC), Color.White, Color(0xFFAAAAAA), "17", true, data, onFieldChange)
}

@Composable
fun ResumeTemplate18Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit) {
    BaseResumeTemplateScreenV2(Color(0xFFFBF3E7), Color(0xFFB8860B), Color(0xFF2B2B2B), Color(0xFF6E6E6E), "18", true, data, onFieldChange)
}

@Composable
fun ResumeTemplate19Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit) {
    BaseResumeTemplateScreenV2(Color(0xFF1A0808), Color(0xFFCC3355), Color.White, Color(0xFFAAAAAA), "19", false, data, onFieldChange)
}

@Composable
fun ResumeTemplate20Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit) {
    BaseResumeTemplateScreenV4(Color(0xFFF3FAF9), Color(0xFF2E7D6B), Color(0xFF1B1B1B), Color(0xFF666666), "20", false, data, onFieldChange)
}

@Composable
private fun SidebarHeader(text: String, accentColor: Color) {
    Text(text, color = accentColor, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 10.dp))
}

@Composable
private fun SectionHeader(text: String, accentColor: Color) {
    Text(text, color = accentColor, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 6.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(accentColor)
    )
    Spacer(Modifier.height(10.dp))
}

@Composable
private fun HorizontalLine(accentColor: Color) {
    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(T01Divider))
}

@Composable
private fun ContactRow(icon: ImageVector, value: String, placeholder: String, accentColor: Color, textColor: Color = T01White, onValueChange: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(10.dp))
        PlaceholderText(value, placeholder, textColor, 12.sp, modifier = Modifier.weight(1f), onValueChange = onValueChange)
    }
}

@Composable
private fun LinkedInRow(value: String, accentColor: Color, textColor: Color = T01White, onValueChange: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(accentColor),
            contentAlignment = Alignment.Center
        ) {
            Text("in", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(10.dp))
        PlaceholderText(value, "linkedin.com/in/yourname", textColor, 12.sp, modifier = Modifier.weight(1f), onValueChange = onValueChange)
    }
}

@Composable
private fun SkillBarRow(value: String, defaultLabel: String, accentColor: Color, textColor: Color = T01White, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
        PlaceholderText(value, defaultLabel, textColor, 12.sp, onValueChange = onValueChange)
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(4.dp)).background(T01BarTrack)
        ) {
            Box(modifier = Modifier.fillMaxWidth(0.8f).fillMaxHeight().background(accentColor))
        }
    }
}

@Composable
private fun SkillDotRow(value: String, defaultLabel: String, accentColor: Color, textColor: Color = T01White, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
        PlaceholderText(value, defaultLabel, textColor, 12.sp, onValueChange = onValueChange)
        Spacer(Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(5) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(RoundedCornerShape(50))
                        .background(accentColor)
                )
            }
        }
    }
}

@Composable
private fun TimelineEntry(accentColor: Color, content: @Composable ColumnScope.() -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.width(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Canvas(modifier = Modifier.size(10.dp)) {
                drawCircle(color = accentColor, radius = size.minDimension / 2, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f))
            }
            Box(modifier = Modifier.width(1.dp).height(50.dp).background(T01Divider))
        }
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f), content = content)
    }
}

@Composable
private fun PlaceholderText(
    value: String,
    placeholder: String,
    color: Color,
    fontSize: TextUnit,
    fontWeight: FontWeight? = null,
    modifier: Modifier = Modifier,
    multiline: Boolean = false,
    onValueChange: (String) -> Unit = {}
) {
    Box(modifier = modifier.fillMaxWidth()) {
        if (value.isEmpty()) {
            Text(placeholder, color = color.copy(alpha = 0.45f), fontSize = fontSize, fontWeight = fontWeight)
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(color = color, fontSize = fontSize, fontWeight = fontWeight),
            cursorBrush = SolidColor(T01White),
            maxLines = if (multiline) Int.MAX_VALUE else 1,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
internal fun BaseResumeTemplateScreenV2(
    backgroundColor: Color,
    accentColor: Color,
    textColor: Color,
    subTextColor: Color,
    badgeNumber: String,
    useDotSkills: Boolean,
    data: ResumeTemplateFields,
    onFieldChange: (ResumeTemplateFields) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.TopCenter
    ) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 700.dp)
            .background(backgroundColor)
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(50))
                    .background(accentColor.copy(alpha = 0.2f))
                    .border(2.dp, accentColor, RoundedCornerShape(50)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Person, contentDescription = null, tint = accentColor, modifier = Modifier.size(30.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column {
                PlaceholderText(data.fullName, "YOUR NAME", textColor, 22.sp, FontWeight.Bold) {
                    onFieldChange(data.copy(fullName = it))
                }
                PlaceholderText(data.professionalTitle, "PROFESSIONAL TITLE", accentColor, 12.sp) {
                    onFieldChange(data.copy(professionalTitle = it))
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(accentColor.copy(alpha = 0.4f)))
        Spacer(Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(0.4f).padding(end = 12.dp)) {
                SidebarHeader("CONTACT", accentColor)
                ContactRow(Icons.Filled.Phone, data.phone, "+63 XXX XXX XXXX", accentColor, textColor) { onFieldChange(data.copy(phone = it)) }
                ContactRow(Icons.Filled.Email, data.email, "youremail@email.com", accentColor, textColor) { onFieldChange(data.copy(email = it)) }
                ContactRow(Icons.Filled.Place, data.location, "City, State, Country", accentColor, textColor) { onFieldChange(data.copy(location = it)) }
                LinkedInRow(data.linkedin, accentColor, textColor) { onFieldChange(data.copy(linkedin = it)) }

                Spacer(Modifier.height(14.dp))
                SidebarHeader("SKILLS", accentColor)
                val skills = listOf(
                    Pair(data.skill1, "Problem Solving"),
                    Pair(data.skill2, "Communication"),
                    Pair(data.skill3, "Teamwork"),
                    Pair(data.skill4, "Leadership"),
                    Pair(data.skill5, "Time Management"),
                    Pair(data.skill6, "Creativity")
                )
                skills.forEachIndexed { index, pair ->
                    if (useDotSkills) {
                        SkillDotRow(pair.first, pair.second, accentColor, textColor) { newVal ->
                            onFieldChange(
                                when (index) {
                                    0 -> data.copy(skill1 = newVal)
                                    1 -> data.copy(skill2 = newVal)
                                    2 -> data.copy(skill3 = newVal)
                                    3 -> data.copy(skill4 = newVal)
                                    4 -> data.copy(skill5 = newVal)
                                    else -> data.copy(skill6 = newVal)
                                }
                            )
                        }
                    } else {
                        SkillBarRow(pair.first, pair.second, accentColor, textColor) { newVal ->
                            onFieldChange(
                                when (index) {
                                    0 -> data.copy(skill1 = newVal)
                                    1 -> data.copy(skill2 = newVal)
                                    2 -> data.copy(skill3 = newVal)
                                    3 -> data.copy(skill4 = newVal)
                                    4 -> data.copy(skill5 = newVal)
                                    else -> data.copy(skill6 = newVal)
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))
                SidebarHeader("REFERENCES", accentColor)
                PlaceholderText(data.refName, "Reference Name", subTextColor, 11.sp) {
                    onFieldChange(data.copy(refName = it))
                }
                PlaceholderText(data.refPositionCompany, "Job Position / Company", subTextColor, 10.sp) {
                    onFieldChange(data.copy(refPositionCompany = it))
                }
                PlaceholderText(data.refContact, "email@email.com", subTextColor, 10.sp) {
                    onFieldChange(data.copy(refContact = it))
                }
            }

            Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(accentColor.copy(alpha = 0.3f)))

            Column(modifier = Modifier.weight(0.6f).padding(start = 12.dp)) {
                SectionHeader("ABOUT ME", accentColor)
                PlaceholderText(data.aboutMe, "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", subTextColor, 11.sp, multiline = true) {
                    onFieldChange(data.copy(aboutMe = it))
                }

                Spacer(Modifier.height(14.dp))
                SectionHeader("EDUCATION", accentColor)
                PlaceholderText(data.edu1Degree, "DEGREE NAME / MAJOR", textColor, 12.sp, FontWeight.Bold) {
                    onFieldChange(data.copy(edu1Degree = it))
                }
                PlaceholderText(data.edu1School, "University Name", subTextColor, 11.sp) {
                    onFieldChange(data.copy(edu1School = it))
                }
                PlaceholderText(data.edu1Years, "2018 - 2022", subTextColor, 11.sp) {
                    onFieldChange(data.copy(edu1Years = it))
                }

                Spacer(Modifier.height(14.dp))
                SectionHeader("EXPERIENCE", accentColor)
                PlaceholderText(data.exp1Position, "JOB POSITION HERE", textColor, 12.sp, FontWeight.Bold) {
                    onFieldChange(data.copy(exp1Position = it))
                }
                PlaceholderText(data.exp1Company, "Company Name | 2020 - Present", accentColor, 10.sp) {
                    onFieldChange(data.copy(exp1Company = it))
                }
                PlaceholderText(data.exp1Desc, "Lorem ipsum dolor sit amet, sed do eiusmod tempor.", subTextColor, 10.sp, multiline = true) {
                    onFieldChange(data.copy(exp1Desc = it))
                }
                Spacer(Modifier.height(8.dp))
                PlaceholderText(data.exp2Position, "JOB POSITION HERE", textColor, 12.sp, FontWeight.Bold) {
                    onFieldChange(data.copy(exp2Position = it))
                }
                PlaceholderText(data.exp2Company, "Company Name | 2018 - 2020", accentColor, 10.sp) {
                    onFieldChange(data.copy(exp2Company = it))
                }
            }
        }
    }
    }
}


// ===== V3: sidebar on RIGHT =====
@Composable
internal fun BaseResumeTemplateScreenV3(
    backgroundColor: Color, accentColor: Color, textColor: Color, subTextColor: Color,
    badgeNumber: String, useDotSkills: Boolean, data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A1A)).verticalScroll(rememberScrollState()), contentAlignment = Alignment.TopCenter) {
    Row(modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 700.dp).background(backgroundColor)) {
        Column(modifier = Modifier.weight(0.6f).padding(20.dp)) {
            PlaceholderText(data.fullName, "YOUR NAME", textColor, 24.sp, FontWeight.Bold) { onFieldChange(data.copy(fullName = it)) }
            PlaceholderText(data.professionalTitle, "PROFESSIONAL TITLE", accentColor, 12.sp) { onFieldChange(data.copy(professionalTitle = it)) }
            Spacer(Modifier.height(16.dp))
            SectionHeader("ABOUT ME", accentColor)
            PlaceholderText(data.aboutMe, "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", subTextColor, 11.sp, multiline = true) { onFieldChange(data.copy(aboutMe = it)) }
            Spacer(Modifier.height(16.dp))
            SectionHeader("EDUCATION", accentColor)
            PlaceholderText(data.edu1Degree, "DEGREE NAME / MAJOR", textColor, 12.sp, FontWeight.Bold) { onFieldChange(data.copy(edu1Degree = it)) }
            PlaceholderText(data.edu1School, "University Name", subTextColor, 11.sp) { onFieldChange(data.copy(edu1School = it)) }
            PlaceholderText(data.edu1Years, "2018 - 2022", subTextColor, 11.sp) { onFieldChange(data.copy(edu1Years = it)) }
            Spacer(Modifier.height(16.dp))
            SectionHeader("EXPERIENCE", accentColor)
            PlaceholderText(data.exp1Position, "JOB POSITION HERE", textColor, 12.sp, FontWeight.Bold) { onFieldChange(data.copy(exp1Position = it)) }
            PlaceholderText(data.exp1Company, "Company Name | 2020 - Present", accentColor, 10.sp) { onFieldChange(data.copy(exp1Company = it)) }
            PlaceholderText(data.exp1Desc, "Lorem ipsum dolor sit amet.", subTextColor, 10.sp, multiline = true) { onFieldChange(data.copy(exp1Desc = it)) }
        }
        Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(accentColor.copy(alpha = 0.3f)))
        Column(modifier = Modifier.weight(0.4f).padding(20.dp).background(accentColor.copy(alpha = 0.06f))) {
            Box(modifier = Modifier.size(64.dp).clip(RoundedCornerShape(50)).background(accentColor.copy(alpha = 0.2f)).border(2.dp, accentColor, RoundedCornerShape(50)), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.Person, contentDescription = null, tint = accentColor, modifier = Modifier.size(34.dp))
            }
            Spacer(Modifier.height(16.dp))
            SidebarHeader("CONTACT", accentColor)
            ContactRow(Icons.Filled.Phone, data.phone, "+63 XXX XXX XXXX", accentColor, textColor) { onFieldChange(data.copy(phone = it)) }
            ContactRow(Icons.Filled.Email, data.email, "youremail@email.com", accentColor, textColor) { onFieldChange(data.copy(email = it)) }
            ContactRow(Icons.Filled.Place, data.location, "City, State, Country", accentColor, textColor) { onFieldChange(data.copy(location = it)) }
            LinkedInRow(data.linkedin, accentColor, textColor) { onFieldChange(data.copy(linkedin = it)) }
            Spacer(Modifier.height(14.dp))
            SidebarHeader("SKILLS", accentColor)
            val skills = listOf(Pair(data.skill1,"Problem Solving"), Pair(data.skill2,"Communication"), Pair(data.skill3,"Teamwork"), Pair(data.skill4,"Leadership"), Pair(data.skill5,"Time Management"), Pair(data.skill6,"Creativity"))
            skills.forEachIndexed { index, pair ->
                if (useDotSkills) SkillDotRow(pair.first, pair.second, accentColor, textColor) { newVal -> onFieldChange(when(index){0->data.copy(skill1=newVal);1->data.copy(skill2=newVal);2->data.copy(skill3=newVal);3->data.copy(skill4=newVal);4->data.copy(skill5=newVal);else->data.copy(skill6=newVal)}) }
                else SkillBarRow(pair.first, pair.second, accentColor, textColor) { newVal -> onFieldChange(when(index){0->data.copy(skill1=newVal);1->data.copy(skill2=newVal);2->data.copy(skill3=newVal);3->data.copy(skill4=newVal);4->data.copy(skill5=newVal);else->data.copy(skill6=newVal)}) }
            }
            Spacer(Modifier.height(14.dp))
            SidebarHeader("REFERENCES", accentColor)
            PlaceholderText(data.refName, "Reference Name", subTextColor, 11.sp) { onFieldChange(data.copy(refName = it)) }
            PlaceholderText(data.refPositionCompany, "Job Position / Company", subTextColor, 10.sp) { onFieldChange(data.copy(refPositionCompany = it)) }
        }
    }
    }
}

// ===== V4: top banner, photo centered =====
@Composable
internal fun BaseResumeTemplateScreenV4(
    backgroundColor: Color, accentColor: Color, textColor: Color, subTextColor: Color,
    badgeNumber: String, useDotSkills: Boolean, data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A1A)).verticalScroll(rememberScrollState()), contentAlignment = Alignment.TopCenter) {
    Column(modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 700.dp).background(backgroundColor)) {
        Column(
            modifier = Modifier.fillMaxWidth().background(accentColor.copy(alpha = 0.15f)).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.size(72.dp).clip(RoundedCornerShape(50)).background(accentColor.copy(alpha = 0.25f)).border(2.dp, accentColor, RoundedCornerShape(50)), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.Person, contentDescription = null, tint = accentColor, modifier = Modifier.size(38.dp))
            }
            Spacer(Modifier.height(10.dp))
            PlaceholderText(data.fullName, "YOUR NAME", textColor, 22.sp, FontWeight.Bold) { onFieldChange(data.copy(fullName = it)) }
            PlaceholderText(data.professionalTitle, "PROFESSIONAL TITLE", accentColor, 12.sp) { onFieldChange(data.copy(professionalTitle = it)) }
        }
        Row(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Column(modifier = Modifier.weight(0.42f).padding(end = 12.dp)) {
                SidebarHeader("CONTACT", accentColor)
                ContactRow(Icons.Filled.Phone, data.phone, "+63 XXX XXX XXXX", accentColor, textColor) { onFieldChange(data.copy(phone = it)) }
                ContactRow(Icons.Filled.Email, data.email, "youremail@email.com", accentColor, textColor) { onFieldChange(data.copy(email = it)) }
                ContactRow(Icons.Filled.Place, data.location, "City, State, Country", accentColor, textColor) { onFieldChange(data.copy(location = it)) }
                Spacer(Modifier.height(14.dp))
                SidebarHeader("SKILLS", accentColor)
                val skills = listOf(Pair(data.skill1,"Problem Solving"), Pair(data.skill2,"Communication"), Pair(data.skill3,"Teamwork"), Pair(data.skill4,"Leadership"), Pair(data.skill5,"Time Management"), Pair(data.skill6,"Creativity"))
                skills.forEachIndexed { index, pair ->
                    if (useDotSkills) SkillDotRow(pair.first, pair.second, accentColor, textColor) { newVal -> onFieldChange(when(index){0->data.copy(skill1=newVal);1->data.copy(skill2=newVal);2->data.copy(skill3=newVal);3->data.copy(skill4=newVal);4->data.copy(skill5=newVal);else->data.copy(skill6=newVal)}) }
                    else SkillBarRow(pair.first, pair.second, accentColor, textColor) { newVal -> onFieldChange(when(index){0->data.copy(skill1=newVal);1->data.copy(skill2=newVal);2->data.copy(skill3=newVal);3->data.copy(skill4=newVal);4->data.copy(skill5=newVal);else->data.copy(skill6=newVal)}) }
                }
            }
            Column(modifier = Modifier.weight(0.58f).padding(start = 12.dp)) {
                SectionHeader("ABOUT ME", accentColor)
                PlaceholderText(data.aboutMe, "Lorem ipsum dolor sit amet.", subTextColor, 11.sp, multiline = true) { onFieldChange(data.copy(aboutMe = it)) }
                Spacer(Modifier.height(14.dp))
                SectionHeader("EXPERIENCE", accentColor)
                PlaceholderText(data.exp1Position, "JOB POSITION HERE", textColor, 12.sp, FontWeight.Bold) { onFieldChange(data.copy(exp1Position = it)) }
                PlaceholderText(data.exp1Company, "Company Name | 2020 - Present", accentColor, 10.sp) { onFieldChange(data.copy(exp1Company = it)) }
                PlaceholderText(data.exp1Desc, "Lorem ipsum dolor sit amet.", subTextColor, 10.sp, multiline = true) { onFieldChange(data.copy(exp1Desc = it)) }
            }
        }
    }
    }
}
