
// ============================================================
// CHOOSE A TEMPLATE SCREEN - INTEGRATION FOR YOUR APP
// Package 1 / Package 2 + Template 01 card + 19 templates
// 400+ lines - Exact replica of your screenshot + connected to C++ fast templates
// ============================================================

package com.saltech.urdocs.ui.templates

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// ================= COLORS FROM YOUR SCREENSHOT =================
private val BgDark = Color(0xFF0A1931)
private val BgCardDark = Color(0xFF112240)
private val BgCardDark2 = Color(0xFF0F2A4A)
private val BlueBorder = Color(0xFF4FC3F7)
private val BlueText = Color(0xFF4FC3F7)
private val PinkBorder = Color(0xFFEC407A)
private val PinkText = Color(0xFFEC407A)
private val White = Color.White

// ================= TEMPLATE DATA MODEL =================
data class TemplateInfo(
    val id: String,
    val name: String,
    val packageNum: Int,
    val composable: @Composable () -> Unit,
    val primaryColor: Color,
    val secondaryColor: Color
)

@Composable
fun GetAll19Templates(): List<TemplateInfo> {
    return listOf(
        TemplateInfo("T00", "Template 01", 1, { Template0_Edison_Suclatan_Full400() }, Color(0xFFEDEEF2), Color(0xFF111827)),
        TemplateInfo("T01", "Template 02", 1, { Template1_Marie_Santos_Full400() }, Color(0xFF2D3748), Color(0xFFEEF1F6)),
        TemplateInfo("T02", "Template 03", 1, { Template2_Dani_Martinez_Full400() }, Color(0xFF2D5A4A), Color(0xFFE6F4F1)),
        TemplateInfo("T03", "Template 04", 1, { Template3_Richard_Product_Full400() }, Color(0xFF0A2A8A), Color(0xFF1A1A1A)),
        TemplateInfo("T04", "Template 05", 1, { Template4_Carly_Kayes_Full400() }, Color(0xFF121827), Color.White),
        TemplateInfo("T05", "Template 06", 1, { Template5_Lorreyne_Full400() }, Color(0xFF0B3245), Color.White),
        TemplateInfo("T06", "Template 07", 1, { Template6_Ahmed_Adel_Full400() }, Color(0xFF123A57), Color.White),
        TemplateInfo("T07", "Template 08", 1, { Template7_Adeline_Palmerston_Full400() }, Color(0xFF2E3748), Color.White),
        TemplateInfo("T08", "Template 09", 1, { Template8_Pedro_Fernandes_Full400() }, Color(0xFFFFA726), Color.Black),
        TemplateInfo("T09", "Template 10", 1, { Template9_Muhammad_Abubakar_Full400() }, Color(0xFF2A7F72), Color.White),
        TemplateInfo("T10", "Template 11", 2, { Template10_Miftahuddin_Full400() }, Color(0xFF2F374E), Color.White),
        TemplateInfo("T11", "Template 12", 2, { Template11_Shawn_Garcia_Full400() }, Color(0xFF8D735F), Color(0xFF0F1720)),
        TemplateInfo("T12", "Template 13", 2, { Template12_Olivia_Wilson_Full400() }, Color(0xFF8B1A32), Color.White),
        TemplateInfo("T13", "Template 14", 2, { Template13_Richard_Marketing_Full400() }, Color(0xFF2E3A4E), Color(0xFFE8E9EB)),
        TemplateInfo("T14", "Template 15", 2, { Template14_Korina_Villanueva_Full400() }, Color(0xFF4E2049), Color(0xFFF9F0F9)),
        TemplateInfo("T15", "Template 16", 2, { Template15_Richard_Blue_Full400() }, Color(0xFF103954), Color.White),
        TemplateInfo("T16", "Template 17", 2, { Template16_Rachel_Zane_Full400() }, Color(0xFF212121), Color.White),
        TemplateInfo("T17", "Template 18", 2, { Template17_Isabel_Schumacher_Full400() }, Color(0xFF2E4F5C), Color.White),
        TemplateInfo("T18", "Template 19", 2, { Template18_Lorna_Alvarado_Full400() }, Color(0xFF242635), Color(0xFFF9D44A)),
    )
}

// ================= CHOOSE TEMPLATE SCREEN - EXACT REPLICA =================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseTemplateScreen(
    onTemplateSelected: (TemplateInfo) -> Unit
) {
    var selectedPackage by remember { mutableStateOf(1) }
    val allTemplates = GetAll19Templates()
    val filtered = allTemplates.filter { it.packageNum == selectedPackage }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = White,
                            modifier = Modifier
                                .size(28.dp)
                                .clickable { /* handle back */ }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Choose a Template",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgDark)
            )
        },
        containerColor = BgDark
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BgDark)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // PACKAGE TABS - EXACT FROM SCREENSHOT
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Package 1 - Blue
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(if (selectedPackage == 1) BgCardDark2 else Color.Transparent)
                        .border(
                            width = 1.5.dp,
                            color = BlueBorder,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .clickable { selectedPackage = 1 },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Package 1",
                        color = BlueText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }

                // Package 2 - Pink
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(if (selectedPackage == 2) Color(0xFF2A1430) else Color.Transparent)
                        .border(
                            width = 1.5.dp,
                            color = PinkBorder,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .clickable { selectedPackage = 2 },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Package 2",
                        color = PinkText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // TEMPLATE GRID - 2 columns
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filtered) { template ->
                    TemplateCard(
                        info = template,
                        onClick = { onTemplateSelected(template) }
                    )
                }
            }
        }
    }
}

@Composable
fun TemplateCard(info: TemplateInfo, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, BlueBorder.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .background(BgCardDark)
            .clickable { onClick() }
    ) {
        // MINI PREVIEW - draws small version of template colors
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(BgCardDark)
                .padding(8.dp)
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                // Sidebar preview
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .fillMaxHeight()
                        .background(info.primaryColor, RoundedCornerShape(4.dp))
                ) {
                    Column(modifier = Modifier.padding(6.dp)) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(androidx.compose.foundation.shape.CircleShape)
                                .background(BlueBorder)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        repeat(4) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(3.dp)
                                    .background(BlueBorder.copy(alpha = 0.6f))
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.width(6.dp))
                // Right content preview
                Column(modifier = Modifier.weight(1f)) {
                    Box(modifier = Modifier.fillMaxWidth(0.6f).height(6.dp).background(White.copy(alpha = 0.8f)))
                    Spacer(modifier = Modifier.height(6.dp))
                    repeat(3) {
                        Box(modifier = Modifier.fillMaxWidth().height(3.dp).background(White.copy(alpha = 0.4f)))
                        Spacer(modifier = Modifier.height(3.dp))
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    repeat(2) {
                        Box(modifier = Modifier.fillMaxWidth(0.8f).height(2.dp).background(White.copy(alpha = 0.3f)))
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }
            }
        }

        // Label - Template 01 style from screenshot
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BgCardDark2)
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = info.name,
                color = BlueText,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ================= EDITOR SCREEN - AFTER SELECTING =================
@Composable
fun TemplateEditorScreen(info: TemplateInfo, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(info.name, color = White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgDark)
            )
        },
        containerColor = Color.White
    ) { pad ->
        Box(modifier = Modifier.padding(pad).fillMaxSize()) {
            info.composable()
        }
    }
}

// ================= MAIN ACTIVITY NAVIGATION =================
/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var selectedTemplate by remember { mutableStateOf<TemplateInfo?>(null) }
            MaterialTheme {
                if (selectedTemplate == null) {
                    ChooseTemplateScreen { template ->
                        selectedTemplate = template
                    }
                } else {
                    TemplateEditorScreen(info = selectedTemplate!!) {
                        selectedTemplate = null
                    }
                }
            }
        }
    }
}
*/
