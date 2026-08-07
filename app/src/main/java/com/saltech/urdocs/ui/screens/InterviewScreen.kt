package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Videocam
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
import com.saltech.urdocs.ui.theme.UrPink

private val IPink = Color(0xFF3B6FE0)
private val IGreen = Color(0xFF0B1530)
private val IGray = Color(0xFF9A9A9A)
private val IGradient = Brush.horizontalGradient(listOf(IPink, IGreen))

@Composable
fun InterviewScreen(
    onBack: () -> Unit,
    onSelect: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // BAGONG BACKGROUND HERE
        PremiumWaveBackground()

        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 24.dp, start = 8.dp)
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = UrPink)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.RecordVoiceOver, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
                Spacer(Modifier.width(10.dp))
                Text("practice ka dito luv", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                Spacer(Modifier.width(6.dp))
                Text("\uD83D\uDC99", fontSize = 22.sp)
            }
            Spacer(Modifier.height(10.dp))
            Row {
                Text("Piliin ang ", color = IGray, fontSize = 14.sp)
                Text("gusto", color = Color.White, fontSize = 14.sp)
                Text(" mong ", color = IGray, fontSize = 14.sp)
                Text("practice", color = Color.White, fontSize = 14.sp)
                Text(".", color = IGray, fontSize = 14.sp)
            }

            Spacer(Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.weight(1f).height(1.dp)
                        .background(Brush.horizontalGradient(listOf(Color.Transparent, IPink)))
                )
                Icon(
                    Icons.Filled.AutoAwesome, contentDescription = null, tint = IGreen,
                    modifier = Modifier.padding(horizontal = 10.dp).size(18.dp)
                )
                Box(
                    modifier = Modifier.weight(1f).height(1.dp)
                        .background(Brush.horizontalGradient(listOf(IGreen, Color.Transparent)))
                )
            }

            Spacer(Modifier.height(20.dp))

            InterviewChoiceCard(
                icon = { Icon(Icons.Filled.RecordVoiceOver, contentDescription = null, tint = Color.White, modifier = it) },
                title = "Local (BPO) • Traditional",
                subtitle = "Live Q&A, kausap ka",
                onClick = { onSelect("local_traditional") }
            )
            Spacer(Modifier.height(16.dp))
            InterviewChoiceCard(
                icon = { Icon(Icons.Filled.Videocam, contentDescription = null, tint = Color.White, modifier = it) },
                title = "Local (BPO) • Async Video",
                subtitle = "Record answer, may time limit",
                onClick = { onSelect("local_async") }
            )
            Spacer(Modifier.height(16.dp))
            InterviewChoiceCard(
                icon = { Icon(Icons.Filled.Public, contentDescription = null, tint = Color.White, modifier = it) },
                title = "International • Traditional",
                subtitle = "Live Q&A, kausap ka",
                onClick = { onSelect("intl_traditional") }
            )
            Spacer(Modifier.height(16.dp))
            InterviewChoiceCard(
                icon = { Icon(Icons.Filled.Language, contentDescription = null, tint = Color.White, modifier = it) },
                title = "International • Async Video",
                subtitle = "Record answer, may time limit",
                onClick = { onSelect("intl_async") }
            )
        }
    }
}

@Composable
private fun InterviewChoiceCard(
    icon: @Composable (Modifier) -> Unit,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Black.copy(alpha = 0.55f))
            .border(1.5.dp, IGradient, RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {
        // Wave background sits behind the card content, clipped to the card's rounded corners.
        PremiumWaveBackground()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.4f))
                    .border(1.5.dp, IGradient, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                icon(Modifier.size(26.dp))
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text(subtitle, color = IGray, fontSize = 13.sp)
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = IGreen, modifier = Modifier.size(24.dp))
        }
    }
}
