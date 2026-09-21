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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

private enum class LayoutStyle { CLASSIC_TWO_COL, ATS_SINGLE_COL, SIDEBAR_BOLD, MINIMAL_MONO, HEADER_BLOCK, LIGHT_SIDEBAR, BLUE_ARCH, TEAL_BAND, PILL_HEADER, NAVY_PHOTO }

private data class TemplateInfo(
    val id: String,
    val label: String,
    val accent: Color,
    val bg: Color,
    val layout: LayoutStyle = LayoutStyle.CLASSIC_TWO_COL
)

private val legacyDarkTemplates = listOf(
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

private val resumeTemplates = listOf(
    TemplateInfo("resume_light_01.html", "01", Color(0xFF111111), Color(0xFFECEEF5), LayoutStyle.LIGHT_SIDEBAR),
    TemplateInfo("ai_template_more_02.html", "02", Color(0xFF0A3FA5), Color(0xFF040B26), LayoutStyle.BLUE_ARCH),
    TemplateInfo("ai_template_more_03.html", "03", Color(0xFF5F9E9A), Color(0xFFF4C7BE), LayoutStyle.TEAL_BAND),
    TemplateInfo("ai_template_more_04.html", "04", Color(0xFF2B2B2B), Color(0xFFFFFFFF), LayoutStyle.PILL_HEADER),
    TemplateInfo("ai_template_more_05.html", "05", Color(0xFF10375F), Color(0xFFFFFFFF), LayoutStyle.NAVY_PHOTO),
)


@Composable
fun ResumeTemplateGalleryScreen(
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

            val currentList = resumeTemplates

            if (currentList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Coming Soon", color = Color.White.copy(alpha = 0.6f), fontSize = 16.sp)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(currentList, key = { it.id }) { t ->
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
                                TemplatePreview(
                                    accent = t.accent,
                                    bg = t.bg,
                                    layout = t.layout,
                                    modifier = Modifier.weight(1f).fillMaxWidth()
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(t.bg)
                                        .border(width = 1.dp, color = t.accent.copy(alpha = 0.4f))
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text("Template ${t.label}", color = t.accent, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PackagePill(label: String, selected: Boolean, accent: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(if (selected) accent.copy(alpha = 0.15f) else Color.Transparent)
            .border(1.dp, accent, RoundedCornerShape(50))
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(label, color = accent, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
private fun TemplatePreview(accent: Color, bg: Color, layout: LayoutStyle, modifier: Modifier = Modifier) {
    when (layout) {
        LayoutStyle.CLASSIC_TWO_COL -> MiniResumePreview(accent, modifier.padding(10.dp))
        LayoutStyle.ATS_SINGLE_COL -> AtsSingleColPreview(accent, modifier.padding(10.dp))
        LayoutStyle.SIDEBAR_BOLD -> SidebarBoldPreview(accent, modifier)
        LayoutStyle.MINIMAL_MONO -> MinimalMonoPreview(accent, modifier.padding(12.dp))
        LayoutStyle.HEADER_BLOCK -> HeaderBlockPreview(accent, modifier)
        LayoutStyle.LIGHT_SIDEBAR -> LightSidebarPreview(modifier)
        LayoutStyle.BLUE_ARCH -> BlueArchPreview(modifier)
        LayoutStyle.TEAL_BAND -> TealBandPreview(modifier)
        LayoutStyle.PILL_HEADER -> PillHeaderPreview(modifier)
        LayoutStyle.NAVY_PHOTO -> NavyPhotoPreview(modifier)
    }
}

/** Classic two-column: circle photo + header bar, 3 rows of 2-column bars. */
@Composable
private fun MiniResumePreview(accent: Color, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(22.dp).clip(CircleShape).border(1.5.dp, accent, CircleShape))
            Spacer(Modifier.width(8.dp))
            Box(modifier = Modifier.weight(1f).height(6.dp).background(Color.White.copy(alpha = 0.85f)))
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

/** ATS-safe single column: no sidebar, big bold name bar, full-width section lines. */
@Composable
private fun AtsSingleColPreview(accent: Color, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Box(modifier = Modifier.fillMaxWidth(0.6f).height(8.dp).background(accent))
        Spacer(Modifier.height(4.dp))
        Box(modifier = Modifier.fillMaxWidth(0.4f).height(4.dp).background(Color.Gray.copy(alpha = 0.6f)))
        Spacer(Modifier.height(14.dp))
        repeat(4) {
            Box(modifier = Modifier.fillMaxWidth(0.3f).height(4.dp).background(accent))
            Spacer(Modifier.height(4.dp))
            repeat(2) {
                Box(modifier = Modifier.fillMaxWidth().height(3.dp).background(Color.Gray.copy(alpha = 0.5f)))
                Spacer(Modifier.height(3.dp))
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

/** Sidebar bold: dark left sidebar block with circle photo, content on right. */
@Composable
private fun SidebarBoldPreview(accent: Color, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(46.dp)
                .background(accent.copy(alpha = 0.15f))
                .padding(6.dp)
        ) {
            Box(modifier = Modifier.size(20.dp).clip(CircleShape).border(1.5.dp, accent, CircleShape))
            Spacer(Modifier.height(10.dp))
            repeat(4) {
                Box(modifier = Modifier.fillMaxWidth().height(3.dp).background(accent))
                Spacer(Modifier.height(6.dp))
            }
        }
        Column(modifier = Modifier.weight(1f).padding(10.dp)) {
            Box(modifier = Modifier.fillMaxWidth(0.7f).height(6.dp).background(Color.White.copy(alpha = 0.85f)))
            Spacer(Modifier.height(12.dp))
            repeat(3) {
                Box(modifier = Modifier.fillMaxWidth().height(3.dp).background(Color.White.copy(alpha = 0.5f)))
                Spacer(Modifier.height(3.dp))
                Box(modifier = Modifier.fillMaxWidth(0.8f).height(3.dp).background(Color.White.copy(alpha = 0.5f)))
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

/** Minimal mono: pure black/white, thin hairlines only, no color blocks. */
@Composable
private fun MinimalMonoPreview(accent: Color, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Box(modifier = Modifier.fillMaxWidth(0.5f).height(2.dp).background(accent))
        Spacer(Modifier.height(16.dp))
        repeat(5) {
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(accent.copy(alpha = 0.7f)))
            Spacer(Modifier.height(10.dp))
        }
    }
}

/** Header block: full-width solid color band at top, content below. */
@Composable
private fun HeaderBlockPreview(accent: Color, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(accent),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(modifier = Modifier.padding(start = 10.dp).fillMaxWidth(0.5f).height(6.dp).background(Color.Black.copy(alpha = 0.7f)))
        }
        Column(modifier = Modifier.padding(10.dp)) {
            repeat(4) {
                Box(modifier = Modifier.fillMaxWidth().height(3.dp).background(Color.White.copy(alpha = 0.5f)))
                Spacer(Modifier.height(6.dp))
            }
        }
    }
}

@Composable
private fun MiniColumn(accent: Color, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Box(modifier = Modifier.fillMaxWidth(0.7f).height(4.dp).background(accent))
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

@Composable
private fun LightSidebarPreview(modifier: Modifier = Modifier) {
    val ink = Color(0xFF111111)
    val gray = Color(0xFF9AA0B0)
    Row(modifier = modifier.background(Color.White)) {
        Column(
            modifier = Modifier.fillMaxHeight().width(38.dp).background(Color(0xFFECEEF5)).padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.size(22.dp).clip(CircleShape).border(2.dp, ink, CircleShape))
            Spacer(Modifier.height(8.dp))
            repeat(3) {
                Box(modifier = Modifier.fillMaxWidth().height(5.dp).background(ink))
                Spacer(Modifier.height(4.dp))
                repeat(2) {
                    Box(modifier = Modifier.fillMaxWidth(0.8f).height(2.dp).background(gray))
                    Spacer(Modifier.height(3.dp))
                }
                Spacer(Modifier.height(5.dp))
            }
        }
        Column(modifier = Modifier.weight(1f).padding(8.dp)) {
            Box(modifier = Modifier.fillMaxWidth(0.8f).height(8.dp).background(ink))
            Spacer(Modifier.height(6.dp))
            Box(modifier = Modifier.fillMaxWidth(0.5f).height(3.dp).background(gray))
            Spacer(Modifier.height(10.dp))
            repeat(3) {
                Box(modifier = Modifier.fillMaxWidth(0.55f).height(4.dp).background(ink))
                Spacer(Modifier.height(3.dp))
                repeat(2) {
                    Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(gray))
                    Spacer(Modifier.height(3.dp))
                }
                Spacer(Modifier.height(6.dp))
            }
        }
    }
}

@Composable
private fun BlueArchPreview(modifier: Modifier = Modifier) {
    val navy = Color(0xFF040B26)
    val blue = Color(0xFF0A3FA5)
    val gray = Color(0xFF9AA0B0)
    Box(modifier = modifier.background(Color.White)) {
        Box(modifier = Modifier.fillMaxWidth().height(46.dp).background(navy))
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxHeight().padding(start = 5.dp, top = 8.dp).width(46.dp)
                    .background(blue, RoundedCornerShape(topStart = 23.dp, topEnd = 23.dp))
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.size(28.dp).clip(CircleShape).background(Color(0xFFDFE3F0)).border(2.dp, Color.White, CircleShape))
                Spacer(Modifier.height(8.dp))
                repeat(3) {
                    Box(modifier = Modifier.fillMaxWidth().height(4.dp).background(Color.White))
                    Spacer(Modifier.height(3.dp))
                    Box(modifier = Modifier.fillMaxWidth(0.7f).height(2.dp).background(Color.White.copy(alpha = 0.5f)))
                    Spacer(Modifier.height(8.dp))
                }
            }
            Column(modifier = Modifier.weight(1f).padding(8.dp)) {
                Box(modifier = Modifier.fillMaxWidth(0.8f).height(8.dp).background(Color.White))
                Spacer(Modifier.height(4.dp))
                Box(modifier = Modifier.fillMaxWidth(0.5f).height(3.dp).background(Color(0xFF1A6BFF)))
                Spacer(Modifier.height(22.dp))
                repeat(3) {
                    Box(modifier = Modifier.fillMaxWidth(0.55f).height(4.dp).background(blue))
                    Spacer(Modifier.height(3.dp))
                    repeat(2) {
                        Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(gray))
                        Spacer(Modifier.height(3.dp))
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun TealBandPreview(modifier: Modifier = Modifier) {
    val teal = Color(0xFF5F9E9A)
    val pink = Color(0xFFF4C7BE)
    val ink = Color(0xFF2F3A3A)
    val gray = Color(0xFF9AA0B0)
    Row(modifier = modifier.background(Color(0xFFFBF3EE))) {
        Column(
            modifier = Modifier.fillMaxHeight().width(46.dp).background(teal).padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.size(28.dp).clip(CircleShape).background(Color(0xFFDFE3F0)).border(2.dp, Color.White, CircleShape))
            Spacer(Modifier.height(10.dp))
            repeat(2) {
                Box(modifier = Modifier.fillMaxWidth(0.7f).height(3.dp).background(Color.White))
                Spacer(Modifier.height(3.dp))
                repeat(3) {
                    Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color.White.copy(alpha = 0.6f)))
                    Spacer(Modifier.height(2.dp))
                }
                Spacer(Modifier.height(8.dp))
            }
        }
        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            Column(modifier = Modifier.fillMaxWidth().height(50.dp).background(pink).padding(8.dp)) {
                Box(modifier = Modifier.fillMaxWidth(0.7f).height(7.dp).background(ink))
                Spacer(Modifier.height(4.dp))
                Box(modifier = Modifier.fillMaxWidth(0.4f).height(3.dp).background(teal))
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Spacer(Modifier.height(4.dp))
                repeat(4) {
                    Box(modifier = Modifier.fillMaxWidth(0.5f).height(4.dp).background(teal))
                    Spacer(Modifier.height(3.dp))
                    repeat(2) {
                        Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(gray))
                        Spacer(Modifier.height(3.dp))
                    }
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
private fun PillHeaderPreview(modifier: Modifier = Modifier) {
    val dark = Color(0xFF2B2B2B)
    val gray = Color(0xFF9AA0B0)
    Column(modifier = modifier.background(Color.White).padding(6.dp)) {
        Row(modifier = Modifier.fillMaxWidth().height(34.dp)) {
            Box(modifier = Modifier.size(26.dp).clip(CircleShape).background(Color(0xFFDDDDDD)))
            Spacer(Modifier.width(16.dp))
            Column {
                Box(modifier = Modifier.width(40.dp).height(6.dp).background(dark))
                Spacer(Modifier.height(3.dp))
                Box(modifier = Modifier.width(40.dp).height(6.dp).background(dark))
                Spacer(Modifier.height(3.dp))
                Box(modifier = Modifier.width(24.dp).height(3.dp).background(gray))
            }
        }
        Spacer(Modifier.height(6.dp))
        Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
            Column(modifier = Modifier.width(34.dp)) {
                repeat(4) {
                    Box(modifier = Modifier.width(28.dp).height(6.dp).background(dark, RoundedCornerShape(topEnd = 3.dp, bottomEnd = 3.dp)))
                    Spacer(Modifier.height(3.dp))
                    repeat(2) {
                        Box(modifier = Modifier.fillMaxWidth(0.8f).height(2.dp).background(gray))
                        Spacer(Modifier.height(2.dp))
                    }
                    Spacer(Modifier.height(6.dp))
                }
            }
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                repeat(3) {
                    Box(modifier = Modifier.fillMaxWidth().height(6.dp).background(dark, RoundedCornerShape(3.dp)))
                    Spacer(Modifier.height(3.dp))
                    repeat(3) {
                        Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(gray))
                        Spacer(Modifier.height(2.dp))
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun NavyPhotoPreview(modifier: Modifier = Modifier) {
    val navy = Color(0xFF10375F)
    val gray = Color(0xFF9AA0B0)
    Row(modifier = modifier.background(Color.White)) {
        Column(modifier = Modifier.fillMaxHeight().width(38.dp).background(navy).padding(5.dp)) {
            Box(modifier = Modifier.fillMaxWidth().height(40.dp).background(Color(0xFFDDE3EE)))
            Spacer(Modifier.height(8.dp))
            repeat(3) {
                Box(modifier = Modifier.fillMaxWidth(0.8f).height(3.dp).background(Color.White))
                Spacer(Modifier.height(3.dp))
                repeat(2) {
                    Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color.White.copy(alpha = 0.55f)))
                    Spacer(Modifier.height(2.dp))
                }
                Spacer(Modifier.height(8.dp))
            }
        }
        Column(modifier = Modifier.weight(1f).padding(8.dp)) {
            Box(modifier = Modifier.fillMaxWidth(0.85f).height(8.dp).background(navy))
            Spacer(Modifier.height(3.dp))
            Box(modifier = Modifier.fillMaxWidth(0.4f).height(3.dp).background(gray))
            Spacer(Modifier.height(10.dp))
            repeat(4) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(navy))
                    Spacer(Modifier.width(3.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(3.dp).background(navy))
                }
                Spacer(Modifier.height(3.dp))
                repeat(2) {
                    Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(gray))
                    Spacer(Modifier.height(2.dp))
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}
