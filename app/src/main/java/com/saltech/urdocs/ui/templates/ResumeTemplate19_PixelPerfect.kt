package com.saltech.urdocs.ui.templates

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import kotlin.math.cos
import kotlin.math.sin

@Composable
private fun HexagonOutline_02(modifier: Modifier, color: Color) {
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

@Composable
private fun DottedMatrix_02(modifier: Modifier, color: Color) {
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
fun ResumeTemplate19_PixelPerfect(
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
    onFieldChange: (String, String) -> Unit = { _, _ -> }
) {
    val accent = Color(0xFFC0392B)
    val nameFontSize = autoShrinkNameFontSize(userName)

    Box(Modifier.fillMaxSize().background(Color(0xFF050505)).padding(0.dp).verticalScroll(rememberScrollState())) {
        Box(Modifier.fillMaxWidth().defaultMinSize(minHeight = 700.dp).padding(10.dp).border(1.dp, accent.copy(alpha=0.35f), RoundedCornerShape(topStart=14.dp, topEnd=0.dp, bottomStart=0.dp, bottomEnd=14.dp))) {

            DottedMatrix_02(Modifier.align(Alignment.TopStart).padding(top=4.dp, start=4.dp).size(24.dp, 28.dp), accent.copy(alpha=0.5f))
            Row(Modifier.align(Alignment.TopEnd).padding(top=4.dp, end=10.dp), horizontalArrangement=Arrangement.spacedBy(2.dp)) {
                HexagonOutline_02(Modifier.size(20.dp), accent)
                HexagonOutline_02(Modifier.size(16.dp), accent.copy(alpha=0.5f))
                HexagonOutline_02(Modifier.size(12.dp), accent.copy(alpha=0.3f))
            }
            Box(Modifier.align(Alignment.BottomStart).padding(bottom=8.dp, start=8.dp)) {
                HexagonOutline_02(Modifier.size(28.dp), accent.copy(alpha=0.5f))
            }
            DottedMatrix_02(Modifier.align(Alignment.BottomEnd).padding(end=8.dp, bottom=8.dp).size(60.dp, 32.dp), accent)

            Column(Modifier.fillMaxWidth().padding(horizontal=16.dp, vertical=16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {

                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    SharedAvatarPicker(avatarUri, 90.dp, accent, userName) { onFieldChange("avatarUri", it) }
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        EditableText_02(userName, "Ex: Your Name", accent, nameFontSize, FontWeight.Bold) { onFieldChange("fullName", it) }
                        Spacer(Modifier.height(2.dp))
                        EditableText_02(userTitle, "Ex: Professional Title", Color.White.copy(alpha=0.7f), 10.sp) { onFieldChange("professionalTitle", it) }
                    }
                }

                Column(verticalArrangement=Arrangement.spacedBy(5.dp)) {
                    SectionLabel_02("ABOUT ME", accent)
                    EditableText_02(aboutMe, "Ex: Dedicated and motivated professional with strong communication skills.", Color.White, 8.sp) { onFieldChange("aboutMe", it) }
                }

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Column(Modifier.weight(1f), verticalArrangement=Arrangement.spacedBy(6.dp)) {
                        SectionLabel_02("CONTACT", accent)
                        IconContactField_02(Icons.Filled.Phone, contactPhone, "Ex: 0912 345 6789", accent) { onFieldChange("phone", it) }
                        IconContactField_02(Icons.Filled.Email, contactEmail, "Ex: youremail@email.com", accent) { onFieldChange("email", it) }
                        IconContactField_02(Icons.Filled.Place, contactAddress, "Ex: City, Province", accent) { onFieldChange("location", it) }
                        LinkedInField_02(contactLinkedin, accent) { onFieldChange("linkedin", it) }
                        IconContactField_02(Icons.Filled.Language, contactWebsite, "Ex: www.yoursite.com", accent) { onFieldChange("website", it) }
                    }
                    Column(Modifier.weight(1f), verticalArrangement=Arrangement.spacedBy(6.dp)) {
                        SectionLabel_02("EDUCATION", accent)
                        IconTimelineEntry_02(Icons.Filled.School, accent) {
                            EditableText_02(edu1Degree, "Ex: BS Information Technology", Color.White, 7.5.sp, FontWeight.Bold) { onFieldChange("edu1Degree", it) }
                            EditableText_02(edu1School, "Ex: University Name", Color.White, 7.sp) { onFieldChange("edu1School", it) }
                            EditableText_02(edu1Years, "Ex: 2018 - 2022", Color.White, 7.sp) { onFieldChange("edu1Years", it) }
                        }
                        IconTimelineEntry_02(Icons.Filled.School, accent) {
                            EditableText_02(edu2Degree, "Ex: Senior High School", Color.White, 7.5.sp, FontWeight.Bold) { onFieldChange("edu2Degree", it) }
                            EditableText_02(edu2School, "Ex: School Name", Color.White, 7.sp) { onFieldChange("edu2School", it) }
                            EditableText_02(edu2Years, "Ex: 2016 - 2018", Color.White, 7.sp) { onFieldChange("edu2Years", it) }
                        }
                    }
                }

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Column(Modifier.weight(1f), verticalArrangement=Arrangement.spacedBy(6.dp)) {
                        SectionLabel_02("SKILLS", accent)
                        val skillHints = listOf("Ex: Problem Solving","Ex: Communication","Ex: Teamwork","Ex: Leadership","Ex: Time Mgmt","Ex: Creativity")
                        skills.forEachIndexed { i, s ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(Modifier.size(5.dp).clip(CircleShape).background(accent))
                                Spacer(Modifier.width(6.dp))
                                EditableText_02(s, skillHints.getOrElse(i){"Ex: Skill"}, Color.White, 7.sp, modifier = Modifier.weight(1f)) { onFieldChange("skill${i+1}", it) }
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                        SectionLabel_02("EXPERIENCE", accent)
                        IconTimelineEntry_02(Icons.Filled.Work, accent) {
                            EditableText_02(exp4Position, "Ex: Customer Service Representative", Color.White, 7.5.sp, FontWeight.Bold) { onFieldChange("exp4Position", it) }
                            EditableText_02(exp4Company, "Ex: TeleConnect Solutions", Color.White, 7.sp) { onFieldChange("exp4Company", it) }
                            EditableText_02(exp4Dates, "Ex: Jan 2020 - Dec 2020", accent, 7.sp) { onFieldChange("exp4Dates", it) }
                            EditableText_02(exp4Desc, "Ex: Handled customer inquiries and provided excellent service.", Color.White.copy(alpha=0.8f), 7.sp) { onFieldChange("exp4Desc", it) }
                        }
                        IconTimelineEntry_02(Icons.Filled.Work, accent) {
                            EditableText_02(exp5Position, "Ex: Sales Assistant", Color.White, 7.5.sp, FontWeight.Bold) { onFieldChange("exp5Position", it) }
                            EditableText_02(exp5Company, "Ex: Retail Mart", Color.White, 7.sp) { onFieldChange("exp5Company", it) }
                            EditableText_02(exp5Dates, "Ex: May 2019 - Dec 2019", accent, 7.sp) { onFieldChange("exp5Dates", it) }
                            EditableText_02(exp5Desc, "Ex: Assisted customers and managed inventory.", Color.White.copy(alpha=0.8f), 7.sp) { onFieldChange("exp5Desc", it) }
                        }
                    }
                    Column(Modifier.weight(1f), verticalArrangement=Arrangement.spacedBy(6.dp)) {
                        SectionLabel_02("EXPERIENCE", accent)
                        IconTimelineEntry_02(Icons.Filled.Work, accent) {
                            EditableText_02(exp1Position, "Ex: IT Support Specialist", Color.White, 7.5.sp, FontWeight.Bold) { onFieldChange("exp1Position", it) }
                            EditableText_02(exp1Company, "Ex: ABC Company", Color.White, 7.sp) { onFieldChange("exp1Company", it) }
                            EditableText_02(exp1Dates, "Ex: Jan 2023 - Present", accent, 7.sp) { onFieldChange("exp1Dates", it) }
                            EditableText_02(exp1Desc, "Ex: Provide technical support, troubleshoot issues, and ensure smooth IT operations.", Color.White.copy(alpha=0.8f), 7.sp) { onFieldChange("exp1Desc", it) }
                        }
                        IconTimelineEntry_02(Icons.Filled.Work, accent) {
                            EditableText_02(exp2Position, "Ex: Web Developer", Color.White, 7.5.sp, FontWeight.Bold) { onFieldChange("exp2Position", it) }
                            EditableText_02(exp2Company, "Ex: XYZ Solutions", Color.White, 7.sp) { onFieldChange("exp2Company", it) }
                            EditableText_02(exp2Dates, "Ex: Jun 2021 - Dec 2022", accent, 7.sp) { onFieldChange("exp2Dates", it) }
                            EditableText_02(exp2Desc, "Ex: Developed and maintained websites and web applications for clients.", Color.White.copy(alpha=0.8f), 7.sp) { onFieldChange("exp2Desc", it) }
                        }
                        IconTimelineEntry_02(Icons.Filled.Work, accent) {
                            EditableText_02(exp3Position, "Ex: On-the-Job Trainee", Color.White, 7.5.sp, FontWeight.Bold) { onFieldChange("exp3Position", it) }
                            EditableText_02(exp3Company, "Ex: TechSoft Inc.", Color.White, 7.sp) { onFieldChange("exp3Company", it) }
                            EditableText_02(exp3Dates, "Ex: Jan 2021 - Apr 2021", accent, 7.sp) { onFieldChange("exp3Dates", it) }
                            EditableText_02(exp3Desc, "Ex: Assisted in system maintenance and documentation.", Color.White.copy(alpha=0.8f), 7.sp) { onFieldChange("exp3Desc", it) }
                        }
                    }
                }

                Column(verticalArrangement=Arrangement.spacedBy(6.dp)) {
                    SectionLabel_02("REFERENCES", accent)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        ReferenceBlock_02(refName, refPositionCompany, refPhone, refEmail, refAvatarUri, accent, Modifier.weight(1f),
                            { onFieldChange("refName", it) }, { onFieldChange("refPositionCompany", it) },
                            { onFieldChange("refPhone", it) }, { onFieldChange("refEmail", it) }, { onFieldChange("refAvatarUri", it) })
                        ReferenceBlock_02(ref2Name, ref2PositionCompany, ref2Phone, ref2Email, ref2AvatarUri, accent, Modifier.weight(1f),
                            { onFieldChange("ref2Name", it) }, { onFieldChange("ref2PositionCompany", it) },
                            { onFieldChange("ref2Phone", it) }, { onFieldChange("ref2Email", it) }, { onFieldChange("ref2AvatarUri", it) })
                    }
                }

                Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween) {
                    Text("02", color=accent.copy(alpha=0.4f), fontSize=8.sp, fontWeight=FontWeight.Bold)
                    Text("Lime Green Wave", color=Color.White.copy(alpha=0.3f), fontSize=7.sp)
                }
            }
        }
    }
}

@Composable
private fun SectionLabel_02(text: String, accent: Color) {
    Row(verticalAlignment=Alignment.CenterVertically) {
        Text(text, color=accent, fontSize=8.sp, fontWeight=FontWeight.Bold, letterSpacing=0.5.sp)
        Spacer(Modifier.width(6.dp))
        Box(Modifier.weight(1f).height(1.dp).background(accent.copy(alpha=0.6f)))
        Box(Modifier.size(5.dp).clip(CircleShape).background(accent))
    }
}

@Composable
private fun IconContactField_02(icon: ImageVector, value: String, placeholder: String, accent: Color, onValueChange: (String) -> Unit) {
    Row(verticalAlignment=Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.size(16.dp).clip(CircleShape).border(0.8.dp, accent.copy(alpha=0.7f), CircleShape), contentAlignment=Alignment.Center) {
            Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(9.dp))
        }
        Spacer(Modifier.width(6.dp))
        EditableText_02(value, placeholder, Color.White, 7.sp, modifier = Modifier.weight(1f), onValueChange = onValueChange)
    }
}

@Composable
private fun LinkedInField_02(value: String, accent: Color, onValueChange: (String) -> Unit) {
    Row(verticalAlignment=Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier.size(16.dp).clip(RoundedCornerShape(4.dp)).background(accent),
            contentAlignment = Alignment.Center
        ) {
            Text("in", color = Color.Black, fontSize = 8.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(6.dp))
        EditableText_02(value, "Ex: linkedin.com/in/yourname", Color.White, 7.sp, modifier = Modifier.weight(1f), onValueChange = onValueChange)
    }
}

@Composable
private fun IconTimelineEntry_02(icon: ImageVector, accent: Color, content: @Composable ColumnScope.() -> Unit) {
    Row(Modifier.fillMaxWidth()) {
        Box(Modifier.size(16.dp).clip(CircleShape).border(1.dp, accent, CircleShape).background(Color.Black), contentAlignment=Alignment.Center) {
            Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(9.dp))
        }
        Spacer(Modifier.width(7.dp))
        Column(verticalArrangement=Arrangement.spacedBy(1.dp), modifier=Modifier.weight(1f), content = content)
    }
}

@Composable
private fun ReferenceBlock_02(
    name: String, positionCompany: String, phone: String, email: String, avatarUri: String, accent: Color, modifier: Modifier,
    onName: (String) -> Unit, onPosition: (String) -> Unit, onPhone: (String) -> Unit, onEmail: (String) -> Unit, onAvatarUri: (String) -> Unit
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        SharedAvatarPicker(avatarUri, 36.dp, accent, name, onAvatarUri)
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            EditableText_02(name, "Ex: Reference Name", Color.White, 8.sp, FontWeight.Bold) { onName(it) }
            EditableText_02(positionCompany, "Ex: Position / Company", Color.White.copy(alpha=0.7f), 7.sp) { onPosition(it) }
            IconContactField_02(Icons.Filled.Phone, phone, "Ex: 0912 111 2222", accent) { onPhone(it) }
            IconContactField_02(Icons.Filled.Email, email, "Ex: reference@email.com", accent) { onEmail(it) }
        }
    }
}
