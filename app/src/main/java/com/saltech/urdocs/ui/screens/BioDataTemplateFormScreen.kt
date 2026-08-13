package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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

// Reference canvas size — matched to the template's natural proportions (roughly 768x700)
private val CANVAS_W = 768.dp
private val CANVAS_H = 700.dp

@Composable
fun BioDataTemplateFormScreen(
    templateName: String,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    var data by remember { mutableStateOf(BioDataTemplateFields()) }
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val resId = remember(templateName) {
        context.resources.getIdentifier(templateName, "drawable", context.packageName)
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0B1530))) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(8.dp)
                .align(Alignment.TopStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 56.dp)
        ) {
            val fitScale = minOf(maxWidth / CANVAS_W, maxHeight / CANVAS_H)

            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceIn(1f, 3f)
                            offset = if (scale <= 1f) Offset.Zero else offset + pan
                        }
                    }
                    .graphicsLayer(
                        scaleX = fitScale * scale,
                        scaleY = fitScale * scale,
                        translationX = offset.x,
                        translationY = offset.y,
                        transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0.5f, 0f)
                    )
                    .requiredWidth(CANVAS_W)
                    .requiredHeight(CANVAS_H)
            ) {
                if (resId != 0) {
                    Image(
                        painter = painterResource(id = resId),
                        contentDescription = templateName,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Left column - personal information
                OverlayField(0.285f, 0.322f, 0.18f, data.fullName) { data = data.copy(fullName = it) }
                OverlayField(0.285f, 0.357f, 0.18f, data.dob) { data = data.copy(dob = it) }
                OverlayField(0.285f, 0.391f, 0.18f, data.placeOfBirth) { data = data.copy(placeOfBirth = it) }
                OverlayField(0.285f, 0.426f, 0.18f, data.civilStatus) { data = data.copy(civilStatus = it) }
                OverlayField(0.285f, 0.460f, 0.18f, data.nationality) { data = data.copy(nationality = it) }
                OverlayField(0.285f, 0.494f, 0.18f, data.religion) { data = data.copy(religion = it) }
                OverlayField(0.285f, 0.529f, 0.18f, data.contactNo) { data = data.copy(contactNo = it) }
                OverlayField(0.285f, 0.563f, 0.18f, data.email) { data = data.copy(email = it) }
                OverlayField(0.285f, 0.598f, 0.18f, data.currentAddress) { data = data.copy(currentAddress = it) }

                // Education table (right side)
                OverlayField(0.505f, 0.395f, 0.13f, data.eduLevel) { data = data.copy(eduLevel = it) }
                OverlayField(0.650f, 0.395f, 0.13f, data.eduSchool) { data = data.copy(eduSchool = it) }
                OverlayField(0.790f, 0.395f, 0.14f, data.eduYearGraduated) { data = data.copy(eduYearGraduated = it) }

                // Work experience table
                OverlayField(0.500f, 0.564f, 0.15f, data.workCompany) { data = data.copy(workCompany = it) }
                OverlayField(0.700f, 0.564f, 0.10f, data.workPosition) { data = data.copy(workPosition = it) }
                OverlayField(0.830f, 0.564f, 0.14f, data.workInclusiveDates) { data = data.copy(workInclusiveDates = it) }

                // Skills (two columns of bullets)
                OverlayField(0.140f, 0.744f, 0.14f, data.skill1) { data = data.copy(skill1 = it) }
                OverlayField(0.140f, 0.774f, 0.14f, data.skill2) { data = data.copy(skill2 = it) }
                OverlayField(0.320f, 0.744f, 0.14f, data.skill3) { data = data.copy(skill3 = it) }
                OverlayField(0.320f, 0.774f, 0.14f, data.skill4) { data = data.copy(skill4 = it) }

                // References (three lines)
                OverlayField(0.500f, 0.717f, 0.32f, data.ref1) { data = data.copy(ref1 = it) }
                OverlayField(0.500f, 0.756f, 0.32f, data.ref2) { data = data.copy(ref2 = it) }
                OverlayField(0.500f, 0.795f, 0.32f, data.ref3) { data = data.copy(ref3 = it) }
            }
        }
    }
}

/**
 * xFrac / yFrac = position as a fraction of the canvas width/height (0f to 1f)
 * widthFrac = field width as a fraction of canvas width
 */
@Composable
private fun androidx.compose.foundation.layout.BoxScope.OverlayField(
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
            .offset(x = CANVAS_W * xFrac, y = CANVAS_H * yFrac)
            .width(CANVAS_W * widthFrac)
    )
}
