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

data class BioDataTemplateFields(
    val fullName: String = "",
    val dob: String = "",
    val placeOfBirth: String = "",
    val civilStatus: String = "",
    val nationality: String = "",
    val religion: String = "",
    val contactNo: String = "",
    val email: String = "",
    val currentAddress: String = "",
    val eduLevel: String = "",
    val eduSchool: String = "",
    val eduYearGraduated: String = "",
    val workCompany: String = "",
    val workPosition: String = "",
    val workInclusiveDates: String = "",
    val skill1: String = "",
    val skill2: String = "",
    val skill3: String = "",
    val skill4: String = "",
    val ref1: String = "",
    val ref2: String = "",
    val ref3: String = ""
)

@Composable
fun BioDataTemplateFormScreen(
    templateName: String,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    var data by remember { mutableStateOf(BioDataTemplateFields()) }
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
            val aspect = if (intrinsic.width > 0f && intrinsic.height > 0f) intrinsic.width / intrinsic.height else 0.9f

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

                OverlayField(boxW, boxH, 0.285f, 0.322f, 0.18f, data.fullName) { data = data.copy(fullName = it) }
                OverlayField(boxW, boxH, 0.285f, 0.357f, 0.18f, data.dob) { data = data.copy(dob = it) }
                OverlayField(boxW, boxH, 0.285f, 0.391f, 0.18f, data.placeOfBirth) { data = data.copy(placeOfBirth = it) }
                OverlayField(boxW, boxH, 0.285f, 0.426f, 0.18f, data.civilStatus) { data = data.copy(civilStatus = it) }
                OverlayField(boxW, boxH, 0.285f, 0.460f, 0.18f, data.nationality) { data = data.copy(nationality = it) }
                OverlayField(boxW, boxH, 0.285f, 0.494f, 0.18f, data.religion) { data = data.copy(religion = it) }
                OverlayField(boxW, boxH, 0.285f, 0.529f, 0.18f, data.contactNo) { data = data.copy(contactNo = it) }
                OverlayField(boxW, boxH, 0.285f, 0.563f, 0.18f, data.email) { data = data.copy(email = it) }
                OverlayField(boxW, boxH, 0.285f, 0.598f, 0.18f, data.currentAddress) { data = data.copy(currentAddress = it) }
                OverlayField(boxW, boxH, 0.505f, 0.395f, 0.13f, data.eduLevel) { data = data.copy(eduLevel = it) }
                OverlayField(boxW, boxH, 0.650f, 0.395f, 0.13f, data.eduSchool) { data = data.copy(eduSchool = it) }
                OverlayField(boxW, boxH, 0.790f, 0.395f, 0.14f, data.eduYearGraduated) { data = data.copy(eduYearGraduated = it) }
                OverlayField(boxW, boxH, 0.500f, 0.564f, 0.15f, data.workCompany) { data = data.copy(workCompany = it) }
                OverlayField(boxW, boxH, 0.700f, 0.564f, 0.10f, data.workPosition) { data = data.copy(workPosition = it) }
                OverlayField(boxW, boxH, 0.830f, 0.564f, 0.14f, data.workInclusiveDates) { data = data.copy(workInclusiveDates = it) }
                OverlayField(boxW, boxH, 0.140f, 0.744f, 0.14f, data.skill1) { data = data.copy(skill1 = it) }
                OverlayField(boxW, boxH, 0.140f, 0.774f, 0.14f, data.skill2) { data = data.copy(skill2 = it) }
                OverlayField(boxW, boxH, 0.320f, 0.744f, 0.14f, data.skill3) { data = data.copy(skill3 = it) }
                OverlayField(boxW, boxH, 0.320f, 0.774f, 0.14f, data.skill4) { data = data.copy(skill4 = it) }
                OverlayField(boxW, boxH, 0.500f, 0.717f, 0.32f, data.ref1) { data = data.copy(ref1 = it) }
                OverlayField(boxW, boxH, 0.500f, 0.756f, 0.32f, data.ref2) { data = data.copy(ref2 = it) }
                OverlayField(boxW, boxH, 0.500f, 0.795f, 0.32f, data.ref3) { data = data.copy(ref3 = it) }
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
        textStyle = TextStyle(color = Color.White, fontSize = 13.sp),
        cursorBrush = SolidColor(Color.White),
        modifier = Modifier
            .offset(x = boxW * xFrac, y = boxH * yFrac)
            .width(boxW * widthFrac)
    )
}
