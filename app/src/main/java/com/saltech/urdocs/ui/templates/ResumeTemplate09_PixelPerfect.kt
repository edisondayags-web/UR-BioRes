package com.saltech.urdocs.ui.templates

import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
private fun EditableText_09(
    value: String, placeholder: String, color: Color, fontSize: TextUnit,
    fontWeight: FontWeight? = null, modifier: Modifier = Modifier, onValueChange: (String) -> Unit
) {
    Box(modifier = modifier) {
        if (value.isEmpty()) {
            Text(placeholder, color = color.copy(alpha = 0.45f), fontSize = fontSize, fontWeight = fontWeight, maxLines = 1)
        }
        BasicTextField(
            value = value, onValueChange = onValueChange,
            textStyle = TextStyle(color = color, fontSize = fontSize, fontWeight = fontWeight),
            cursorBrush = SolidColor(color), maxLines = 1, modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun PillHeader_09(text: String, bg: Color, textColor: Color) {
    Box(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(50)).background(bg).padding(vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = textColor, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
    }
}

@Composable
private fun IconRow_09(icon: ImageVector, value: String, placeholder: String, accent: Color, onValueChange: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.size(16.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.15f)).border(0.5.dp, Color.White.copy(alpha = 0.4f), CircleShape), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(9.dp))
        }
        Spacer(Modifier.width(6.dp))
        EditableText_09(value, placeholder, Color.White, 7.sp, modifier = Modifier.weight(1f), onValueChange = onValueChange)
    }
}

@Composable
private fun LanguageBar_09(name: String, level: Float, accent: Color) {
    Column(Modifier.fillMaxWidth().padding(vertical = 3.dp)) {
        Text(name, color = Color.White, fontSize = 7.5.sp)
        Spacer(Modifier.height(2.dp))
        Box(Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)).background(Color.White.copy(alpha = 0.2f))) {
            Box(Modifier.fillMaxWidth(level).fillMaxHeight().clip(RoundedCornerShape(2.dp)).background(Color.White))
        }
    }
}

@Composable
private fun SkillDots_09(name: String, level: Int, accent: Color) {
    Row(Modifier.fillMaxWidth().padding(vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(name, color = Color(0xFF333333), fontSize = 7.5.sp, modifier = Modifier.weight(1f))
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            repeat(5) { i ->
                Box(Modifier.size(6.dp).clip(CircleShape).background(if (i < level) accent else Color(0xFFDDDDDD)))
            }
        }
    }
}

@Composable
private fun ReferenceBlock_09(name: String, position: String, phone: String, accent: Color) {
    Column(Modifier.fillMaxWidth().padding(vertical = 3.dp)) {
        Text(name.ifEmpty { "Ex: Reference Name" }, color = Color.White, fontSize = 7.5.sp, fontWeight = FontWeight.Bold)
        Text(position.ifEmpty { "Position / Company" }, color = Color.White.copy(alpha = 0.7f), fontSize = 7.sp)
        Text(phone.ifEmpty { "000-000-0000" }, color = Color.White.copy(alpha = 0.7f), fontSize = 7.sp)
    }
}

@Composable
fun ResumeTemplate09_PixelPerfect(
    userName: String = "", userTitle: String = "", avatarUri: String = "",
    contactPhone: String = "", contactEmail: String = "", contactAddress: String = "",
    eduDegree: String = "", eduSchool: String = "", eduYears: String = "",
    lang1: String = "English", lang1Level: Float = 0.9f,
    lang2: String = "Spanish", lang2Level: Float = 0.6f,
    lang3: String = "Japanese", lang3Level: Float = 0.4f,
    aboutMe: String = "",
    exp1Position: String = "", exp1Company: String = "", exp1Dates: String = "", exp1Desc: String = "",
    exp2Position: String = "", exp2Company: String = "", exp2Dates: String = "", exp2Desc: String = "",
    skill1: String = "", skill1Level: Int = 4,
    skill2: String = "", skill2Level: Int = 4,
    skill3: String = "", skill3Level: Int = 3,
    skill4: String = "", skill4Level: Int = 3,
    refName: String = "", refPosition: String = "", refPhone: String = "",
    ref2Name: String = "", ref2Position: String = "", ref2Phone: String = "",
    onFieldChange: (String, String) -> Unit = { _, _ -> },
    onHomeOverride: () -> Unit = {}
) {
    val navy = Color(0xFF1B3358)
    val graphicsLayer = androidx.compose.ui.graphics.rememberGraphicsLayer()
    val nameFontSize = autoShrinkNameFontSize(userName)

    Box(Modifier.fillMaxWidth()) {
        Box(
            Modifier.fillMaxWidth().defaultMinSize(minHeight = 700.dp).background(Color.White)
                .drawWithContent {
                    graphicsLayer.record { this@drawWithContent.drawContent() }
                    drawLayer(graphicsLayer)
                }
        ) {
            Row(Modifier.fillMaxWidth()) {
                // LEFT NAVY SIDEBAR
                Column(
                    Modifier.width(120.dp).fillMaxHeight().background(navy).padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(8.dp))
                    SharedAvatarPicker(avatarUri, 70.dp, Color.White, userName) { onFieldChange("avatarUri", it) }
                    Spacer(Modifier.height(14.dp))

                    PillHeader_09("CONTACT", Color.White, navy)
                    Spacer(Modifier.height(6.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        IconRow_09(Icons.Filled.Phone, contactPhone, "Ex: 0912 345 6789", navy) { onFieldChange("phone", it) }
                        IconRow_09(Icons.Filled.Email, contactEmail, "Ex: you@email.com", navy) { onFieldChange("email", it) }
                        IconRow_09(Icons.Filled.Place, contactAddress, "Ex: City, Province", navy) { onFieldChange("location", it) }
                    }
                    Spacer(Modifier.height(12.dp))

                    PillHeader_09("EDUCATION", Color.White, navy)
                    Spacer(Modifier.height(6.dp))
                    Column {
                        EditableText_09(eduDegree, "Ex: BS Marketing", Color.White, 7.5.sp, FontWeight.Bold) { onFieldChange("eduDegree", it) }
                        EditableText_09(eduSchool, "Ex: University Name", Color.White, 7.sp) { onFieldChange("eduSchool", it) }
                        EditableText_09(eduYears, "Ex: 2018 - 2022", Color.White.copy(alpha = 0.7f), 7.sp) { onFieldChange("eduYears", it) }
                    }
                    Spacer(Modifier.height(12.dp))

                    PillHeader_09("LANGUAGE", Color.White, navy)
                    Spacer(Modifier.height(6.dp))
                    Column {
                        LanguageBar_09(lang1, lang1Level, navy)
                        LanguageBar_09(lang2, lang2Level, navy)
                        LanguageBar_09(lang3, lang3Level, navy)
                    }
                    Spacer(Modifier.height(12.dp))

                    PillHeader_09("REFERENCE", Color.White, navy)
                    Spacer(Modifier.height(6.dp))
                    Column {
                        ReferenceBlock_09(refName, refPosition, refPhone, navy)
                        Spacer(Modifier.height(6.dp))
                        ReferenceBlock_09(ref2Name, ref2Position, ref2Phone, navy)
                }

                // RIGHT WHITE CONTENT
                Column(Modifier.weight(1f)) {
                    Box(Modifier.fillMaxWidth().background(navy).padding(14.dp)) {
                        Column {
                            EditableText_09(userName, "Ex: Your Name", Color.White, nameFontSize, FontWeight.Bold) { onFieldChange("fullName", it) }
                            EditableText_09(userTitle, "Ex: Professional Title", Color.White.copy(alpha = 0.85f), 10.sp) { onFieldChange("professionalTitle", it) }
                        }
                 Ğ ¢6öÇVÖâ„ÖöF–f–W"æf–ÆÄÖ…v–GF‚‚’çFF–ærƒBæG’ÂfW'F–6Ä'&ævVÖVçBÒ'&ævVÖVçBç76VD'’ƒ"æG’’°¢6öÇVÖâ°¢–ÆÄ†VFW%ó’‚$$õUBÔR"Âæg’Â6öÆ÷"åv†—FR¢76W"„ÖöF–f–W"æ†V–v‡BƒbæG’¢VF—F&ÆUFW‡Eó’†&÷WDÖRÂ$Wƒ¢FVF–6FVB&öfW76–öæÂv—F‚7G&öær6öÖ×Væ–6F–öâ6¶–ÆÇ2â"Â6öÆ÷"ƒ„dcCCCCCB’ÂrãRç7’²öäf–VÆD6†ævR‚&&÷WDÖR"Â—B’Ğ¢Ğ ¢6öÇVÖâ°¢–ÆÄ†VFW%ó’‚$U…U$”Tä4R"Âæg’Â6öÆ÷"åv†—FR¢76W"„ÖöF–f–W"æ†V–v‡BƒbæG’¢6öÇVÖâ‡fW'F–6Ä'&ævVÖVçBÒ'&ævVÖVçBç76VD'’ƒ‚æG’’°¢6öÇVÖâ°¢VF—F&ÆUFW‡Eó’†W‡FFW2Â$Wƒ¢##"Ò&W6VçB"Âæg’Ârç7ÂföçEvV–v‡Bä&öÆB’²öäf–VÆD6†ævR‚&W‡FFW2"Â—B’Ğ¢VF—F&ÆUFW‡Eó’†W‡6ö×ç’Â$Wƒ¢6ö×ç’ÂFG&W72"Â6öÆ÷"ƒ„dcCCCCCB’Ârç7’²öäf–VÆD6†ævR‚&W‡6ö×ç’"Â—B’Ğ¢VF—F&ÆUFW‡Eó’†W‡÷6—F–öâÂ$Wƒ¢¦ö"F—FÆR"Â6öÆ÷"ƒ„dc’ÂrãRç7ÂföçEvV–v‡Bä&öÆB’²öäf–VÆD6†ævR‚&W‡÷6—F–öâ"Â—B’Ğ¢VF—F&ÆUFW‡Eó’†W‡FW62Â$Wƒ¢¶W’6†–WfVÖVçB÷"&W7öç6–&–Æ—G’â"Â6öÆ÷"ƒ„dcCCCCCB’Ârç7’²öäf–VÆD6†ævR‚&W‡FW62"Â—B’Ğ¢Ğ¢6öÇVÖâ°¢VF—F&ÆUFW‡Eó’†W‡$FFW2Â$Wƒ¢##Ò##""Âæg’Ârç7ÂföçEvV–v‡Bä&öÆB’²öäf–VÆD6†ævR‚&W‡$FFW2"Â—B’Ğ¢VF—F&ÆUFW‡Eó’†W‡$6ö×ç’Â$Wƒ¢6ö×ç’ÂFG&W72"Â6öÆ÷"ƒ„dcCCCCCB’Ârç7’²öäf–VÆD6†ævR‚&W‡$6ö×ç’"Â—B’Ğ¢VF—F&ÆUFW‡Eó’†W‡%÷6—F–öâÂ$Wƒ¢¦ö"F—FÆR"Â6öÆ÷"ƒ„dc’ÂrãRç7ÂföçEvV–v‡Bä&öÆB’²öäf–VÆD6†ævR‚&W‡%÷6—F–öâ"Â—B’Ğ¢VF—F&ÆUFW‡Eó’†W‡$FW62Â$Wƒ¢¶W’6†–WfVÖVçB÷"&W7öç6–&–Æ—G’â"Â6öÆ÷"ƒ„dcCCCCCB’Ârç7’²öäf–VÆD6†ævR‚&W‡$FW62"Â—B’Ğ¢Ğ¢Ğ¢Ğ ¢6öÇVÖâ°¢–ÆÄ†VFW%ó’‚%4´”ÄÂ"Âæg’Â6öÆ÷"åv†—FR¢76W"„ÖöF–f–W"æ†V–v‡BƒbæG’¢6¶–ÆÄF÷G5ó’‡6¶–ÆÃæ–dV×G’²$Wƒ¢Ö&¶WBæÇ—F–72"ÒÂ6¶–ÆÃÆWfVÂÂæg’¢6¶–ÆÄF÷G5ó’‡6¶–ÆÃ"æ–dV×G’²$Wƒ¢4Tò"ÒÂ6¶–ÆÃ$ÆWfVÂÂæg’¢6¶–ÆÄF÷G5ó’‡6¶–ÆÃ2æ–dV×G’²$Wƒ¢6÷—w&—F–ær"ÒÂ6¶–ÆÃ4ÆWfVÂÂæg’¢6¶–ÆÄF÷G5ó’‡6¶–ÆÃBæ–dV×G’²$Wƒ¢vV"&öw&ÖÖ–ær"ÒÂ6¶–ÆÃDÆWfVÂÂæg’¢Ğ¢Ğ¢Ğ¢Ğ¢Ğ¢6öÒç6ÇFV6‚çW&Fö72çV’çFV×ÆFW2åFV×ÆFTW‡÷'DÖVçR€¢w&†–74Æ–W"À¢'&W7VÖUòGW6W$æÖR"À¢öä†öÖRÒöä†öÖT÷fW'&–FRÀ¢ÖöF–f–W"ÒÖöF–f–W"æÆ–vâ„Æ–væÖVçBåF÷VæB’çFF–ær‡F÷ÒBæGÂVæBÒ"æG¢¢Ğ§Ğ