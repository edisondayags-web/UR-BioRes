package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ResumeChoiceScreen(onChoose: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "📄 Pumili ng Resume Style",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { onChoose("modern") }, modifier = Modifier.fillMaxWidth()) {
            Text("✨ Modern (CV-style, walang photo box)")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onChoose("corporate") }, modifier = Modifier.fillMaxWidth()) {
            Text("🏢 Corporate (may 2x2 photo box, tradisyunal)")
        }
    }
}
