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
                accent = Color(0xFF1B2A4A),
                modifier = Modifier.weight(1f),
                onClick = { onTemplateSelected(1) }
            ) { accent ->
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    drawRect(Color.White, Offset.Zero, size)
                    // centered title + name
                    drawRect(accent, Offset(w * 0.28f, 10f), Size(w * 0.44f, 9f))
                    drawRect(Color.DarkGray, Offset(w * 0.35f, 24f), Size(w * 0.3f, 6f))
                    // centered contact row
                    drawRect(Color.LightGray, Offset(w * 0.2f, 36f), Size(w * 0.6f, 4f))
                    drawRect(accent, Offset(6f, 46f), Size(w - 12f, 2f))
                    // section: SUMMARY
                    drawRect(accent, Offset(6f, 54f), Size(w * 0.35f, 6f))
                    drawRect(accent, Offset(6f, 62f), Size(w - 12f, 1.5f))
                    drawRect(Color.LightGray, Offset(6f, 68f), Size(w - 12f, 4f))
                    drawRect(Color.LightGray, Offset(6f, 75f), Size(w - 20f, 4f))
                    // section: SKILLS bullets
                    drawRect(accent, Offset(6f, 86f), Size(w * 0.3f, 6f))
                    drawRect(accent, Offset(6f, 94f), Size(w - 12f, 1.5f))
                    for (i in 0..2) {
                        val y = 100f + i * 9f
                        drawCircle(Color.Gray, 1.5f, Offset(9f, y + 2f))
                        drawRect(Color.LightGray, Offset(14f, y), Size(w * 0.5f, 4f))
                    }
                    // section: EXPERIENCE
                    drawRect(accent, Offset(6f, 132f), Size(w * 0.45f, 6f))
                    drawRect(accent, Offset(6f, 140f), Size(w - 12f, 1.5f))
                    drawRect(Color.DarkGray, Offset(6f, 146f), Size(w * 0.5f, 4f))
                    for (i in 0..1) {
                        val y = 154f + i * 8f
                        drawRect(Color.LightGray, Offset(6f, y), Size(w - 20f, 4f))
                    }
                }
            }

            TemplateGalleryCard(
                label = "Modern Minimal",
                accent = Color(0xFF0F766E),
                modifier = Modifier.weight(1f),
                onClick = { onTemplateSelected(2) }
            ) { accent ->
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    drawRect(Color.White, Offset.Zero, size)
                    // left-aligned title + name
                    drawRect(accent, Offset(6f, 10f), Size(w * 0.55f, 9f))
                    drawRect(Color.DarkGray, Offset(6f, 24f), Size(w * 0.4f, 6f))
                    // left-aligned contact row
                    drawRect(Color.LightGray, Offset(6f, 36f), Size(w * 0.5f, 4f))
                    drawRect(accent, Offset(6f, 46f), Size(w - 12f, 2f))
                    // more whitespace before sections (Modern Minimal trait)
                    drawRect(accent, Offset(6f, 60f), Size(w * 0.35f, 6f))
                    drawRect(accent, Offset(6f, 68f), Size(w - 12f, 1.5f))
                    drawRect(Color.LightGray, Offset(6f, 76f), Size(w - 12f, 4f))
                    drawRect(Color.LightGray, Offset(6f, 84f), Size(w - 20f, 4f))
                    // SKILLS bullets
                    drawRect(accent, Offset(6f, 98f), Size(w * 0.3f, 6f))
                    drawRect(accent, Offset(6f, 106f), Size(w - 12f, 1.5f))
                    for (i in 0..2) {
                        val y = 114f + i * 10f
                        drawCircle(Color.Gray, 1.5f, Offset(9f, y + 2f))
                        drawRect(Color.LightGray, Offset(14f, y), Size(w * 0.5f, 4f))
                    }
                    // EXPERIENCE
                    drawRect(accent, Offset(6f, 150f), Size(w * 0.45f, 6f))
                    drawRect(accent, Offset(6f, 158f), Size(w - 12f, 1.5f))
                    drawRect(Color.DarkGray, Offset(6f, 166f), Size(w * 0.5f, 4f))
                }
            }

            TemplateGalleryCard(
                label = "Executive Classic",
                accent = Color(0xFF000000),
                modifier = Modifier.weight(1f),
                onClick = { onTemplateSelected(3) }
            ) { accent ->
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    drawRect(Color.White, Offset.Zero, size)
                    // centered title + name, tighter spacing (Executive trait)
                    drawRect(accent, Offset(w * 0.25f, 10f), Size(w * 0.5f, 9f))
                    drawRect(Color.DarkGray, Offset(w * 0.32f, 24f), Size(w * 0.36f, 6f))
                    drawRect(Color.LightGray, Offset(w * 0.2f, 36f), Size(w * 0.6f, 4f))
                    // double-line divider (Executive trait)
                    drawRect(accent, Offset(6f, 44f), Size(w - 12f, 2f))
                    drawRect(accent, Offset(6f, 48f), Size(w - 12f, 2f))
                    // SUMMARY
                    drawRect(accent, Offset(6f, 58f), Size(w * 0.35f, 6f))
                    drawRect(accent, Offset(6f, 66f), Size(w - 12f, 1.5f))
                    drawRect(accent, Offset(6f, 68.5f), Size(w - 12f, 1.5f))
                    drawRect(Color.LightGray, Offset(6f, 76f), Size(w - 12f, 4f))
                    drawRect(Color.LightGray, Offset(6f, 83f), Size(w - 20f, 4f))
                    // SKILLS
                    drawRect(accent, Offset(6f, 96f), Size(w * 0.3f, 6f))
                    drawRect(accent, Offset(6f, 104f), Size(w - 12f, 1.5f))
                    drawRect(accent, Offset(6f, 106.5f), Size(w - 12f, 1.5f))
                    for (i in 0..2) {
                        val y = 114f + i * 9f
                        drawCircle(Color.Gray, 1.5f, Offset(9f, y + 2f))
                        drawRect(Color.LightGray, Offset(14f, y), Size(w * 0.5f, 4f))
                    }
                    // EXPERIENCE
                    drawRect(accent, Offset(6f, 148f), Size(w * 0.45f, 6f))
                    drawRect(accent, Offset(6f, 156f), Size(w - 12f, 1.5f))
                    drawRect(accent, Offset(6f, 158.5f), Size(w - 12f, 1.5f))
                    drawRect(Color.DarkGray, Offset(6f, 166f), Size(w * 0.5f, 4f))
                }
            }
        }
    }
}

@Composable
private fun TemplateGalleryCard(
    label: String,
    accent: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    preview: @Composable (Color) -> Unit
) {
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
