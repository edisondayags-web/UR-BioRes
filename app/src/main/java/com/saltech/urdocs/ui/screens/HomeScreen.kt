package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.border
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saltech.urdocs.R
import com.saltech.urdocs.ui.theme.UrGray
import com.saltech.urdocs.ui.theme.UrNeon
import com.saltech.urdocs.ui.theme.UrPink
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import com.saltech.urdocs.ui.theme.pressScale
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

data class HomeMenuItem(
    val emoji: String,
    val title: String,
    val subtitle: String,
    val route: String
)

@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit
) {
    var showComingSoon by remember { mutableStateOf(false) }
    val items = listOf(
        HomeMenuItem("📄", "RESUME", "Pang BPO/Office etc • Professional CV", "resume"),
        HomeMenuItem("📝", "BIO-DATA", "Pang Company/Store etc. • PH Job Application", "biodata"),
        HomeMenuItem("✉️", "LETTERS", "Leave, Excuse, Resign, etc.", "letters"),
        HomeMenuItem("💬", "INTERVIEW", "Practice Office Interview (Q&A / Tips)", "interview"),
        HomeMenuItem("🔎", "JOB RESEARCHER", "Hahanapan ka ng work near you", "job_researcher"),
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        IconButton(
            onClick = { onNavigate("settings") },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 24.dp, end = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = UrPink
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 56.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Pili ka ng gusto mo luv🩵",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.weight(1f))

            Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                items.forEach { item ->
                    HomeMenuCard(
                        item = item,
                        onClick = { onNavigate(item.route) }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "DEVELOPER: EDISON SUCLATAN DAYAGUIT",
                color = UrGray,
                fontSize = 10.sp
            )
        }
        if (showComingSoon) {
            AlertDialog(
                onDismissRequest = { showComingSoon = false },
                title = { Text("Coming Soon pato Luv❤️🩵") },
                text = { Text("under maintenance pa luv sorry") },
                confirmButton = {
                    TextButton(onClick = { showComingSoon = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@Composable
private fun HomeMenuCard(item: HomeMenuItem, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .pressScale(interactionSource)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black.copy(alpha = 0.55f))
            .border(1.dp, Brush.linearGradient(listOf(Color(0xFF4C8DFF), Color.Black)), RoundedCornerShape(16.dp))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .padding(horizontal = 18.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = item.emoji, fontSize = 26.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                color = UrPink,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
            Text(
                text = item.subtitle,
                color = UrNeon,
                fontSize = 13.sp
            )
        }
    }
}
