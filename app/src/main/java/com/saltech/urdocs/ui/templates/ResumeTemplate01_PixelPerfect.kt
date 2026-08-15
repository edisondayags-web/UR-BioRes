package com.saltech.urdocs.ui.templates

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

// HEXAGON HELPER
@Composable
fun HexagonOutline_01(modifier: Modifier, color: Color) {
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
fun DottedMatrix_01(modifier: Modifier, color: Color) {
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
fun TimelineNode_01(accent: Color) {
    Box(Modifier.size(12.dp).clip(CircleShape).border(1.dp, accent, CircleShape).background(Color.Black), contentAlignment=Alignment.Center) {
        Box(Modifier.size(4.dp).clip(CircleShape).background(accent))
    }
}

@Composable
private fun EditableText_01(
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
            Text(placeholder, color = color.copy(alpha = 0.45f), fontSize = fontSize, fontWeight = fontWeight)
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
fun ResumeTemplate01_PixelPerfect(
    userName: String = "",
    avatarUri: String = "",
    onFieldChange: (String, String) -> Unit = { _, _ -> },
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
    val accent = Color(0xFFD4AF37)
    Box(Modifier.fillMaxSize().background(Color(0xFF050505)).padding(0.dp).verticalScroll(rememberScrollState())) {
        Box(Modifier.fillMaxWidth().defaultMinSize(minHeight = 700.dp).padding(10.dp).border(1.dp, accent.copy(alpha=0.35f), RoundedCornerShape(topStart=14.dp, topEnd=0.dp, bottomStart=0.dp, bottomEnd=14.dp))) {

            DottedMatrix_01(Modifier.align(Alignment.TopStart).padding(top=4.dp, start=4.dp).size(24.dp, 28.dp), accent.copy(alpha=0.5f))
            DottedMatrix_01(Modifier.align(Alignment.BottomEnd).padding(end=8.dp, bottom=8.dp).size(60.dp, 32.dp), accent)

            Column(Modifier.fillMaxWidth().padding(horizontal=16.dp, vertical=18.dp)) {

                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("01", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(14.dp))
                        EditableText_01(userName, "EX: YOUR NAME", accent, 26.sp, FontWeight.Bold) { onFieldChange("fullName", it) }
                        Spacer(Modifier.height(6.dp))
                        EditableText_01(userTitle, "Ex: PROFESSIONAL TITLE", Color.White, 12.sp) { onFieldChange("professionalTitle", it) }
                        Spacer(Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.width(90.dp).height(1.5.dp).background(accent))
                            Box(Modifier.size(5.dp).clip(CircleShape).background(accent))
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    SharedAvatarPicker(avatarUri, 70.dp, accent, userName) { onFieldChange("avatarUri", it) }
                }

                Spacer(Modifier.height(20.dp))

                Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                    Column(Modifier.weight(0.78f).fillMaxHeight(), verticalArrangement=Arrangement.spacedBy(10.dp)) {

                        Column(verticalArrangement=Arrangement.spacedBy(9.dp)) {
                            SectionLabel_01("EX: CONTACT", accent)
                            ContactField_01(contactPhone, "Ex: +123 456 7890", accent) { onFieldChange("phone", it) }
                            ContactField_01(contactEmail, "Ex: youremail@email.com", accent) { onFieldChange("email", it) }
                            ContactField_01(contactAddress, "Ex: City, State, Country", accent) { onFieldChange("location", it) }
                            ContactField_01(contactLinkedin, "Ex: linkedin.com/in/yourname", accent) { onFieldChange("linkedin", it) }
                            ContactField_01(contactWebsite, "Ex: www.yourwebsite.com", accent) { onFieldChange("website", it) }
                        }

                        Spacer(Modifier.height(4.dp))
                        Column(verticalArrangement=Arrangement.spacedBy(9.dp)) {
                            SectionLabel_01("EX: SKILLS", accent)
                            val skillHints = listOf("Ex: Problem Solving","Ex: Communication","Ex: Teamwork","Ex: Leadership","Ex: Time Management","Ex: Creativity")
                            skills.forEachIndexed { i, s ->
                                EditableText_01(s, skillHints.getOrElse(i){"Ex: Skill"}, Color.White, 8.sp) { onFieldChange("skill${i+1}", it) }
                            }
                        }

                        Spacer(Modifier.height(4.dp))
                        Column(verticalArrangement=Arrangement.spacedBy(7.dp)) {
                            SectionLabel_01("EX: REFERENCES", accent)
                            EditableText_01(refName, "Ex: Reference Name", Color.White, 9.sp, FontWeight.Bold) { onFieldChange("refName", it) }
                            EditableText_01(refPositionCompany, "Ex: Job Position / Company", Color.White, 8.sp) { onFieldChange("refPositionCompany", it) }
                            ContactField_01(refPhone, "Ex: +123 456 7890", accent) { onFieldChange("refPhone", it) }
                            ContactField_01(refEmail, "Ex: reference@email.com", accent) { onFieldChange("refEmail", it) }
                        }
                    }

                    Box(Modifier.width(1.dp).fillMaxHeight().padding(horizontal=12.dp).background(accent.copy(alpha=0.3f)))

                    Column(Modifier.weight(1.22f).fillMaxHeight(), verticalArrangement=Arrangement.spacedBy(14.dp)) {

                        Column(verticalArrangement=Arrangement.spacedBy(6.dp)) {
                            SectionLabel_01("EX: ABOUT ME", accent)
                            EditableText_01(aboutMe, "Ex: Lorem ipsum dolor sit amet, consectetur adipiscing elit.", Color.White, 9.sp) { onFieldChange("aboutMe", it) }
                        }

                        Column(verticalArrangement=Arrangement.spacedBy(8.dp)) {
                            SectionLabel_01("EX: EDUCATION", accent)
                            TimelineEntry_01(accent) {
                                EditableText_01(edu1Degree, "Ex: DEGREE NAME / MAJOR", Color.White, 9.sp, FontWeight.Bold) { onFieldChange("edu1Degree", it) }
                                EditableText_01(edu1School, "Ex: University Name", Color.White, 8.sp) { onFieldChange("edu1School", it) }
                                EditableText_01(edu1Years, "Ex: 2016 - 2020", Color.White, 8.sp) { onFieldChange("edu1Years", it) }
                            }
                            TimelineEntry_01(accent) {
                                EditableText_01(edu2Degree, "Ex: DEGREE NAME / MAJOR", Color.White, 9.sp, FontWeight.Bold) { onFieldChange("edu2Degree", it) }
                                EditableText_01(edu2School, "Ex: University Name", Color.White, 8.sp) { onFieldChange("edu2School", it) }
                                EditableText_01(edu2Years, "Ex: 2012 - 2016", Color.White, 8.sp) { onFieldChange("edu2Years", it) }
                            }
                        }

                        Column(verticalArrangement=Arrangement.spacedBy(8.dp)) {
                            SectionLabel_01("EX: EXPERIENCE", accent)
                            TimelineEntry_01(accent) {
                                EditableText_01(exp1Position, "Ex: JOB POSITION HERE", Color.White, 9.sp, FontWeight.Bold) { onFieldChange("exp1Position", it) }
                                EditableText_01(exp1Company, "Ex: Company Name | 2020 - Present", accent, 8.sp) { onFieldChange("exp1Company", it) }
                                EditableText_01(exp1Desc, "Ex: Lorem ipsum dolor sit amet, consectetur adipiscing elit.", Color.White.copy(alpha=0.8f), 8.sp) { onFieldChange("exp1Desc", it) }
                            }
                            TimelineEntry_01(accent) {
                                EditableText_01(exp2Position, "Ex: JOB POSITION HERE", Color.White, 9.sp, FontWeight.Bold) { onFieldChange("exp2Position", it) }
                                EditableText_01(exp2Company, "Ex: Company Name | 2018 - 2020", accent, 8.sp) { onFieldChange("exp2Company", it) }
                                EditableText_01(exp2Desc, "Ex: Lorem ipsum dolor sit amet, consectetur adipiscing elit.", Color.White.copy(alpha=0.8f), 8.sp) { onFieldChange("exp2Desc", it) }
                            }
                            TimelineEntry_01(accent) {
                                EditableText_01(exp3Position, "Ex: JOB POSITION HERE", Color.White, 9.sp, FontWeight.Bold) { onFieldChange("exp3Position", it) }
                                EditableText_01(exp3Company, "Ex: Company Name | 2016 - 2018", accent, 8.sp) { onFieldChange("exp3Company", it) }
                                EditableText_01(exp3Desc, "Ex: Lorem ipsum dolor sit amet, consectetur adipiscing elit.", Color.White.copy(alpha=0.8f), 8.sp) { onFieldChange("exp3Desc", it) }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionLabel_01(text: String, accent: Color) {
    Row(verticalAlignment=Alignment.CenterVertically) {
        Text(text, color=accent, fontSize=10.sp, fontWeight=FontWeight.Bold, letterSpacing=0.8.sp)
        Spacer(Modifier.width(6.dp))
        Box(Modifier.weight(1f).height(1.dp).background(accent.copy(alpha=0.6f)))
        Box(Modifier.size(5.dp).clip(CircleShape).background(accent))
    }
}

@Composable
private fun ContactField_01(value: String, placeholder: String, accent: Color, onValueChange: (String) -> Unit) {
    Row(verticalAlignment=Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.size(16.dp).clip(CircleShape).border(0.8.dp, accent.copy(alpha=0.7f), CircleShape), contentAlignment=Alignment.Center) {
            Box(Modifier.size(5.dp).clip(CircleShape).background(accent.copy(alpha=0.8f)))
        }
        Spacer(Modifier.width(8.dp))
        EditableText_01(value, placeholder, Color.White, 8.sp, modifier = Modifier.weight(1f), onValueChange = onValueChange)
    }
}

@Composable
private fun TimelineEntry_01(accent: Color, content: @Composable ColumnScope.() -> Unit) {
    Row(Modifier.fillMaxWidth()) {
        Column(horizontalAlignment=Alignment.CenterHorizontally, modifier=Modifier.padding(top=2.dp)) {
            TimelineNode_01(accent)
            Box(Modifier.width(1.dp).weight(1f).defaultMinSize(minHeight = 30.dp).background(accent.copy(alpha=0.5f)))
        }
        Spacer(Modifier.width(8.dp))
        Column(verticalArrangement=Arrangement.spacedBy(2.dp), modifier=Modifier.weight(1f).padding(bottom = 10.dp), content = content)
    }
}
