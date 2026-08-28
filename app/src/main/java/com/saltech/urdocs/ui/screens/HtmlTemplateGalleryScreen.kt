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
    val accent: Color,
    val contentBg: Color
)

private val htmlColorTemplates = listOf(
    HtmlColorInfo("ai_template_02.html", "Blue", Color(0xFF4FC3F7), Color.White),
    HtmlColorInfo("ai_template_03_green.html", "Green", Color(0xFF4FF3A0), Color.White),
    HtmlColorInfo("ai_template_04_purple.html", "Purple", Color(0xFFB14FF3), Color.White),
    HtmlColorInfo("ai_template_05_maroon.html", "Maroon", Color(0xFFF34F6C), Color.White),
    HtmlColorInfo("ai_template_06_orange.html", "Orange", Color(0xFFF3A94F), Color.White),
    HtmlColorInfo("ai_template_07_teal.html", "Teal", Color(0xFF4FE0F3), Color.White),
    HtmlColorInfo("ai_template_08_pink.html", "Pink", Color(0xFFF34FC0), Color.White),
    HtmlColorInfo("ai_template_09_gray.html", "Gray", Color(0xFFB0B0B0), Color.White),
    HtmlColorInfo("ai_template_10_gold.html", "Gold", Color(0xFFF3D14F), Color.White),
    HtmlColorInfo("ai_template_11_indigo.html", "Indigo", Color(0xFF6B4FF3), Color.White),
    HtmlColorInfo("ai_template_02_light.html", "Blue Light", Color(0xFF4FC3F7), Color(0xFFE4F6FD)),
    HtmlColorInfo("ai_template_03_green_light.html", "Green Light", Color(0xFF4FF3A0), Color(0xFFE4FDF0)),
    HtmlColorInfo("ai_template_04_purple_light.html", "Purple Light", Color(0xFFB14FF3), Color(0xFFF3E4FD)),
    HtmlColorInfo("ai_template_05_maroon_light.html", "Maroon Light", Color(0xFFF34F6C), Color(0xFFFDE4E8)),
    HtmlColorInfo("ai_template_06_orange_light.html", "Orange Light", Color(0xFFF3A94F), Color(0xFFFDF2E4)),
    HtmlColorInfo("ai_template_07_teal_light.html", "Teal Light", Color(0xFF4FE0F3), Color(0xFFE4FAFD)),
    HtmlColorInfo("ai_template_08_pink_light.html", "Pink Light", Color(0xFFF34FC0), Color(0xFFFDE4F5)),
    HtmlColorInfo("ai_template_09_gray_light.html", "Gray Light", Color(0xFFB0B0B0), Color(0xFFF3F3F3)),
    HtmlColorInfo("ai_template_10_gold_light.html", "Gold Light", Color(0xFFF3D14F), Color(0xFFFDF8E4)),
    HtmlColorInfo("ai_template_11_indigo_light.html", "Indigo Light", Color(0xFF6B4FF3), Color(0xFFE8E4FD))
)

@Composable
fun HtmlTemplateGalleryScreen(
    onTemplateSelected: (String) -> Unit,
    onBack: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0B1530))) {
        PremiumWaveBackground()
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
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(htmlColorTemplates, key = { it.fileName }) { t ->
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth()
                            .height(190.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(t.contentBg)
                            .border(1.dp, t.accent.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                            .clickable { onTemplateSelected(t.fileName) }
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            HtmlMiniPreview(
                                accent = t.accent,
                                contentBg = t.contentBg,
                                modifier = Modifier.weight(1f).fillMaxWidth()
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(t.contentBg)
                                    .border(width = 1.dp, color = t.accent.copy(alpha = 0.4f))
                                    .padding(6.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(t.label, color = t.accent, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HtmlMiniPreview(accent: Color, contentBg: Color, modifier: Modifier = Modifier) {
    val textColor = if (contentBg == Color.White) Color.Black.copy(alpha = 0.55f) else Color.Black.copy(alpha = 0.6f)
    Row(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(40.dp)
                .background(accent)
                .padding(5.dp)
        ) {
            Box(modifier = Modifier.size(18.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.3f)).border(1.dp, Color.White, CircleShape))
            Spacer(Modifier.height(8.dp))
            repeat(4) {
                Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color.White.copy(alpha = 0.8f)))
                Spacer(Modifier.height(5.dp))
            }
        }
        Column(modifier = Modifier.weight(1f).background(contentBg).padding(8.dp)) {
            Box(modifier = Modifier.fillMaxWidth(0.7f).height(5.dp).background(Color.Black.copy(alpha = 0.75f)))
            Spacer(Modifier.height(3.dp))
            Box(modifier = Modifier.fillMaxWidth(0.4f).height(3.dp).background(accent))
            Spacer(Modifier.height(10.dp))
            repeat(3) {
                Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(textColor))
                Spacer(Modifier.height(2.dp))
                Box(modifier = Modifier.fillMaxWidth(0.75f).height(2.dp).background(textColor))
                Spacer(Modifier.height(6.dp))
            }
        }
    }
}
