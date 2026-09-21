package com.saltech.urdocs.ui.templates

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.graphics.BitmapFactory
import android.net.Uri

private val Navy08 = Color(0xFF16305E)
private val Gold08 = Color(0xFFC9A227)
private val Ink08 = Color(0xFF1C1C1C)
private val Gray08 = Color(0xFF6B6B6B)

/**
 * Template 08 — "Corporate Diagonal": navy/gold diagonal ribbon header,
 * circular photo top-right, two-column body.
 */
@Composable
fun ResumeTemplate08_PixelPerfect(
    userName: String,
    userTitle: String,
    avatarUri: String,
    contactPhone: String,
    contactEmail: String,
    contactAddress: String,
    contactWebsite: String,
    contactLinkedin: String,
    aboutMe: String,
    edu1Degree: String, edu1School: String, edu1Years: String,
    edu2Degree: String, edu2School: String, edu2Years: String,
    skills: List<String>,
    exp1Position: String, exp1Company: String, exp1Dates: String, exp1Desc: String,
    exp2Position: String, exp2Company: String, exp2Dates: String, exp2Desc: String,
    exp3Position: String, exp3Company: String, exp3Dates: String, exp3Desc: String,
    exp4Position: String, exp4Company: String, exp4Dates: String, exp4Desc: String,
    exp5Position: String, exp5Company: String, exp5Dates: String, exp5Desc: String,
    refName: String, refPositionCompany: String, refPhone: String, refEmail: String, refAvatarUri: String,
    ref2Name: String, ref2PositionCompany: String, ref2Phone: String, ref2Email: String, ref2AvatarUri: String,
    onFieldChange: (String, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        // ===== Diagonal ribbon header =====
        Box(modifier = Modifier.fillMaxWidth().height(150.dp)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                // wide navy diagonal band from top-left
                val navyPath = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(w * 0.62f, 0f)
                    lineTo(0f, h * 0.85f)
                    close()
                }
                drawPath(navyPath, color = Navy08)
                // thin gold border band just outside the navy diagonal
                val goldPath = Path().apply {
                    moveTo(w * 0.62f, 0f)
                    lineTo(w * 0.70f, 0f)
                    lineTo(0f, h)
                    lineTo(0f, h * 0.85f)
                    close()
                }
                drawPath(goldPath, color = Gold08)
            }

            // circular photo, top-right, gold ring
            AvatarCircle08(
                avatarUri = avatarUri,
                onAvatarChange = { onFieldChange("avatarUri", it) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 20.dp, end = 20.dp)
                    .size(96.dp)
            )
        }

        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
            Spacer(Modifier.height(16.dp))
            EditableText08(
                value = userName,
                placeholder = "Ex: Your Name",
                color = Navy08,
                fontSize = 24.sp,
                weight = FontWeight.ExtraBold
            ) { onFieldChange("fullName", it) }
            EditableText08(
                value = userTitle,
                placeholder = "Ex: Professional Title",
                color = Gray08,
                fontSize = 13.sp
            ) { onFieldChange("professionalTitle", it) }

            Spacer(Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                // ===== LEFT COLUMN =====
                Column(modifier = Modifier.weight(1f)) {
                    SectionLabel08("SKILLS")
                    skills.forEachIndexed { i, s ->
                        BulletLine08(
                            value = s,
                            placeholder = "Ex: Skill",
                        ) { onFieldChange("skill${i + 1}", it) }
                    }

                    Spacer(Modifier.height(18.dp))
                    SectionLabel08("EDUCATION")
                    EduEntry08(edu1Degree, edu1School, edu1Years,
                        onDegree = { onFieldChange("edu1Degree", it) },
                        onSchool = { onFieldChange("edu1School", it) },
                        onYears = { onFieldChange("edu1Years", it) })
                    Spacer(Modifier.height(10.dp))
                    EduEntry08(edu2Degree, edu2School, edu2Years,
                        onDegree = { onFieldChange("edu2Degree", it) },
                        onSchool = { onFieldChange("edu2School", it) },
                        onYears = { onFieldChange("edu2Years", it) })

                    Spacer(Modifier.height(18.dp))
                    SectionLabel08("REFERENCES")
                    RefEntry08(refName, refPositionCompany, refPhone, refEmail,
                        onName = { onFieldChange("refName", it) },
                        onPos = { onFieldChange("refPositionCompany", it) },
                        onPhone = { onFieldChange("refContact", it) },
                        onEmail = { onFieldChange("refEmail", it) })
                    Spacer(Modifier.height(10.dp))
                    RefEntry08(ref2Name, ref2PositionCompany, ref2Phone, ref2Email,
                        onName = { onFieldChange("ref2Name", it) },
                        onPos = { onFieldChange("ref2PositionCompany", it) },
                        onPhone = { onFieldChange("ref2Contact", it) },
                        onEmail = { onFieldChange("ref2Email", it) })

                    Spacer(Modifier.height(18.dp))
                    SectionLabel08("CONTACT")
                    IconLine08(Icons.Filled.Phone, contactPhone, "Ex: 0912 345 6789") { onFieldChange("phone", it) }
                    IconLine08(Icons.Filled.Email, contactEmail, "Ex: youremail@email.com") { onFieldChange("email", it) }
                    IconLine08(Icons.Filled.Home, contactAddress, "Ex: City, Province") { onFieldChange("location", it) }
                }

                Spacer(Modifier.width(20.dp))

                // ===== RIGHT COLUMN =====
                Column(modifier = Modifier.weight(1.3f)) {
                    EditableText08(
                        value = aboutMe,
                        placeholder = "Ex: Dedicated professional with strong track record delivering results.",
                        color = Ink08,
                        fontSize = 12.sp,
                        weight = FontWeight.Medium
                    ) { onFieldChange("aboutMe", it) }

                    Spacer(Modifier.height(20.dp))
                    SectionLabel08("WORK EXPERIENCE")
                    ExpEntry08(exp1Position, exp1Company, exp1Dates, exp1Desc,
                        { onFieldChange("exp1Position", it) }, { onFieldChange("exp1Company", it) },
                        { onFieldChange("exp1Dates", it) }, { onFieldChange("exp1Desc", it) })
                    Spacer(Modifier.height(14.dp))
                    ExpEntry08(exp2Position, exp2Company, exp2Dates, exp2Desc,
                        { onFieldChange("exp2Position", it) }, { onFieldChange("exp2Company", it) },
                        { onFieldChange("exp2Dates", it) }, { onFieldChange("exp2Desc", it) })
                    Spacer(Modifier.height(14.dp))
                    ExpEntry08(exp3Position, exp3Company, exp3Dates, exp3Desc,
                        { onFieldChange("exp3Position", it) }, { onFieldChange("exp3Company", it) },
                        { onFieldChange("exp3Dates", it) }, { onFieldChange("exp3Desc", it) })
                    Spacer(Modifier.height(14.dp))
                    ExpEntry08(exp4Position, exp4Company, exp4Dates, exp4Desc,
                        { onFieldChange("exp4Position", it) }, { onFieldChange("exp4Company", it) },
                        { onFieldChange("exp4Dates", it) }, { onFieldChange("exp4Desc", it) })
                    Spacer(Modifier.height(14.dp))
                    ExpEntry08(exp5Position, exp5Company, exp5Dates, exp5Desc,
                        { onFieldChange("exp5Position", it) }, { onFieldChange("exp5Company", it) },
                        { onFieldChange("exp5Dates", it) }, { onFieldChange("exp5Desc", it) })
                }
            }

            Spacer(Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                androidx.compose.material3.Text("08", color = Navy08.copy(alpha = 0.4f), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                androidx.compose.material3.Text("Corporate Diagonal", color = Gray08.copy(alpha = 0.6f), fontSize = 7.sp)
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

// ===================== Shared small pieces (local to Template 08) =====================

@Composable
private fun SectionLabel08(text: String) {
    Column {
        androidx.compose.material3.Text(text, color = Navy08, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Spacer(Modifier.height(3.dp))
        Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Gold08))
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun EditableText08(
    value: String,
    placeholder: String,
    color: Color,
    fontSize: TextUnit,
    weight: FontWeight = FontWeight.Normal,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(color = color, fontSize = fontSize, fontWeight = weight),
        decorationBox = { inner ->
            if (value.isEmpty()) {
                androidx.compose.material3.Text(placeholder, color = color.copy(alpha = 0.4f), fontSize = fontSize, fontWeight = weight)
            }
            inner()
        }
    )
}

@Composable
private fun BulletLine08(value: String, placeholder: String, onValueChange: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 4.dp)) {
        Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(Gold08))
        Spacer(Modifier.width(8.dp))
        EditableText08(value, placeholder, Ink08, 11.sp) { onValueChange(it) }
    }
}

@Composable
private fun IconLine08(icon: ImageVector, value: String, placeholder: String, onValueChange: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 6.dp)) {
        Icon(icon, contentDescription = null, tint = Navy08, modifier = Modifier.size(14.dp))
        Spacer(Modifier.width(8.dp))
        EditableText08(value, placeholder, Ink08, 10.5.sp) { onValueChange(it) }
    }
}

@Composable
private fun EduEntry08(
    degree: String, school: String, years: String,
    onDegree: (String) -> Unit, onSchool: (String) -> Unit, onYears: (String) -> Unit
) {
    Column {
        EditableText08(degree, "Ex: BS Information Technology", Ink08, 11.sp, FontWeight.Bold) { onDegree(it) }
        EditableText08(school, "Ex: University Name", Gray08, 10.sp) { onSchool(it) }
        EditableText08(years, "Ex: 2018 - 2022", Gold08, 9.5.sp) { onYears(it) }
    }
}

@Composable
private fun ExpEntry08(
    position: String, company: String, dates: String, desc: String,
    onPosition: (String) -> Unit, onCompany: (String) -> Unit, onDates: (String) -> Unit, onDesc: (String) -> Unit
) {
    Column {
        EditableText08(position, "Ex: Account Executive", Ink08, 12.sp, FontWeight.Bold) { onPosition(it) }
        Row {
            EditableText08(company, "Ex: Company Name", Navy08, 10.5.sp, FontWeight.Medium) { onCompany(it) }
        }
        EditableText08(dates, "Ex: 2021 - Present", Gold08, 9.5.sp) { onDates(it) }
        Spacer(Modifier.height(3.dp))
        EditableText08(desc, "Ex: Describe your role and key achievements.", Gray08, 10.sp) { onDesc(it) }
    }
}

@Composable
private fun RefEntry08(
    name: String, positionCompany: String, phone: String, email: String,
    onName: (String) -> Unit, onPos: (String) -> Unit, onPhone: (String) -> Unit, onEmail: (String) -> Unit
) {
    Column {
        EditableText08(name, "Ex: Reference Name", Ink08, 11.sp, FontWeight.Bold) { onName(it) }
        EditableText08(positionCompany, "Ex: Position / Company", Gray08, 9.5.sp) { onPos(it) }
        EditableText08(phone, "Ex: 0912 111 2222", Gray08, 9.5.sp) { onPhone(it) }
        EditableText08(email, "Ex: reference@email.com", Gray08, 9.5.sp) { onEmail(it) }
    }
}

@Composable
private fun AvatarCircle08(
    avatarUri: String,
    onAvatarChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(Color.White)
            .border(3.dp, Gold08, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (avatarUri.isNotEmpty()) {
            val bitmap: ImageBitmap? = remember(avatarUri) {
                try {
                    context.contentResolver.openInputStream(Uri.parse(avatarUri))?.use {
                        BitmapFactory.decodeStream(it)?.asImageBitmap()
                    }
                } catch (e: Exception) {
                    null
                }
            }
            if (bitmap != null) {
                androidx.compose.foundation.Image(
                    bitmap = bitmap,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().clip(CircleShape)
                )
            } else {
                Icon(Icons.Filled.AddAPhoto, contentDescription = null, tint = Gray08)
            }
        } else {
            Icon(Icons.Filled.AddAPhoto, contentDescription = null, tint = Gray08)
        }
    }
}
