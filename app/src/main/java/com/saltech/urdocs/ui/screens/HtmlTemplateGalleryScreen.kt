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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    HtmlColorInfo("ai_template_11_indigo_light.html", "Indigo Light", Color(0xFF6B4FF3), Color(0xFFE8E4FD)),
    HtmlColorInfo("ai_template_02_dark1.html", "Blue Dark", Color(0xFF4FC3F7), Color(0xFF2B6B87)),
    HtmlColorInfo("ai_template_03_green_dark1.html", "Green Dark", Color(0xFF4FF3A0), Color(0xFF2B8558)),
    HtmlColorInfo("ai_template_04_purple_dark1.html", "Purple Dark", Color(0xFFB14FF3), Color(0xFF612B85)),
    HtmlColorInfo("ai_template_05_maroon_dark1.html", "Maroon Dark", Color(0xFFF34F6C), Color(0xFF852B3B)),
    HtmlColorInfo("ai_template_06_orange_dark1.html", "Orange Dark", Color(0xFFF3A94F), Color(0xFF855C2B)),
    HtmlColorInfo("ai_template_07_teal_dark1.html", "Teal Dark", Color(0xFF4FE0F3), Color(0xFF2B7B85)),
    HtmlColorInfo("ai_template_08_pink_dark1.html", "Pink Dark", Color(0xFFF34FC0), Color(0xFF852B69)),
    HtmlColorInfo("ai_template_09_gray_dark1.html", "Gray Dark", Color(0xFFB0B0B0), Color(0xFF606060)),
    HtmlColorInfo("ai_template_10_gold_dark1.html", "Gold Dark", Color(0xFFF3D14F), Color(0xFF85722B)),
    HtmlColorInfo("ai_template_11_indigo_dark1.html", "Indigo Dark", Color(0xFF6B4FF3), Color(0xFF3A2B85)),
)

private val htmlPackage2Templates = listOf(
    HtmlColorInfo("ai_template_03.html", "AI-3", Color(0xFF6FBE44), Color(0xFF1E2A3A)),
    HtmlColorInfo("ai_template_03_v1.html", "AI-3 V1", Color(0xFF4FC3F7), Color(0xFF12203D)),
    HtmlColorInfo("ai_template_03_v2.html", "AI-3 V2", Color(0xFFB14FF3), Color(0xFF2B1A45)),
    HtmlColorInfo("ai_template_03_v3.html", "AI-3 V3", Color(0xFFF34F6C), Color(0xFF3A1420)),
    HtmlColorInfo("ai_template_03_v4.html", "AI-3 V4", Color(0xFFF3A94F), Color(0xFF3A2A10)),
    HtmlColorInfo("ai_template_03_v5.html", "AI-3 V5", Color(0xFF4FE0F3), Color(0xFF0F2E33)),
    HtmlColorInfo("ai_template_03_v6.html", "AI-3 V6", Color(0xFFF34FC0), Color(0xFF33102B)),
    HtmlColorInfo("ai_template_03_v7.html", "AI-3 V7", Color(0xFFB0B0B0), Color(0xFF232323)),
    HtmlColorInfo("ai_template_03_v8.html", "AI-3 V8", Color(0xFFF3D14F), Color(0xFF332B10)),
    HtmlColorInfo("ai_template_03_v9.html", "AI-3 V9", Color(0xFF6B4FF3), Color(0xFF1A1233)),
    HtmlColorInfo("ai_template_03_v10.html", "AI-3 V10", Color(0xFF2ECC71), Color(0xFF0E2B1A)),
    HtmlColorInfo("ai_template_03_v11.html", "AI-3 V11", Color(0xFFE63950), Color(0xFF2B0E14)),
    HtmlColorInfo("ai_template_03_v12.html", "AI-3 V12", Color(0xFF38B6FF), Color(0xFF0E1F33)),
    HtmlColorInfo("ai_template_03_v13.html", "AI-3 V13", Color(0xFFFFB300), Color(0xFF332600)),
    HtmlColorInfo("ai_template_03_v14.html", "AI-3 V14", Color(0xFF8E44AD), Color(0xFF22102E)),
    HtmlColorInfo("ai_template_03_v15.html", "AI-3 V15", Color(0xFFFF7F50), Color(0xFF331A10)),
    HtmlColorInfo("ai_template_03_v16.html", "AI-3 V16", Color(0xFF2EE6A8), Color(0xFF0E2B24)),
    HtmlColorInfo("ai_template_03_v17.html", "AI-3 V17", Color(0xFFFF5C8A), Color(0xFF33101B)),
    HtmlColorInfo("ai_template_03_v18.html", "AI-3 V18", Color(0xFF64748B), Color(0xFF1A2130)),
    HtmlColorInfo("ai_template_03_v19.html", "AI-3 V19", Color(0xFFA6E22E), Color(0xFF1E2A0E)),
    HtmlColorInfo("ai_template_03_v20.html", "AI-3 V20", Color(0xFF1ABC9C), Color(0xFF0E2B28)),
    HtmlColorInfo("ai_template_03_v21.html", "AI-3 V21", Color(0xFF9B59B6), Color(0xFF251133)),
    HtmlColorInfo("ai_template_03_v22.html", "AI-3 V22", Color(0xFFC0392B), Color(0xFF2E0F0B)),
    HtmlColorInfo("ai_template_03_v23.html", "AI-3 V23", Color(0xFF2E5CFF), Color(0xFF0E1A33)),
    HtmlColorInfo("ai_template_03_v24.html", "AI-3 V24", Color(0xFFFFAB91), Color(0xFF331F14)),
    HtmlColorInfo("ai_template_03_v25.html", "AI-3 V25", Color(0xFF808000), Color(0xFF2A2A0E)),
    HtmlColorInfo("ai_template_03_v26.html", "AI-3 V26", Color(0xFF7F8C8D), Color(0xFF1C1C1C)),
    HtmlColorInfo("ai_template_03_v27.html", "AI-3 V27", Color(0xFFE91E8C), Color(0xFF330A22)),
    HtmlColorInfo("ai_template_03_v28.html", "AI-3 V28", Color(0xFF00BCD4), Color(0xFF0A2E33)),
    HtmlColorInfo("ai_template_03_v29.html", "AI-3 V29", Color(0xFFCD7F32), Color(0xFF2E1D0E)),
)

@Composable
fun HtmlTemplateGalleryScreen(
    onTemplateSelected: (String) -> Unit,
    onBack: () -> Unit = {}
) {
    var selectedPackage by remember { mutableStateOf(1) }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0B1530))) {
        PremiumWaveBackground()
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp, start = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(Modifier.width(8.dp))
                Text("tap kalang dyan luv🩵", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PackageTab(
                    label = "Package 1",
                    selected = selectedPackage == 1,
                    onClick = { selectedPackage = 1 },
                    modifier = Modifier.weight(1f)
                )
                PackageTab(
                    label = "Package 2",
                    selected = selectedPackage == 2,
                    onClick = { selectedPackage = 2 },
                    modifier = Modifier.weight(1f)
                )
            }

            val currentTemplates = if (selectedPackage == 1) htmlColorTemplates else htmlPackage2Templates

            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                contentPadding = PaddingValues(10.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(currentTemplates, key = { it.fileName }) { t ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(t.contentBg)
                            .border(1.dp, t.accent.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
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
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(t.label, color = t.accent, fontWeight = FontWeight.Bold, fontSize = 8.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PackageTab(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) Color(0xFF3B6FE0) else Color.Black.copy(alpha = 0.4f))
            .border(1.dp, Color(0xFF3B6FE0).copy(alpha = if (selected) 1f else 0.4f), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
private fun HtmlMiniPreview(accent: Color, contentBg: Color, modifier: Modifier = Modifier) {
    val isDark = (contentBg.red + contentBg.green + contentBg.blue) < 1.2f
    val textColor = if (isDark) Color.White.copy(alpha = 0.85f) else Color.Black.copy(alpha = 0.55f)
    val nameColor = if (isDark) Color.White else Color.Black.copy(alpha = 0.75f)
    Row(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(32.dp)
                .background(accent)
                .padding(4.dp)
        ) {
            Box(modifier = Modifier.size(14.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.3f)).border(1.dp, Color.White, CircleShape))
            Spacer(Modifier.height(6.dp))
            repeat(3) {
                Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color.White.copy(alpha = 0.8f)))
                Spacer(Modifier.height(4.dp))
            }
        }
        Column(modifier = Modifier.weight(1f).background(contentBg).padding(6.dp)) {
            Box(modifier = Modifier.fillMaxWidth(0.7f).height(4.dp).background(nameColor))
            Spacer(Modifier.height(3.dp))
            Box(modifier = Modifier.fillMaxWidth(0.4f).height(2.dp).background(accent))
            Spacer(Modifier.height(8.dp))
            repeat(2) {
                Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(textColor))
                Spacer(Modifier.height(2.dp))
                Box(modifier = Modifier.fillMaxWidth(0.75f).height(2.dp).background(textColor))
                Spacer(Modifier.height(5.dp))
            }
        }
    }
}
