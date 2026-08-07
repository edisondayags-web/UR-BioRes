package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.Icon
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

private val BPink = Color(0xFF3B6FE0)
private val BGreen = Color(0xFF0B1530)
private val BGray = Color(0xFF9A9A9A)
private val BGradient = Brush.horizontalGradient(listOf(BPink, BGreen))

@Composable
fun BiodataChoiceScreen(onChoose: (String) -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Transparent)) {
        PremiumWaveBackground()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Badge, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
                Spacer(Modifier.width(10.dp))
                Text("san dito luv", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                Spacer(Modifier.width(6.dp))
                Text("\uD83D\uDC99", fontSize = 22.sp)
            }
            Spacer(Modifier.height(10.dp))
            Row {
                Text("Pili ka mg ", color = BGray, fontSize = 14.sp)
                Text("gusto", color = Color.White, fontSize = 14.sp)
                Text(" mong ", color = BGray, fontSize = 14.sp)
                Text("format", color = Color.White, fontSize = 14.sp)
                Text(" luv🩵.", color = BGray, fontSize = 14.sp)
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(Brush.horizontalGradient(listOf(Color.Transparent, BPink)))
                )
                Icon(
                    Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = BGreen,
                    modifier = Modifier.padding(horizontal = 10.dp).size(18.dp)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(Brush.horizontalGradient(listOf(BGreen, Color.Transparent)))
                )
            }

            Spacer(Modifier.height(20.dp))

            BiodataChoiceCard(
                icon = { Icon(Icons.Filled.EditNote, contentDescription = null, tint = Color.White, modifier = it) },
                title = "Standard Bio-Data",
                subtitle = "(Standard)",
                onClick = { onChoose("standard") }
            )

            Spacer(Modifier.height(16.dp))

            BiodataChoiceCard(
                icon = { Icon(Icons.Filled.Badge, contentDescription = null, tint = Color.White, modifier = it) },
                title = "PH Job Application Bio-Data",
                subtitle = "(Traditional)",
                onClick = { onChoose("ph_form") }
            )
        }
    }
}

@Composable
private fun BiodataChoiceCard(
    icon: @Composable (Modifier) -> Unit,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Black.copy(alpha = 0.55f))
            .border(1.5.dp, BGradient, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black.copy(alpha = 0.4f))
                .border(1.5.dp, BGradient, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            icon(Modifier.size(26.dp))
        }
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(subtitle, color = BGray, fontSize = 13.sp)
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = BGreen, modifier = Modifier.size(24.dp))
    }
}
