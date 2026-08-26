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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val templateColors: Map<Int, Pair<Color, Color>> = mapOf(
    1 to (Color(0xFFA6FF00) to Color(0xFFFF4D8D)),
    2 to (Color(0xFF00FF99) to Color(0xFFFF6EC7)),
    3 to (Color(0xFF2E7D32) to Color(0xFFE91E63)),
    4 to (Color(0xFFFF4D8D) to Color(0xFFA6FF00)),
    5 to (Color(0xFFFF8C00) to Color(0xFF00BFFF)),
    6 to (Color(0xFF4CAF50) to Color(0xFFE91E63)),
    7 to (Color(0xFFD4AF37) to Color(0xFFD4AF37)),
    8 to (Color(0xFF27AE60) to Color(0xFFFF4D8D)),
    9 to (Color(0xFFD4AF37) to Color(0xFF1A237E)),
    10 to (Color(0xFF1976D2) to Color(0xFF0D47A1)),
    11 to (Color(0xFF6A9B7A) to Color(0xFF8BC34A)),
    12 to (Color(0xFFE91E63) to Color(0xFFF48FB1)),
    13 to (Color(0xFFD4AF37) to Color(0xFFB7950B)),
    14 to (Color(0xFF64B5F6) to Color(0xFF1976D2)),
    15 to (Color(0xFF212121) to Color(0xFF757575)),
    16 to (Color(0xFF009688) to Color(0xFF004D40))
)

@Composable
fun BioDataMoreTemplatesScreen(
    onTemplateSelected: (String) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val templateNames = (1..16).map { it }

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
                items(templateNames, key = { it }) { num ->
                    val (colorA, colorB) = templateColors[num] ?: (Color.Gray to Color.DarkGray)
                    Column(
                        modifier = Modifier
                            .padding(6.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .background(Color(0xFF13224A))
                            .clickable { onTemplateSelected("biodata_template_" + num.toString().padStart(2, '0')) }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(0.72f)
                                .background(Brush.verticalGradient(listOf(colorA, colorB)))
                        ) {
                            Column(modifier = Modifier.padding(10.dp).fillMaxWidth()) {
                                Box(modifier = Modifier.size(22.dp).clip(RoundedCornerShape(50)).background(Color.White.copy(alpha = 0.85f)))
                                Spacer(Modifier.height(8.dp))
                                repeat(4) {
                                    Box(modifier = Modifier.fillMaxWidth(0.7f).height(3.dp).background(Color.White.copy(alpha = 0.6f)))
                                    Spacer(Modifier.height(5.dp))
                                }
                            }
                        }
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Template " + num.toString().padStart(2, '0'), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}
