package com.saltech.urdocs.ui.screens
import com.saltech.urdocs.ui.templates.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.ui.graphics.graphicsLayer

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
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.Constraints

private val T01White = Color(0xFFF5F5F5)
private val T01Gray = Color(0xFF6E6E6E)
private val T01Divider = Color(0xFF3A3A3A)
private val T01BarTrack = Color(0xFF2A2A2A)

// ===== DATA CLASS =====
data class ResumeTemplateFields(
    val fullName: String = "",
    val professionalTitle: String = "",
    val avatarUri: String = "",
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
    val exp2Position: String = "", val exp2Company: String = "", val exp2Dates: String = "", val exp2Desc: String = "",
    val exp3Position: String = "", val exp3Company: String = "", val exp3Dates: String = "", val exp3Desc: String = "",
    val exp4Position: String = "", val exp4Company: String = "", val exp4Dates: String = "", val exp4Desc: String = "",
    val exp5Position: String = "", val exp5Company: String = "", val exp5Dates: String = "", val exp5Desc: String = "",
    val refName: String = "", val refPositionCompany: String = "", val refContact: String = "", val refEmail: String = "", val refAvatarUri: String = "",
    val ref2Name: String = "", val ref2PositionCompany: String = "", val ref2Contact: String = "", val ref2Email: String = "", val ref2AvatarUri: String = "",
    val exp1Desc2: String = "", val exp1Desc3: String = "",
    val exp2Desc2: String = "", val exp2Desc3: String = "",
    val exp3Desc2: String = "", val exp3Desc3: String = "",
    val workSetup: String = "", val workSchedule: String = "", val preferredRole: String = "",
    val prefLocations: String = "", val availability: String = "", val languages: String = "",
    val certifications: String = "", val hobbies: String = "", val careerGoal: String = "",
    val strengths: String = "", val otherInfo: String = ""
)


// ===== AUTO-GENERATED FIELD MAPPER (fix for templates 02-30 not binding to state) =====
fun applyFieldChange(data: ResumeTemplateFields, key: String, value: String): ResumeTemplateFields = when (key) {
    "fullName" -> data.copy(fullName = value)
    "professionalTitle" -> data.copy(professionalTitle = value)
    "avatarUri" -> data.copy(avatarUri = value)
    "phone" -> data.copy(phone = value)
    "email" -> data.copy(email = value)
    "location" -> data.copy(location = value)
    "linkedin" -> data.copy(linkedin = value)
    "website" -> data.copy(website = value)
    "aboutMe" -> data.copy(aboutMe = value)
    "edu1Degree" -> data.copy(edu1Degree = value)
    "edu1School" -> data.copy(edu1School = value)
    "edu1Years" -> data.copy(edu1Years = value)
    "edu2Degree" -> data.copy(edu2Degree = value)
    "edu2School" -> data.copy(edu2School = value)
    "edu2Years" -> data.copy(edu2Years = value)
    "skill1" -> data.copy(skill1 = value)
    "skill2" -> data.copy(skill2 = value)
    "skill3" -> data.copy(skill3 = value)
    "skill4" -> data.copy(skill4 = value)
    "skill5" -> data.copy(skill5 = value)
    "skill6" -> data.copy(skill6 = value)
    "exp1Position" -> data.copy(exp1Position = value)
    "exp1Company" -> data.copy(exp1Company = value)
    "exp1Dates" -> data.copy(exp1Dates = value)
    "exp1Desc" -> data.copy(exp1Desc = value)
    "exp1Desc2" -> data.copy(exp1Desc2 = value)
    "exp1Desc3" -> data.copy(exp1Desc3 = value)
    "exp2Position" -> data.copy(exp2Position = value)
    "exp2Company" -> data.copy(exp2Company = value)
    "exp2Dates" -> data.copy(exp2Dates = value)
    "exp2Desc" -> data.copy(exp2Desc = value)
    "exp2Desc2" -> data.copy(exp2Desc2 = value)
    "exp2Desc3" -> data.copy(exp2Desc3 = value)
    "exp3Position" -> data.copy(exp3Position = value)
    "exp3Company" -> data.copy(exp3Company = value)
    "exp3Dates" -> data.copy(exp3Dates = value)
    "exp3Desc" -> data.copy(exp3Desc = value)
    "exp3Desc2" -> data.copy(exp3Desc2 = value)
    "exp3Desc3" -> data.copy(exp3Desc3 = value)
    "exp4Position" -> data.copy(exp4Position = value)
    "exp4Company" -> data.copy(exp4Company = value)
    "exp4Dates" -> data.copy(exp4Dates = value)
    "exp4Desc" -> data.copy(exp4Desc = value)
    "exp5Position" -> data.copy(exp5Position = value)
    "exp5Company" -> data.copy(exp5Company = value)
    "exp5Dates" -> data.copy(exp5Dates = value)
    "exp5Desc" -> data.copy(exp5Desc = value)
    "refName" -> data.copy(refName = value)
    "refPositionCompany" -> data.copy(refPositionCompany = value)
    "refContact" -> data.copy(refContact = value)
    "refEmail" -> data.copy(refEmail = value)
    "refAvatarUri" -> data.copy(refAvatarUri = value)
    "ref2Name" -> data.copy(ref2Name = value)
    "ref2PositionCompany" -> data.copy(ref2PositionCompany = value)
    "ref2Contact" -> data.copy(ref2Contact = value)
    "ref2Email" -> data.copy(ref2Email = value)
    "ref2AvatarUri" -> data.copy(ref2AvatarUri = value)
    "workSetup" -> data.copy(workSetup = value)
    "workSchedule" -> data.copy(workSchedule = value)
    "preferredRole" -> data.copy(preferredRole = value)
    "prefLocations" -> data.copy(prefLocations = value)
    "availability" -> data.copy(availability = value)
    "languages" -> data.copy(languages = value)
    "certifications" -> data.copy(certifications = value)
    "hobbies" -> data.copy(hobbies = value)
    "careerGoal" -> data.copy(careerGoal = value)
    "strengths" -> data.copy(strengths = value)
    "otherInfo" -> data.copy(otherInfo = value)
    "avatarUri2" -> data.copy(avatarUri = value)
    else -> data
}

@Composable
fun ScaledToFitContent(
    scale: Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(content = content, modifier = modifier) { measurables, _ ->
        val placeable = measurables.first().measure(Constraints())
        val scaledWidth = (placeable.width * scale).toInt()
        val scaledHeight = (placeable.height * scale).toInt()
        layout(scaledWidth, scaledHeight) {
            placeable.placeWithLayer(0, 0) {
                scaleX = scale
                scaleY = scale
                transformOrigin = TransformOrigin(0f, 0f)
            }
        }
    }
}

// ===== FORM SCREEN WRAPPER =====
@Composable
fun ResumeTemplateFormScreen(
    templateName: String,
    onBack: () -> Unit = {}
) {
    var data by remember { mutableStateOf(ResumeTemplateFields()) }
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0D0D0D))) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart).padding(top = 4.dp, start = 8.dp)
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
        }
        val paperWidthDp = 850.dp
        androidx.compose.foundation.layout.BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val fitScale = maxWidth / paperWidthDp
            ScaledToFitContent(
                scale = fitScale,
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                Box(modifier = Modifier.requiredWidth(paperWidthDp)) {
        when (templateName) {
            "resume_template_01" -> ResumeTemplate01Screen(data, { data = it }, onBack)
            "resume_template_02" -> ResumeTemplate02Screen(data, { data = it }, onBack)
            "resume_template_03" -> ResumeTemplate03Screen(data, { data = it }, onBack)
            "resume_template_04" -> ResumeTemplate04Screen(data, { data = it }, onBack)
            "resume_template_05" -> ResumeTemplate05Screen(data, { data = it }, onBack)
            "resume_template_06" -> ResumeTemplate06Screen(data, { data = it }, onBack)
            "resume_template_07" -> ResumeTemplate07Screen(data, { data = it }, onBack)
            "resume_template_08" -> ResumeTemplate08Screen(data, { data = it }, onBack)
            "resume_template_09" -> ResumeTemplate09Screen(data, { data = it }, onBack)
            "resume_template_10" -> ResumeTemplate10Screen(data, { data = it }, onBack)
            "resume_template_11" -> ResumeTemplate11Screen(data, { data = it }, onBack)
            "resume_template_12" -> ResumeTemplate12Screen(data, { data = it }, onBack)
            "resume_template_13" -> ResumeTemplate13Screen(data, { data = it }, onBack)
            "resume_template_14" -> ResumeTemplate14Screen(data, { data = it }, onBack)
            "resume_template_15" -> ResumeTemplate15Screen(data, { data = it }, onBack)
            "resume_template_16" -> ResumeTemplate16Screen(data, { data = it }, onBack)
            "resume_template_17" -> ResumeTemplate17Screen(data, { data = it }, onBack)
            "resume_template_18" -> ResumeTemplate18Screen(data, { data = it }, onBack)
            "resume_template_19" -> ResumeTemplate19Screen(data, { data = it }, onBack)
            "resume_template_20" -> ResumeTemplate20Screen(data, { data = it }, onBack)
            "resume_template_21" -> ResumeTemplate21Screen(data, { data = it }, onBack)
            "resume_template_22" -> ResumeTemplate22Screen(data, { data = it }, onBack)
            "resume_template_23" -> ResumeTemplate23Screen(data, { data = it }, onBack)
            "package1_template_01" -> ResumeTemplateP1_01Screen(data, { data = it }, onBack)
            "package1_template_02" -> ResumeTemplateP1_02_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_03" -> ResumeTemplateP1_03_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_04" -> ResumeTemplateP1_04_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_05" -> ResumeTemplateP1_05_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_06" -> ResumeTemplateP1_06_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_07" -> ResumeTemplateP1_07_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_08" -> ResumeTemplateP1_08_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_09" -> ResumeTemplateP1_09_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_10" -> ResumeTemplateP1_10_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_11" -> ResumeTemplateP1_11_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_12" -> ResumeTemplateP1_12_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_13" -> ResumeTemplateP1_13_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_14" -> ResumeTemplateP1_14_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_15" -> ResumeTemplateP1_15_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_16" -> ResumeTemplateP1_16_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_17" -> ResumeTemplateP1_17_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_18" -> ResumeTemplateP1_18_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_19" -> ResumeTemplateP1_19_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_20" -> ResumeTemplateP1_20_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_21" -> ResumeTemplateP1_21_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_22" -> ResumeTemplateP1_22_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_23" -> ResumeTemplateP1_23_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_24" -> ResumeTemplateP1_24_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_25" -> ResumeTemplateP1_25_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_26" -> ResumeTemplateP1_26_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_27" -> ResumeTemplateP1_27_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_28" -> ResumeTemplateP1_28_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_29" -> ResumeTemplateP1_29_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
            "package1_template_30" -> ResumeTemplateP1_30_PixelPerfect(
                userName = data.fullName,
                userTitle = data.professionalTitle,
                avatarUri = data.avatarUri,
                contactPhone = data.phone,
                contactEmail = data.email,
                contactAddress = data.location,
                contactWebsite = data.website,
                contactLinkedin = data.linkedin,
                aboutMe = data.aboutMe,
                edu1Degree = data.edu1Degree,
                edu1School = data.edu1School,
                edu1Years = data.edu1Years,
                edu2Degree = data.edu2Degree,
                edu2School = data.edu2School,
                edu2Years = data.edu2Years,
                skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
                exp1Position = data.exp1Position,
                exp1Company = data.exp1Company,
                exp1Dates = data.exp1Dates,
                exp1Desc = data.exp1Desc,
                exp1Desc2 = data.exp1Desc2,
                exp1Desc3 = data.exp1Desc3,
                exp2Position = data.exp2Position,
                exp2Company = data.exp2Company,
                exp2Dates = data.exp2Dates,
                exp2Desc = data.exp2Desc,
                exp2Desc2 = data.exp2Desc2,
                exp2Desc3 = data.exp2Desc3,
                exp3Position = data.exp3Position,
                exp3Company = data.exp3Company,
                exp3Dates = data.exp3Dates,
                exp3Desc = data.exp3Desc,
                exp3Desc2 = data.exp3Desc2,
                exp3Desc3 = data.exp3Desc3,
                refName = data.refName,
                refPositionCompany = data.refPositionCompany,
                refPhone = data.refContact,
                refEmail = data.refEmail,
                refAvatarUri = data.refAvatarUri,
                ref2Name = data.ref2Name,
                ref2PositionCompany = data.ref2PositionCompany,
                ref2Phone = data.ref2Contact,
                ref2Email = data.ref2Email,
                ref2AvatarUri = data.ref2AvatarUri,
                workSetup = data.workSetup,
                workSchedule = data.workSchedule,
                preferredRole = data.preferredRole,
                prefLocations = data.prefLocations,
                availability = data.availability,
                languages = data.languages,
                certifications = data.certifications,
                hobbies = data.hobbies,
                careerGoal = data.careerGoal,
                strengths = data.strengths,
                otherInfo = data.otherInfo,
                onFieldChange = { k, v -> data = applyFieldChange(data, k, v) }
            )
        }
            }
        }
        }
    }
}

// ===== INDIVIDUAL TEMPLATES (01 to 23) - AUTO-WIRED TO PIXELPERFECT =====

@Composable
fun ResumeTemplate01Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit, onHome: () -> Unit = {}) {
    com.saltech.urdocs.ui.templates.ResumeTemplate01_PixelPerfect(
        userName = data.fullName,
        userTitle = data.professionalTitle,
        avatarUri = data.avatarUri,
        contactPhone = data.phone,
        contactEmail = data.email,
        contactAddress = data.location,
        contactWebsite = data.website,
        contactLinkedin = data.linkedin,
        aboutMe = data.aboutMe,
        edu1Degree = data.edu1Degree, edu1School = data.edu1School, edu1Years = data.edu1Years,
        edu2Degree = data.edu2Degree, edu2School = data.edu2School, edu2Years = data.edu2Years,
        skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
        exp1Position = data.exp1Position, exp1Company = data.exp1Company, exp1Dates = data.exp1Dates, exp1Desc = data.exp1Desc,
        exp2Position = data.exp2Position, exp2Company = data.exp2Company, exp2Dates = data.exp2Dates, exp2Desc = data.exp2Desc,
        exp3Position = data.exp3Position, exp3Company = data.exp3Company, exp3Dates = data.exp3Dates, exp3Desc = data.exp3Desc,
        exp4Position = data.exp4Position, exp4Company = data.exp4Company, exp4Dates = data.exp4Dates, exp4Desc = data.exp4Desc,
        exp5Position = data.exp5Position, exp5Company = data.exp5Company, exp5Dates = data.exp5Dates, exp5Desc = data.exp5Desc,
        refName = data.refName, refPositionCompany = data.refPositionCompany, refPhone = data.refContact, refEmail = data.refEmail, refAvatarUri = data.refAvatarUri,
        ref2Name = data.ref2Name, ref2PositionCompany = data.ref2PositionCompany, ref2Phone = data.ref2Contact, ref2Email = data.ref2Email, ref2AvatarUri = data.ref2AvatarUri,
        onFieldChange = { field, value ->
            onFieldChange(
                when (field) {
                    "fullName" -> data.copy(fullName = value)
                    "professionalTitle" -> data.copy(professionalTitle = value)
                    "avatarUri" -> data.copy(avatarUri = value)
                    "phone" -> data.copy(phone = value)
                    "email" -> data.copy(email = value)
                    "location" -> data.copy(location = value)
                    "website" -> data.copy(website = value)
                    "linkedin" -> data.copy(linkedin = value)
                    "aboutMe" -> data.copy(aboutMe = value)
                    "edu1Degree" -> data.copy(edu1Degree = value)
                    "edu1School" -> data.copy(edu1School = value)
                    "edu1Years" -> data.copy(edu1Years = value)
                    "edu2Degree" -> data.copy(edu2Degree = value)
                    "edu2School" -> data.copy(edu2School = value)
                    "edu2Years" -> data.copy(edu2Years = value)
                    "skill1" -> data.copy(skill1 = value)
                    "skill2" -> data.copy(skill2 = value)
                    "skill3" -> data.copy(skill3 = value)
                    "skill4" -> data.copy(skill4 = value)
                    "skill5" -> data.copy(skill5 = value)
                    "skill6" -> data.copy(skill6 = value)
                    "exp1Position" -> data.copy(exp1Position = value)
                    "exp1Company" -> data.copy(exp1Company = value)
                    "exp1Dates" -> data.copy(exp1Dates = value)
                    "exp1Desc" -> data.copy(exp1Desc = value)
                    "exp2Position" -> data.copy(exp2Position = value)
                    "exp2Company" -> data.copy(exp2Company = value)
                    "exp2Dates" -> data.copy(exp2Dates = value)
                    "exp2Desc" -> data.copy(exp2Desc = value)
                    "exp3Position" -> data.copy(exp3Position = value)
                    "exp3Company" -> data.copy(exp3Company = value)
                    "exp3Dates" -> data.copy(exp3Dates = value)
                    "exp3Desc" -> data.copy(exp3Desc = value)
                    "exp4Position" -> data.copy(exp4Position = value)
                    "exp4Company" -> data.copy(exp4Company = value)
                    "exp4Dates" -> data.copy(exp4Dates = value)
                    "exp4Desc" -> data.copy(exp4Desc = value)
                    "exp5Position" -> data.copy(exp5Position = value)
                    "exp5Company" -> data.copy(exp5Company = value)
                    "exp5Dates" -> data.copy(exp5Dates = value)
                    "exp5Desc" -> data.copy(exp5Desc = value)
                    "refName" -> data.copy(refName = value)
                    "refPositionCompany" -> data.copy(refPositionCompany = value)
                    "refPhone" -> data.copy(refContact = value)
                    "refEmail" -> data.copy(refEmail = value)
                    "ref2Name" -> data.copy(ref2Name = value)
                    "ref2PositionCompany" -> data.copy(ref2PositionCompany = value)
                    "ref2Phone" -> data.copy(ref2Contact = value)
                    "ref2Email" -> data.copy(ref2Email = value)
                    "refAvatarUri" -> data.copy(refAvatarUri = value)
                    "ref2AvatarUri" -> data.copy(ref2AvatarUri = value)
                    else -> data
                }
            )
        },
        onHomeOverride = onHome
    )
}

@Composable
fun ResumeTemplate02Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit, onHome: () -> Unit = {}) {
    com.saltech.urdocs.ui.templates.ResumeTemplate02_PixelPerfect(
        userName = data.fullName,
        userTitle = data.professionalTitle,
        avatarUri = data.avatarUri,
        contactPhone = data.phone,
        contactEmail = data.email,
        contactAddress = data.location,
        contactWebsite = data.website,
        contactLinkedin = data.linkedin,
        aboutMe = data.aboutMe,
        edu1Degree = data.edu1Degree, edu1School = data.edu1School, edu1Years = data.edu1Years,
        edu2Degree = data.edu2Degree, edu2School = data.edu2School, edu2Years = data.edu2Years,
        skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
        exp1Position = data.exp1Position, exp1Company = data.exp1Company, exp1Dates = data.exp1Dates, exp1Desc = data.exp1Desc,
        exp2Position = data.exp2Position, exp2Company = data.exp2Company, exp2Dates = data.exp2Dates, exp2Desc = data.exp2Desc,
        exp3Position = data.exp3Position, exp3Company = data.exp3Company, exp3Dates = data.exp3Dates, exp3Desc = data.exp3Desc,
        exp4Position = data.exp4Position, exp4Company = data.exp4Company, exp4Dates = data.exp4Dates, exp4Desc = data.exp4Desc,
        exp5Position = data.exp5Position, exp5Company = data.exp5Company, exp5Dates = data.exp5Dates, exp5Desc = data.exp5Desc,
        refName = data.refName, refPositionCompany = data.refPositionCompany, refPhone = data.refContact, refEmail = data.refEmail, refAvatarUri = data.refAvatarUri,
        ref2Name = data.ref2Name, ref2PositionCompany = data.ref2PositionCompany, ref2Phone = data.ref2Contact, ref2Email = data.ref2Email, ref2AvatarUri = data.ref2AvatarUri,
        onFieldChange = { field, value ->
            onFieldChange(
                when (field) {
                    "fullName" -> data.copy(fullName = value)
                    "professionalTitle" -> data.copy(professionalTitle = value)
                    "avatarUri" -> data.copy(avatarUri = value)
                    "phone" -> data.copy(phone = value)
                    "email" -> data.copy(email = value)
                    "location" -> data.copy(location = value)
                    "website" -> data.copy(website = value)
                    "linkedin" -> data.copy(linkedin = value)
                    "aboutMe" -> data.copy(aboutMe = value)
                    "edu1Degree" -> data.copy(edu1Degree = value)
                    "edu1School" -> data.copy(edu1School = value)
                    "edu1Years" -> data.copy(edu1Years = value)
                    "edu2Degree" -> data.copy(edu2Degree = value)
                    "edu2School" -> data.copy(edu2School = value)
                    "edu2Years" -> data.copy(edu2Years = value)
                    "skill1" -> data.copy(skill1 = value)
                    "skill2" -> data.copy(skill2 = value)
                    "skill3" -> data.copy(skill3 = value)
                    "skill4" -> data.copy(skill4 = value)
                    "skill5" -> data.copy(skill5 = value)
                    "skill6" -> data.copy(skill6 = value)
                    "exp1Position" -> data.copy(exp1Position = value)
                    "exp1Company" -> data.copy(exp1Company = value)
                    "exp1Dates" -> data.copy(exp1Dates = value)
                    "exp1Desc" -> data.copy(exp1Desc = value)
                    "exp2Position" -> data.copy(exp2Position = value)
                    "exp2Company" -> data.copy(exp2Company = value)
                    "exp2Dates" -> data.copy(exp2Dates = value)
                    "exp2Desc" -> data.copy(exp2Desc = value)
                    "exp3Position" -> data.copy(exp3Position = value)
                    "exp3Company" -> data.copy(exp3Company = value)
                    "exp3Dates" -> data.copy(exp3Dates = value)
                    "exp3Desc" -> data.copy(exp3Desc = value)
                    "exp4Position" -> data.copy(exp4Position = value)
                    "exp4Company" -> data.copy(exp4Company = value)
                    "exp4Dates" -> data.copy(exp4Dates = value)
                    "exp4Desc" -> data.copy(exp4Desc = value)
                    "exp5Position" -> data.copy(exp5Position = value)
                    "exp5Company" -> data.copy(exp5Company = value)
                    "exp5Dates" -> data.copy(exp5Dates = value)
                    "exp5Desc" -> data.copy(exp5Desc = value)
                    "refName" -> data.copy(refName = value)
                    "refPositionCompany" -> data.copy(refPositionCompany = value)
                    "refPhone" -> data.copy(refContact = value)
                    "refEmail" -> data.copy(refEmail = value)
                    "ref2Name" -> data.copy(ref2Name = value)
                    "ref2PositionCompany" -> data.copy(ref2PositionCompany = value)
                    "ref2Phone" -> data.copy(ref2Contact = value)
                    "ref2Email" -> data.copy(ref2Email = value)
                    "refAvatarUri" -> data.copy(refAvatarUri = value)
                    "ref2AvatarUri" -> data.copy(ref2AvatarUri = value)
                    else -> data
                }
            )
        },
        onHomeOverride = onHome
    )
}

@Composable
fun ResumeTemplate03Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit, onHome: () -> Unit = {}) {
    com.saltech.urdocs.ui.templates.ResumeTemplate03_PixelPerfect(
        userName = data.fullName,
        userTitle = data.professionalTitle,
        avatarUri = data.avatarUri,
        contactPhone = data.phone,
        contactEmail = data.email,
        contactAddress = data.location,
        contactWebsite = data.website,
        contactLinkedin = data.linkedin,
        aboutMe = data.aboutMe,
        edu1Degree = data.edu1Degree, edu1School = data.edu1School, edu1Years = data.edu1Years,
        edu2Degree = data.edu2Degree, edu2School = data.edu2School, edu2Years = data.edu2Years,
        skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
        exp1Position = data.exp1Position, exp1Company = data.exp1Company, exp1Dates = data.exp1Dates, exp1Desc = data.exp1Desc,
        exp2Position = data.exp2Position, exp2Company = data.exp2Company, exp2Dates = data.exp2Dates, exp2Desc = data.exp2Desc,
        exp3Position = data.exp3Position, exp3Company = data.exp3Company, exp3Dates = data.exp3Dates, exp3Desc = data.exp3Desc,
        exp4Position = data.exp4Position, exp4Company = data.exp4Company, exp4Dates = data.exp4Dates, exp4Desc = data.exp4Desc,
        exp5Position = data.exp5Position, exp5Company = data.exp5Company, exp5Dates = data.exp5Dates, exp5Desc = data.exp5Desc,
        refName = data.refName, refPositionCompany = data.refPositionCompany, refPhone = data.refContact, refEmail = data.refEmail, refAvatarUri = data.refAvatarUri,
        ref2Name = data.ref2Name, ref2PositionCompany = data.ref2PositionCompany, ref2Phone = data.ref2Contact, ref2Email = data.ref2Email, ref2AvatarUri = data.ref2AvatarUri,
        onFieldChange = { field, value ->
            onFieldChange(
                when (field) {
                    "fullName" -> data.copy(fullName = value)
                    "professionalTitle" -> data.copy(professionalTitle = value)
                    "avatarUri" -> data.copy(avatarUri = value)
                    "phone" -> data.copy(phone = value)
                    "email" -> data.copy(email = value)
                    "location" -> data.copy(location = value)
                    "website" -> data.copy(website = value)
                    "linkedin" -> data.copy(linkedin = value)
                    "aboutMe" -> data.copy(aboutMe = value)
                    "edu1Degree" -> data.copy(edu1Degree = value)
                    "edu1School" -> data.copy(edu1School = value)
                    "edu1Years" -> data.copy(edu1Years = value)
                    "edu2Degree" -> data.copy(edu2Degree = value)
                    "edu2School" -> data.copy(edu2School = value)
                    "edu2Years" -> data.copy(edu2Years = value)
                    "skill1" -> data.copy(skill1 = value)
                    "skill2" -> data.copy(skill2 = value)
                    "skill3" -> data.copy(skill3 = value)
                    "skill4" -> data.copy(skill4 = value)
                    "skill5" -> data.copy(skill5 = value)
                    "skill6" -> data.copy(skill6 = value)
                    "exp1Position" -> data.copy(exp1Position = value)
                    "exp1Company" -> data.copy(exp1Company = value)
                    "exp1Dates" -> data.copy(exp1Dates = value)
                    "exp1Desc" -> data.copy(exp1Desc = value)
                    "exp2Position" -> data.copy(exp2Position = value)
                    "exp2Company" -> data.copy(exp2Company = value)
                    "exp2Dates" -> data.copy(exp2Dates = value)
                    "exp2Desc" -> data.copy(exp2Desc = value)
                    "exp3Position" -> data.copy(exp3Position = value)
                    "exp3Company" -> data.copy(exp3Company = value)
                    "exp3Dates" -> data.copy(exp3Dates = value)
                    "exp3Desc" -> data.copy(exp3Desc = value)
                    "exp4Position" -> data.copy(exp4Position = value)
                    "exp4Company" -> data.copy(exp4Company = value)
                    "exp4Dates" -> data.copy(exp4Dates = value)
                    "exp4Desc" -> data.copy(exp4Desc = value)
                    "exp5Position" -> data.copy(exp5Position = value)
                    "exp5Company" -> data.copy(exp5Company = value)
                    "exp5Dates" -> data.copy(exp5Dates = value)
                    "exp5Desc" -> data.copy(exp5Desc = value)
                    "refName" -> data.copy(refName = value)
                    "refPositionCompany" -> data.copy(refPositionCompany = value)
                    "refPhone" -> data.copy(refContact = value)
                    "refEmail" -> data.copy(refEmail = value)
                    "ref2Name" -> data.copy(ref2Name = value)
                    "ref2PositionCompany" -> data.copy(ref2PositionCompany = value)
                    "ref2Phone" -> data.copy(ref2Contact = value)
                    "ref2Email" -> data.copy(ref2Email = value)
                    "refAvatarUri" -> data.copy(refAvatarUri = value)
                    "ref2AvatarUri" -> data.copy(ref2AvatarUri = value)
                    else -> data
                }
            )
        },
        onHomeOverride = onHome
    )
}

@Composable
fun ResumeTemplate04Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit, onHome: () -> Unit = {}) {
    com.saltech.urdocs.ui.templates.ResumeTemplate04_PixelPerfect(
        userName = data.fullName,
        userTitle = data.professionalTitle,
        avatarUri = data.avatarUri,
        contactPhone = data.phone,
        contactEmail = data.email,
        contactAddress = data.location,
        contactWebsite = data.website,
        contactLinkedin = data.linkedin,
        aboutMe = data.aboutMe,
        edu1Degree = data.edu1Degree, edu1School = data.edu1School, edu1Years = data.edu1Years,
        edu2Degree = data.edu2Degree, edu2School = data.edu2School, edu2Years = data.edu2Years,
        skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
        exp1Position = data.exp1Position, exp1Company = data.exp1Company, exp1Dates = data.exp1Dates, exp1Desc = data.exp1Desc,
        exp2Position = data.exp2Position, exp2Company = data.exp2Company, exp2Dates = data.exp2Dates, exp2Desc = data.exp2Desc,
        exp3Position = data.exp3Position, exp3Company = data.exp3Company, exp3Dates = data.exp3Dates, exp3Desc = data.exp3Desc,
        exp4Position = data.exp4Position, exp4Company = data.exp4Company, exp4Dates = data.exp4Dates, exp4Desc = data.exp4Desc,
        exp5Position = data.exp5Position, exp5Company = data.exp5Company, exp5Dates = data.exp5Dates, exp5Desc = data.exp5Desc,
        refName = data.refName, refPositionCompany = data.refPositionCompany, refPhone = data.refContact, refEmail = data.refEmail, refAvatarUri = data.refAvatarUri,
        ref2Name = data.ref2Name, ref2PositionCompany = data.ref2PositionCompany, ref2Phone = data.ref2Contact, ref2Email = data.ref2Email, ref2AvatarUri = data.ref2AvatarUri,
        onFieldChange = { field, value ->
            onFieldChange(
                when (field) {
                    "fullName" -> data.copy(fullName = value)
                    "professionalTitle" -> data.copy(professionalTitle = value)
                    "avatarUri" -> data.copy(avatarUri = value)
                    "phone" -> data.copy(phone = value)
                    "email" -> data.copy(email = value)
                    "location" -> data.copy(location = value)
                    "website" -> data.copy(website = value)
                    "linkedin" -> data.copy(linkedin = value)
                    "aboutMe" -> data.copy(aboutMe = value)
                    "edu1Degree" -> data.copy(edu1Degree = value)
                    "edu1School" -> data.copy(edu1School = value)
                    "edu1Years" -> data.copy(edu1Years = value)
                    "edu2Degree" -> data.copy(edu2Degree = value)
                    "edu2School" -> data.copy(edu2School = value)
                    "edu2Years" -> data.copy(edu2Years = value)
                    "skill1" -> data.copy(skill1 = value)
                    "skill2" -> data.copy(skill2 = value)
                    "skill3" -> data.copy(skill3 = value)
                    "skill4" -> data.copy(skill4 = value)
                    "skill5" -> data.copy(skill5 = value)
                    "skill6" -> data.copy(skill6 = value)
                    "exp1Position" -> data.copy(exp1Position = value)
                    "exp1Company" -> data.copy(exp1Company = value)
                    "exp1Dates" -> data.copy(exp1Dates = value)
                    "exp1Desc" -> data.copy(exp1Desc = value)
                    "exp2Position" -> data.copy(exp2Position = value)
                    "exp2Company" -> data.copy(exp2Company = value)
                    "exp2Dates" -> data.copy(exp2Dates = value)
                    "exp2Desc" -> data.copy(exp2Desc = value)
                    "exp3Position" -> data.copy(exp3Position = value)
                    "exp3Company" -> data.copy(exp3Company = value)
                    "exp3Dates" -> data.copy(exp3Dates = value)
                    "exp3Desc" -> data.copy(exp3Desc = value)
                    "exp4Position" -> data.copy(exp4Position = value)
                    "exp4Company" -> data.copy(exp4Company = value)
                    "exp4Dates" -> data.copy(exp4Dates = value)
                    "exp4Desc" -> data.copy(exp4Desc = value)
                    "exp5Position" -> data.copy(exp5Position = value)
                    "exp5Company" -> data.copy(exp5Company = value)
                    "exp5Dates" -> data.copy(exp5Dates = value)
                    "exp5Desc" -> data.copy(exp5Desc = value)
                    "refName" -> data.copy(refName = value)
                    "refPositionCompany" -> data.copy(refPositionCompany = value)
                    "refPhone" -> data.copy(refContact = value)
                    "refEmail" -> data.copy(refEmail = value)
                    "ref2Name" -> data.copy(ref2Name = value)
                    "ref2PositionCompany" -> data.copy(ref2PositionCompany = value)
                    "ref2Phone" -> data.copy(ref2Contact = value)
                    "ref2Email" -> data.copy(ref2Email = value)
                    "refAvatarUri" -> data.copy(refAvatarUri = value)
                    "ref2AvatarUri" -> data.copy(ref2AvatarUri = value)
                    else -> data
                }
            )
        },
        onHomeOverride = onHome
    )
}

@Composable
fun ResumeTemplate05Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit, onHome: () -> Unit = {}) {
    com.saltech.urdocs.ui.templates.ResumeTemplate05_PixelPerfect(
        userName = data.fullName,
        userTitle = data.professionalTitle,
        avatarUri = data.avatarUri,
        contactPhone = data.phone,
        contactEmail = data.email,
        contactAddress = data.location,
        contactWebsite = data.website,
        contactLinkedin = data.linkedin,
        aboutMe = data.aboutMe,
        edu1Degree = data.edu1Degree, edu1School = data.edu1School, edu1Years = data.edu1Years,
        edu2Degree = data.edu2Degree, edu2School = data.edu2School, edu2Years = data.edu2Years,
        skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
        exp1Position = data.exp1Position, exp1Company = data.exp1Company, exp1Dates = data.exp1Dates, exp1Desc = data.exp1Desc,
        exp2Position = data.exp2Position, exp2Company = data.exp2Company, exp2Dates = data.exp2Dates, exp2Desc = data.exp2Desc,
        exp3Position = data.exp3Position, exp3Company = data.exp3Company, exp3Dates = data.exp3Dates, exp3Desc = data.exp3Desc,
        exp4Position = data.exp4Position, exp4Company = data.exp4Company, exp4Dates = data.exp4Dates, exp4Desc = data.exp4Desc,
        exp5Position = data.exp5Position, exp5Company = data.exp5Company, exp5Dates = data.exp5Dates, exp5Desc = data.exp5Desc,
        refName = data.refName, refPositionCompany = data.refPositionCompany, refPhone = data.refContact, refEmail = data.refEmail, refAvatarUri = data.refAvatarUri,
        ref2Name = data.ref2Name, ref2PositionCompany = data.ref2PositionCompany, ref2Phone = data.ref2Contact, ref2Email = data.ref2Email, ref2AvatarUri = data.ref2AvatarUri,
        onFieldChange = { field, value ->
            onFieldChange(
                when (field) {
                    "fullName" -> data.copy(fullName = value)
                    "professionalTitle" -> data.copy(professionalTitle = value)
                    "avatarUri" -> data.copy(avatarUri = value)
                    "phone" -> data.copy(phone = value)
                    "email" -> data.copy(email = value)
                    "location" -> data.copy(location = value)
                    "website" -> data.copy(website = value)
                    "linkedin" -> data.copy(linkedin = value)
                    "aboutMe" -> data.copy(aboutMe = value)
                    "edu1Degree" -> data.copy(edu1Degree = value)
                    "edu1School" -> data.copy(edu1School = value)
                    "edu1Years" -> data.copy(edu1Years = value)
                    "edu2Degree" -> data.copy(edu2Degree = value)
                    "edu2School" -> data.copy(edu2School = value)
                    "edu2Years" -> data.copy(edu2Years = value)
                    "skill1" -> data.copy(skill1 = value)
                    "skill2" -> data.copy(skill2 = value)
                    "skill3" -> data.copy(skill3 = value)
                    "skill4" -> data.copy(skill4 = value)
                    "skill5" -> data.copy(skill5 = value)
                    "skill6" -> data.copy(skill6 = value)
                    "exp1Position" -> data.copy(exp1Position = value)
                    "exp1Company" -> data.copy(exp1Company = value)
                    "exp1Dates" -> data.copy(exp1Dates = value)
                    "exp1Desc" -> data.copy(exp1Desc = value)
                    "exp2Position" -> data.copy(exp2Position = value)
                    "exp2Company" -> data.copy(exp2Company = value)
                    "exp2Dates" -> data.copy(exp2Dates = value)
                    "exp2Desc" -> data.copy(exp2Desc = value)
                    "exp3Position" -> data.copy(exp3Position = value)
                    "exp3Company" -> data.copy(exp3Company = value)
                    "exp3Dates" -> data.copy(exp3Dates = value)
                    "exp3Desc" -> data.copy(exp3Desc = value)
                    "exp4Position" -> data.copy(exp4Position = value)
                    "exp4Company" -> data.copy(exp4Company = value)
                    "exp4Dates" -> data.copy(exp4Dates = value)
                    "exp4Desc" -> data.copy(exp4Desc = value)
                    "exp5Position" -> data.copy(exp5Position = value)
                    "exp5Company" -> data.copy(exp5Company = value)
                    "exp5Dates" -> data.copy(exp5Dates = value)
                    "exp5Desc" -> data.copy(exp5Desc = value)
                    "refName" -> data.copy(refName = value)
                    "refPositionCompany" -> data.copy(refPositionCompany = value)
                    "refPhone" -> data.copy(refContact = value)
                    "refEmail" -> data.copy(refEmail = value)
                    "ref2Name" -> data.copy(ref2Name = value)
                    "ref2PositionCompany" -> data.copy(ref2PositionCompany = value)
                    "ref2Phone" -> data.copy(ref2Contact = value)
                    "ref2Email" -> data.copy(ref2Email = value)
                    "refAvatarUri" -> data.copy(refAvatarUri = value)
                    "ref2AvatarUri" -> data.copy(ref2AvatarUri = value)
                    else -> data
                }
            )
        },
        onHomeOverride = onHome
    )
}

@Composable
fun ResumeTemplate06Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit, onHome: () -> Unit = {}) {
    com.saltech.urdocs.ui.templates.ResumeTemplate06_PixelPerfect(
        userName = data.fullName,
        userTitle = data.professionalTitle,
        avatarUri = data.avatarUri,
        contactPhone = data.phone,
        contactEmail = data.email,
        contactAddress = data.location,
        contactWebsite = data.website,
        contactLinkedin = data.linkedin,
        aboutMe = data.aboutMe,
        edu1Degree = data.edu1Degree, edu1School = data.edu1School, edu1Years = data.edu1Years,
        edu2Degree = data.edu2Degree, edu2School = data.edu2School, edu2Years = data.edu2Years,
        skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
        exp1Position = data.exp1Position, exp1Company = data.exp1Company, exp1Dates = data.exp1Dates, exp1Desc = data.exp1Desc,
        exp2Position = data.exp2Position, exp2Company = data.exp2Company, exp2Dates = data.exp2Dates, exp2Desc = data.exp2Desc,
        exp3Position = data.exp3Position, exp3Company = data.exp3Company, exp3Dates = data.exp3Dates, exp3Desc = data.exp3Desc,
        exp4Position = data.exp4Position, exp4Company = data.exp4Company, exp4Dates = data.exp4Dates, exp4Desc = data.exp4Desc,
        exp5Position = data.exp5Position, exp5Company = data.exp5Company, exp5Dates = data.exp5Dates, exp5Desc = data.exp5Desc,
        refName = data.refName, refPositionCompany = data.refPositionCompany, refPhone = data.refContact, refEmail = data.refEmail, refAvatarUri = data.refAvatarUri,
        ref2Name = data.ref2Name, ref2PositionCompany = data.ref2PositionCompany, ref2Phone = data.ref2Contact, ref2Email = data.ref2Email, ref2AvatarUri = data.ref2AvatarUri,
        onFieldChange = { field, value ->
            onFieldChange(
                when (field) {
                    "fullName" -> data.copy(fullName = value)
                    "professionalTitle" -> data.copy(professionalTitle = value)
                    "avatarUri" -> data.copy(avatarUri = value)
                    "phone" -> data.copy(phone = value)
                    "email" -> data.copy(email = value)
                    "location" -> data.copy(location = value)
                    "website" -> data.copy(website = value)
                    "linkedin" -> data.copy(linkedin = value)
                    "aboutMe" -> data.copy(aboutMe = value)
                    "edu1Degree" -> data.copy(edu1Degree = value)
                    "edu1School" -> data.copy(edu1School = value)
                    "edu1Years" -> data.copy(edu1Years = value)
                    "edu2Degree" -> data.copy(edu2Degree = value)
                    "edu2School" -> data.copy(edu2School = value)
                    "edu2Years" -> data.copy(edu2Years = value)
                    "skill1" -> data.copy(skill1 = value)
                    "skill2" -> data.copy(skill2 = value)
                    "skill3" -> data.copy(skill3 = value)
                    "skill4" -> data.copy(skill4 = value)
                    "skill5" -> data.copy(skill5 = value)
                    "skill6" -> data.copy(skill6 = value)
                    "exp1Position" -> data.copy(exp1Position = value)
                    "exp1Company" -> data.copy(exp1Company = value)
                    "exp1Dates" -> data.copy(exp1Dates = value)
                    "exp1Desc" -> data.copy(exp1Desc = value)
                    "exp2Position" -> data.copy(exp2Position = value)
                    "exp2Company" -> data.copy(exp2Company = value)
                    "exp2Dates" -> data.copy(exp2Dates = value)
                    "exp2Desc" -> data.copy(exp2Desc = value)
                    "exp3Position" -> data.copy(exp3Position = value)
                    "exp3Company" -> data.copy(exp3Company = value)
                    "exp3Dates" -> data.copy(exp3Dates = value)
                    "exp3Desc" -> data.copy(exp3Desc = value)
                    "exp4Position" -> data.copy(exp4Position = value)
                    "exp4Company" -> data.copy(exp4Company = value)
                    "exp4Dates" -> data.copy(exp4Dates = value)
                    "exp4Desc" -> data.copy(exp4Desc = value)
                    "exp5Position" -> data.copy(exp5Position = value)
                    "exp5Company" -> data.copy(exp5Company = value)
                    "exp5Dates" -> data.copy(exp5Dates = value)
                    "exp5Desc" -> data.copy(exp5Desc = value)
                    "refName" -> data.copy(refName = value)
                    "refPositionCompany" -> data.copy(refPositionCompany = value)
                    "refPhone" -> data.copy(refContact = value)
                    "refEmail" -> data.copy(refEmail = value)
                    "ref2Name" -> data.copy(ref2Name = value)
                    "ref2PositionCompany" -> data.copy(ref2PositionCompany = value)
                    "ref2Phone" -> data.copy(ref2Contact = value)
                    "ref2Email" -> data.copy(ref2Email = value)
                    "refAvatarUri" -> data.copy(refAvatarUri = value)
                    "ref2AvatarUri" -> data.copy(ref2AvatarUri = value)
                    else -> data
                }
            )
        },
        onHomeOverride = onHome
    )
}

@Composable
fun ResumeTemplate07Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit, onHome: () -> Unit = {}) {
    com.saltech.urdocs.ui.templates.ResumeTemplate07_PixelPerfect(
        userName = data.fullName,
        userTitle = data.professionalTitle,
        avatarUri = data.avatarUri,
        contactPhone = data.phone,
        contactEmail = data.email,
        contactAddress = data.location,
        contactWebsite = data.website,
        contactLinkedin = data.linkedin,
        aboutMe = data.aboutMe,
        edu1Degree = data.edu1Degree, edu1School = data.edu1School, edu1Years = data.edu1Years,
        edu2Degree = data.edu2Degree, edu2School = data.edu2School, edu2Years = data.edu2Years,
        skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
        exp1Position = data.exp1Position, exp1Company = data.exp1Company, exp1Dates = data.exp1Dates, exp1Desc = data.exp1Desc,
        exp2Position = data.exp2Position, exp2Company = data.exp2Company, exp2Dates = data.exp2Dates, exp2Desc = data.exp2Desc,
        exp3Position = data.exp3Position, exp3Company = data.exp3Company, exp3Dates = data.exp3Dates, exp3Desc = data.exp3Desc,
        exp4Position = data.exp4Position, exp4Company = data.exp4Company, exp4Dates = data.exp4Dates, exp4Desc = data.exp4Desc,
        exp5Position = data.exp5Position, exp5Company = data.exp5Company, exp5Dates = data.exp5Dates, exp5Desc = data.exp5Desc,
        refName = data.refName, refPositionCompany = data.refPositionCompany, refPhone = data.refContact, refEmail = data.refEmail, refAvatarUri = data.refAvatarUri,
        ref2Name = data.ref2Name, ref2PositionCompany = data.ref2PositionCompany, ref2Phone = data.ref2Contact, ref2Email = data.ref2Email, ref2AvatarUri = data.ref2AvatarUri,
        onFieldChange = { field, value ->
            onFieldChange(
                when (field) {
                    "fullName" -> data.copy(fullName = value)
                    "professionalTitle" -> data.copy(professionalTitle = value)
                    "avatarUri" -> data.copy(avatarUri = value)
                    "phone" -> data.copy(phone = value)
                    "email" -> data.copy(email = value)
                    "location" -> data.copy(location = value)
                    "website" -> data.copy(website = value)
                    "linkedin" -> data.copy(linkedin = value)
                    "aboutMe" -> data.copy(aboutMe = value)
                    "edu1Degree" -> data.copy(edu1Degree = value)
                    "edu1School" -> data.copy(edu1School = value)
                    "edu1Years" -> data.copy(edu1Years = value)
                    "edu2Degree" -> data.copy(edu2Degree = value)
                    "edu2School" -> data.copy(edu2School = value)
                    "edu2Years" -> data.copy(edu2Years = value)
                    "skill1" -> data.copy(skill1 = value)
                    "skill2" -> data.copy(skill2 = value)
                    "skill3" -> data.copy(skill3 = value)
                    "skill4" -> data.copy(skill4 = value)
                    "skill5" -> data.copy(skill5 = value)
                    "skill6" -> data.copy(skill6 = value)
                    "exp1Position" -> data.copy(exp1Position = value)
                    "exp1Company" -> data.copy(exp1Company = value)
                    "exp1Dates" -> data.copy(exp1Dates = value)
                    "exp1Desc" -> data.copy(exp1Desc = value)
                    "exp2Position" -> data.copy(exp2Position = value)
                    "exp2Company" -> data.copy(exp2Company = value)
                    "exp2Dates" -> data.copy(exp2Dates = value)
                    "exp2Desc" -> data.copy(exp2Desc = value)
                    "exp3Position" -> data.copy(exp3Position = value)
                    "exp3Company" -> data.copy(exp3Company = value)
                    "exp3Dates" -> data.copy(exp3Dates = value)
                    "exp3Desc" -> data.copy(exp3Desc = value)
                    "exp4Position" -> data.copy(exp4Position = value)
                    "exp4Company" -> data.copy(exp4Company = value)
                    "exp4Dates" -> data.copy(exp4Dates = value)
                    "exp4Desc" -> data.copy(exp4Desc = value)
                    "exp5Position" -> data.copy(exp5Position = value)
                    "exp5Company" -> data.copy(exp5Company = value)
                    "exp5Dates" -> data.copy(exp5Dates = value)
                    "exp5Desc" -> data.copy(exp5Desc = value)
                    "refName" -> data.copy(refName = value)
                    "refPositionCompany" -> data.copy(refPositionCompany = value)
                    "refPhone" -> data.copy(refContact = value)
                    "refEmail" -> data.copy(refEmail = value)
                    "ref2Name" -> data.copy(ref2Name = value)
                    "ref2PositionCompany" -> data.copy(ref2PositionCompany = value)
                    "ref2Phone" -> data.copy(ref2Contact = value)
                    "ref2Email" -> data.copy(ref2Email = value)
                    "refAvatarUri" -> data.copy(refAvatarUri = value)
                    "ref2AvatarUri" -> data.copy(ref2AvatarUri = value)
                    else -> data
                }
            )
        },
        onHomeOverride = onHome
    )
}

@Composable
fun ResumeTemplate08Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit, onHome: () -> Unit = {}) {
    com.saltech.urdocs.ui.templates.ResumeTemplate08_PixelPerfect(
        userName = data.fullName,
        userTitle = data.professionalTitle,
        avatarUri = data.avatarUri,
        contactPhone = data.phone,
        contactEmail = data.email,
        contactAddress = data.location,
        contactWebsite = data.website,
        contactLinkedin = data.linkedin,
        aboutMe = data.aboutMe,
        edu1Degree = data.edu1Degree, edu1School = data.edu1School, edu1Years = data.edu1Years,
        edu2Degree = data.edu2Degree, edu2School = data.edu2School, edu2Years = data.edu2Years,
        skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
        exp1Position = data.exp1Position, exp1Company = data.exp1Company, exp1Dates = data.exp1Dates, exp1Desc = data.exp1Desc,
        exp2Position = data.exp2Position, exp2Company = data.exp2Company, exp2Dates = data.exp2Dates, exp2Desc = data.exp2Desc,
        exp3Position = data.exp3Position, exp3Company = data.exp3Company, exp3Dates = data.exp3Dates, exp3Desc = data.exp3Desc,
        exp4Position = data.exp4Position, exp4Company = data.exp4Company, exp4Dates = data.exp4Dates, exp4Desc = data.exp4Desc,
        exp5Position = data.exp5Position, exp5Company = data.exp5Company, exp5Dates = data.exp5Dates, exp5Desc = data.exp5Desc,
        refName = data.refName, refPositionCompany = data.refPositionCompany, refPhone = data.refContact, refEmail = data.refEmail, refAvatarUri = data.refAvatarUri,
        ref2Name = data.ref2Name, ref2PositionCompany = data.ref2PositionCompany, ref2Phone = data.ref2Contact, ref2Email = data.ref2Email, ref2AvatarUri = data.ref2AvatarUri,
        onFieldChange = { field, value ->
            onFieldChange(
                when (field) {
                    "fullName" -> data.copy(fullName = value)
                    "professionalTitle" -> data.copy(professionalTitle = value)
                    "avatarUri" -> data.copy(avatarUri = value)
                    "phone" -> data.copy(phone = value)
                    "email" -> data.copy(email = value)
                    "location" -> data.copy(location = value)
                    "website" -> data.copy(website = value)
                    "linkedin" -> data.copy(linkedin = value)
                    "aboutMe" -> data.copy(aboutMe = value)
                    "edu1Degree" -> data.copy(edu1Degree = value)
                    "edu1School" -> data.copy(edu1School = value)
                    "edu1Years" -> data.copy(edu1Years = value)
                    "edu2Degree" -> data.copy(edu2Degree = value)
                    "edu2School" -> data.copy(edu2School = value)
                    "edu2Years" -> data.copy(edu2Years = value)
                    "skill1" -> data.copy(skill1 = value)
                    "skill2" -> data.copy(skill2 = value)
                    "skill3" -> data.copy(skill3 = value)
                    "skill4" -> data.copy(skill4 = value)
                    "skill5" -> data.copy(skill5 = value)
                    "skill6" -> data.copy(skill6 = value)
                    "exp1Position" -> data.copy(exp1Position = value)
                    "exp1Company" -> data.copy(exp1Company = value)
                    "exp1Dates" -> data.copy(exp1Dates = value)
                    "exp1Desc" -> data.copy(exp1Desc = value)
                    "exp2Position" -> data.copy(exp2Position = value)
                    "exp2Company" -> data.copy(exp2Company = value)
                    "exp2Dates" -> data.copy(exp2Dates = value)
                    "exp2Desc" -> data.copy(exp2Desc = value)
                    "exp3Position" -> data.copy(exp3Position = value)
                    "exp3Company" -> data.copy(exp3Company = value)
                    "exp3Dates" -> data.copy(exp3Dates = value)
                    "exp3Desc" -> data.copy(exp3Desc = value)
                    "exp4Position" -> data.copy(exp4Position = value)
                    "exp4Company" -> data.copy(exp4Company = value)
                    "exp4Dates" -> data.copy(exp4Dates = value)
                    "exp4Desc" -> data.copy(exp4Desc = value)
                    "exp5Position" -> data.copy(exp5Position = value)
                    "exp5Company" -> data.copy(exp5Company = value)
                    "exp5Dates" -> data.copy(exp5Dates = value)
                    "exp5Desc" -> data.copy(exp5Desc = value)
                    "refName" -> data.copy(refName = value)
                    "refPositionCompany" -> data.copy(refPositionCompany = value)
                    "refPhone" -> data.copy(refContact = value)
                    "refEmail" -> data.copy(refEmail = value)
                    "ref2Name" -> data.copy(ref2Name = value)
                    "ref2PositionCompany" -> data.copy(ref2PositionCompany = value)
                    "ref2Phone" -> data.copy(ref2Contact = value)
                    "ref2Email" -> data.copy(ref2Email = value)
                    "refAvatarUri" -> data.copy(refAvatarUri = value)
                    "ref2AvatarUri" -> data.copy(ref2AvatarUri = value)
                    else -> data
                }
            )
        },
        onHomeOverride = onHome
    )
}

@Composable
fun ResumeTemplate09Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit, onHome: () -> Unit = {}) {
    com.saltech.urdocs.ui.templates.ResumeTemplate09_PixelPerfect(
        userName = data.fullName,
        userTitle = data.professionalTitle,
        avatarUri = data.avatarUri,
        contactPhone = data.phone,
        contactEmail = data.email,
        contactAddress = data.location,
        contactWebsite = data.website,
        contactLinkedin = data.linkedin,
        aboutMe = data.aboutMe,
        edu1Degree = data.edu1Degree, edu1School = data.edu1School, edu1Years = data.edu1Years,
        edu2Degree = data.edu2Degree, edu2School = data.edu2School, edu2Years = data.edu2Years,
        skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
        exp1Position = data.exp1Position, exp1Company = data.exp1Company, exp1Dates = data.exp1Dates, exp1Desc = data.exp1Desc,
        exp2Position = data.exp2Position, exp2Company = data.exp2Company, exp2Dates = data.exp2Dates, exp2Desc = data.exp2Desc,
        exp3Position = data.exp3Position, exp3Company = data.exp3Company, exp3Dates = data.exp3Dates, exp3Desc = data.exp3Desc,
        exp4Position = data.exp4Position, exp4Company = data.exp4Company, exp4Dates = data.exp4Dates, exp4Desc = data.exp4Desc,
        exp5Position = data.exp5Position, exp5Company = data.exp5Company, exp5Dates = data.exp5Dates, exp5Desc = data.exp5Desc,
        refName = data.refName, refPositionCompany = data.refPositionCompany, refPhone = data.refContact, refEmail = data.refEmail, refAvatarUri = data.refAvatarUri,
        ref2Name = data.ref2Name, ref2PositionCompany = data.ref2PositionCompany, ref2Phone = data.ref2Contact, ref2Email = data.ref2Email, ref2AvatarUri = data.ref2AvatarUri,
        onFieldChange = { field, value ->
            onFieldChange(
                when (field) {
                    "fullName" -> data.copy(fullName = value)
                    "professionalTitle" -> data.copy(professionalTitle = value)
                    "avatarUri" -> data.copy(avatarUri = value)
                    "phone" -> data.copy(phone = value)
                    "email" -> data.copy(email = value)
                    "location" -> data.copy(location = value)
                    "website" -> data.copy(website = value)
                    "linkedin" -> data.copy(linkedin = value)
                    "aboutMe" -> data.copy(aboutMe = value)
                    "edu1Degree" -> data.copy(edu1Degree = value)
                    "edu1School" -> data.copy(edu1School = value)
                    "edu1Years" -> data.copy(edu1Years = value)
                    "edu2Degree" -> data.copy(edu2Degree = value)
                    "edu2School" -> data.copy(edu2School = value)
                    "edu2Years" -> data.copy(edu2Years = value)
                    "skill1" -> data.copy(skill1 = value)
                    "skill2" -> data.copy(skill2 = value)
                    "skill3" -> data.copy(skill3 = value)
                    "skill4" -> data.copy(skill4 = value)
                    "skill5" -> data.copy(skill5 = value)
                    "skill6" -> data.copy(skill6 = value)
                    "exp1Position" -> data.copy(exp1Position = value)
                    "exp1Company" -> data.copy(exp1Company = value)
                    "exp1Dates" -> data.copy(exp1Dates = value)
                    "exp1Desc" -> data.copy(exp1Desc = value)
                    "exp2Position" -> data.copy(exp2Position = value)
                    "exp2Company" -> data.copy(exp2Company = value)
                    "exp2Dates" -> data.copy(exp2Dates = value)
                    "exp2Desc" -> data.copy(exp2Desc = value)
                    "exp3Position" -> data.copy(exp3Position = value)
                    "exp3Company" -> data.copy(exp3Company = value)
                    "exp3Dates" -> data.copy(exp3Dates = value)
                    "exp3Desc" -> data.copy(exp3Desc = value)
                    "exp4Position" -> data.copy(exp4Position = value)
                    "exp4Company" -> data.copy(exp4Company = value)
                    "exp4Dates" -> data.copy(exp4Dates = value)
                    "exp4Desc" -> data.copy(exp4Desc = value)
                    "exp5Position" -> data.copy(exp5Position = value)
                    "exp5Company" -> data.copy(exp5Company = value)
                    "exp5Dates" -> data.copy(exp5Dates = value)
                    "exp5Desc" -> data.copy(exp5Desc = value)
                    "refName" -> data.copy(refName = value)
                    "refPositionCompany" -> data.copy(refPositionCompany = value)
                    "refPhone" -> data.copy(refContact = value)
                    "refEmail" -> data.copy(refEmail = value)
                    "ref2Name" -> data.copy(ref2Name = value)
                    "ref2PositionCompany" -> data.copy(ref2PositionCompany = value)
                    "ref2Phone" -> data.copy(ref2Contact = value)
                    "ref2Email" -> data.copy(ref2Email = value)
                    "refAvatarUri" -> data.copy(refAvatarUri = value)
                    "ref2AvatarUri" -> data.copy(ref2AvatarUri = value)
                    else -> data
                }
            )
        },
        onHomeOverride = onHome
    )
}

@Composable
fun ResumeTemplate10Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit, onHome: () -> Unit = {}) {
    com.saltech.urdocs.ui.templates.ResumeTemplate10_PixelPerfect(
        userName = data.fullName,
        userTitle = data.professionalTitle,
        avatarUri = data.avatarUri,
        contactPhone = data.phone,
        contactEmail = data.email,
        contactAddress = data.location,
        contactWebsite = data.website,
        contactLinkedin = data.linkedin,
        aboutMe = data.aboutMe,
        edu1Degree = data.edu1Degree, edu1School = data.edu1School, edu1Years = data.edu1Years,
        edu2Degree = data.edu2Degree, edu2School = data.edu2School, edu2Years = data.edu2Years,
        skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
        exp1Position = data.exp1Position, exp1Company = data.exp1Company, exp1Dates = data.exp1Dates, exp1Desc = data.exp1Desc,
        exp2Position = data.exp2Position, exp2Company = data.exp2Company, exp2Dates = data.exp2Dates, exp2Desc = data.exp2Desc,
        exp3Position = data.exp3Position, exp3Company = data.exp3Company, exp3Dates = data.exp3Dates, exp3Desc = data.exp3Desc,
        exp4Position = data.exp4Position, exp4Company = data.exp4Company, exp4Dates = data.exp4Dates, exp4Desc = data.exp4Desc,
        exp5Position = data.exp5Position, exp5Company = data.exp5Company, exp5Dates = data.exp5Dates, exp5Desc = data.exp5Desc,
        refName = data.refName, refPositionCompany = data.refPositionCompany, refPhone = data.refContact, refEmail = data.refEmail, refAvatarUri = data.refAvatarUri,
        ref2Name = data.ref2Name, ref2PositionCompany = data.ref2PositionCompany, ref2Phone = data.ref2Contact, ref2Email = data.ref2Email, ref2AvatarUri = data.ref2AvatarUri,
        onFieldChange = { field, value ->
            onFieldChange(
                when (field) {
                    "fullName" -> data.copy(fullName = value)
                    "professionalTitle" -> data.copy(professionalTitle = value)
                    "avatarUri" -> data.copy(avatarUri = value)
                    "phone" -> data.copy(phone = value)
                    "email" -> data.copy(email = value)
                    "location" -> data.copy(location = value)
                    "website" -> data.copy(website = value)
                    "linkedin" -> data.copy(linkedin = value)
                    "aboutMe" -> data.copy(aboutMe = value)
                    "edu1Degree" -> data.copy(edu1Degree = value)
                    "edu1School" -> data.copy(edu1School = value)
                    "edu1Years" -> data.copy(edu1Years = value)
                    "edu2Degree" -> data.copy(edu2Degree = value)
                    "edu2School" -> data.copy(edu2School = value)
                    "edu2Years" -> data.copy(edu2Years = value)
                    "skill1" -> data.copy(skill1 = value)
                    "skill2" -> data.copy(skill2 = value)
                    "skill3" -> data.copy(skill3 = value)
                    "skill4" -> data.copy(skill4 = value)
                    "skill5" -> data.copy(skill5 = value)
                    "skill6" -> data.copy(skill6 = value)
                    "exp1Position" -> data.copy(exp1Position = value)
                    "exp1Company" -> data.copy(exp1Company = value)
                    "exp1Dates" -> data.copy(exp1Dates = value)
                    "exp1Desc" -> data.copy(exp1Desc = value)
                    "exp2Position" -> data.copy(exp2Position = value)
                    "exp2Company" -> data.copy(exp2Company = value)
                    "exp2Dates" -> data.copy(exp2Dates = value)
                    "exp2Desc" -> data.copy(exp2Desc = value)
                    "exp3Position" -> data.copy(exp3Position = value)
                    "exp3Company" -> data.copy(exp3Company = value)
                    "exp3Dates" -> data.copy(exp3Dates = value)
                    "exp3Desc" -> data.copy(exp3Desc = value)
                    "exp4Position" -> data.copy(exp4Position = value)
                    "exp4Company" -> data.copy(exp4Company = value)
                    "exp4Dates" -> data.copy(exp4Dates = value)
                    "exp4Desc" -> data.copy(exp4Desc = value)
                    "exp5Position" -> data.copy(exp5Position = value)
                    "exp5Company" -> data.copy(exp5Company = value)
                    "exp5Dates" -> data.copy(exp5Dates = value)
                    "exp5Desc" -> data.copy(exp5Desc = value)
                    "refName" -> data.copy(refName = value)
                    "refPositionCompany" -> data.copy(refPositionCompany = value)
                    "refPhone" -> data.copy(refContact = value)
                    "refEmail" -> data.copy(refEmail = value)
                    "ref2Name" -> data.copy(ref2Name = value)
                    "ref2PositionCompany" -> data.copy(ref2PositionCompany = value)
                    "ref2Phone" -> data.copy(ref2Contact = value)
                    "ref2Email" -> data.copy(ref2Email = value)
                    "refAvatarUri" -> data.copy(refAvatarUri = value)
                    "ref2AvatarUri" -> data.copy(ref2AvatarUri = value)
                    else -> data
                }
            )
        },
        onHomeOverride = onHome
    )
}

@Composable
fun ResumeTemplate11Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit, onHome: () -> Unit = {}) {
    com.saltech.urdocs.ui.templates.ResumeTemplate11_PixelPerfect(
        userName = data.fullName,
        userTitle = data.professionalTitle,
        avatarUri = data.avatarUri,
        contactPhone = data.phone,
        contactEmail = data.email,
        contactAddress = data.location,
        contactWebsite = data.website,
        contactLinkedin = data.linkedin,
        aboutMe = data.aboutMe,
        edu1Degree = data.edu1Degree, edu1School = data.edu1School, edu1Years = data.edu1Years,
        edu2Degree = data.edu2Degree, edu2School = data.edu2School, edu2Years = data.edu2Years,
        skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
        exp1Position = data.exp1Position, exp1Company = data.exp1Company, exp1Dates = data.exp1Dates, exp1Desc = data.exp1Desc,
        exp2Position = data.exp2Position, exp2Company = data.exp2Company, exp2Dates = data.exp2Dates, exp2Desc = data.exp2Desc,
        exp3Position = data.exp3Position, exp3Company = data.exp3Company, exp3Dates = data.exp3Dates, exp3Desc = data.exp3Desc,
        exp4Position = data.exp4Position, exp4Company = data.exp4Company, exp4Dates = data.exp4Dates, exp4Desc = data.exp4Desc,
        exp5Position = data.exp5Position, exp5Company = data.exp5Company, exp5Dates = data.exp5Dates, exp5Desc = data.exp5Desc,
        refName = data.refName, refPositionCompany = data.refPositionCompany, refPhone = data.refContact, refEmail = data.refEmail, refAvatarUri = data.refAvatarUri,
        ref2Name = data.ref2Name, ref2PositionCompany = data.ref2PositionCompany, ref2Phone = data.ref2Contact, ref2Email = data.ref2Email, ref2AvatarUri = data.ref2AvatarUri,
        onFieldChange = { field, value ->
            onFieldChange(
                when (field) {
                    "fullName" -> data.copy(fullName = value)
                    "professionalTitle" -> data.copy(professionalTitle = value)
                    "avatarUri" -> data.copy(avatarUri = value)
                    "phone" -> data.copy(phone = value)
                    "email" -> data.copy(email = value)
                    "location" -> data.copy(location = value)
                    "website" -> data.copy(website = value)
                    "linkedin" -> data.copy(linkedin = value)
                    "aboutMe" -> data.copy(aboutMe = value)
                    "edu1Degree" -> data.copy(edu1Degree = value)
                    "edu1School" -> data.copy(edu1School = value)
                    "edu1Years" -> data.copy(edu1Years = value)
                    "edu2Degree" -> data.copy(edu2Degree = value)
                    "edu2School" -> data.copy(edu2School = value)
                    "edu2Years" -> data.copy(edu2Years = value)
                    "skill1" -> data.copy(skill1 = value)
                    "skill2" -> data.copy(skill2 = value)
                    "skill3" -> data.copy(skill3 = value)
                    "skill4" -> data.copy(skill4 = value)
                    "skill5" -> data.copy(skill5 = value)
                    "skill6" -> data.copy(skill6 = value)
                    "exp1Position" -> data.copy(exp1Position = value)
                    "exp1Company" -> data.copy(exp1Company = value)
                    "exp1Dates" -> data.copy(exp1Dates = value)
                    "exp1Desc" -> data.copy(exp1Desc = value)
                    "exp2Position" -> data.copy(exp2Position = value)
                    "exp2Company" -> data.copy(exp2Company = value)
                    "exp2Dates" -> data.copy(exp2Dates = value)
                    "exp2Desc" -> data.copy(exp2Desc = value)
                    "exp3Position" -> data.copy(exp3Position = value)
                    "exp3Company" -> data.copy(exp3Company = value)
                    "exp3Dates" -> data.copy(exp3Dates = value)
                    "exp3Desc" -> data.copy(exp3Desc = value)
                    "exp4Position" -> data.copy(exp4Position = value)
                    "exp4Company" -> data.copy(exp4Company = value)
                    "exp4Dates" -> data.copy(exp4Dates = value)
                    "exp4Desc" -> data.copy(exp4Desc = value)
                    "exp5Position" -> data.copy(exp5Position = value)
                    "exp5Company" -> data.copy(exp5Company = value)
                    "exp5Dates" -> data.copy(exp5Dates = value)
                    "exp5Desc" -> data.copy(exp5Desc = value)
                    "refName" -> data.copy(refName = value)
                    "refPositionCompany" -> data.copy(refPositionCompany = value)
                    "refPhone" -> data.copy(refContact = value)
                    "refEmail" -> data.copy(refEmail = value)
                    "ref2Name" -> data.copy(ref2Name = value)
                    "ref2PositionCompany" -> data.copy(ref2PositionCompany = value)
                    "ref2Phone" -> data.copy(ref2Contact = value)
                    "ref2Email" -> data.copy(ref2Email = value)
                    "refAvatarUri" -> data.copy(refAvatarUri = value)
                    "ref2AvatarUri" -> data.copy(ref2AvatarUri = value)
                    else -> data
                }
            )
        },
        onHomeOverride = onHome
    )
}

@Composable
fun ResumeTemplate12Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit, onHome: () -> Unit = {}) {
    com.saltech.urdocs.ui.templates.ResumeTemplate12_PixelPerfect(
        userName = data.fullName,
        userTitle = data.professionalTitle,
        avatarUri = data.avatarUri,
        contactPhone = data.phone,
        contactEmail = data.email,
        contactAddress = data.location,
        contactWebsite = data.website,
        contactLinkedin = data.linkedin,
        aboutMe = data.aboutMe,
        edu1Degree = data.edu1Degree, edu1School = data.edu1School, edu1Years = data.edu1Years,
        edu2Degree = data.edu2Degree, edu2School = data.edu2School, edu2Years = data.edu2Years,
        skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
        exp1Position = data.exp1Position, exp1Company = data.exp1Company, exp1Dates = data.exp1Dates, exp1Desc = data.exp1Desc,
        exp2Position = data.exp2Position, exp2Company = data.exp2Company, exp2Dates = data.exp2Dates, exp2Desc = data.exp2Desc,
        exp3Position = data.exp3Position, exp3Company = data.exp3Company, exp3Dates = data.exp3Dates, exp3Desc = data.exp3Desc,
        exp4Position = data.exp4Position, exp4Company = data.exp4Company, exp4Dates = data.exp4Dates, exp4Desc = data.exp4Desc,
        exp5Position = data.exp5Position, exp5Company = data.exp5Company, exp5Dates = data.exp5Dates, exp5Desc = data.exp5Desc,
        refName = data.refName, refPositionCompany = data.refPositionCompany, refPhone = data.refContact, refEmail = data.refEmail, refAvatarUri = data.refAvatarUri,
        ref2Name = data.ref2Name, ref2PositionCompany = data.ref2PositionCompany, ref2Phone = data.ref2Contact, ref2Email = data.ref2Email, ref2AvatarUri = data.ref2AvatarUri,
        onFieldChange = { field, value ->
            onFieldChange(
                when (field) {
                    "fullName" -> data.copy(fullName = value)
                    "professionalTitle" -> data.copy(professionalTitle = value)
                    "avatarUri" -> data.copy(avatarUri = value)
                    "phone" -> data.copy(phone = value)
                    "email" -> data.copy(email = value)
                    "location" -> data.copy(location = value)
                    "website" -> data.copy(website = value)
                    "linkedin" -> data.copy(linkedin = value)
                    "aboutMe" -> data.copy(aboutMe = value)
                    "edu1Degree" -> data.copy(edu1Degree = value)
                    "edu1School" -> data.copy(edu1School = value)
                    "edu1Years" -> data.copy(edu1Years = value)
                    "edu2Degree" -> data.copy(edu2Degree = value)
                    "edu2School" -> data.copy(edu2School = value)
                    "edu2Years" -> data.copy(edu2Years = value)
                    "skill1" -> data.copy(skill1 = value)
                    "skill2" -> data.copy(skill2 = value)
                    "skill3" -> data.copy(skill3 = value)
                    "skill4" -> data.copy(skill4 = value)
                    "skill5" -> data.copy(skill5 = value)
                    "skill6" -> data.copy(skill6 = value)
                    "exp1Position" -> data.copy(exp1Position = value)
                    "exp1Company" -> data.copy(exp1Company = value)
                    "exp1Dates" -> data.copy(exp1Dates = value)
                    "exp1Desc" -> data.copy(exp1Desc = value)
                    "exp2Position" -> data.copy(exp2Position = value)
                    "exp2Company" -> data.copy(exp2Company = value)
                    "exp2Dates" -> data.copy(exp2Dates = value)
                    "exp2Desc" -> data.copy(exp2Desc = value)
                    "exp3Position" -> data.copy(exp3Position = value)
                    "exp3Company" -> data.copy(exp3Company = value)
                    "exp3Dates" -> data.copy(exp3Dates = value)
                    "exp3Desc" -> data.copy(exp3Desc = value)
                    "exp4Position" -> data.copy(exp4Position = value)
                    "exp4Company" -> data.copy(exp4Company = value)
                    "exp4Dates" -> data.copy(exp4Dates = value)
                    "exp4Desc" -> data.copy(exp4Desc = value)
                    "exp5Position" -> data.copy(exp5Position = value)
                    "exp5Company" -> data.copy(exp5Company = value)
                    "exp5Dates" -> data.copy(exp5Dates = value)
                    "exp5Desc" -> data.copy(exp5Desc = value)
                    "refName" -> data.copy(refName = value)
                    "refPositionCompany" -> data.copy(refPositionCompany = value)
                    "refPhone" -> data.copy(refContact = value)
                    "refEmail" -> data.copy(refEmail = value)
                    "ref2Name" -> data.copy(ref2Name = value)
                    "ref2PositionCompany" -> data.copy(ref2PositionCompany = value)
                    "ref2Phone" -> data.copy(ref2Contact = value)
                    "ref2Email" -> data.copy(ref2Email = value)
                    "refAvatarUri" -> data.copy(refAvatarUri = value)
                    "ref2AvatarUri" -> data.copy(ref2AvatarUri = value)
                    else -> data
                }
            )
        },
        onHomeOverride = onHome
    )
}

@Composable
fun ResumeTemplate13Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit, onHome: () -> Unit = {}) {
    com.saltech.urdocs.ui.templates.ResumeTemplate13_PixelPerfect(
        userName = data.fullName,
        userTitle = data.professionalTitle,
        avatarUri = data.avatarUri,
        contactPhone = data.phone,
        contactEmail = data.email,
        contactAddress = data.location,
        contactWebsite = data.website,
        contactLinkedin = data.linkedin,
        aboutMe = data.aboutMe,
        edu1Degree = data.edu1Degree, edu1School = data.edu1School, edu1Years = data.edu1Years,
        edu2Degree = data.edu2Degree, edu2School = data.edu2School, edu2Years = data.edu2Years,
        skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
        exp1Position = data.exp1Position, exp1Company = data.exp1Company, exp1Dates = data.exp1Dates, exp1Desc = data.exp1Desc,
        exp2Position = data.exp2Position, exp2Company = data.exp2Company, exp2Dates = data.exp2Dates, exp2Desc = data.exp2Desc,
        exp3Position = data.exp3Position, exp3Company = data.exp3Company, exp3Dates = data.exp3Dates, exp3Desc = data.exp3Desc,
        exp4Position = data.exp4Position, exp4Company = data.exp4Company, exp4Dates = data.exp4Dates, exp4Desc = data.exp4Desc,
        exp5Position = data.exp5Position, exp5Company = data.exp5Company, exp5Dates = data.exp5Dates, exp5Desc = data.exp5Desc,
        refName = data.refName, refPositionCompany = data.refPositionCompany, refPhone = data.refContact, refEmail = data.refEmail, refAvatarUri = data.refAvatarUri,
        ref2Name = data.ref2Name, ref2PositionCompany = data.ref2PositionCompany, ref2Phone = data.ref2Contact, ref2Email = data.ref2Email, ref2AvatarUri = data.ref2AvatarUri,
        onFieldChange = { field, value ->
            onFieldChange(
                when (field) {
                    "fullName" -> data.copy(fullName = value)
                    "professionalTitle" -> data.copy(professionalTitle = value)
                    "avatarUri" -> data.copy(avatarUri = value)
                    "phone" -> data.copy(phone = value)
                    "email" -> data.copy(email = value)
                    "location" -> data.copy(location = value)
                    "website" -> data.copy(website = value)
                    "linkedin" -> data.copy(linkedin = value)
                    "aboutMe" -> data.copy(aboutMe = value)
                    "edu1Degree" -> data.copy(edu1Degree = value)
                    "edu1School" -> data.copy(edu1School = value)
                    "edu1Years" -> data.copy(edu1Years = value)
                    "edu2Degree" -> data.copy(edu2Degree = value)
                    "edu2School" -> data.copy(edu2School = value)
                    "edu2Years" -> data.copy(edu2Years = value)
                    "skill1" -> data.copy(skill1 = value)
                    "skill2" -> data.copy(skill2 = value)
                    "skill3" -> data.copy(skill3 = value)
                    "skill4" -> data.copy(skill4 = value)
                    "skill5" -> data.copy(skill5 = value)
                    "skill6" -> data.copy(skill6 = value)
                    "exp1Position" -> data.copy(exp1Position = value)
                    "exp1Company" -> data.copy(exp1Company = value)
                    "exp1Dates" -> data.copy(exp1Dates = value)
                    "exp1Desc" -> data.copy(exp1Desc = value)
                    "exp2Position" -> data.copy(exp2Position = value)
                    "exp2Company" -> data.copy(exp2Company = value)
                    "exp2Dates" -> data.copy(exp2Dates = value)
                    "exp2Desc" -> data.copy(exp2Desc = value)
                    "exp3Position" -> data.copy(exp3Position = value)
                    "exp3Company" -> data.copy(exp3Company = value)
                    "exp3Dates" -> data.copy(exp3Dates = value)
                    "exp3Desc" -> data.copy(exp3Desc = value)
                    "exp4Position" -> data.copy(exp4Position = value)
                    "exp4Company" -> data.copy(exp4Company = value)
                    "exp4Dates" -> data.copy(exp4Dates = value)
                    "exp4Desc" -> data.copy(exp4Desc = value)
                    "exp5Position" -> data.copy(exp5Position = value)
                    "exp5Company" -> data.copy(exp5Company = value)
                    "exp5Dates" -> data.copy(exp5Dates = value)
                    "exp5Desc" -> data.copy(exp5Desc = value)
                    "refName" -> data.copy(refName = value)
                    "refPositionCompany" -> data.copy(refPositionCompany = value)
                    "refPhone" -> data.copy(refContact = value)
                    "refEmail" -> data.copy(refEmail = value)
                    "ref2Name" -> data.copy(ref2Name = value)
                    "ref2PositionCompany" -> data.copy(ref2PositionCompany = value)
                    "ref2Phone" -> data.copy(ref2Contact = value)
                    "ref2Email" -> data.copy(ref2Email = value)
                    "refAvatarUri" -> data.copy(refAvatarUri = value)
                    "ref2AvatarUri" -> data.copy(ref2AvatarUri = value)
                    else -> data
                }
            )
        },
        onHomeOverride = onHome
    )
}

@Composable
fun ResumeTemplate14Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit, onHome: () -> Unit = {}) {
    com.saltech.urdocs.ui.templates.ResumeTemplate14_PixelPerfect(
        userName = data.fullName,
        userTitle = data.professionalTitle,
        avatarUri = data.avatarUri,
        contactPhone = data.phone,
        contactEmail = data.email,
        contactAddress = data.location,
        contactWebsite = data.website,
        contactLinkedin = data.linkedin,
        aboutMe = data.aboutMe,
        edu1Degree = data.edu1Degree, edu1School = data.edu1School, edu1Years = data.edu1Years,
        edu2Degree = data.edu2Degree, edu2School = data.edu2School, edu2Years = data.edu2Years,
        skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
        exp1Position = data.exp1Position, exp1Company = data.exp1Company, exp1Dates = data.exp1Dates, exp1Desc = data.exp1Desc,
        exp2Position = data.exp2Position, exp2Company = data.exp2Company, exp2Dates = data.exp2Dates, exp2Desc = data.exp2Desc,
        exp3Position = data.exp3Position, exp3Company = data.exp3Company, exp3Dates = data.exp3Dates, exp3Desc = data.exp3Desc,
        exp4Position = data.exp4Position, exp4Company = data.exp4Company, exp4Dates = data.exp4Dates, exp4Desc = data.exp4Desc,
        exp5Position = data.exp5Position, exp5Company = data.exp5Company, exp5Dates = data.exp5Dates, exp5Desc = data.exp5Desc,
        refName = data.refName, refPositionCompany = data.refPositionCompany, refPhone = data.refContact, refEmail = data.refEmail, refAvatarUri = data.refAvatarUri,
        ref2Name = data.ref2Name, ref2PositionCompany = data.ref2PositionCompany, ref2Phone = data.ref2Contact, ref2Email = data.ref2Email, ref2AvatarUri = data.ref2AvatarUri,
        onFieldChange = { field, value ->
            onFieldChange(
                when (field) {
                    "fullName" -> data.copy(fullName = value)
                    "professionalTitle" -> data.copy(professionalTitle = value)
                    "avatarUri" -> data.copy(avatarUri = value)
                    "phone" -> data.copy(phone = value)
                    "email" -> data.copy(email = value)
                    "location" -> data.copy(location = value)
                    "website" -> data.copy(website = value)
                    "linkedin" -> data.copy(linkedin = value)
                    "aboutMe" -> data.copy(aboutMe = value)
                    "edu1Degree" -> data.copy(edu1Degree = value)
                    "edu1School" -> data.copy(edu1School = value)
                    "edu1Years" -> data.copy(edu1Years = value)
                    "edu2Degree" -> data.copy(edu2Degree = value)
                    "edu2School" -> data.copy(edu2School = value)
                    "edu2Years" -> data.copy(edu2Years = value)
                    "skill1" -> data.copy(skill1 = value)
                    "skill2" -> data.copy(skill2 = value)
                    "skill3" -> data.copy(skill3 = value)
                    "skill4" -> data.copy(skill4 = value)
                    "skill5" -> data.copy(skill5 = value)
                    "skill6" -> data.copy(skill6 = value)
                    "exp1Position" -> data.copy(exp1Position = value)
                    "exp1Company" -> data.copy(exp1Company = value)
                    "exp1Dates" -> data.copy(exp1Dates = value)
                    "exp1Desc" -> data.copy(exp1Desc = value)
                    "exp2Position" -> data.copy(exp2Position = value)
                    "exp2Company" -> data.copy(exp2Company = value)
                    "exp2Dates" -> data.copy(exp2Dates = value)
                    "exp2Desc" -> data.copy(exp2Desc = value)
                    "exp3Position" -> data.copy(exp3Position = value)
                    "exp3Company" -> data.copy(exp3Company = value)
                    "exp3Dates" -> data.copy(exp3Dates = value)
                    "exp3Desc" -> data.copy(exp3Desc = value)
                    "exp4Position" -> data.copy(exp4Position = value)
                    "exp4Company" -> data.copy(exp4Company = value)
                    "exp4Dates" -> data.copy(exp4Dates = value)
                    "exp4Desc" -> data.copy(exp4Desc = value)
                    "exp5Position" -> data.copy(exp5Position = value)
                    "exp5Company" -> data.copy(exp5Company = value)
                    "exp5Dates" -> data.copy(exp5Dates = value)
                    "exp5Desc" -> data.copy(exp5Desc = value)
                    "refName" -> data.copy(refName = value)
                    "refPositionCompany" -> data.copy(refPositionCompany = value)
                    "refPhone" -> data.copy(refContact = value)
                    "refEmail" -> data.copy(refEmail = value)
                    "ref2Name" -> data.copy(ref2Name = value)
                    "ref2PositionCompany" -> data.copy(ref2PositionCompany = value)
                    "ref2Phone" -> data.copy(ref2Contact = value)
                    "ref2Email" -> data.copy(ref2Email = value)
                    "refAvatarUri" -> data.copy(refAvatarUri = value)
                    "ref2AvatarUri" -> data.copy(ref2AvatarUri = value)
                    else -> data
                }
            )
        },
        onHomeOverride = onHome
    )
}

@Composable
fun ResumeTemplate15Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit, onHome: () -> Unit = {}) {
    com.saltech.urdocs.ui.templates.ResumeTemplate15_PixelPerfect(
        userName = data.fullName,
        userTitle = data.professionalTitle,
        avatarUri = data.avatarUri,
        contactPhone = data.phone,
        contactEmail = data.email,
        contactAddress = data.location,
        contactWebsite = data.website,
        contactLinkedin = data.linkedin,
        aboutMe = data.aboutMe,
        edu1Degree = data.edu1Degree, edu1School = data.edu1School, edu1Years = data.edu1Years,
        edu2Degree = data.edu2Degree, edu2School = data.edu2School, edu2Years = data.edu2Years,
        skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
        exp1Position = data.exp1Position, exp1Company = data.exp1Company, exp1Dates = data.exp1Dates, exp1Desc = data.exp1Desc,
        exp2Position = data.exp2Position, exp2Company = data.exp2Company, exp2Dates = data.exp2Dates, exp2Desc = data.exp2Desc,
        exp3Position = data.exp3Position, exp3Company = data.exp3Company, exp3Dates = data.exp3Dates, exp3Desc = data.exp3Desc,
        exp4Position = data.exp4Position, exp4Company = data.exp4Company, exp4Dates = data.exp4Dates, exp4Desc = data.exp4Desc,
        exp5Position = data.exp5Position, exp5Company = data.exp5Company, exp5Dates = data.exp5Dates, exp5Desc = data.exp5Desc,
        refName = data.refName, refPositionCompany = data.refPositionCompany, refPhone = data.refContact, refEmail = data.refEmail, refAvatarUri = data.refAvatarUri,
        ref2Name = data.ref2Name, ref2PositionCompany = data.ref2PositionCompany, ref2Phone = data.ref2Contact, ref2Email = data.ref2Email, ref2AvatarUri = data.ref2AvatarUri,
        onFieldChange = { field, value ->
            onFieldChange(
                when (field) {
                    "fullName" -> data.copy(fullName = value)
                    "professionalTitle" -> data.copy(professionalTitle = value)
                    "avatarUri" -> data.copy(avatarUri = value)
                    "phone" -> data.copy(phone = value)
                    "email" -> data.copy(email = value)
                    "location" -> data.copy(location = value)
                    "website" -> data.copy(website = value)
                    "linkedin" -> data.copy(linkedin = value)
                    "aboutMe" -> data.copy(aboutMe = value)
                    "edu1Degree" -> data.copy(edu1Degree = value)
                    "edu1School" -> data.copy(edu1School = value)
                    "edu1Years" -> data.copy(edu1Years = value)
                    "edu2Degree" -> data.copy(edu2Degree = value)
                    "edu2School" -> data.copy(edu2School = value)
                    "edu2Years" -> data.copy(edu2Years = value)
                    "skill1" -> data.copy(skill1 = value)
                    "skill2" -> data.copy(skill2 = value)
                    "skill3" -> data.copy(skill3 = value)
                    "skill4" -> data.copy(skill4 = value)
                    "skill5" -> data.copy(skill5 = value)
                    "skill6" -> data.copy(skill6 = value)
                    "exp1Position" -> data.copy(exp1Position = value)
                    "exp1Company" -> data.copy(exp1Company = value)
                    "exp1Dates" -> data.copy(exp1Dates = value)
                    "exp1Desc" -> data.copy(exp1Desc = value)
                    "exp2Position" -> data.copy(exp2Position = value)
                    "exp2Company" -> data.copy(exp2Company = value)
                    "exp2Dates" -> data.copy(exp2Dates = value)
                    "exp2Desc" -> data.copy(exp2Desc = value)
                    "exp3Position" -> data.copy(exp3Position = value)
                    "exp3Company" -> data.copy(exp3Company = value)
                    "exp3Dates" -> data.copy(exp3Dates = value)
                    "exp3Desc" -> data.copy(exp3Desc = value)
                    "exp4Position" -> data.copy(exp4Position = value)
                    "exp4Company" -> data.copy(exp4Company = value)
                    "exp4Dates" -> data.copy(exp4Dates = value)
                    "exp4Desc" -> data.copy(exp4Desc = value)
                    "exp5Position" -> data.copy(exp5Position = value)
                    "exp5Company" -> data.copy(exp5Company = value)
                    "exp5Dates" -> data.copy(exp5Dates = value)
                    "exp5Desc" -> data.copy(exp5Desc = value)
                    "refName" -> data.copy(refName = value)
                    "refPositionCompany" -> data.copy(refPositionCompany = value)
                    "refPhone" -> data.copy(refContact = value)
                    "refEmail" -> data.copy(refEmail = value)
                    "ref2Name" -> data.copy(ref2Name = value)
                    "ref2PositionCompany" -> data.copy(ref2PositionCompany = value)
                    "ref2Phone" -> data.copy(ref2Contact = value)
                    "ref2Email" -> data.copy(ref2Email = value)
                    "refAvatarUri" -> data.copy(refAvatarUri = value)
                    "ref2AvatarUri" -> data.copy(ref2AvatarUri = value)
                    else -> data
                }
            )
        },
        onHomeOverride = onHome
    )
}

@Composable
fun ResumeTemplate16Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit, onHome: () -> Unit = {}) {
    com.saltech.urdocs.ui.templates.ResumeTemplate16_PixelPerfect(
        userName = data.fullName,
        userTitle = data.professionalTitle,
        avatarUri = data.avatarUri,
        contactPhone = data.phone,
        contactEmail = data.email,
        contactAddress = data.location,
        contactWebsite = data.website,
        contactLinkedin = data.linkedin,
        aboutMe = data.aboutMe,
        edu1Degree = data.edu1Degree, edu1School = data.edu1School, edu1Years = data.edu1Years,
        edu2Degree = data.edu2Degree, edu2School = data.edu2School, edu2Years = data.edu2Years,
        skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
        exp1Position = data.exp1Position, exp1Company = data.exp1Company, exp1Dates = data.exp1Dates, exp1Desc = data.exp1Desc,
        exp2Position = data.exp2Position, exp2Company = data.exp2Company, exp2Dates = data.exp2Dates, exp2Desc = data.exp2Desc,
        exp3Position = data.exp3Position, exp3Company = data.exp3Company, exp3Dates = data.exp3Dates, exp3Desc = data.exp3Desc,
        exp4Position = data.exp4Position, exp4Company = data.exp4Company, exp4Dates = data.exp4Dates, exp4Desc = data.exp4Desc,
        exp5Position = data.exp5Position, exp5Company = data.exp5Company, exp5Dates = data.exp5Dates, exp5Desc = data.exp5Desc,
        refName = data.refName, refPositionCompany = data.refPositionCompany, refPhone = data.refContact, refEmail = data.refEmail, refAvatarUri = data.refAvatarUri,
        ref2Name = data.ref2Name, ref2PositionCompany = data.ref2PositionCompany, ref2Phone = data.ref2Contact, ref2Email = data.ref2Email, ref2AvatarUri = data.ref2AvatarUri,
        onFieldChange = { field, value ->
            onFieldChange(
                when (field) {
                    "fullName" -> data.copy(fullName = value)
                    "professionalTitle" -> data.copy(professionalTitle = value)
                    "avatarUri" -> data.copy(avatarUri = value)
                    "phone" -> data.copy(phone = value)
                    "email" -> data.copy(email = value)
                    "location" -> data.copy(location = value)
                    "website" -> data.copy(website = value)
                    "linkedin" -> data.copy(linkedin = value)
                    "aboutMe" -> data.copy(aboutMe = value)
                    "edu1Degree" -> data.copy(edu1Degree = value)
                    "edu1School" -> data.copy(edu1School = value)
                    "edu1Years" -> data.copy(edu1Years = value)
                    "edu2Degree" -> data.copy(edu2Degree = value)
                    "edu2School" -> data.copy(edu2School = value)
                    "edu2Years" -> data.copy(edu2Years = value)
                    "skill1" -> data.copy(skill1 = value)
                    "skill2" -> data.copy(skill2 = value)
                    "skill3" -> data.copy(skill3 = value)
                    "skill4" -> data.copy(skill4 = value)
                    "skill5" -> data.copy(skill5 = value)
                    "skill6" -> data.copy(skill6 = value)
                    "exp1Position" -> data.copy(exp1Position = value)
                    "exp1Company" -> data.copy(exp1Company = value)
                    "exp1Dates" -> data.copy(exp1Dates = value)
                    "exp1Desc" -> data.copy(exp1Desc = value)
                    "exp2Position" -> data.copy(exp2Position = value)
                    "exp2Company" -> data.copy(exp2Company = value)
                    "exp2Dates" -> data.copy(exp2Dates = value)
                    "exp2Desc" -> data.copy(exp2Desc = value)
                    "exp3Position" -> data.copy(exp3Position = value)
                    "exp3Company" -> data.copy(exp3Company = value)
                    "exp3Dates" -> data.copy(exp3Dates = value)
                    "exp3Desc" -> data.copy(exp3Desc = value)
                    "exp4Position" -> data.copy(exp4Position = value)
                    "exp4Company" -> data.copy(exp4Company = value)
                    "exp4Dates" -> data.copy(exp4Dates = value)
                    "exp4Desc" -> data.copy(exp4Desc = value)
                    "exp5Position" -> data.copy(exp5Position = value)
                    "exp5Company" -> data.copy(exp5Company = value)
                    "exp5Dates" -> data.copy(exp5Dates = value)
                    "exp5Desc" -> data.copy(exp5Desc = value)
                    "refName" -> data.copy(refName = value)
                    "refPositionCompany" -> data.copy(refPositionCompany = value)
                    "refPhone" -> data.copy(refContact = value)
                    "refEmail" -> data.copy(refEmail = value)
                    "ref2Name" -> data.copy(ref2Name = value)
                    "ref2PositionCompany" -> data.copy(ref2PositionCompany = value)
                    "ref2Phone" -> data.copy(ref2Contact = value)
                    "ref2Email" -> data.copy(ref2Email = value)
                    "refAvatarUri" -> data.copy(refAvatarUri = value)
                    "ref2AvatarUri" -> data.copy(ref2AvatarUri = value)
                    else -> data
                }
            )
        },
        onHomeOverride = onHome
    )
}

@Composable
fun ResumeTemplate17Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit, onHome: () -> Unit = {}) {
    com.saltech.urdocs.ui.templates.ResumeTemplate17_PixelPerfect(
        userName = data.fullName,
        userTitle = data.professionalTitle,
        avatarUri = data.avatarUri,
        contactPhone = data.phone,
        contactEmail = data.email,
        contactAddress = data.location,
        contactWebsite = data.website,
        contactLinkedin = data.linkedin,
        aboutMe = data.aboutMe,
        edu1Degree = data.edu1Degree, edu1School = data.edu1School, edu1Years = data.edu1Years,
        edu2Degree = data.edu2Degree, edu2School = data.edu2School, edu2Years = data.edu2Years,
        skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
        exp1Position = data.exp1Position, exp1Company = data.exp1Company, exp1Dates = data.exp1Dates, exp1Desc = data.exp1Desc,
        exp2Position = data.exp2Position, exp2Company = data.exp2Company, exp2Dates = data.exp2Dates, exp2Desc = data.exp2Desc,
        exp3Position = data.exp3Position, exp3Company = data.exp3Company, exp3Dates = data.exp3Dates, exp3Desc = data.exp3Desc,
        exp4Position = data.exp4Position, exp4Company = data.exp4Company, exp4Dates = data.exp4Dates, exp4Desc = data.exp4Desc,
        exp5Position = data.exp5Position, exp5Company = data.exp5Company, exp5Dates = data.exp5Dates, exp5Desc = data.exp5Desc,
        refName = data.refName, refPositionCompany = data.refPositionCompany, refPhone = data.refContact, refEmail = data.refEmail, refAvatarUri = data.refAvatarUri,
        ref2Name = data.ref2Name, ref2PositionCompany = data.ref2PositionCompany, ref2Phone = data.ref2Contact, ref2Email = data.ref2Email, ref2AvatarUri = data.ref2AvatarUri,
        onFieldChange = { field, value ->
            onFieldChange(
                when (field) {
                    "fullName" -> data.copy(fullName = value)
                    "professionalTitle" -> data.copy(professionalTitle = value)
                    "avatarUri" -> data.copy(avatarUri = value)
                    "phone" -> data.copy(phone = value)
                    "email" -> data.copy(email = value)
                    "location" -> data.copy(location = value)
                    "website" -> data.copy(website = value)
                    "linkedin" -> data.copy(linkedin = value)
                    "aboutMe" -> data.copy(aboutMe = value)
                    "edu1Degree" -> data.copy(edu1Degree = value)
                    "edu1School" -> data.copy(edu1School = value)
                    "edu1Years" -> data.copy(edu1Years = value)
                    "edu2Degree" -> data.copy(edu2Degree = value)
                    "edu2School" -> data.copy(edu2School = value)
                    "edu2Years" -> data.copy(edu2Years = value)
                    "skill1" -> data.copy(skill1 = value)
                    "skill2" -> data.copy(skill2 = value)
                    "skill3" -> data.copy(skill3 = value)
                    "skill4" -> data.copy(skill4 = value)
                    "skill5" -> data.copy(skill5 = value)
                    "skill6" -> data.copy(skill6 = value)
                    "exp1Position" -> data.copy(exp1Position = value)
                    "exp1Company" -> data.copy(exp1Company = value)
                    "exp1Dates" -> data.copy(exp1Dates = value)
                    "exp1Desc" -> data.copy(exp1Desc = value)
                    "exp2Position" -> data.copy(exp2Position = value)
                    "exp2Company" -> data.copy(exp2Company = value)
                    "exp2Dates" -> data.copy(exp2Dates = value)
                    "exp2Desc" -> data.copy(exp2Desc = value)
                    "exp3Position" -> data.copy(exp3Position = value)
                    "exp3Company" -> data.copy(exp3Company = value)
                    "exp3Dates" -> data.copy(exp3Dates = value)
                    "exp3Desc" -> data.copy(exp3Desc = value)
                    "exp4Position" -> data.copy(exp4Position = value)
                    "exp4Company" -> data.copy(exp4Company = value)
                    "exp4Dates" -> data.copy(exp4Dates = value)
                    "exp4Desc" -> data.copy(exp4Desc = value)
                    "exp5Position" -> data.copy(exp5Position = value)
                    "exp5Company" -> data.copy(exp5Company = value)
                    "exp5Dates" -> data.copy(exp5Dates = value)
                    "exp5Desc" -> data.copy(exp5Desc = value)
                    "refName" -> data.copy(refName = value)
                    "refPositionCompany" -> data.copy(refPositionCompany = value)
                    "refPhone" -> data.copy(refContact = value)
                    "refEmail" -> data.copy(refEmail = value)
                    "ref2Name" -> data.copy(ref2Name = value)
                    "ref2PositionCompany" -> data.copy(ref2PositionCompany = value)
                    "ref2Phone" -> data.copy(ref2Contact = value)
                    "ref2Email" -> data.copy(ref2Email = value)
                    "refAvatarUri" -> data.copy(refAvatarUri = value)
                    "ref2AvatarUri" -> data.copy(ref2AvatarUri = value)
                    else -> data
                }
            )
        },
        onHomeOverride = onHome
    )
}

@Composable
fun ResumeTemplate18Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit, onHome: () -> Unit = {}) {
    com.saltech.urdocs.ui.templates.ResumeTemplate18_PixelPerfect(
        userName = data.fullName,
        userTitle = data.professionalTitle,
        avatarUri = data.avatarUri,
        contactPhone = data.phone,
        contactEmail = data.email,
        contactAddress = data.location,
        contactWebsite = data.website,
        contactLinkedin = data.linkedin,
        aboutMe = data.aboutMe,
        edu1Degree = data.edu1Degree, edu1School = data.edu1School, edu1Years = data.edu1Years,
        edu2Degree = data.edu2Degree, edu2School = data.edu2School, edu2Years = data.edu2Years,
        skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
        exp1Position = data.exp1Position, exp1Company = data.exp1Company, exp1Dates = data.exp1Dates, exp1Desc = data.exp1Desc,
        exp2Position = data.exp2Position, exp2Company = data.exp2Company, exp2Dates = data.exp2Dates, exp2Desc = data.exp2Desc,
        exp3Position = data.exp3Position, exp3Company = data.exp3Company, exp3Dates = data.exp3Dates, exp3Desc = data.exp3Desc,
        exp4Position = data.exp4Position, exp4Company = data.exp4Company, exp4Dates = data.exp4Dates, exp4Desc = data.exp4Desc,
        exp5Position = data.exp5Position, exp5Company = data.exp5Company, exp5Dates = data.exp5Dates, exp5Desc = data.exp5Desc,
        refName = data.refName, refPositionCompany = data.refPositionCompany, refPhone = data.refContact, refEmail = data.refEmail, refAvatarUri = data.refAvatarUri,
        ref2Name = data.ref2Name, ref2PositionCompany = data.ref2PositionCompany, ref2Phone = data.ref2Contact, ref2Email = data.ref2Email, ref2AvatarUri = data.ref2AvatarUri,
        onFieldChange = { field, value ->
            onFieldChange(
                when (field) {
                    "fullName" -> data.copy(fullName = value)
                    "professionalTitle" -> data.copy(professionalTitle = value)
                    "avatarUri" -> data.copy(avatarUri = value)
                    "phone" -> data.copy(phone = value)
                    "email" -> data.copy(email = value)
                    "location" -> data.copy(location = value)
                    "website" -> data.copy(website = value)
                    "linkedin" -> data.copy(linkedin = value)
                    "aboutMe" -> data.copy(aboutMe = value)
                    "edu1Degree" -> data.copy(edu1Degree = value)
                    "edu1School" -> data.copy(edu1School = value)
                    "edu1Years" -> data.copy(edu1Years = value)
                    "edu2Degree" -> data.copy(edu2Degree = value)
                    "edu2School" -> data.copy(edu2School = value)
                    "edu2Years" -> data.copy(edu2Years = value)
                    "skill1" -> data.copy(skill1 = value)
                    "skill2" -> data.copy(skill2 = value)
                    "skill3" -> data.copy(skill3 = value)
                    "skill4" -> data.copy(skill4 = value)
                    "skill5" -> data.copy(skill5 = value)
                    "skill6" -> data.copy(skill6 = value)
                    "exp1Position" -> data.copy(exp1Position = value)
                    "exp1Company" -> data.copy(exp1Company = value)
                    "exp1Dates" -> data.copy(exp1Dates = value)
                    "exp1Desc" -> data.copy(exp1Desc = value)
                    "exp2Position" -> data.copy(exp2Position = value)
                    "exp2Company" -> data.copy(exp2Company = value)
                    "exp2Dates" -> data.copy(exp2Dates = value)
                    "exp2Desc" -> data.copy(exp2Desc = value)
                    "exp3Position" -> data.copy(exp3Position = value)
                    "exp3Company" -> data.copy(exp3Company = value)
                    "exp3Dates" -> data.copy(exp3Dates = value)
                    "exp3Desc" -> data.copy(exp3Desc = value)
                    "exp4Position" -> data.copy(exp4Position = value)
                    "exp4Company" -> data.copy(exp4Company = value)
                    "exp4Dates" -> data.copy(exp4Dates = value)
                    "exp4Desc" -> data.copy(exp4Desc = value)
                    "exp5Position" -> data.copy(exp5Position = value)
                    "exp5Company" -> data.copy(exp5Company = value)
                    "exp5Dates" -> data.copy(exp5Dates = value)
                    "exp5Desc" -> data.copy(exp5Desc = value)
                    "refName" -> data.copy(refName = value)
                    "refPositionCompany" -> data.copy(refPositionCompany = value)
                    "refPhone" -> data.copy(refContact = value)
                    "refEmail" -> data.copy(refEmail = value)
                    "ref2Name" -> data.copy(ref2Name = value)
                    "ref2PositionCompany" -> data.copy(ref2PositionCompany = value)
                    "ref2Phone" -> data.copy(ref2Contact = value)
                    "ref2Email" -> data.copy(ref2Email = value)
                    "refAvatarUri" -> data.copy(refAvatarUri = value)
                    "ref2AvatarUri" -> data.copy(ref2AvatarUri = value)
                    else -> data
                }
            )
        },
        onHomeOverride = onHome
    )
}

@Composable
fun ResumeTemplate19Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit, onHome: () -> Unit = {}) {
    com.saltech.urdocs.ui.templates.ResumeTemplate19_PixelPerfect(
        userName = data.fullName,
        userTitle = data.professionalTitle,
        avatarUri = data.avatarUri,
        contactPhone = data.phone,
        contactEmail = data.email,
        contactAddress = data.location,
        contactWebsite = data.website,
        contactLinkedin = data.linkedin,
        aboutMe = data.aboutMe,
        edu1Degree = data.edu1Degree, edu1School = data.edu1School, edu1Years = data.edu1Years,
        edu2Degree = data.edu2Degree, edu2School = data.edu2School, edu2Years = data.edu2Years,
        skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
        exp1Position = data.exp1Position, exp1Company = data.exp1Company, exp1Dates = data.exp1Dates, exp1Desc = data.exp1Desc,
        exp2Position = data.exp2Position, exp2Company = data.exp2Company, exp2Dates = data.exp2Dates, exp2Desc = data.exp2Desc,
        exp3Position = data.exp3Position, exp3Company = data.exp3Company, exp3Dates = data.exp3Dates, exp3Desc = data.exp3Desc,
        exp4Position = data.exp4Position, exp4Company = data.exp4Company, exp4Dates = data.exp4Dates, exp4Desc = data.exp4Desc,
        exp5Position = data.exp5Position, exp5Company = data.exp5Company, exp5Dates = data.exp5Dates, exp5Desc = data.exp5Desc,
        refName = data.refName, refPositionCompany = data.refPositionCompany, refPhone = data.refContact, refEmail = data.refEmail, refAvatarUri = data.refAvatarUri,
        ref2Name = data.ref2Name, ref2PositionCompany = data.ref2PositionCompany, ref2Phone = data.ref2Contact, ref2Email = data.ref2Email, ref2AvatarUri = data.ref2AvatarUri,
        onFieldChange = { field, value ->
            onFieldChange(
                when (field) {
                    "fullName" -> data.copy(fullName = value)
                    "professionalTitle" -> data.copy(professionalTitle = value)
                    "avatarUri" -> data.copy(avatarUri = value)
                    "phone" -> data.copy(phone = value)
                    "email" -> data.copy(email = value)
                    "location" -> data.copy(location = value)
                    "website" -> data.copy(website = value)
                    "linkedin" -> data.copy(linkedin = value)
                    "aboutMe" -> data.copy(aboutMe = value)
                    "edu1Degree" -> data.copy(edu1Degree = value)
                    "edu1School" -> data.copy(edu1School = value)
                    "edu1Years" -> data.copy(edu1Years = value)
                    "edu2Degree" -> data.copy(edu2Degree = value)
                    "edu2School" -> data.copy(edu2School = value)
                    "edu2Years" -> data.copy(edu2Years = value)
                    "skill1" -> data.copy(skill1 = value)
                    "skill2" -> data.copy(skill2 = value)
                    "skill3" -> data.copy(skill3 = value)
                    "skill4" -> data.copy(skill4 = value)
                    "skill5" -> data.copy(skill5 = value)
                    "skill6" -> data.copy(skill6 = value)
                    "exp1Position" -> data.copy(exp1Position = value)
                    "exp1Company" -> data.copy(exp1Company = value)
                    "exp1Dates" -> data.copy(exp1Dates = value)
                    "exp1Desc" -> data.copy(exp1Desc = value)
                    "exp2Position" -> data.copy(exp2Position = value)
                    "exp2Company" -> data.copy(exp2Company = value)
                    "exp2Dates" -> data.copy(exp2Dates = value)
                    "exp2Desc" -> data.copy(exp2Desc = value)
                    "exp3Position" -> data.copy(exp3Position = value)
                    "exp3Company" -> data.copy(exp3Company = value)
                    "exp3Dates" -> data.copy(exp3Dates = value)
                    "exp3Desc" -> data.copy(exp3Desc = value)
                    "exp4Position" -> data.copy(exp4Position = value)
                    "exp4Company" -> data.copy(exp4Company = value)
                    "exp4Dates" -> data.copy(exp4Dates = value)
                    "exp4Desc" -> data.copy(exp4Desc = value)
                    "exp5Position" -> data.copy(exp5Position = value)
                    "exp5Company" -> data.copy(exp5Company = value)
                    "exp5Dates" -> data.copy(exp5Dates = value)
                    "exp5Desc" -> data.copy(exp5Desc = value)
                    "refName" -> data.copy(refName = value)
                    "refPositionCompany" -> data.copy(refPositionCompany = value)
                    "refPhone" -> data.copy(refContact = value)
                    "refEmail" -> data.copy(refEmail = value)
                    "ref2Name" -> data.copy(ref2Name = value)
                    "ref2PositionCompany" -> data.copy(ref2PositionCompany = value)
                    "ref2Phone" -> data.copy(ref2Contact = value)
                    "ref2Email" -> data.copy(ref2Email = value)
                    "refAvatarUri" -> data.copy(refAvatarUri = value)
                    "ref2AvatarUri" -> data.copy(ref2AvatarUri = value)
                    else -> data
                }
            )
        },
        onHomeOverride = onHome
    )
}

@Composable
fun ResumeTemplate20Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit, onHome: () -> Unit = {}) {
    com.saltech.urdocs.ui.templates.ResumeTemplate20_PixelPerfect(
        userName = data.fullName,
        userTitle = data.professionalTitle,
        avatarUri = data.avatarUri,
        contactPhone = data.phone,
        contactEmail = data.email,
        contactAddress = data.location,
        contactWebsite = data.website,
        contactLinkedin = data.linkedin,
        aboutMe = data.aboutMe,
        edu1Degree = data.edu1Degree, edu1School = data.edu1School, edu1Years = data.edu1Years,
        edu2Degree = data.edu2Degree, edu2School = data.edu2School, edu2Years = data.edu2Years,
        skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
        exp1Position = data.exp1Position, exp1Company = data.exp1Company, exp1Dates = data.exp1Dates, exp1Desc = data.exp1Desc,
        exp2Position = data.exp2Position, exp2Company = data.exp2Company, exp2Dates = data.exp2Dates, exp2Desc = data.exp2Desc,
        exp3Position = data.exp3Position, exp3Company = data.exp3Company, exp3Dates = data.exp3Dates, exp3Desc = data.exp3Desc,
        exp4Position = data.exp4Position, exp4Company = data.exp4Company, exp4Dates = data.exp4Dates, exp4Desc = data.exp4Desc,
        exp5Position = data.exp5Position, exp5Company = data.exp5Company, exp5Dates = data.exp5Dates, exp5Desc = data.exp5Desc,
        refName = data.refName, refPositionCompany = data.refPositionCompany, refPhone = data.refContact, refEmail = data.refEmail, refAvatarUri = data.refAvatarUri,
        ref2Name = data.ref2Name, ref2PositionCompany = data.ref2PositionCompany, ref2Phone = data.ref2Contact, ref2Email = data.ref2Email, ref2AvatarUri = data.ref2AvatarUri,
        onFieldChange = { field, value ->
            onFieldChange(
                when (field) {
                    "fullName" -> data.copy(fullName = value)
                    "professionalTitle" -> data.copy(professionalTitle = value)
                    "avatarUri" -> data.copy(avatarUri = value)
                    "phone" -> data.copy(phone = value)
                    "email" -> data.copy(email = value)
                    "location" -> data.copy(location = value)
                    "website" -> data.copy(website = value)
                    "linkedin" -> data.copy(linkedin = value)
                    "aboutMe" -> data.copy(aboutMe = value)
                    "edu1Degree" -> data.copy(edu1Degree = value)
                    "edu1School" -> data.copy(edu1School = value)
                    "edu1Years" -> data.copy(edu1Years = value)
                    "edu2Degree" -> data.copy(edu2Degree = value)
                    "edu2School" -> data.copy(edu2School = value)
                    "edu2Years" -> data.copy(edu2Years = value)
                    "skill1" -> data.copy(skill1 = value)
                    "skill2" -> data.copy(skill2 = value)
                    "skill3" -> data.copy(skill3 = value)
                    "skill4" -> data.copy(skill4 = value)
                    "skill5" -> data.copy(skill5 = value)
                    "skill6" -> data.copy(skill6 = value)
                    "exp1Position" -> data.copy(exp1Position = value)
                    "exp1Company" -> data.copy(exp1Company = value)
                    "exp1Dates" -> data.copy(exp1Dates = value)
                    "exp1Desc" -> data.copy(exp1Desc = value)
                    "exp2Position" -> data.copy(exp2Position = value)
                    "exp2Company" -> data.copy(exp2Company = value)
                    "exp2Dates" -> data.copy(exp2Dates = value)
                    "exp2Desc" -> data.copy(exp2Desc = value)
                    "exp3Position" -> data.copy(exp3Position = value)
                    "exp3Company" -> data.copy(exp3Company = value)
                    "exp3Dates" -> data.copy(exp3Dates = value)
                    "exp3Desc" -> data.copy(exp3Desc = value)
                    "exp4Position" -> data.copy(exp4Position = value)
                    "exp4Company" -> data.copy(exp4Company = value)
                    "exp4Dates" -> data.copy(exp4Dates = value)
                    "exp4Desc" -> data.copy(exp4Desc = value)
                    "exp5Position" -> data.copy(exp5Position = value)
                    "exp5Company" -> data.copy(exp5Company = value)
                    "exp5Dates" -> data.copy(exp5Dates = value)
                    "exp5Desc" -> data.copy(exp5Desc = value)
                    "refName" -> data.copy(refName = value)
                    "refPositionCompany" -> data.copy(refPositionCompany = value)
                    "refPhone" -> data.copy(refContact = value)
                    "refEmail" -> data.copy(refEmail = value)
                    "ref2Name" -> data.copy(ref2Name = value)
                    "ref2PositionCompany" -> data.copy(ref2PositionCompany = value)
                    "ref2Phone" -> data.copy(ref2Contact = value)
                    "ref2Email" -> data.copy(ref2Email = value)
                    "refAvatarUri" -> data.copy(refAvatarUri = value)
                    "ref2AvatarUri" -> data.copy(ref2AvatarUri = value)
                    else -> data
                }
            )
        },
        onHomeOverride = onHome
    )
}

@Composable
fun ResumeTemplate21Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit, onHome: () -> Unit = {}) {
    com.saltech.urdocs.ui.templates.ResumeTemplate21_PixelPerfect(
        userName = data.fullName,
        userTitle = data.professionalTitle,
        avatarUri = data.avatarUri,
        contactPhone = data.phone,
        contactEmail = data.email,
        contactAddress = data.location,
        contactWebsite = data.website,
        contactLinkedin = data.linkedin,
        aboutMe = data.aboutMe,
        edu1Degree = data.edu1Degree, edu1School = data.edu1School, edu1Years = data.edu1Years,
        edu2Degree = data.edu2Degree, edu2School = data.edu2School, edu2Years = data.edu2Years,
        skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
        exp1Position = data.exp1Position, exp1Company = data.exp1Company, exp1Dates = data.exp1Dates, exp1Desc = data.exp1Desc,
        exp2Position = data.exp2Position, exp2Company = data.exp2Company, exp2Dates = data.exp2Dates, exp2Desc = data.exp2Desc,
        exp3Position = data.exp3Position, exp3Company = data.exp3Company, exp3Dates = data.exp3Dates, exp3Desc = data.exp3Desc,
        exp4Position = data.exp4Position, exp4Company = data.exp4Company, exp4Dates = data.exp4Dates, exp4Desc = data.exp4Desc,
        exp5Position = data.exp5Position, exp5Company = data.exp5Company, exp5Dates = data.exp5Dates, exp5Desc = data.exp5Desc,
        refName = data.refName, refPositionCompany = data.refPositionCompany, refPhone = data.refContact, refEmail = data.refEmail, refAvatarUri = data.refAvatarUri,
        ref2Name = data.ref2Name, ref2PositionCompany = data.ref2PositionCompany, ref2Phone = data.ref2Contact, ref2Email = data.ref2Email, ref2AvatarUri = data.ref2AvatarUri,
        onFieldChange = { field, value ->
            onFieldChange(
                when (field) {
                    "fullName" -> data.copy(fullName = value)
                    "professionalTitle" -> data.copy(professionalTitle = value)
                    "avatarUri" -> data.copy(avatarUri = value)
                    "phone" -> data.copy(phone = value)
                    "email" -> data.copy(email = value)
                    "location" -> data.copy(location = value)
                    "website" -> data.copy(website = value)
                    "linkedin" -> data.copy(linkedin = value)
                    "aboutMe" -> data.copy(aboutMe = value)
                    "edu1Degree" -> data.copy(edu1Degree = value)
                    "edu1School" -> data.copy(edu1School = value)
                    "edu1Years" -> data.copy(edu1Years = value)
                    "edu2Degree" -> data.copy(edu2Degree = value)
                    "edu2School" -> data.copy(edu2School = value)
                    "edu2Years" -> data.copy(edu2Years = value)
                    "skill1" -> data.copy(skill1 = value)
                    "skill2" -> data.copy(skill2 = value)
                    "skill3" -> data.copy(skill3 = value)
                    "skill4" -> data.copy(skill4 = value)
                    "skill5" -> data.copy(skill5 = value)
                    "skill6" -> data.copy(skill6 = value)
                    "exp1Position" -> data.copy(exp1Position = value)
                    "exp1Company" -> data.copy(exp1Company = value)
                    "exp1Dates" -> data.copy(exp1Dates = value)
                    "exp1Desc" -> data.copy(exp1Desc = value)
                    "exp2Position" -> data.copy(exp2Position = value)
                    "exp2Company" -> data.copy(exp2Company = value)
                    "exp2Dates" -> data.copy(exp2Dates = value)
                    "exp2Desc" -> data.copy(exp2Desc = value)
                    "exp3Position" -> data.copy(exp3Position = value)
                    "exp3Company" -> data.copy(exp3Company = value)
                    "exp3Dates" -> data.copy(exp3Dates = value)
                    "exp3Desc" -> data.copy(exp3Desc = value)
                    "exp4Position" -> data.copy(exp4Position = value)
                    "exp4Company" -> data.copy(exp4Company = value)
                    "exp4Dates" -> data.copy(exp4Dates = value)
                    "exp4Desc" -> data.copy(exp4Desc = value)
                    "exp5Position" -> data.copy(exp5Position = value)
                    "exp5Company" -> data.copy(exp5Company = value)
                    "exp5Dates" -> data.copy(exp5Dates = value)
                    "exp5Desc" -> data.copy(exp5Desc = value)
                    "refName" -> data.copy(refName = value)
                    "refPositionCompany" -> data.copy(refPositionCompany = value)
                    "refPhone" -> data.copy(refContact = value)
                    "refEmail" -> data.copy(refEmail = value)
                    "ref2Name" -> data.copy(ref2Name = value)
                    "ref2PositionCompany" -> data.copy(ref2PositionCompany = value)
                    "ref2Phone" -> data.copy(ref2Contact = value)
                    "ref2Email" -> data.copy(ref2Email = value)
                    "refAvatarUri" -> data.copy(refAvatarUri = value)
                    "ref2AvatarUri" -> data.copy(ref2AvatarUri = value)
                    else -> data
                }
            )
        },
        onHomeOverride = onHome
    )
}

@Composable
fun ResumeTemplate22Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit, onHome: () -> Unit = {}) {
    com.saltech.urdocs.ui.templates.ResumeTemplate22_PixelPerfect(
        userName = data.fullName,
        userTitle = data.professionalTitle,
        avatarUri = data.avatarUri,
        contactPhone = data.phone,
        contactEmail = data.email,
        contactAddress = data.location,
        contactWebsite = data.website,
        contactLinkedin = data.linkedin,
        aboutMe = data.aboutMe,
        edu1Degree = data.edu1Degree, edu1School = data.edu1School, edu1Years = data.edu1Years,
        edu2Degree = data.edu2Degree, edu2School = data.edu2School, edu2Years = data.edu2Years,
        skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
        exp1Position = data.exp1Position, exp1Company = data.exp1Company, exp1Dates = data.exp1Dates, exp1Desc = data.exp1Desc,
        exp2Position = data.exp2Position, exp2Company = data.exp2Company, exp2Dates = data.exp2Dates, exp2Desc = data.exp2Desc,
        exp3Position = data.exp3Position, exp3Company = data.exp3Company, exp3Dates = data.exp3Dates, exp3Desc = data.exp3Desc,
        exp4Position = data.exp4Position, exp4Company = data.exp4Company, exp4Dates = data.exp4Dates, exp4Desc = data.exp4Desc,
        exp5Position = data.exp5Position, exp5Company = data.exp5Company, exp5Dates = data.exp5Dates, exp5Desc = data.exp5Desc,
        refName = data.refName, refPositionCompany = data.refPositionCompany, refPhone = data.refContact, refEmail = data.refEmail, refAvatarUri = data.refAvatarUri,
        ref2Name = data.ref2Name, ref2PositionCompany = data.ref2PositionCompany, ref2Phone = data.ref2Contact, ref2Email = data.ref2Email, ref2AvatarUri = data.ref2AvatarUri,
        onFieldChange = { field, value ->
            onFieldChange(
                when (field) {
                    "fullName" -> data.copy(fullName = value)
                    "professionalTitle" -> data.copy(professionalTitle = value)
                    "avatarUri" -> data.copy(avatarUri = value)
                    "phone" -> data.copy(phone = value)
                    "email" -> data.copy(email = value)
                    "location" -> data.copy(location = value)
                    "website" -> data.copy(website = value)
                    "linkedin" -> data.copy(linkedin = value)
                    "aboutMe" -> data.copy(aboutMe = value)
                    "edu1Degree" -> data.copy(edu1Degree = value)
                    "edu1School" -> data.copy(edu1School = value)
                    "edu1Years" -> data.copy(edu1Years = value)
                    "edu2Degree" -> data.copy(edu2Degree = value)
                    "edu2School" -> data.copy(edu2School = value)
                    "edu2Years" -> data.copy(edu2Years = value)
                    "skill1" -> data.copy(skill1 = value)
                    "skill2" -> data.copy(skill2 = value)
                    "skill3" -> data.copy(skill3 = value)
                    "skill4" -> data.copy(skill4 = value)
                    "skill5" -> data.copy(skill5 = value)
                    "skill6" -> data.copy(skill6 = value)
                    "exp1Position" -> data.copy(exp1Position = value)
                    "exp1Company" -> data.copy(exp1Company = value)
                    "exp1Dates" -> data.copy(exp1Dates = value)
                    "exp1Desc" -> data.copy(exp1Desc = value)
                    "exp2Position" -> data.copy(exp2Position = value)
                    "exp2Company" -> data.copy(exp2Company = value)
                    "exp2Dates" -> data.copy(exp2Dates = value)
                    "exp2Desc" -> data.copy(exp2Desc = value)
                    "exp3Position" -> data.copy(exp3Position = value)
                    "exp3Company" -> data.copy(exp3Company = value)
                    "exp3Dates" -> data.copy(exp3Dates = value)
                    "exp3Desc" -> data.copy(exp3Desc = value)
                    "exp4Position" -> data.copy(exp4Position = value)
                    "exp4Company" -> data.copy(exp4Company = value)
                    "exp4Dates" -> data.copy(exp4Dates = value)
                    "exp4Desc" -> data.copy(exp4Desc = value)
                    "exp5Position" -> data.copy(exp5Position = value)
                    "exp5Company" -> data.copy(exp5Company = value)
                    "exp5Dates" -> data.copy(exp5Dates = value)
                    "exp5Desc" -> data.copy(exp5Desc = value)
                    "refName" -> data.copy(refName = value)
                    "refPositionCompany" -> data.copy(refPositionCompany = value)
                    "refPhone" -> data.copy(refContact = value)
                    "refEmail" -> data.copy(refEmail = value)
                    "ref2Name" -> data.copy(ref2Name = value)
                    "ref2PositionCompany" -> data.copy(ref2PositionCompany = value)
                    "ref2Phone" -> data.copy(ref2Contact = value)
                    "ref2Email" -> data.copy(ref2Email = value)
                    "refAvatarUri" -> data.copy(refAvatarUri = value)
                    "ref2AvatarUri" -> data.copy(ref2AvatarUri = value)
                    else -> data
                }
            )
        },
        onHomeOverride = onHome
    )
}

@Composable
fun ResumeTemplate23Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit, onHome: () -> Unit = {}) {
    com.saltech.urdocs.ui.templates.ResumeTemplate23_PixelPerfect(
        userName = data.fullName,
        userTitle = data.professionalTitle,
        avatarUri = data.avatarUri,
        contactPhone = data.phone,
        contactEmail = data.email,
        contactAddress = data.location,
        contactWebsite = data.website,
        contactLinkedin = data.linkedin,
        aboutMe = data.aboutMe,
        edu1Degree = data.edu1Degree, edu1School = data.edu1School, edu1Years = data.edu1Years,
        edu2Degree = data.edu2Degree, edu2School = data.edu2School, edu2Years = data.edu2Years,
        skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
        exp1Position = data.exp1Position, exp1Company = data.exp1Company, exp1Dates = data.exp1Dates, exp1Desc = data.exp1Desc,
        exp2Position = data.exp2Position, exp2Company = data.exp2Company, exp2Dates = data.exp2Dates, exp2Desc = data.exp2Desc,
        exp3Position = data.exp3Position, exp3Company = data.exp3Company, exp3Dates = data.exp3Dates, exp3Desc = data.exp3Desc,
        exp4Position = data.exp4Position, exp4Company = data.exp4Company, exp4Dates = data.exp4Dates, exp4Desc = data.exp4Desc,
        exp5Position = data.exp5Position, exp5Company = data.exp5Company, exp5Dates = data.exp5Dates, exp5Desc = data.exp5Desc,
        refName = data.refName, refPositionCompany = data.refPositionCompany, refPhone = data.refContact, refEmail = data.refEmail, refAvatarUri = data.refAvatarUri,
        ref2Name = data.ref2Name, ref2PositionCompany = data.ref2PositionCompany, ref2Phone = data.ref2Contact, ref2Email = data.ref2Email, ref2AvatarUri = data.ref2AvatarUri,
        onFieldChange = { field, value ->
            onFieldChange(
                when (field) {
                    "fullName" -> data.copy(fullName = value)
                    "professionalTitle" -> data.copy(professionalTitle = value)
                    "avatarUri" -> data.copy(avatarUri = value)
                    "phone" -> data.copy(phone = value)
                    "email" -> data.copy(email = value)
                    "location" -> data.copy(location = value)
                    "website" -> data.copy(website = value)
                    "linkedin" -> data.copy(linkedin = value)
                    "aboutMe" -> data.copy(aboutMe = value)
                    "edu1Degree" -> data.copy(edu1Degree = value)
                    "edu1School" -> data.copy(edu1School = value)
                    "edu1Years" -> data.copy(edu1Years = value)
                    "edu2Degree" -> data.copy(edu2Degree = value)
                    "edu2School" -> data.copy(edu2School = value)
                    "edu2Years" -> data.copy(edu2Years = value)
                    "skill1" -> data.copy(skill1 = value)
                    "skill2" -> data.copy(skill2 = value)
                    "skill3" -> data.copy(skill3 = value)
                    "skill4" -> data.copy(skill4 = value)
                    "skill5" -> data.copy(skill5 = value)
                    "skill6" -> data.copy(skill6 = value)
                    "exp1Position" -> data.copy(exp1Position = value)
                    "exp1Company" -> data.copy(exp1Company = value)
                    "exp1Dates" -> data.copy(exp1Dates = value)
                    "exp1Desc" -> data.copy(exp1Desc = value)
                    "exp2Position" -> data.copy(exp2Position = value)
                    "exp2Company" -> data.copy(exp2Company = value)
                    "exp2Dates" -> data.copy(exp2Dates = value)
                    "exp2Desc" -> data.copy(exp2Desc = value)
                    "exp3Position" -> data.copy(exp3Position = value)
                    "exp3Company" -> data.copy(exp3Company = value)
                    "exp3Dates" -> data.copy(exp3Dates = value)
                    "exp3Desc" -> data.copy(exp3Desc = value)
                    "exp4Position" -> data.copy(exp4Position = value)
                    "exp4Company" -> data.copy(exp4Company = value)
                    "exp4Dates" -> data.copy(exp4Dates = value)
                    "exp4Desc" -> data.copy(exp4Desc = value)
                    "exp5Position" -> data.copy(exp5Position = value)
                    "exp5Company" -> data.copy(exp5Company = value)
                    "exp5Dates" -> data.copy(exp5Dates = value)
                    "exp5Desc" -> data.copy(exp5Desc = value)
                    "refName" -> data.copy(refName = value)
                    "refPositionCompany" -> data.copy(refPositionCompany = value)
                    "refPhone" -> data.copy(refContact = value)
                    "refEmail" -> data.copy(refEmail = value)
                    "ref2Name" -> data.copy(ref2Name = value)
                    "ref2PositionCompany" -> data.copy(ref2PositionCompany = value)
                    "ref2Phone" -> data.copy(ref2Contact = value)
                    "ref2Email" -> data.copy(ref2Email = value)
                    "refAvatarUri" -> data.copy(refAvatarUri = value)
                    "ref2AvatarUri" -> data.copy(ref2AvatarUri = value)
                    else -> data
                }
            )
        },
        onHomeOverride = onHome
    )
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

@Composable
fun ResumeTemplateP1_01Screen(data: ResumeTemplateFields, onFieldChange: (ResumeTemplateFields) -> Unit, onHome: () -> Unit = {}) {
    com.saltech.urdocs.ui.templates.ResumeTemplateP1_01_PixelPerfect(
        userName = data.fullName, userTitle = data.professionalTitle, avatarUri = data.avatarUri,
        contactPhone = data.phone, contactEmail = data.email, contactAddress = data.location,
        contactWebsite = data.website, contactLinkedin = data.linkedin, aboutMe = data.aboutMe,
        edu1Degree = data.edu1Degree, edu1School = data.edu1School, edu1Years = data.edu1Years,
        edu2Degree = data.edu2Degree, edu2School = data.edu2School, edu2Years = data.edu2Years,
        skills = listOf(data.skill1, data.skill2, data.skill3, data.skill4, data.skill5, data.skill6),
        exp1Position = data.exp1Position, exp1Company = data.exp1Company, exp1Dates = data.exp1Dates, exp1Desc = data.exp1Desc, exp1Desc2 = data.exp1Desc2, exp1Desc3 = data.exp1Desc3,
        exp2Position = data.exp2Position, exp2Company = data.exp2Company, exp2Dates = data.exp2Dates, exp2Desc = data.exp2Desc, exp2Desc2 = data.exp2Desc2, exp2Desc3 = data.exp2Desc3,
        exp3Position = data.exp3Position, exp3Company = data.exp3Company, exp3Dates = data.exp3Dates, exp3Desc = data.exp3Desc, exp3Desc2 = data.exp3Desc2, exp3Desc3 = data.exp3Desc3,
        refName = data.refName, refPositionCompany = data.refPositionCompany, refPhone = data.refContact, refEmail = data.refEmail, refAvatarUri = data.refAvatarUri,
        ref2Name = data.ref2Name, ref2PositionCompany = data.ref2PositionCompany, ref2Phone = data.ref2Contact, ref2Email = data.ref2Email, ref2AvatarUri = data.ref2AvatarUri,
        workSetup = data.workSetup, workSchedule = data.workSchedule, preferredRole = data.preferredRole,
        prefLocations = data.prefLocations, availability = data.availability, languages = data.languages,
        certifications = data.certifications, hobbies = data.hobbies, careerGoal = data.careerGoal, strengths = data.strengths, otherInfo = data.otherInfo,
        onFieldChange = { field, value ->
            onFieldChange(
                when (field) {
                    "fullName" -> data.copy(fullName = value)
                    "professionalTitle" -> data.copy(professionalTitle = value)
                    "avatarUri" -> data.copy(avatarUri = value)
                    "phone" -> data.copy(phone = value)
                    "email" -> data.copy(email = value)
                    "location" -> data.copy(location = value)
                    "website" -> data.copy(website = value)
                    "linkedin" -> data.copy(linkedin = value)
                    "aboutMe" -> data.copy(aboutMe = value)
                    "edu1Degree" -> data.copy(edu1Degree = value)
                    "edu1School" -> data.copy(edu1School = value)
                    "edu1Years" -> data.copy(edu1Years = value)
                    "edu2Degree" -> data.copy(edu2Degree = value)
                    "edu2School" -> data.copy(edu2School = value)
                    "edu2Years" -> data.copy(edu2Years = value)
                    "skill1" -> data.copy(skill1 = value)
                    "skill2" -> data.copy(skill2 = value)
                    "skill3" -> data.copy(skill3 = value)
                    "skill4" -> data.copy(skill4 = value)
                    "skill5" -> data.copy(skill5 = value)
                    "skill6" -> data.copy(skill6 = value)
                    "exp1Position" -> data.copy(exp1Position = value)
                    "exp1Company" -> data.copy(exp1Company = value)
                    "exp1Dates" -> data.copy(exp1Dates = value)
                    "exp1Desc" -> data.copy(exp1Desc = value)
                    "exp1Desc2" -> data.copy(exp1Desc2 = value)
                    "exp1Desc3" -> data.copy(exp1Desc3 = value)
                    "exp2Position" -> data.copy(exp2Position = value)
                    "exp2Company" -> data.copy(exp2Company = value)
                    "exp2Dates" -> data.copy(exp2Dates = value)
                    "exp2Desc" -> data.copy(exp2Desc = value)
                    "exp2Desc2" -> data.copy(exp2Desc2 = value)
                    "exp2Desc3" -> data.copy(exp2Desc3 = value)
                    "exp3Position" -> data.copy(exp3Position = value)
                    "exp3Company" -> data.copy(exp3Company = value)
                    "exp3Dates" -> data.copy(exp3Dates = value)
                    "exp3Desc" -> data.copy(exp3Desc = value)
                    "exp3Desc2" -> data.copy(exp3Desc2 = value)
                    "exp3Desc3" -> data.copy(exp3Desc3 = value)
                    "refName" -> data.copy(refName = value)
                    "refPositionCompany" -> data.copy(refPositionCompany = value)
                    "refPhone" -> data.copy(refContact = value)
                    "refEmail" -> data.copy(refEmail = value)
                    "ref2Name" -> data.copy(ref2Name = value)
                    "ref2PositionCompany" -> data.copy(ref2PositionCompany = value)
                    "ref2Phone" -> data.copy(ref2Contact = value)
                    "ref2Email" -> data.copy(ref2Email = value)
                    "refAvatarUri" -> data.copy(refAvatarUri = value)
                    "ref2AvatarUri" -> data.copy(ref2AvatarUri = value)
                    "workSetup" -> data.copy(workSetup = value)
                    "workSchedule" -> data.copy(workSchedule = value)
                    "preferredRole" -> data.copy(preferredRole = value)
                    "prefLocations" -> data.copy(prefLocations = value)
                    "availability" -> data.copy(availability = value)
                    "languages" -> data.copy(languages = value)
                    "certifications" -> data.copy(certifications = value)
                    "hobbies" -> data.copy(hobbies = value)
                    "careerGoal" -> data.copy(careerGoal = value)
                    "strengths" -> data.copy(strengths = value)
                    "otherInfo" -> data.copy(otherInfo = value)
                    else -> data
                }
            )
        },
        onHomeOverride = onHome
    )
}
