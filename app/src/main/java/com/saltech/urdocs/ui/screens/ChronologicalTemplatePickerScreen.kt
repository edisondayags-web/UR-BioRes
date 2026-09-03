package com.saltech.urdocs.ui.screens

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
            Text("Choose a Template", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TemplatePickerCard(
                title = "Classic Navy",
                subtitle = "Centered header, navy accents — ATS-friendly",
                onClick = { onTemplateSelected(1) }
            )
            TemplatePickerCard(
                title = "Modern Minimal",
                subtitle = "Clean teal accents, extra white space — ATS-friendly",
                onClick = { onTemplateSelected(2) }
            )
            TemplatePickerCard(
                title = "Executive Classic",
                subtitle = "Monochrome, double-line dividers — ATS-friendly",
                onClick = { onTemplateSelected(3) }
            )
        }
    }
}

@Composable
private fun TemplatePickerCard(title: String, subtitle: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1A2440))
            .clickable(onClick = onClick)
            .padding(20.dp)
    ) {
        Text(title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text(subtitle, color = Color(0xFFB0B8C9), fontSize = 13.sp)
    }
}
