package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class TemplateInfo(
    val id: String,
    val label: String,
    val accent: Color,
    val bg: Color
)

private val resumeTemplates = listOf(
    TemplateInfo("resume_template_01", "01", Color(0xFFD4AF37), Color(0xFF0E0E0E)),
    TemplateInfo("resume_template_02", "02", Color(0xFFFFFFFF), Color(0xFF1B3358)),
    TemplateInfo("resume_template_03", "03", Color(0xFF4CAF50), Color(0xFF0E2B1A)),
    TemplateInfo("resume_template_04", "04", Color(0xFFC9A227), Color(0xFF0E0E0E)),
    TemplateInfo("resume_template_05", "05", Color(0xFF9B6FE0), Color(0xFF1A0E2B)),
    TemplateInfo("resume_template_06", "06", Color(0xFF2E7D6B), Color(0xFF0E2020)),
    TemplateInfo("resume_template_07", "07", Color(0xFFD4AF37), Color(0xFF0E0E0E)),
    TemplateInfo("resume_template_08", "08", Color(0xFF2F4B7C), Color(0xFF0E1730)),
    TemplateInfo("resume_template_09", "09", Color(0xFFC9A227), Color(0xFF0E0E0E)),
    TemplateInfo("resume_template_10", "10", Color(0xFFCC2B2B), Color(0xFF2B0E0E)),
    TemplateInfo("resume_template_11", "11", Color(0xFF2E5E3E), Color(0xFF0E1E14)),
    TemplateInfo("resume_template_12", "12", Color(0xFFB744C4), Color(0xFF20102B)),
    TemplateInfo("resume_template_13", "13", Color(0xFF6B8E4E), Color(0xFF14200E)),
    TemplateInfo("resume_template_14", "14", Color(0xFFB794F6), Color(0xFF1A1330)),
    TemplateInfo("resume_template_15", "15", Color(0xFF1B3358), Color(0xFF0B1530)),
    TemplateInfo("resume_template_16", "16", Color(0xFFD4AF37), Color(0xFF0E0E0E)),
    TemplateInfo("resume_template_17", "17", Color(0xFF33CCCC), Color(0xFF0E2626)),
    TemplateInfo("resume_template_18", "18", Color(0xFFB8860B), Color(0xFF1A1408)),
    TemplateInfo("resume_template_19", "19", Color(0xFFCC3355), Color(0xFF250E14)),
    TemplateInfo("resume_template_20", "20", Color(0xFF2E7D6B), Color(0xFF0E2020)),
    TemplateInfo("resume_template_21", "21", Color(0xFF7EC8E3), Color(0xFF0E1E26)),
    TemplateInfo("resume_template_22", "22", Color(0xFFFF8A65), Color(0xFF2B160E)),
    TemplateInfo("resume_template_23", "23", Color(0xFF80DEEA), Color(0xFF0E1F26)),
)

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
                items(resumeTemplates, key = { it.id }) { t ->
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(t.bg)
                            .border(1.dp, t.accent.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                            .clickable { onTemplateSelected(t.id) }
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            MiniResumePreview(
                                accent = t.accent,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(t.bg)
                                    .border(width = 1.dp, color = t.accent.copy(alpha = 0.4f))
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text("Template ${t.label}", color = t.accent, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Code-drawn mock resume preview: a circle (photo placeholder) + header bar
 * + two columns of line bars. No image assets needed — purely Compose shapes.
 */
@Composable
private fun MiniResumePreview(accent: Color, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, accent, CircleShape)
            )
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .background(Color.White.copy(alpha = 0.85f))
            )
        }
        Spacer(Modifier.height(10.dp))
        repeat(3) {
            Row(modifier = Modifier.fillMaxWidth()) {
                MiniColumn(accent = accent, modifier = Modifier.weight(1f))
                Spacer(Modifier.width(6.dp))
                MiniColumn(accent = accent, modifier = Modifier.weight(1f))
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun MiniColumn(accent: Color, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(4.dp)
                .background(accent)
        )
        Spacer(Modifier.height(4.dp))
        repeat(3) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(if (it == 2) 0.5f else 1f)
                    .height(3.dp)
                    .background(Color.White.copy(alpha = 0.5f))
            )
            Spacer(Modifier.height(3.dp))
        }
    }
}

