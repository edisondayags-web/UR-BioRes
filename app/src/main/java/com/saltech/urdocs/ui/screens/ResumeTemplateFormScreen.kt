package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---- Colors (Template 01 - red theme) ----
private val T01Bg = Color(0xFF0D0D0D)
private val T01Accent = Color(0xFFE8121F)
private val T01White = Color(0xFFF5F5F5)
private val T01Gray = Color(0xFFB3B3B3)
private val T01Divider = Color(0xFF3A3A3A)
private val T01BarTrack = Color(0xFF2A2A2A)

@Composable
fun ResumeTemplate01Screen(
    data: ResumeTemplateFields,
    onFieldChange: (ResumeTemplateFields) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(T01Bg)
            .verticalScroll(rememberScrollState())
    ) {
        // ===== LEFT SIDEBAR =====
        Column(
            modifier = Modifier
                .weight(0.34f)
                .padding(start = 20.dp, end = 16.dp, bottom = 32.dp)
        ) {
            // corner badge "01"
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .padding(top = 12.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    val path = Path().apply {
                        moveTo(0f, 0f)
                        lineTo(size.width, 0f)
                        lineTo(0f, size.height)
                        close()
                    }
                    drawPath(path, color = T01Accent)
                }
            }

            Spacer(Modifier.height(20.dp))

            EditableText(
                value = "UR",
                onValueChange = {},
                style = TextStyle(color = T01Accent, fontSize = 40.sp, fontWeight = FontWeight.Bold),
                readOnly = true
            )
            EditableText(
                value = "Resume",
                onValueChange = {},
                style = TextStyle(color = T01White, fontSize = 16.sp),
                readOnly = true
            )

            Spacer(Modifier.height(16.dp))
            Divider(color = T01Divider, thickness = 60.dp, height = true)
            Spacer(Modifier.height(20.dp))

            SidebarHeader("CONTACT")
            ContactRow(data.phone) { onFieldChange(data.copy(phone = it)) }
            ContactRow(data.email) { onFieldChange(data.copy(email = it)) }
            ContactRow(data.location) { onFieldChange(data.copy(location = it)) }
            ContactRow(data.linkedin) { onFieldChange(data.copy(linkedin = it)) }
            ContactRow(data.website) { onFieldChange(data.copy(website = it)) }

            Spacer(Modifier.height(16.dp))
            HorizontalLine()
            Spacer(Modifier.height(16.dp))

            SidebarHeader("SKILLS")
            SkillRow(data.skill1) { onFieldChange(data.copy(skill1 = it)) }
            SkillRow(data.skill2) { onFieldChange(data.copy(skill2 = it)) }
            SkillRow(data.skill3) { onFieldChange(data.copy(skill3 = it)) }
            SkillRow(data.skill4) { onFieldChange(data.copy(skill4 = it)) }
            SkillRow(data.skill5) { onFieldChange(data.copy(skill5 = it)) }

            Spacer(Modifier.height(16.dp))
            HorizontalLine()
            Spacer(Modifier.height(16.dp))

            SidebarHeader("REFERENCES")
            EditableText(
                value = data.refName,
                onValueChange = { onFieldChange(data.copy(refName = it)) },
                style = TextStyle(color = T01Gray, fontSize = 12.sp)
            )
        }

        // ===== RIGHT CONTENT =====
        Column(
            modifier = Modifier
                .weight(0.66f)
                .padding(start = 16.dp, end = 24.dp, top = 32.dp, bottom = 32.dp)
        ) {
            EditableText(
                value = data.fullName,
                onValueChange = { onFieldChange(data.copy(fullName = it)) },
                style = TextStyle(color = T01White, fontSize = 28.sp, fontWeight = FontWeight.Bold),
                placeholder = "YOUR NAME"
            )
            EditableText(
                value = data.professionalTitle,
                onValueChange = { onFieldChange(data.copy(professionalTitle = it)) },
                style = TextStyle(color = T01Accent, fontSize = 13.sp),
                placeholder = "PROFESSIONAL TITLE"
            )

            Spacer(Modifier.height(20.dp))
            SectionHeader("ABOUT ME")
            EditableText(
                value = data.aboutMe,
                onValueChange = { onFieldChange(data.copy(aboutMe = it)) },
                style = TextStyle(color = T01Gray, fontSize = 13.sp),
                multiline = true
            )

            Spacer(Modifier.height(24.dp))
            SectionHeader("EDUCATION")
            EducationBlock(data.edu1Degree, data.edu1School, data.edu1Years) {
                degree, school, years -> onFieldChange(data.copy(edu1Degree = degree, edu1School = school, edu1Years = years))
            }
            Spacer(Modifier.height(12.dp))
            EducationBlock(data.edu2Degree, data.edu2School, data.edu2Years) {
                degree, school, years -> onFieldChange(data.copy(edu2Degree = degree, edu2School = school, edu2Years = years))
            }

            Spacer(Modifier.height(24.dp))
            SectionHeader("EXPERIENCE")
            ExperienceBlock(data.exp1Position, data.exp1Company, data.exp1Dates, data.exp1Desc) {
                pos, comp, dates, desc -> onFieldChange(data.copy(exp1Position = pos, exp1Company = comp, exp1Dates = dates, exp1Desc = desc))
            }
            Spacer(Modifier.height(16.dp))
            ExperienceBlock(data.exp2Position, data.exp2Company, data.exp2Dates, "") {
                pos, comp, dates, _ -> onFieldChange(data.copy(exp2Position = pos, exp2Company = comp, exp2Dates = dates))
            }
        }
    }
}

// ===== Reusable pieces =====

@Composable
private fun SidebarHeader(text: String) {
    androidx.compose.material3.Text(
        text = text,
        color = T01Accent,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 10.dp)
    )
}

@Composable
private fun SectionHeader(text: String) {
    androidx.compose.material3.Text(
        text = text,
        color = T01Accent,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 6.dp)
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(T01Accent)
            .padding(bottom = 10.dp)
    )
}

@Composable
private fun HorizontalLine() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(T01Divider)
    )
}

@Composable
private fun Divider(color: Color, thickness: androidx.compose.ui.unit.Dp, height: Boolean) {
    Box(
        modifier = Modifier
            .width(thickness)
            .height(1.dp)
            .background(color)
    )
}

@Composable
private fun ContactRow(value: String, onValueChange: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(RoundedCornerShape(50))
                .background(T01Accent)
        )
        Spacer(Modifier.width(10.dp))
        EditableText(
            value = value,
            onValueChange = onValueChange,
            style = TextStyle(color = T01White, fontSize = 12.sp),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SkillRow(value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
        EditableText(
            value = value,
            onValueChange = onValueChange,
            style = TextStyle(color = T01White, fontSize = 12.sp),
            placeholder = "Skill"
        )
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(T01BarTrack)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight()
                    .background(T01Accent)
            )
        }
    }
}

@Composable
private fun EducationBlock(
    degree: String, school: String, years: String,
    onChange: (String, String, String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        EditableText(degree, { onChange(it, school, years) }, TextStyle(color = T01White, fontSize = 13.sp, fontWeight = FontWeight.Bold), placeholder = "DEGREE NAME / MAJOR")
        EditableText(school, { onChange(degree, it, years) }, TextStyle(color = T01Gray, fontSize = 12.sp), placeholder = "University Name")
        EditableText(years, { onChange(degree, school, it) }, TextStyle(color = T01Gray, fontSize = 12.sp), placeholder = "2016 - 2020")
    }
}

@Composable
private fun ExperienceBlock(
    position: String, company: String, dates: String, desc: String,
    onChange: (String, String, String, String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        EditableText(position, { onChange(it, company, dates, desc) }, TextStyle(color = T01White, fontSize = 13.sp, fontWeight = FontWeight.Bold), placeholder = "JOB POSITION HERE")
        EditableText(company, { onChange(position, it, dates, desc) }, TextStyle(color = T01Accent, fontSize = 11.sp), placeholder = "Company Name | Dates")
        EditableText(desc, { onChange(position, company, dates, it) }, TextStyle(color = T01Gray, fontSize = 12.sp), multiline = true)
    }
}

/** Auto-growing text field — no fixed width/position, wraps and expands naturally. */
@Composable
private fun EditableText(
    value: String,
    onValueChange: (String) -> Unit,
    style: TextStyle,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    multiline: Boolean = false,
    readOnly: Boolean = false
) {
    BasicTextField(
        value = value,
        onValueChange = if (readOnly) { {} } else onValueChange,
        textStyle = style,
        cursorBrush = SolidColor(T01White),
        maxLines = if (multiline) Int.MAX_VALUE else 1,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun ResumeTemplateFormScreen(
    templateName: String,
    onBack: () -> Unit = {}
) {
    var data by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(ResumeTemplateFields()) }
    Column(modifier = Modifier.fillMaxSize()) {
        androidx.compose.material3.IconButton(onClick = onBack) {
            androidx.compose.material3.Icon(
                androidx.compose.material.icons.Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
        ResumeTemplate01Screen(data = data, onFieldChange = { data = it })
    }
}

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
    val skill1: String = "", val skill2: String = "", val skill3: String = "", val skill4: String = "", val skill5: String = "",
    val exp1Position: String = "", val exp1Company: String = "", val exp1Dates: String = "", val exp1Desc: String = "",
    val exp2Position: String = "", val exp2Company: String = "", val exp2Dates: String = "",
    val refName: String = "", val refPositionCompany: String = "", val refContact: String = ""
)
