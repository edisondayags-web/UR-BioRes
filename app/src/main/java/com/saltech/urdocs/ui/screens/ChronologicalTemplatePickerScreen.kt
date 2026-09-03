package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChronologicalTemplatePickerScreen(
    onTemplateSelected: (Int) -> Unit,
    onBack: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF0B1530))) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text("tap kalang dyan luv💙", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TemplateGalleryCard(
                label = "Classic Navy",
                modifier = Modifier.weight(1f),
                onClick = { onTemplateSelected(1) }
            ) { accent ->
                // centered header mockup
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    drawRect(Color.White, Offset.Zero, size)
                    drawRect(accent, Offset(w * 0.3f, 12f), Size(w * 0.4f, 8f))
                    drawRect(Color.Gray, Offset(w * 0.35f, 26f), Size(w * 0.3f, 5f))
                    drawRect(accent, Offset(8f, 42f), Size(w - 16f, 2f))
                    for (i in 0..5) {
                        val y = 55f + i * 14f
                        drawRect(Color.LightGray, Offset(8f, y), Size(w - 16f, 5f))
                    }
                }
            }
            TemplateGalleryCard(
                label = "Modern Minimal",
                modifier = Modifier.weight(1f),
                onClick = { onTemplateSelected(2) }
            ) { accent ->
                // left-aligned header mockup
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    drawRect(Color.White, Offset.Zero, size)
                    drawRect(accent, Offset(8f, 12f), Size(w * 0.5f, 8f))
                    drawRect(Color.Gray, Offset(8f, 26f), Size(w * 0.35f, 5f))
                    drawRect(accent, Offset(8f, 42f), Size(w - 16f, 2f))
                    for (i in 0..5) {
                        val y = 55f + i * 14f
                        drawRect(Color.LightGray, Offset(8f, y), Size(w - 16f, 5f))
                    }
                }
            }
            TemplateGalleryCard(
                label = "Executive Classic",
                modifier = Modifier.weight(1f),
                onClick = { onTemplateSelected(3) }
            ) { accent ->
                // double-line monochrome mockup
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    drawRect(Color.White, Offset.Zero, size)
                    drawRect(accent, Offset(w * 0.25f, 12f), Size(w * 0.5f, 8f))
                    drawRect(Color.Gray, Offset(w * 0.3f, 26f), Size(w * 0.4f, 5f))
                    drawRect(accent, Offset(8f, 40f), Size(w - 16f, 2f))
                    drawRect(accent, Offset(8f, 44f), Size(w - 16f, 2f))
                    for (i in 0..5) {
                        val y = 58f + i * 14f
                        drawRect(Color.LightGray, Offset(8f, y), Size(w - 16f, 5f))
                    }
                }
            }
        }
    }
}

@Composable
private fun TemplateGalleryCard(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    preview: @Composable (Color) -> Unit
) {
    val accent = when (label) {
        "Classic Navy" -> Color(0xFF1B2A4A)
        "Modern Minimal" -> Color(0xFF0F766E)
        else -> Color(0xFF000000)
    }
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1A2440))
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.7f)
                .clip(RoundedCornerShape(6.dp))
        ) {
            preview(accent)
        }
        Spacer(Modifier.height(6.dp))
        Text(label, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}
