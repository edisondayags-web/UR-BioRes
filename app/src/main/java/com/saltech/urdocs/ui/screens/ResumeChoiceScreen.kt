package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saltech.urdocs.R

private val RPink = Color(0xFFFF2E7E)
private val RGreen = Color(0xFF39FF6A)
private val RGray = Color(0xFF9A9A9A)

@Composable
fun ResumeChoiceScreen(onChoose: (String) -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 220.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Description, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
                Spacer(Modifier.width(10.dp))
                Text("san dito luv", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                Text("\uD83D\uDC99", fontSize = 22.sp)
            }
            Spacer(Modifier.height(6.dp))
            Row {
                Text("Piliin ang ", color = RGray, fontSize = 14.sp)
                Text("gusto", color = RGreen, fontSize = 14.sp)
                Text(" mong ", color = RGray, fontSize = 14.sp)
                Text("format", color = RPink, fontSize = 14.sp)
                Text(" ng resume.", color = RGray, fontSize = 14.sp)
            }

            Spacer(Modifier.height(24.dp))

            ResumeChoiceCard(
                icon = { Icon(Icons.Filled.Description, contentDescription = null, tint = RPink, modifier = it) },
                title = "Traditional Resume",
                subtitle = "(may 2x2 photo)",
                accentColor = RPink,
                onClick = { onChoose("traditional") }
            )

            Spacer(Modifier.height(16.dp))

            ResumeChoiceCard(
                icon = { Icon(Icons.Filled.Work, contentDescription = null, tint = RGreen, modifier = it) },
                title = "Chronological Resume",
                subtitle = "(walang photo, tech/CV style)",
                accentColor = RGreen,
                onClick = { onChoose("chronological") }
            )
        }
    }
}

@Composable
private fun ResumeChoiceCard(
    icon: @Composable (Modifier) -> Unit,
    title: String,
    subtitle: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF0A0A0A).copy(alpha = 0.75f))
            .border(1.5.dp, accentColor, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF1A1A1A))
                .border(1.dp, accentColor.copy(alpha = 0.6f), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            icon(Modifier.size(24.dp))
        }
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
            Text(subtitle, color = RGray, fontSize = 13.sp)
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = accentColor, modifier = Modifier.size(22.dp))
    }
}
