package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

@Composable
fun ResumeTemplateFormScreen(
    templateName: String,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    var data by remember { mutableStateOf(ResumeTemplateFields()) }
    val resId = remember(templateName) {
        context.resources.getIdentifier(templateName, "drawable", context.packageName)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B1530))
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        }

        if (resId != 0) {
            val painter = painterResource(id = resId)
            val intrinsic = painter.intrinsicSize
            val aspect = if (intrinsic.width > 0f && intrinsic.height > 0f) intrinsic.width / intrinsic.height else 0.73f

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspect)
            ) {
                Image(
                    painter = painter,
                    contentDescription = templateName,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
                val boxW = maxWidth
                val boxH = maxHeight

                OverlayField(boxW, boxH, 0.42f, 0.135f, 0.40f, data.fullName) { data = data.copy(fullName = it) }
                OverlayField(boxW, boxH, 0.42f, 0.168f, 0.40f, data.professionalTitle) { data = data.copy(professionalTitle = it) }
                OverlayField(boxW, boxH, 0.12f, 0.310f, 0.30f, data.phone) { data = data.copy(phone = it) }
                OverlayField(boxW, boxH, 0.12f, 0.348f, 0.30f, data.email) { data = data.copy(email = it) }
                OverlayField(boxW, boxH, 0.12f, 0.385f, 0.30f, data.location) { data = data.copy(location = it) }
                OverlayField(boxW, boxH, 0.12f, 0.420f, 0.30f, data.linkedin) { data = data.copy(linkedin = it) }
                OverlayField(boxW, boxH, 0.12f, 0.455f, 0.30f, data.website) { data = data.copy(website = it) }
                OverlayField(boxW, boxH, 0.12f, 0.485f, 0.28f, data.skill1) { data = data.copy(skill1 = it) }
                OverlayField(boxW, boxH, 0.12f, 0.512f, 0.28f, data.skill2) { data = data.copy(skill2 = it) }
                OverlayField(boxW, boxH, 0.12f, 0.538f, 0.28f, data.skill3) { data = data.copy(skill3 = it) }
                OverlayField(boxW, boxH, 0.12f, 0.564f, 0.28f, data.skill4) { data = data.copy(skill4 = it) }
                OverlayField(boxW, boxH, 0.12f, 0.590f, 0.28f, data.skill5) { data = data.copy(skill5 = it) }
                OverlayField(boxW, boxH, 0.12f, 0.632f, 0.30f, data.refName) { data = data.copy(refName = it) }
                OverlayField(boxW, boxH, 0.12f, 0.658f, 0.30f, data.refPositionCompany) { data = data.copy(refPositionCompany = it) }
                OverlayField(boxW, boxH, 0.12f, 0.685f, 0.30f, data.refContact) { data = data.copy(refContact = it) }
                OverlayField(boxW, boxH, 0.545f, 0.335f, 0.42f, data.aboutMe) { data = data.copy(aboutMe = it) }
                OverlayField(boxW, boxH, 0.545f, 0.393f, 0.42f, data.edu1Degree) { data = data.copy(edu1Degree = it) }
                OverlayField(boxW, boxH, 0.545f, 0.412f, 0.30f, data.edu1School) { data = data.copy(edu1School = it) }
                OverlayField(boxW, boxH, 0.86f, 0.412f, 0.12f, data.edu1Years) { data = data.copy(edu1Years = it) }
                OverlayField(boxW, boxH, 0.545f, 0.437f, 0.42f, data.edu2Degree) { data = data.copy(edu2Degree = it) }
                OverlayField(boxW, boxH, 0.545f, 0.456f, 0.30f, data.edu2School) { data = data.copy(edu2School = it) }
                OverlayField(boxW, boxH, 0.86f, 0.456f, 0.12f, data.edu2Years) { data = data.copy(edu2Years = it) }
                OverlayField(boxW, boxH, 0.545f, 0.512f, 0.35f, data.exp1Position) { data = data.copy(exp1Position = it) }
                OverlayField(boxW, boxH, 0.545f, 0.529f, 0.30f, data.exp1Company) { data = data.copy(exp1Company = it) }
                OverlayField(boxW, boxH, 0.86f, 0.529f, 0.12f, data.exp1Dates) { data = data.copy(exp1Dates = it) }
                OverlayField(boxW, boxH, 0.545f, 0.547f, 0.42f, data.exp1Desc) { data = data.copy(exp1Desc = it) }
                OverlayField(boxW, boxH, 0.545f, 0.578f, 0.35f, data.exp2Position) { data = data.copy(exp2Position = it) }
                OverlayField(boxW, boxH, 0.545f, 0.596f, 0.30f, data.exp2Company) { data = data.copy(exp2Company = it) }
                OverlayField(boxW, boxH, 0.86f, 0.596f, 0.12f, data.exp2Dates) { data = data.copy(exp2Dates = it) }
            }
        }
        Spacer(Modifier.height(40.dp))
    }
}

@Composable
private fun androidx.compose.foundation.layout.BoxScope.OverlayField(
    boxW: Dp,
    boxH: Dp,
    xFrac: Float,
    yFrac: Float,
    widthFrac: Float,
    value: String,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(color = Color.White, fontSize = 11.sp),
        cursorBrush = SolidColor(Color.White),
        modifier = Modifier
            .offset(x = boxW * xFrac, y = boxH * yFrac)
            .width(boxW * widthFrac)
    )
}
