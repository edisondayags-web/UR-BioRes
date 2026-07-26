package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
    val items = listOf(
        HomeMenuItem("📄", "RESUME", "Pang BPO/Office etc • Professional CV", "resume"),
        HomeMenuItem("📝", "BIO-DATA", "Pang Company/Store etc. • PH Job Application", "biodata"),
        HomeMenuItem("🏛️", "GOV'T WEBSITES", "All Links You Want", "govt_forms"),
        HomeMenuItem("✉️", "LETTERS", "Leave, Excuse, Resign, etc.", "letters")
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // Gear icon -- malapit sa status bar, palaging nakikita
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
                .padding(top = 80.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Pili ka ng gusto mo luv🩵",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))

            items.forEach { item ->
                HomeMenuCard(item = item, onClick = { onNavigate(item.route) })
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "DEVELOPER: EDISON SUCLATAN DAYAGUIT",
                color = UrGray,
                fontSize = 10.sp,
                modifier = Modifier.padding(bottom = 20.dp)
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
            .background(UrGray)
            .border(1.dp, Brush.linearGradient(listOf(Color.Black, Color(0xFF4C8DFF))), RoundedCornerShape(16.dp))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = item.emoji, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                color = UrPink,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = item.subtitle,
                color = UrNeon,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
