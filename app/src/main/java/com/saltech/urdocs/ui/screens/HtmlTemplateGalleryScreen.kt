package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.saveable.rememberSaveable
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

private val htmlPackage3Templates = listOf(
    HtmlColorInfo("ai_template_30_navygreen.html", "Navy Green", Color(0xFF8BC34A), Color(0xFF0A1931)),
    HtmlColorInfo("ai_template_p3_02.html", "Crimson Charcoal (Diagonal)", Color(0xFFE53950), Color(0xFF1A1418)),
    HtmlColorInfo("ai_template_p3_03.html", "Royal Sapphire (V-Cut)", Color(0xFF2E7CFF), Color(0xFF0B1B3A)),
    HtmlColorInfo("ai_template_p3_04.html", "Amber Espresso (W-Cut)", Color(0xFFFFB300), Color(0xFF2B1D10)),
    HtmlColorInfo("ai_template_p3_05.html", "Emerald Midnight (Horizontal Split)", Color(0xFF1ABC9C), Color(0xFF0A2620)),
    HtmlColorInfo("ai_template_p3_06.html", "Violet Onyx (Vertical Split)", Color(0xFF9B59B6), Color(0xFF1C1024)),
    HtmlColorInfo("ai_template_p3_07.html", "Coral Navy (Half-Slant)", Color(0xFFFF6F59), Color(0xFF101E3A)),
    HtmlColorInfo("ai_template_p3_08.html", "Teal Slate (Chevron)", Color(0xFF2EE6C6), Color(0xFF16232E)),
    HtmlColorInfo("ai_template_p3_09.html", "Rose Plum (Arrow)", Color(0xFFF35CA0), Color(0xFF2B1024)),
    HtmlColorInfo("ai_template_p3_10.html", "Cyan Depths (Reverse Diagonal)", Color(0xFF22D3EE), Color(0xFF0A1F2E)),
    HtmlColorInfo("ai_template_p3_11.html", "Lime Forest (Zigzag)", Color(0xFF8BE23C), Color(0xFF12261A)),
    HtmlColorInfo("ai_template_p3_12.html", "Gold Mahogany (Diagonal)", Color(0xFFF3C94F), Color(0xFF2E1810)),
    HtmlColorInfo("ai_template_p3_13.html", "Sky Indigo (V-Cut)", Color(0xFF5CB8FF), Color(0xFF151038)),
    HtmlColorInfo("ai_template_p3_14.html", "Magenta Ink (W-Cut)", Color(0xFFE91E8C), Color(0xFF22102E)),
    HtmlColorInfo("ai_template_p3_15.html", "Mint Pine (Horizontal Split)", Color(0xFF4FF3A0), Color(0xFF0E241A)),
    HtmlColorInfo("ai_template_p3_16.html", "Tangerine Slate (Vertical Split)", Color(0xFFFF8A3D), Color(0xFF211E2A)),
    HtmlColorInfo("ai_template_p3_17.html", "Orchid Storm (Half-Slant)", Color(0xFFC084FC), Color(0xFF1E1730)),
    HtmlColorInfo("ai_template_p3_18.html", "Steel Blue (Chevron)", Color(0xFF4FA3F7), Color(0xFF111A26)),
    HtmlColorInfo("ai_template_p3_19.html", "Copper Bronze (Arrow)", Color(0xFFCD7F32), Color(0xFF241A10)),
    HtmlColorInfo("ai_template_p3_20.html", "Seafoam Navy (Reverse Diagonal)", Color(0xFF3DDC97), Color(0xFF0C1B2E)),
    HtmlColorInfo("ai_template_p3_21.html", "Ruby Slate (Zigzag)", Color(0xFFFF4569), Color(0xFF1E1620)),
    HtmlColorInfo("ai_template_p3_22.html", "Sunflower Charcoal (Diagonal)", Color(0xFFF3D14F), Color(0xFF201C10)),
    HtmlColorInfo("ai_template_p3_23.html", "Aqua Marine (V-Cut)", Color(0xFF26C6DA), Color(0xFF0C2226)),
    HtmlColorInfo("ai_template_p3_24.html", "Berry Night (W-Cut)", Color(0xFFEC5990), Color(0xFF1A0F22)),
    HtmlColorInfo("ai_template_p3_25.html", "Chartreuse Deep (Horizontal Split)", Color(0xFFB4E23C), Color(0xFF1A2210)),
    HtmlColorInfo("ai_template_p3_26.html", "Periwinkle Dusk (Vertical Split)", Color(0xFF7C8CFF), Color(0xFF161233)),
    HtmlColorInfo("ai_template_p3_27.html", "Peach Cocoa (Half-Slant)", Color(0xFFFFA26B), Color(0xFF2A1D16)),
    HtmlColorInfo("ai_template_p3_28.html", "Turquoise Ink (Chevron)", Color(0xFF1FD1B8), Color(0xFF0A2224)),
    HtmlColorInfo("ai_template_p3_29.html", "Fuchsia Midnight (Arrow)", Color(0xFFD946EF), Color(0xFF210E2E)),
    HtmlColorInfo("ai_template_p3_30.html", "Ocean Breeze (Reverse Diagonal)", Color(0xFF38BDF8), Color(0xFF0D1B2E)),
)

private val htmlPackage4Templates = listOf(
    HtmlColorInfo("ai_template_p4_01.html", "Teal Chevron", Color(0xFF1AB5A3), Color(0xFF191D24)),
    HtmlColorInfo("ai_template_p4_02.html", "Ruby Chevron", Color(0xFFC53939), Color(0xFF291313)),
    HtmlColorInfo("ai_template_p4_03.html", "Emerald Chevron", Color(0xFF39C562), Color(0xFF13291A)),
    HtmlColorInfo("ai_template_p4_04.html", "Violet Chevron", Color(0xFF8B39C5), Color(0xFF201329)),
    HtmlColorInfo("ai_template_p4_05.html", "Olive Chevron", Color(0xFFC5B439), Color(0xFF292613)),
    HtmlColorInfo("ai_template_p4_06.html", "Sky Chevron", Color(0xFF39AEC5), Color(0xFF132529)),
    HtmlColorInfo("ai_template_p4_07.html", "Rose Chevron", Color(0xFFC53985), Color(0xFF29131F)),
    HtmlColorInfo("ai_template_p4_08.html", "Lime Chevron", Color(0xFF5CC539), Color(0xFF192913)),
    HtmlColorInfo("ai_template_p4_09.html", "Indigo Chevron", Color(0xFF3F39C5), Color(0xFF141329)),
    HtmlColorInfo("ai_template_p4_10.html", "Amber Chevron", Color(0xFFC56839), Color(0xFF291B13)),
    HtmlColorInfo("ai_template_p4_11.html", "Mint Chevron", Color(0xFF39C591), Color(0xFF132921)),
    HtmlColorInfo("ai_template_p4_12.html", "Orchid Chevron", Color(0xFFB939C5), Color(0xFF271329)),
    HtmlColorInfo("ai_template_p4_13.html", "Chartreuse Chevron", Color(0xFFA8C539), Color(0xFF242913)),
    HtmlColorInfo("ai_template_p4_14.html", "Azure Chevron", Color(0xFF397FC5), Color(0xFF131E29)),
    HtmlColorInfo("ai_template_p4_15.html", "Crimson Chevron", Color(0xFFC53956), Color(0xFF291318)),
    HtmlColorInfo("ai_template_p4_16.html", "Jade Chevron", Color(0xFF39C545), Color(0xFF132915)),
    HtmlColorInfo("ai_template_p4_17.html", "Purple Chevron", Color(0xFF6D39C5), Color(0xFF1B1329)),
    HtmlColorInfo("ai_template_p4_18.html", "Mustard Chevron", Color(0xFFC59639), Color(0xFF292213)),
    HtmlColorInfo("ai_template_p4_19.html", "Turquoise Chevron", Color(0xFF39C5BF), Color(0xFF132928)),
    HtmlColorInfo("ai_template_p4_20.html", "Magenta Chevron", Color(0xFFC539A2), Color(0xFF291323)),
    HtmlColorInfo("ai_template_p4_21.html", "Grass Chevron", Color(0xFF79C539), Color(0xFF1D2913)),
    HtmlColorInfo("ai_template_p4_22.html", "Cobalt Chevron", Color(0xFF3950C5), Color(0xFF131729)),
    HtmlColorInfo("ai_template_p4_23.html", "Rust Chevron", Color(0xFFC54A39), Color(0xFF291613)),
    HtmlColorInfo("ai_template_p4_24.html", "Teal Chevron", Color(0xFF39C573), Color(0xFF13291C)),
    HtmlColorInfo("ai_template_p4_25.html", "Fuchsia Chevron", Color(0xFF9C39C5), Color(0xFF231329)),
    HtmlColorInfo("ai_template_p4_26.html", "Lemon Chevron", Color(0xFFC5C539), Color(0xFF292913)),
    HtmlColorInfo("ai_template_p4_27.html", "Cyan Chevron", Color(0xFF399CC5), Color(0xFF132329)),
    HtmlColorInfo("ai_template_p4_28.html", "Pink Chevron", Color(0xFFC53973), Color(0xFF29131C)),
    HtmlColorInfo("ai_template_p4_29.html", "Green Chevron", Color(0xFF4AC539), Color(0xFF162913)),
    HtmlColorInfo("ai_template_p4_30.html", "Blue Chevron", Color(0xFF5039C5), Color(0xFF171329)),
)

private val htmlPackage5Templates = listOf(
    HtmlColorInfo("ai_template_p5_01.html", "Sky Circle", Color(0xFF1F8FE0), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_02.html", "Crimson Circle", Color(0xFFC92A2A), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_03.html", "Rust Circle", Color(0xFFA64830), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_04.html", "Amber Circle", Color(0xFFDC7E3B), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_05.html", "Gold Circle", Color(0xFFC98D2A), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_06.html", "Olive Circle", Color(0xFFA69130), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_07.html", "Lime Circle", Color(0xFFD7DC3B), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_08.html", "Chartreuse Circle", Color(0xFFA3C92A), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_09.html", "Fern Circle", Color(0xFF71A630), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_10.html", "Emerald Circle", Color(0xFF73DC3B), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_11.html", "Jade Circle", Color(0xFF40C92A), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_12.html", "Forest Circle", Color(0xFF30A638), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_13.html", "Mint Circle", Color(0xFF3BDC68), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_14.html", "Teal Circle", Color(0xFF2AC977), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_15.html", "Ocean Circle", Color(0xFF30A681), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_16.html", "Aqua Circle", Color(0xFF3BDCCB), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_17.html", "Cyan Circle", Color(0xFF2AB9C9), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_18.html", "Steel Circle", Color(0xFF3081A6), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_19.html", "Azure Circle", Color(0xFF3B89DC), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_20.html", "Cobalt Circle", Color(0xFF2A56C9), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_21.html", "Indigo Circle", Color(0xFF3038A6), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_22.html", "Violet Circle", Color(0xFF523BDC), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_23.html", "Amethyst Circle", Color(0xFF612AC9), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_24.html", "Plum Circle", Color(0xFF7130A6), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_25.html", "Orchid Circle", Color(0xFFB53BDC), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_26.html", "Magenta Circle", Color(0xFFC42AC9), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_27.html", "Mauve Circle", Color(0xFFA63091), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_28.html", "Rose Circle", Color(0xFFDC3B9F), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_29.html", "Berry Circle", Color(0xFFC92A6C), Color(0xFF14161A)),
    HtmlColorInfo("ai_template_p5_30.html", "Wine Circle", Color(0xFFA63048), Color(0xFF14161A)),
)

@Composable
fun HtmlTemplateGalleryScreen(
    onTemplateSelected: (String) -> Unit,
    onBack: () -> Unit = {}
) {
    var selectedPackage by rememberSaveable { mutableStateOf(1) }

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
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PackageTab(
                    label = "Package 1",
                    selected = selectedPackage == 1,
                    onClick = { selectedPackage = 1 },
                    modifier = Modifier.width(110.dp)
                )
                PackageTab(
                    label = "Package 2",
                    selected = selectedPackage == 2,
                    onClick = { selectedPackage = 2 },
                    modifier = Modifier.width(110.dp)
                )
                PackageTab(
                    label = "Package 3",
                    selected = selectedPackage == 3,
                    onClick = { selectedPackage = 3 },
                    modifier = Modifier.width(110.dp)
                )
                PackageTab(
                    label = "Package 4",
                    selected = selectedPackage == 4,
                    onClick = { selectedPackage = 4 },
                    modifier = Modifier.width(110.dp)
                )
                PackageTab(
                    label = "Package 5",
                    selected = selectedPackage == 5,
                    onClick = { selectedPackage = 5 },
                    modifier = Modifier.width(110.dp)
                )
            }

            val currentTemplates = when (selectedPackage) { 1 -> htmlColorTemplates; 2 -> htmlPackage2Templates; 3 -> htmlPackage3Templates; 4 -> htmlPackage4Templates; else -> htmlPackage5Templates }

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
