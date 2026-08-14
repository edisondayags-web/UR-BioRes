package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class TemplateInfo(val id: String, val label: String, val color: Color)

private val resumeTemplates = listOf(
    TemplateInfo("resume_template_01", "01", Color(0xFFD4AF37)),
    TemplateInfo("resume_template_02", "02", Color(0xFF1B3358)),
    TemplateInfo("resume_template_03", "03", Color(0xFF4CAF50)),
    TemplateInfo("resume_template_04", "04", Color(0xFFC9A227)),
    TemplateInfo("resume_template_05", "05", Color(0xFF9B6FE0)),
    TemplateInfo("resume_template_06", "06", Color(0xFF2E7D6B)),
    TemplateInfo("resume_template_07", "07", Color(0xFFD4AF37)),
    TemplateInfo("resume_template_08", "08", Color(0xFF2F4B7C)),
    TemplateInfo("resume_template_09", "09", Color(0xFFC9A227)),
    TemplateInfo("resume_template_10", "10", Color(0xFFCC2B2B)),
    TemplateInfo("resume_template_11", "11", Color(0xFF2E5E3E)),
    TemplateInfo("resume_template_12", "12", Color(0xFFB744C4)),
    TemplateInfo("resume_template_13", "13", Color(0xFF6B8E4E)),
    TemplateInfo("resume_template_14", "14", Color(0xFFB794F6)),
    TemplateInfo("resume_template_15", "15", Color(0xFF1B3358)),
    TemplateInfo("resume_template_16", "16", Color(0xFFD4AF37)),
    TemplateInfo("resume_template_17", "17", Color(0xFF33CCCC)),
    TemplateInfo("resume_template_18", "18", Color(0xFFB8860B)),
    TemplateInfo("resume_template_19", "19", Color(0xFFCC3355)),
    TemplateInfo("resume_template_20", "20", Color(0xFF2E7D6B)),
)

private val sampleData = ResumeTemplateFields(
    fullName = "Juan Dela Cruz",
    professionalTitle = "Software Developer",
    phone = "+63 912 345 6789",
    email = "juan@email.com",
    location = "Manila, Philippines",
    aboutMe = "Passionate developer with experience building mobile apps.",
    edu1Degree = "BS Computer Science", edu1School = "State University", edu1Years = "2018-2022",
    skill1 = "Kotlin", skill2 = "Android", skill3 = "Firebase",
    exp1Position = "Junior Developer", exp1Company = "Tech Co.", exp1Dates = "2022-Present"
)

@Composable
private fun MiniTemplatePreview(templateId: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .layout { measurable, constraints ->
                val fullWidth = 800
                val fullHeight = 1130
                val placeable = measurable.measure(
                    androidx.compose.ui.unit.Constraints.fixed(fullWidth, fullHeight)
                )
                val scale = constraints.maxWidth.toFloat() / fullWidth.toFloat()
                layout(constraints.maxWidth, (fullHeight * scale).toInt()) {
                    placeable.placeWithLayer(0, 0) {
                        scaleX = scale
                        scaleY = scale
                        transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 0f)
                    }
                }
            }
    ) {
        val data = remember { mutableStateOf(sampleData) }
        when (templateId) {
            "resume_template_01" -> ResumeTemplate01Screen(data.value) {}
            "resume_template_02" -> ResumeTemplate02Screen(data.value) {}
            "resume_template_03" -> ResumeTemplate03Screen(data.value) {}
            "resume_template_04" -> ResumeTemplate04Screen(data.value) {}
            "resume_template_05" -> ResumeTemplate05Screen(data.value) {}
            "resume_template_06" -> ResumeTemplate06Screen(data.value) {}
            "resume_template_07" -> ResumeTemplate07Screen(data.value) {}
            "resume_template_08" -> ResumeTemplate08Screen(data.value) {}
            "resume_template_09" -> ResumeTemplate09Screen(data.value) {}
            "resume_template_10" -> ResumeTemplate10Screen(data.value) {}
            "resume_template_11" -> ResumeTemplate11Screen(data.value) {}
            "resume_template_12" -> ResumeTemplate12Screen(data.value) {}
            "resume_template_13" -> ResumeTemplate13Screen(data.value) {}
            "resume_template_14" -> ResumeTemplate14Screen(data.value) {}
            "resume_template_15" -> ResumeTemplate15Screen(data.value) {}
            "resume_template_16" -> ResumeTemplate16Screen(data.value) {}
            "resume_template_17" -> ResumeTemplate17Screen(data.value) {}
            "resume_template_18" -> ResumeTemplate18Screen(data.value) {}
            "resume_template_19" -> ResumeTemplate19Screen(data.value) {}
            "resume_template_20" -> ResumeTemplate20Screen(data.value) {}
        }
    }
}

@Composable
fun ResumeTemplateGalleryScreen(
    onTemplateSelected: (String) -> Unit,
    onBack: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0B1530))) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(Modifier.width(8.dp))
                Text("Choose a Template", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(resumeTemplates) { t ->
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF14213D))
                            .border(1.dp, t.color.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                            .clickable { onTemplateSelected(t.id) }
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                            ) {
                                MiniTemplatePreview(t.id)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(8.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text("Template ${t.label}", color = t.color, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
