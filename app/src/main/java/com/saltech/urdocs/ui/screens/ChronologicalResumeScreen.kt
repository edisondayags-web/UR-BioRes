package com.saltech.urdocs.ui.screens

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Picture
import android.os.Build
import android.provider.MediaStore
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.saltech.urdocs.R
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.scale

/**
 * "Chronological" na Resume -- tech/CV style, walang photo box.
 * NAME: line sa taas, contact row (phone | email | location | github),
 * Profile / Education / Technical Skills / Personal Skills / References
 * sa kaliwa, Work Experience / Projects / Certifications / Declaration
 * sa kanan. Parehong "papel" + pinch-zoom + Download pattern.
 */
data class ChronoWorkEntry(
    val role: String = "",
    val from: String = "",
    val to: String = "",
    val bullets: List<String> = List(5) { "" }
)

data class ChronoProjectEntry(
    val name: String = "",
    val status: String = "",
    val bullets: List<String> = List(3) { "" }
)

data class ChronologicalResumeFields(
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val location: String = "",
    val github: String = "",
    val profile: List<String> = List(5) { "" },
    val eduDegree: String = "",
    val eduSchool: String = "",
    val eduCity: String = "",
    val eduYear: String = "",
    val programmingLangs: List<String> = List(3) { "" },
    val frameworks: List<String> = List(3) { "" },
    val tools: List<String> = List(3) { "" },
    val otherSkills: List<String> = List(3) { "" },
    val personalSkills: List<String> = List(4) { "" },
    val work: List<ChronoWorkEntry> = List(3) { ChronoWorkEntry() },
    val projects: List<ChronoProjectEntry> = List(3) { ChronoProjectEntry() },
    val certifications: List<String> = List(3) { "" },
    val signatureName: String = "",
    val date: String = ""
)

@Composable
fun ChronologicalResumeScreen() {
    val paperWidthDp = 850.dp
    val paperHeightDp = 1250.dp

    var data by remember { mutableStateOf(ChronologicalResumeFields()) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val context = LocalContext.current
    val picture = remember { Picture() }
    val coroutineScope = rememberCoroutineScope()

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize().background(Color(0xFFCFCFCF))
    ) {
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
                    val width = this.size.width.toInt().coerceAtLeast(1)
                    val height = this.size.height.toInt().coerceAtLeast(1)
                    onDrawWithContent {
                        val pictureCanvas = androidx.compose.ui.graphics.Canvas(
                            picture.beginRecording(width, height)
                        )
                        draw(this, this.layoutDirection, pictureCanvas, this.size) {
                            this@onDrawWithContent.drawContent()
                        }
                        picture.endRecording()
                        drawIntoCanvas { canvas -> canvas.nativeCanvas.drawPicture(picture) }
                    }
                }
                .border(1.dp, Color.Black)
                .background(Color.White)
                .padding(28.dp)
        ) {
            Image(  
               painter = painterResource(id = R.drawable.ic_launcher_bg),  
                contentDescription = null,  
                 modifier = Modifier.fillMaxSize().scale(1.1f),  
                  contentScale = ContentScale.Crop  
        )
            Column(modifier = Modifier.fillMaxSize()) {

                // ===== HEADER =====
                FieldLine("NAME", data.name, bigLabel = true) { data = data.copy(name = it) }
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ContactField("📞", data.phone, Modifier.weight(1f)) { data = data.copy(phone = it) }
                    Text(" | ", color = Color.Black)
                    ContactField("✉", data.email, Modifier.weight(1f)) { data = data.copy(email = it) }
                    Text(" | ", color = Color.Black)
                    ContactField("📍", data.location, Modifier.weight(1f)) { data = data.copy(location = it) }
                    Text(" | ", color = Color.Black)
                    ContactField("🐙", data.github, Modifier.weight(1f)) { data = data.copy(github = it) }
                }
                Spacer(Modifier.height(6.dp))
                Spacer(Modifier.fillMaxWidth().height(1.dp).background(Color.Black))
                Spacer(Modifier.height(18.dp))

                // ===== TWO COLUMNS =====
                Row(modifier = Modifier.weight(1f)) {
                    // LEFT COLUMN
                    Column(modifier = Modifier.weight(1f)) {
                        SectionHeader2("👤", "PROFILE")
                        MultiLineField2(data.profile) { idx, v ->
                            data = data.copy(profile = data.profile.toMutableList().also { it[idx] = v })
                        }

                        Spacer(Modifier.height(14.dp))
                        SectionHeader2("🎓", "EDUCATION")
                        FieldLine("Degree/Course", data.eduDegree) { data = data.copy(eduDegree = it) }
                        FieldLine("School/University", data.eduSchool) { data = data.copy(eduSchool = it) }
                        FieldLine("City, Country", data.eduCity) { data = data.copy(eduCity = it) }
                        FieldLine("Year", data.eduYear) { data = data.copy(eduYear = it) }

                        Spacer(Modifier.height(14.dp))
                        SectionHeader2("⚙", "TECHNICAL SKILLS")
                        Text("Programming Languages:", fontSize = 12.sp, color = Color.Black, modifier = Modifier.padding(top = 4.dp))
                        BulletLines2(data.programmingLangs) { idx, v ->
                            data = data.copy(programmingLangs = data.programmingLangs.toMutableList().also { it[idx] = v })
                        }
                        Text("Frameworks & Libraries:", fontSize = 12.sp, color = Color.Black, modifier = Modifier.padding(top = 8.dp))
                        BulletLines2(data.frameworks) { idx, v ->
                            data = data.copy(frameworks = data.frameworks.toMutableList().also { it[idx] = v })
                        }
                        Text("Tools & Technologies:", fontSize = 12.sp, color = Color.Black, modifier = Modifier.padding(top = 8.dp))
                        BulletLines2(data.tools) { idx, v ->
                            data = data.copy(tools = data.tools.toMutableList().also { it[idx] = v })
                        }
                        Text("Other Skills:", fontSize = 12.sp, color = Color.Black, modifier = Modifier.padding(top = 8.dp))
                        BulletLines2(data.otherSkills) { idx, v ->
                            data = data.copy(otherSkills = data.otherSkills.toMutableList().also { it[idx] = v })
                        }

                        Spacer(Modifier.height(14.dp))
                        SectionHeader2("⭐", "PERSONAL SKILLS")
                        BulletLines2(data.personalSkills) { idx, v ->
                            data = data.copy(personalSkills = data.personalSkills.toMutableList().also { it[idx] = v })
                        }

                        Spacer(Modifier.height(14.dp))
                        SectionHeader2("👤", "REFERENCES")
                        Text(
                            "Available upon request.",
                            fontSize = 12.sp, fontStyle = FontStyle.Italic, color = Color.Black,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Spacer(Modifier.width(24.dp))

                    // RIGHT COLUMN
                    Column(modifier = Modifier.weight(1f)) {
                        SectionHeader2("💼", "WORK EXPERIENCE")
                        data.work.forEachIndexed { i, entry ->
                            Row(modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
                                MiniField2("", entry.role, Modifier.weight(1f)) { v ->
                                    data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(role = v) })
                                }
                                Spacer(Modifier.width(8.dp))
                                MiniField2("", entry.from, Modifier.width(70.dp)) { v ->
                                    data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(from = v) })
                                }
                                Text(" - ", fontSize = 12.sp, color = Color.Black)
                                MiniField2("", entry.to, Modifier.width(70.dp)) { v ->
                                    data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(to = v) })
                                }
                            }
                            Text("(Position / Role)", fontSize = 10.sp, fontStyle = FontStyle.Italic, color = Color.Black)
                            BulletLines2(entry.bullets) { idx, v ->
                                val newBullets = entry.bullets.toMutableList().also { it[idx] = v }
                                data = data.copy(work = data.work.toMutableList().also { it[i] = entry.copy(bullets = newBullets) })
                            }
                            if (i != data.work.lastIndex) Spacer(Modifier.height(12.dp))
                        }

                        Spacer(Modifier.height(14.dp))
                        SectionHeader2("📁", "PROJECTS")
                        data.projects.forEachIndexed { i, entry ->
                            Row(modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
                                MiniField2("", entry.name, Modifier.weight(1f)) { v ->
                                    data = data.copy(projects = data.projects.toMutableList().also { it[i] = entry.copy(name = v) })
                                }
                            }
                            Text("(In Progress / In Development / Completed)", fontSize = 10.sp, fontStyle = FontStyle.Italic, color = Color.Black)
                            BulletLines2(entry.bullets) { idx, v ->
                                val newBullets = entry.bullets.toMutableList().also { it[idx] = v }
                                data = data.copy(projects = data.projects.toMutableList().also { it[i] = entry.copy(bullets = newBullets) })
                            }
                            if (i != data.projects.lastIndex) Spacer(Modifier.height(12.dp))
                        }

                        Spacer(Modifier.height(14.dp))
                        SectionHeader2("🏅", "CERTIFICATIONS")
                        BulletLines2(data.certifications) { idx, v ->
                            data = data.copy(certifications = data.certifications.toMutableList().also { it[idx] = v })
                        }

                        Spacer(Modifier.height(14.dp))
                        SectionHeader2("✏", "DECLARATION")
                        Text(
                            "I hereby declare that the information above is true and correct to the best of my knowledge and belief.",
                            fontSize = 11.sp, color = Color.Black, modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                        )
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            Column(horizontalAlignment = Alignment.End) {
                                MiniField2("", data.signatureName, Modifier.width(180.dp)) { data = data.copy(signatureName = it) }
                                Text("(Signature)", fontSize = 10.sp, fontStyle = FontStyle.Italic, color = Color.Black)
                                Spacer(Modifier.height(6.dp))
                                MiniField2("", data.date, Modifier.width(180.dp)) { data = data.copy(date = it) }
                                Text("(Date)", fontSize = 10.sp, fontStyle = FontStyle.Italic, color = Color.Black)
                            }
                        }
                    }
                }
            }
        }

        // Download button -- laging nakikita, hindi kasama sa zoom/pan
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
                    saveBitmapToGalleryChrono(context, bitmap)
                }
            },
            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
        ) { Text("Download") }
    }
}

private fun saveBitmapToGalleryChrono(context: android.content.Context, bitmap: Bitmap) {
    val filename = "Resume_Chronological_${System.currentTimeMillis()}.png"
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
    }
}

// ================== Reusable "papel" pieces ==================

private fun Modifier.bottomLine2(color: Color = Color.Black, thickness: Dp = 1.dp): Modifier =
    this.height(thickness).drawBehind { drawRect(color = color) }

@Composable
private fun SectionHeader2(icon: String, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 4.dp)) {
        Text("$icon  ", fontSize = 14.sp)
        Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
    }
    Spacer(Modifier.fillMaxWidth().height(1.dp).background(Color.Black))
    Spacer(Modifier.height(6.dp))
}

@Composable
private fun FieldLine(label: String, value: String, bigLabel: Boolean = false, onChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    Column(modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                "$label: ",
                fontSize = if (bigLabel) 20.sp else 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            BasicTextField(
                value = value, onValueChange = onChange,
                textStyle = TextStyle(fontSize = if (bigLabel) 20.sp else 12.sp, color = Color.Black),
                cursorBrush = SolidColor(Color.Black),
                interactionSource = interactionSource,
                modifier = Modifier.weight(1f).background(if (isFocused) Color(0xFFFFF3CD) else Color.Transparent)
            )
        }
        Spacer(Modifier.height(2.dp))
        Spacer(Modifier.fillMaxWidth().bottomLine2())
    }
}

@Composable
private fun ContactField(icon: String, value: String, modifier: Modifier = Modifier, onChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text("$icon  ", fontSize = 12.sp)
            BasicTextField(
                value = value, onValueChange = onChange,
                textStyle = TextStyle(fontSize = 11.sp, color = Color.Black),
                cursorBrush = SolidColor(Color.Black),
                interactionSource = interactionSource,
                modifier = Modifier.weight(1f).background(if (isFocused) Color(0xFFFFF3CD) else Color.Transparent)
            )
        }
        Spacer(Modifier.height(2.dp))
        Spacer(Modifier.fillMaxWidth().bottomLine2())
    }
}

@Composable
private fun MiniField2(label: String, value: String, modifier: Modifier = Modifier, onChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.Bottom) {
            if (label.isNotEmpty()) Text("$label: ", fontSize = 12.sp, color = Color.Black)
            BasicTextField(
                value = value, onValueChange = onChange,
                textStyle = TextStyle(fontSize = 12.sp, color = Color.Black),
                cursorBrush = SolidColor(Color.Black),
                interactionSource = interactionSource,
                modifier = Modifier.weight(1f).background(if (isFocused) Color(0xFFFFF3CD) else Color.Transparent)
            )
        }
        Spacer(Modifier.height(2.dp))
        Spacer(Modifier.fillMaxWidth().bottomLine2())
    }
}

@Composable
private fun MultiLineField2(values: List<String>, onChange: (Int, String) -> Unit) {
    Column {
        values.forEachIndexed { i, v ->
            val interactionSource = remember { MutableInteractionSource() }
            val isFocused by interactionSource.collectIsFocusedAsState()
            BasicTextField(
                value = v, onValueChange = { onChange(i, it) },
                textStyle = TextStyle(fontSize = 12.sp, color = Color.Black),
                cursorBrush = SolidColor(Color.Black),
                interactionSource = interactionSource,
                modifier = Modifier.fillMaxWidth().background(if (isFocused) Color(0xFFFFF3CD) else Color.Transparent)
            )
            Spacer(Modifier.height(2.dp))
            Spacer(Modifier.fillMaxWidth().bottomLine2())
            Spacer(Modifier.height(4.dp))
        }
    }
}

@Composable
private fun BulletLines2(values: List<String>, onChange: (Int, String) -> Unit) {
    Column {
        values.forEachIndexed { i, v ->
            val interactionSource = remember { MutableInteractionSource() }
            val isFocused by interactionSource.collectIsFocusedAsState()
            Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(top = 4.dp)) {
                Text("•  ", fontSize = 12.sp, color = Color.Black)
                BasicTextField(
                    value = v, onValueChange = { onChange(i, it) },
                    textStyle = TextStyle(fontSize = 12.sp, color = Color.Black),
                    cursorBrush = SolidColor(Color.Black),
                    interactionSource = interactionSource,
                    modifier = Modifier.weight(1f).background(if (isFocused) Color(0xFFFFF3CD) else Color.Transparent)
                )
            }
            Spacer(Modifier.height(2.dp))
            Spacer(Modifier.fillMaxWidth().padding(start = 16.dp).bottomLine2())
        }
    }
}
