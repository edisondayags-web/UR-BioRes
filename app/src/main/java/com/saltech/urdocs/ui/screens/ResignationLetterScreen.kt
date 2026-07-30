package com.saltech.urdocs.ui.screens

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Picture
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saltech.urdocs.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val RLBlue = Color(0xFF1D3FB5)
private val RLBlueDark = Color(0xFF16309C)

data class ResignationFields(
    val date: String = "",
    val to1: String = "",
    val to2: String = "",
    val position: String = "",
    val effectiveDate: String = "",
    val name: String = "",
    val employeeId: String = "",
    val department: String = "",
    val signature: String = ""
)

@Composable
fun ResignationLetterScreen() {
    val paperWidthDp = 850.dp
    val paperHeightDp = 1250.dp
    val context = LocalContext.current
    var data by remember { mutableStateOf(ResignationFields()) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val picture = remember { Picture() }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints(modifier = Modifier.weight(1f).background(Color.Black)) {
            val fitScale = minOf(maxWidth / paperWidthDp, maxHeight / paperHeightDp)
            var scale by remember { mutableStateOf(fitScale) }

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceIn(fitScale, 4f)
                            offset = if (scale <= fitScale) Offset.Zero else offset + pan
                        }
                    }
                    .graphicsLayer(
                        scaleX = scale, scaleY = scale,
                        translationX = offset.x, translationY = offset.y
                    )
                    .requiredWidth(paperWidthDp)
                    .requiredHeight(paperHeightDp)
                    .drawWithCache {
                        val w = size.width.toInt().coerceAtLeast(1)
                        val h = size.height.toInt().coerceAtLeast(1)
                        onDrawWithContent {
                            val pictureCanvas = androidx.compose.ui.graphics.Canvas(picture.beginRecording(w, h))
                            draw(this, this.layoutDirection, pictureCanvas, this.size) {
                                this@onDrawWithContent.drawContent()
                            }
                            picture.endRecording()
                            drawIntoCanvas { canvas -> canvas.nativeCanvas.drawPicture(picture) }
                        }
                    }
                    .background(Color.White)
            ) {
                // Double border frame
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val inset1 = 14.dp.toPx()
                    val inset2 = 22.dp.toPx()
                    drawRect(color = RLBlue, topLeft = Offset(inset1, inset1),
                        size = androidx.compose.ui.geometry.Size(size.width - inset1 * 2, size.height - inset1 * 2),
                        style = Stroke(width = 2.dp.toPx()))
                    drawRect(color = RLBlue, topLeft = Offset(inset2, inset2),
                        size = androidx.compose.ui.geometry.Size(size.width - inset2 * 2, size.height - inset2 * 2),
                        style = Stroke(width = 1.dp.toPx()))
                }

                // Corner flowers - totoong image na, buong frame
                Image(
                    painter = painterResource(R.drawable.resignation_flowers),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 50.dp, vertical = 60.dp)
                ) {
                    Text(
                        "RESIGNATION LETTER",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 34.sp,
                        color = RLBlue,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    Spacer(Modifier.height(30.dp))
                    Spacer(Modifier.height(24.dp))

                    RLField("Date", data.date) { data = data.copy(date = it) }
                    Spacer(Modifier.height(14.dp))
                    RLText("To,")
                    RLText("The Manager / Principal")
                    RLLine(data.to1) { data = data.copy(to1 = it) }
                    RLLine(data.to2) { data = data.copy(to2 = it) }
                    Spacer(Modifier.height(14.dp))
                    RLText("Subject: Resignation Letter", bold = true)
                    Spacer(Modifier.height(10.dp))
                    RLText("Respected Sir/Madam,")
                    Spacer(Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.Bottom) {
                        RLText("I am writing to formally tender my resignation from my position as ")
                        RLInlineField(data.position, 140.dp) { data = data.copy(position = it) }
                        RLText(", effective ")
                        RLInlineField(data.effectiveDate, 140.dp) { data = data.copy(effectiveDate = it) }
                        RLText(".")
                    }
                    Spacer(Modifier.height(10.dp))
                    RLText("Please accept this letter as my official notice of resignation in accordance with the company's policy.")
                    Spacer(Modifier.height(14.dp))
                    RLText("I have truly valued the opportunities for growth and development that I have gained during my time here. I am grateful for the support, guidance, and encouragement I have received from you and the entire team.")
                    Spacer(Modifier.height(14.dp))
                    RLText("I will do my best to ensure a smooth transition by completing my assigned tasks and assisting in the turnover process before my last day.")
                    Spacer(Modifier.height(14.dp))
                    RLText("Thank you once again for the experience and for everything I have learned during my tenure.")
                    Spacer(Modifier.height(28.dp))
                    RLText("Yours sincerely,")
                    Spacer(Modifier.height(30.dp))

                    RLField("Name", data.name) { data = data.copy(name = it) }
                    RLField("Employee ID", data.employeeId) { data = data.copy(employeeId = it) }
                    RLField("Department", data.department) { data = data.copy(department = it) }
                    RLField("Signature", data.signature) { data = data.copy(signature = it) }
                }
            }

            Button(
                onClick = {
                    scale = fitScale
                    offset = Offset.Zero
                    coroutineScope.launch {
                        delay(100)
                        val bitmap = Bitmap.createBitmap(
                            picture.width.coerceAtLeast(1),
                            picture.height.coerceAtLeast(1),
                            Bitmap.Config.ARGB_8888
                        )
                        val canvas = android.graphics.Canvas(bitmap)
                        canvas.drawColor(android.graphics.Color.WHITE)
                        canvas.drawPicture(picture)
                        saveResignationToGallery(context, bitmap)
                    }
                },
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = RLBlue),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) { Text("Download", color = Color.White, fontWeight = FontWeight.Bold) }
        }
    }
}

@Composable
private fun RLText(text: String, bold: Boolean = false) {
    Text(
        text,
        fontFamily = FontFamily.Serif,
        fontStyle = FontStyle.Italic,
        fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
        fontSize = 14.sp,
        color = RLBlue,
        modifier = Modifier.padding(vertical = 2.dp)
    )
}

@Composable
private fun RLField(label: String, value: String, onChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(top = 6.dp)) {
        Text("$label: ", fontFamily = FontFamily.Serif, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = RLBlue)
        BasicTextField(
            value = value, onValueChange = onChange,
            textStyle = TextStyle(fontFamily = FontFamily.Serif, fontStyle = FontStyle.Italic, fontSize = 14.sp, color = RLBlue),
            cursorBrush = SolidColor(RLBlue),
            interactionSource = interactionSource,
            modifier = Modifier
                .width(220.dp)
                .background(if (isFocused) Color(0xFFEFF3FF) else Color.Transparent)
        )
    }
    Spacer(Modifier.fillMaxWidth(0.5f).height(1.dp).background(RLBlue))
}

@Composable
private fun RLLine(value: String, onChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    BasicTextField(
        value = value, onValueChange = onChange,
        textStyle = TextStyle(fontFamily = FontFamily.Serif, fontStyle = FontStyle.Italic, fontSize = 14.sp, color = RLBlue),
        cursorBrush = SolidColor(RLBlue),
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isFocused) Color(0xFFEFF3FF) else Color.Transparent)
    )
    Spacer(Modifier.fillMaxWidth().height(1.dp).background(RLBlue))
    Spacer(Modifier.height(4.dp))
}

@Composable
private fun RLInlineField(value: String, width: androidx.compose.ui.unit.Dp, onChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    BasicTextField(
        value = value, onValueChange = onChange,
        textStyle = TextStyle(fontFamily = FontFamily.Serif, fontStyle = FontStyle.Italic, fontSize = 14.sp, color = RLBlue),
        cursorBrush = SolidColor(RLBlue),
        interactionSource = interactionSource,
        modifier = Modifier
            .width(width)
            .background(if (isFocused) Color(0xFFEFF3FF) else Color.Transparent)
    )
}

private fun saveResignationToGallery(context: android.content.Context, bitmap: Bitmap) {
    val filename = "Resignation_${System.currentTimeMillis()}.png"
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/URDocs")
        }
    }
    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    uri?.let {
        resolver.openOutputStream(it)?.use { out -> bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) }
        android.widget.Toast.makeText(context, "see your gallery luv🩵", android.widget.Toast.LENGTH_LONG).show()
    } ?: run {
        android.widget.Toast.makeText(context, "Hindi na-download, subukan ulit.", android.widget.Toast.LENGTH_LONG).show()
    }
}
