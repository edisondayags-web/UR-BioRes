package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val T01Bg = Color(0xFF0D0D0D)
private val T01Accent = Color(0xFFE8121F)
private val T01White = Color(0xFFF5F5F5)
private val T01Gray = Color(0xFF6E6E6E)
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
                    .size(64.dp)
                    .padding(top = 12.dp)
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
                Text(
                    text = "01",
                    color = T01White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 6.dp, top = 4.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            Text("UR", color = T01Accent, fontSize = 40.sp, fontWeight = FontWeight.Bold)
            Text("Resume", color = T01White, fontSize = 16.sp)

            Spacer(Modifier.height(16.dp))
            HorizontalLine()
            Spacer(Modifier.height(20.dp))

            SidebarHeader("CONTACT")
            ContactRow(Icons.Filled.Phone, data.phone, "+63 XXX XXX XXXX") { onFieldChange(data.copy(phone = it)) }
            ContactRow(Icons.Filled.Email, data.email, "youremail@email.com") { onFieldChange(data.copy(email = it)) }
            ContactRow(Icons.Filled.Place, data.location, "City, State, Country") { onFieldChange(data.copy(location = it)) }
            LinkedInRow(data.linkedin) { onFieldChange(data.copy(linkedin = it)) }
            ContactRow(Icons.Filled.Language, data.website, "yourwebsite.com") { onFieldChange(data.copy(website = it)) }

            Spacer(Modifier.height(16.dp))
            HorizontalLine()
            Spacer(Modifier.height(16.dp))

            SidebarHeader("SKILLS")
            SkillRow(data.skill1, "Problem Solving") { onFieldChange(data.copy(skill1 = it)) }
            SkillRow(data.skill2, "Communication") { onFieldChange(data.copy(skill2 = it)) }
            SkillRow(data.skill3, "Teamwork") { onFieldChange(data.copy(skill3 = it)) }
            SkillRow(data.skill4, "Leadership") { onFieldChange(data.copy(skill4 = it)) }
            SkillRow(data.skill5, "Time Management") { onFieldChange(data.copy(skill5 = it)) }

            Spacer(Modifier.height(16.dp))
            HorizontalLine()
            Spacer(Modifier.height(16.dp))

            SidebarHeader("REFERENCES")
            PlaceholderText(data.refName, "Available upon request", T01Gray, 12.sp) {
                onFieldChange(data.copy(refName = it))
            }
        }

        // ===== RIGHT CONTENT =====
        Column(
            modifier = Modifier
                .weight(0.66f)
                .padding(start = 16.dp, end = 24.dp, top = 32.dp, bottom = 32.dp)
        ) {
            PlaceholderText(data.fullName, "YOUR NAME", T01White, 28.sp, FontWeight.Bold) {
                onFieldChange(data.copy(fullName = it))
            }
            PlaceholderText(data.professionalTitle, "PROFESSIONAL TITLE", T01Accent, 13.sp) {
                onFieldChange(data.copy(professionalTitle = it))
            }

            Spacer(Modifier.height(20.dp))
            SectionHeader("ABOUT ME")
            PlaceholderText(data.aboutMe, "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", T01Gray, 13.sp, multiline = true) {
                onFieldChange(data.copy(aboutMe = it))
            }

            Spacer(Modifier.height(24.dp))
            SectionHeader("EDUCATION")
            TimelineEntry {
                PlaceholderText(data.edu1Degree, "DEGREE NAME / MAJOR", T01White, 13.sp, FontWeight.Bold) {
                    onFieldChange(data.copy(edu1Degree = it))
                }
                PlaceholderText(data.edu1School, "University Name", T01Gray, 12.sp) {
                    onFieldChange(data.copy(edu1School = it))
                }
                PlaceholderText(data.edu1Years, "2016 - 2020", T01Gray, 12.sp) {
                    onFieldChange(data.copy(edu1Years = it))
                }
            }
            Spacer(Modifier.height(12.dp))
            TimelineEntry {
                PlaceholderText(data.edu2Degree, "DEGREE NAME / MAJOR", T01White, 13.sp, FontWeight.Bold) {
                    onFieldChange(data.copy(edu2Degree = it))
                }
                PlaceholderText(data.edu2School, "University Name", T01Gray, 12.sp) {
                    onFieldChange(data.copy(edu2School = it))
                }
                PlaceholderText(data.edu2Years, "2018 - 2020", T01Gray, 12.sp) {
                    onFieldChange(data.copy(edu2Years = it))
                }
            }

            Spacer(Modifier.height(24.dp))
            SectionHeader("EXPERIENCE")
            TimelineEntry {
                PlaceholderText(data.exp1Position, "JOB POSITION HERE", T01White, 13.sp, FontWeight.Bold) {
                    onFieldChange(data.copy(exp1Position = it))
                }
                PlaceholderText(data.exp1Company, "Company Name | 2020 - Present", T01Accent, 11.sp) {
                    onFieldChange(data.copy(exp1Company = it))
                }
                PlaceholderText(data.exp1Desc, "Lorem ipsum dolor sit amet, sed do eiusmod tempor.", T01Gray, 12.sp, multiline = true) {
                    onFieldChange(data.copy(exp1Desc = it))
                }
            }
            Spacer(Modifier.height(16.dp))
            TimelineEntry {
                PlaceholderText(data.exp2Position, "JOB POSITION HERE", T01White, 13.sp, FontWeight.Bold) {
                    onFieldChange(data.copy(exp2Position = it))
                }
                PlaceholderText(data.exp2Company, "Company Name | 2018 - 2020", T01Accent, 11.sp) {
                    onFieldChange(data.copy(exp2Company = it))
                }
            }
        }
    }
}

// ===== Reusable pieces =====

@Composable
private fun SidebarHeader(text: String) {
    Text(text, color = T01Accent, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 10.dp))
}

@Composable
private fun SectionHeader(text: String) {
    Text(text, color = T01Accent, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 6.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(T01Accent)
    )
    Spacer(Modifier.height(10.dp))
}

@Composable
private fun HorizontalLine() {
    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(T01Divider))
}

@Composable
private fun ContactRow(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String, placeholder: String, onValueChange: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = T01Accent, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(10.dp))
        PlaceholderText(value, placeholder, T01White, 12.sp, modifier = Modifier.weight(1f), onValueChange = onValueChange)
    }
}

@Composable
private fun LinkedInRow(value: String, onValueChange: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(T01Accent),
            contentAlignment = Alignment.Center
        ) {
            Text("in", color = T01White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(10.dp))
        PlaceholderText(value, "linkedin.com/in/yourname", T01White, 12.sp, modifier = Modifier.weight(1f), onValueChange = onValueChange)
    }
}

@Composable
private fun SkillRow(value: String, defaultLabel: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
        PlaceholderText(value, defaultLabel, T01White, 12.sp, onValueChange = onValueChange)
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(4.dp)).background(T01BarTrack)
        ) {
            Box(modifier = Modifier.fillMaxWidth(0.8f).fillMaxHeight().background(T01Accent))
        }
    }
}

/** Timeline row with connector dot + line on the left, matching the reference design's education/experience list. */
@Composable
private fun TimelineEntry(content: @Composable ColumnScope.() -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.width(16.dp).fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Canvas(modifier = Modifier.size(10.dp)) {
                drawCircle(color = T01Accent, radius = size.minDimension / 2, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f))
            }
            Box(modifier = Modifier.width(1.dp).weight(1f).background(T01Divider))
        }
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f), content = content)
    }
}

/** Text field that shows a dim placeholder when empty, and real typed text otherwise. */
@Composable
private fun PlaceholderText(
    value: String,
    placeholder: String,
    color: Color,
    fontSize: androidx.compose.ui.unit.TextUnit,
    fontWeight: FontWeight? = null,
    modifier: Modifier = Modifier,
    multiline: Boolean = false,
    onValueChange: (String) -> Unit
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
fun ResumeTemplateFormScreen(
    templateName: String,
    onBack: () -> Unit = {}
) {
    var data by remember { mutableStateOf(ResumeTemplateFields()) }
    Column(modifier = Modifier.fillMaxSize().background(T01Bg)) {
        IconButton(onClick = onBack) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
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
