package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saltech.urdocs.R

@Composable
fun ResumeChoiceScreen(onChoose: (String) -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().scale(1.5f),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "📄 san dito luv💙",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = { onChoose("traditional") }, modifier = Modifier.fillMaxWidth()) {
                Text("📃 Traditional Resume (may 2x2 photo)")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { onChoose("chronological") }, modifier = Modifier.fillMaxWidth()) {
                Text("💼 Chronological Resume (walang photo, tech/CV style)")
            }
        }
    }
}
