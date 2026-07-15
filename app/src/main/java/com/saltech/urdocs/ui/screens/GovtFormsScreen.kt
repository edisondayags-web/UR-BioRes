package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GovtFormsScreen(onNavigate: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("🏛️ Gov't Forms", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))
        Text("SSS, Pag-IBIG, PhilHealth requests. Gagamitin din nito ang Bio-Data form mo -- TODO: PDF template filling sa susunod na milestone.")
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { onNavigate("biodata") }, modifier = Modifier.fillMaxWidth()) {
            Text("Gamitin ang Bio-Data info")
        }
    }
}
