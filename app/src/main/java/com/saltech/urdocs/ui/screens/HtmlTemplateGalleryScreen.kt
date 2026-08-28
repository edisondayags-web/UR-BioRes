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

private data class HtmlColorInfo(
    val fileName: String,
    val label: String,
    val accent: Color
)

private val htmlColorTemplates = listOf(
    HtmlColorInfo("ai_template_02.html", "Blue", Color(0xFF4FC3F7)),
    HtmlColorInfo("ai_template_03_green.html", "Green", Color(0xFF4FF3A0)),
    HtmlColorInfo("ai_template_04_purple.html", "Purple", Color(0xFFB14FF3)),
    HtmlColorInfo("ai_template_05_maroon.html", "Maroon", Color(0xFFF34F6C)),
    HtmlColorInfo("ai_template_06_orange.html", "Orange", Color(0xFFF3A94F)),
    HtmlColorInfo("ai_template_07_teal.html", "Teal", Color(0xFF4FE0F3)),
    HtmlColorInfo("ai_template_08_pink.html", "Pink", Color(0xFFF34FC0)),
    HtmlColorInfo("ai_template_09_gray.html", "Gray", Color(0xFFB0B0B0)),
    HtmlColorInfo("ai_template_10_gold.html", "Gold", Color(0xFFF3D14F)),
    HtmlColorInfo("ai_template_11_indigo.html", "Indigo", Color(0xFF6B4FF3))
)

@Composable
fun HtmlTemplateGalleryScreen(
    onTemplateSelected: (String) -> Unit,
    onBack: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF0B0F1A))) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp, start = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text("Pili ng Kulay luv", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(htmlColorTemplates) { info ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onTemplateSelected(info.fileName) }
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(info.accent)
                            .border(1.5.dp, Color.White.copy(alpha = 0.4f), CircleShape)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(info.label, color = Color.White, fontSize = 11.sp)
                }
            }
        }
    }
}
