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
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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

private val CANVAS_W = 570.dp
private val CANVAS_H = 780.dp

@Composable
fun ResumeTemplateFormScreen(
    templateName: String,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    var data by remember { mutableStateOf(ResumeTemplateFields()) }
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
                        transformOrigin = TransformOrigin(0.5f, 0f)
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

                // Header
                OverlayField(0.42f, 0.135f, 0.40f, data.fullName) { data = data.copy(fullName = it) }
                OverlayField(0.42f, 0.168f, 0.40f, data.professionalTitle) { data = data.copy(professionalTitle = it) }

                // Left column - CONTACT
                OverlayField(0.12f, 0.310f, 0.30f, data.phone) { data = data.copy(phone = it) }
                OverlayField(0.12f, 0.348f, 0.30f, data.email) { data = data.copy(email = it) }
                OverlayField(0.12f, 0.385f, 0.30f, data.location) { data = data.copy(location = it) }
                OverlayField(0.12f, 0.420f, 0.30f, data.linkedin) { data = data.copy(linkedin = it) }
                OverlayField(0.12f, 0.455f, 0.30f, data.website) { data = data.copy(website = it) }

                // Left column - SKILLS
                OverlayField(0.12f, 0.485f, 0.28f, data.skill1) { data = data.copy(skill1 = it) }
                OverlayField(0.12f, 0.512f, 0.28f, data.skill2) { data = data.copy(skill2 = it) }
                OverlayField(0.12f, 0.538f, 0.28f, data.skill3) { data = data.copy(skill3 = it) }
                OverlayField(0.12f, 0.564f, 0.28f, data.skill4) { data = data.copy(skill4 = it) }
                OverlayField(0.12f, 0.590f, 0.28f, data.skill5) { data = data.copy(skill5 = it) }

                // Left column - REFERENCES
                OverlayField(0.12f, 0.632f, 0.30f, data.refName) { data = data.copy(refName = it) }
                OverlayField(0.12f, 0.658f, 0.30f, data.refPositionCompany) { data = data.copy(refPositionCompany = it) }
                OverlayField(0.12f, 0.685f, 0.30f, data.refContact) { data = data.copy(refContact = it) }

                // Right column - ABOUT ME
                OverlayField(0.545f, 0.335f, 0.42f, data.aboutMe) { data = data.copy(aboutMe = it) }

                // Right column - EDUCATION
                OverlayField(0.545f, 0.393f, 0.42f, data.edu1Degree) { data = data.copy(edu1Degree = it) }
                OverlayField(0.545f, 0.412f, 0.30f, data.edu1School) { data = data.copy(edu1School = it) }
                OverlayField(0.86f, 0.412f, 0.12f, data.edu1Years) { data = data.copy(edu1Years = it) }
                OverlayField(0.545f, 0.437f, 0.42f, data.edu2Degree) { data = data.copy(edu2Degree = it) }
                OverlayField(0.545f, 0.456f, 0.30f, data.edu2School) { data = data.copy(edu2School = it) }
                OverlayField(0.86f, 0.456f, 0.12f, data.edu2Years) { data = data.copy(edu2Years = it) }

                // Right column - EXPERIENCE
                OverlayField(0.545f, 0.512f, 0.35f, data.exp1Position) { data = data.copy(exp1Position = it) }
                OverlayField(0.545f, 0.529f, 0.30f, data.exp1Company) { data = data.copy(exp1Company = it) }
                OverlayField(0.86f, 0.529f, 0.12f, data.exp1Dates) { data = data.copy(exp1Dates = it) }
                OverlayField(0.545f, 0.547f, 0.42f, data.exp1Desc) { data = data.copy(exp1Desc = it) }

                OverlayField(0.545f, 0.578f, 0.35f, data.exp2Position) { data = data.copy(exp2Position = it) }
                OverlayField(0.545f, 0.596f, 0.30f, data.exp2Company) { data = data.copy(exp2Company = it) }
                OverlayField(0.86f, 0.596f, 0.12f, data.exp2Dates) { data = data.copy(exp2Dates = it) }
            }
        }
    }
}

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
        textStyle = TextStyle(color = Color.White, fontSize = 12.sp),
        cursorBrush = SolidColor(Color.White),
        modifier = Modifier
            .offset(x = CANVAS_W * xFrac, y = CANVAS_H * yFrac)
            .width(CANVAS_W * widthFrac)
    )
}
