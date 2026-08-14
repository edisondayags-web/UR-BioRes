
package com.saltech.urdocs.ui.templates

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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

// HEXAGON HELPER
@Composable
fun HexagonOutline_02(modifier: Modifier, color: Color) {
    Canvas(modifier) {
        val r = size.minDimension/2; val cx = size.width/2; val cy = size.height/2
        val path = Path()
        for(i in 0..5) {
            val a = Math.toRadians((60*i-30).toDouble())
            val x = cx + r * cos(a).toFloat(); val y = cy + r * sin(a).toFloat()
            if(i==0) path.moveTo(x,y) else path.lineTo(x,y)
        }
        path.close(); drawPath(path, color, style = Stroke(width=1.5f))
    }
}

// DOTTED MATRIX
@Composable
fun DottedMatrix_02(modifier: Modifier, color: Color) {
    Canvas(modifier) {
        for(r in 0..7) {
            for(c in 0..13) {
                val ox = if(r%2==0) 0f else 3f
                drawCircle(color.copy(alpha=0.6f - r*0.06f), radius=1.8f, center=Offset(c*6f+ox, r*6f))
            }
        }
    }
}

@Composable
fun TimelineNode_02(accent: Color) {
    Box(Modifier.size(12.dp).clip(CircleShape).border(1.dp, accent, CircleShape).background(Color.Black), contentAlignment=Alignment.Center) {
        Box(Modifier.size(4.dp).clip(CircleShape).background(accent))
    }
}

@Composable
private fun EditableText_02(
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
fun ResumeTemplate02_PixelPerfect(
    userName: String = "",
    userTitle: String = "",
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
    refName: String = "", refPositionCompany: String = "", refPhone: String = "", refEmail: String = "",
    onFieldChange: (String, String) -> Unit = { _, _ -> }
) {
    val accent = Color(0xFF9EFF00)
    Box(Modifier.fillMaxSize().background(Color(0xFF050505)).padding(0.dp)) {
        Box(Modifier.fillMaxSize().padding(10.dp).border(1.dp, accent.copy(alpha=0.35f), RoundedCornerShape(topStart=14.dp, topEnd=0.dp, bottomStart=0.dp, bottomEnd=14.dp))) {

            Row(Modifier.align(Alignment.TopEnd).padding(top=4.dp, end=10.dp), horizontalArrangement=Arrangement.spacedBy(2.dp)) {
                Column(verticalArrangement=Arrangement.spacedBy(2.dp)) {
                    HexagonOutline_02(Modifier.size(22.dp), accent)
                    HexagonOutline_02(Modifier.size(18.dp), accent.copy(alpha=0.6f))
                    HexagonOutline_02(Modifier.size(12.dp), accent.copy(alpha=0.3f))
                }
                Column(verticalArrangement=Arrangement.spacedBy(4.dp), modifier=Modifier.padding(top=6.dp)) {
                    HexagonOutline_02(Modifier.size(28.dp), accent)
                    HexagonOutline_02(Modifier.size(16.dp), accent.copy(alpha=0.5f))
                    HexagonOutline_02(Modifier.size(10.dp), accent.copy(alpha=0.2f))
                }
                Column(verticalArrangement=Arrangement.spacedBy(3.dp)) {
                    HexagonOutline_02(Modifier.size(20.dp), accent.copy(alpha=0.7f))
                    HexagonOutline_02(Modifier.size(14.dp), accent.copy(alpha=0.4f))
                    HexagonOutline_02(Modifier.size(8.dp), accent.copy(alpha=0.2f))
                }
            }
            Box(Modifier.align(Alignment.BottomStart).padding(bottom=8.dp, start=8.dp)) {
                Row {
                    Box {
                        HexagonOutline_02(Modifier.size(32.dp), accent)
                        HexagonOutline_02(Modifier.size(26.dp).offset(x=8.dp, y=8.dp), accent.copy(alpha=0.7f))
                        HexagonOutline_02(Modifier.size(20.dp).offset(x=18.dp, y=-4.dp), accent.copy(alpha=0.5f))
                        HexagonOutline_02(Modifier.size(14.dp).offset(x=28.dp, y=10.dp), accent.copy(alpha=0.3f))
                    }
                    Spacer(Modifier.width(8.dp))
                    Box {
                        HexagonOutline_02(Modifier.size(24.dp), accent.copy(alpha=0.6f))
                        HexagonOutline_02(Modifier.size(18.dp).offset(x=10.dp, y=12.dp), accent.copy(alpha=0.4f))
                        HexagonOutline_02(Modifier.size(12.dp).offset(x=20.dp, y=2.dp), accent.copy(alpha=0.2f))
                    }
                }
            }
            DottedMatrix_02(Modifier.align(Alignment.TopStart).padding(top=4.dp, start=4.dp).size(24.dp, 28.dp), accent.copy(alpha=0.5f))
            DottedMatrix_02(Modifier.align(Alignment.BottomEnd).padding(end=8.dp, bottom=8.dp).size(60.dp, 32.dp), accent)

            Column(Modifier.fillMaxSize().padding(horizontal=16.dp, vertical=18.dp)) {

                // ===== HEADER: badge "02", Name + Title + underline, photo circle =====
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("02", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(12.dp))
                        EditableText_02(userName, "Ex: YOUR NAME", accent, 20.sp, FontWeight.Bold) { onFieldChange("fullName", it) }
                        Spacer(Modifier.height(4.dp))
                        EditableText_02(userTitle, "Ex: PROFESSIONAL TITLE", Color.White, 10.sp) { onFieldChange("professionalTitle", it) }
                        Spacer(Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.width(80.dp).height(1.5.dp).background(accent))
                            Box(Modifier.size(5.dp).clip(CircleShape).background(accent))
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                    Box(Modifier.size(64.dp).clip(CircleShape).border(1.5.dp, accent, CircleShape).background(Color.Transparent), contentAlignment=Alignment.Center) {
                        if(userName.isNotEmpty()) Text(userName.take(1), color=accent, fontSize=20.sp, fontWeight=FontWeight.Bold)
                    }
                }

                Spacer(Modifier.height(18.dp))

                Row(Modifier.fillMaxSize()) {
                    // ===== LEFT COLUMN: Contact / Skills / References =====
                    Column(Modifier.weight(0.78f).fillMaxHeight(), verticalArrangement=Arrangement.spacedBy(9.dp)) {

                        Column(verticalArrangement=Arrangement.spacedBy(8.dp)) {
                            SectionLabel_02("CONTACT", accent)
                            IconContactField_02(Icons.Filled.Phone, contactPhone, "Ex: 0912 345 6789", accent) { onFieldChange("phone", it) }
                            IconContactField_02(Icons.Filled.Email, contactEmail, "Ex: youremail@email.com", accent) { onFieldChange("email", it) }
                            IconContactField_02(Icons.Filled.Place, contactAddress, "Ex: City, Province", accent) { onFieldChange("location", it) }
                            LinkedInField_02(contactLinkedin, accent) { onFieldChange("linkedin", it) }
                            IconContactField_02(Icons.Filled.Language, contactWebsite, "Ex: www.yoursite.com", accent) { onFieldChange("website", it) }
                        }

                        Spacer(Modifier.height(2.dp))
                        Column(verticalArrangement=Arrangement.spacedBy(8.dp)) {
                            SectionLabel_02("SKILLS", accent)
                            val skillHints = listOf("Ex: Problem Solving","Ex: Communication","Ex: Teamwork","Ex: Leadership","Ex: Time Mgmt","Ex: Creativity")
                            skills.forEachIndexed { i, s ->
                                EditableText_02(s, skillHints.getOrElse(i){"Ex: Skill"}, Color.White, 7.sp) { onFieldChange("skill${i+1}", it) }
                            }
                        }

                        Spacer(Modifier.height(2.dp))
                        Column(verticalArrangement=Arrangement.spacedBy(6.dp)) {
                            SectionLabel_02("REFERENCES", accent)
                            EditableText_02(refName, "Ex: Reference Name", Color.White, 8.sp, FontWeight.Bold) { onFieldChange("refName", it) }
                            EditableText_02(refPositionCompany, "Ex: Position / Company", Color.White, 7.sp) { onFieldChange("refPositionCompany", it) }
                            IconContactField_02(Icons.Filled.Phone, refPhone, "Ex: 0912 111 2222", accent) { onFieldChange("refPhone", it) }
                            IconContactField_02(Icons.Filled.Email, refEmail, "Ex: reference@email.com", accent) { onFieldChange("refEmail", it) }
                        }
                    }

                    Box(Modifier.width(1.dp).fillMaxHeight().padding(horizontal=12.dp).background(accent.copy(alpha=0.3f)))

                    // ===== RIGHT COLUMN: About Me / Education / Experience =====
                    Column(Modifier.weight(1.22f).fillMaxHeight(), verticalArrangement=Arrangement.spacedBy(12.dp)) {

                        Column(verticalArrangement=Arrangement.spacedBy(5.dp)) {
                            SectionLabel_02("ABOUT ME", accent)
                            EditableText_02(aboutMe, "Ex: Dedicated and motivated professional with strong skills.", Color.White, 8.sp) { onFieldChange("aboutMe", it) }
                        }

                        Column(verticalArrangement=Arrangement.spacedBy(8.dp)) {
                            SectionLabel_02("EDUCATION", accent)
                            IconTimelineEntry_02(Icons.Filled.School, accent) {
                                EditableText_02(edu1Degree, "Ex: Bachelor of Science in IT", Color.White, 8.sp, FontWeight.Bold) { onFieldChange("edu1Degree", it) }
                                EditableText_02(edu1School, "Ex: University Name", Color.White, 7.sp) { onFieldChange("edu1School", it) }
                                EditableText_02(edu1Years, "Ex: 2018 - 2022", Color.White, 7.sp) { onFieldChange("edu1Years", it) }
                            }
                            IconTimelineEntry_02(Icons.Filled.School, accent) {
                                EditableText_02(edu2Degree, "Ex: Senior High School Diploma", Color.White, 8.sp, FontWeight.Bold) { onFieldChange("edu2Degree", it) }
                                EditableText_02(edu2School, "Ex: School Name", Color.White, 7.sp) { onFieldChange("edu2School", it) }
                                EditableText_02(edu2Years, "Ex: 2016 - 2018", Color.White, 7.sp) { onFieldChange("edu2Years", it) }
                            }
                        }

                        Column(verticalArrangement=Arrangement.spacedBy(8.dp)) {
                            SectionLabel_02("EXPERIENCE", accent)
                            IconTimelineEntry_02(Icons.Filled.Work, accent) {
                                EditableText_02(exp1Position, "Ex: IT Support Specialist", Color.White, 8.sp, FontWeight.Bold) { onFieldChange("exp1Position", it) }
                                EditableText_02(exp1Company, "Ex: Company | 2023 - Present", accent, 7.sp) { onFieldChange("exp1Company", it) }
                                EditableText_02(exp1Desc, "Ex: Provide technical support and troubleshooting.", Color.White.copy(alpha=0.8f), 7.sp) { onFieldChange("exp1Desc", it) }
                            }
                            IconTimelineEntry_02(Icons.Filled.Work, accent) {
                                EditableText_02(exp2Position, "Ex: Web Developer", Color.White, 8.sp, FontWeight.Bold) { onFieldChange("exp2Position", it) }
                                EditableText_02(exp2Company, "Ex: Company | 2021 - 2022", accent, 7.sp) { onFieldChange("exp2Company", it) }
                                EditableText_02(exp2Desc, "Ex: Developed and maintained websites.", Color.White.copy(alpha=0.8f), 7.sp) { onFieldChange("exp2Desc", it) }
                            }
                            IconTimelineEntry_02(Icons.Filled.Work, accent) {
                                EditableText_02(exp3Position, "Ex: On-the-Job Trainee", Color.White, 8.sp, FontWeight.Bold) { onFieldChange("exp3Position", it) }
                                EditableText_02(exp3Company, "Ex: Company | 2021", accent, 7.sp) { onFieldChange("exp3Company", it) }
                                EditableText_02(exp3Desc, "Ex: Assisted in system maintenance.", Color.White.copy(alpha=0.8f), 7.sp) { onFieldChange("exp3Desc", it) }
                            }
                        }

                        Spacer(Modifier.weight(1f))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween) {
                            Text("02", color=accent.copy(alpha=0.4f), fontSize=8.sp, fontWeight=FontWeight.Bold)
                            Text("Lime Green Wave", color=Color.White.copy(alpha=0.3f), fontSize=7.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionLabel_02(text: String, accent: Color) {
    Row(verticalAlignment=Alignment.CenterVertically) {
        Text(text, color=accent, fontSize=9.sp, fontWeight=FontWeight.Bold, letterSpacing=0.6.sp)
        Spacer(Modifier.width(6.dp))
        Box(Modifier.weight(1f).height(1.dp).background(accent.copy(alpha=0.6f)))
        Box(Modifier.size(5.dp).clip(CircleShape).background(accent))
    }
}

@Composable
private fun IconContactField_02(icon: ImageVector, value: String, placeholder: String, accent: Color, onValueChange: (String) -> Unit) {
    Row(verticalAlignment=Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.size(17.dp).clip(CircleShape).border(0.8.dp, accent.copy(alpha=0.7f), CircleShape), contentAlignment=Alignment.Center) {
            Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(9.dp))
        }
        Spacer(Modifier.width(7.dp))
        EditableText_02(value, placeholder, Color.White, 7.sp, modifier = Modifier.weight(1f), onValueChange = onValueChange)
    }
}

@Composable
private fun LinkedInField_02(value: String, accent: Color, onValueChange: (String) -> Unit) {
    Row(verticalAlignment=Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier.size(17.dp).clip(RoundedCornerShape(4.dp)).background(accent),
            contentAlignment = Alignment.Center
        ) {
            Text("in", color = Color.Black, fontSize = 8.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(7.dp))
        EditableText_02(value, "Ex: linkedin.com/in/yourname", Color.White, 7.sp, modifier = Modifier.weight(1f), onValueChange = onValueChange)
    }
}

@Composable
private fun IconTimelineEntry_02(icon: ImageVector, accent: Color, content: @Composable ColumnScope.() -> Unit) {
    Row(Modifier.fillMaxWidth()) {
        Column(horizontalAlignment=Alignment.CenterHorizontally, modifier=Modifier.padding(top=2.dp)) {
            Box(Modifier.size(18.dp).clip(CircleShape).border(1.dp, accent, CircleShape).background(Color.Black), contentAlignment=Alignment.Center) {
                Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(10.dp))
            }
            Box(Modifier.width(1.dp).weight(1f).defaultMinSize(minHeight = 28.dp).background(accent.copy(alpha=0.5f)))
        }
        Spacer(Modifier.width(8.dp))
        Column(verticalArrangement=Arrangement.spacedBy(2.dp), modifier=Modifier.weight(1f).padding(bottom = 9.dp), content = content)
    }
}

