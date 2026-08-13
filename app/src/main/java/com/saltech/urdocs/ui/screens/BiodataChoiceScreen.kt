package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BPink = Color(0xFF3B6FE0)
private val BGreen = Color(0xFF0B1530)
private val BGray = Color(0xFF9A9A9A)
private val BGradient = Brush.horizontalGradient(listOf(BPink, BGreen))

@Composable
fun BiodataChoiceScreen(onChoose: (String) -> Unit, onBack: () -> Unit = {}) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Transparent)) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 24.dp, start = 8.dp)
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
        }
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
                Text("Pili kalang ", color = BGray, fontSize = 14.sp)
                Text("ng gusto", color = Color.White, fontSize = 14.sp)
                Text(" mo ", color = BGray, fontSize = 14.sp)
                Text("luv", color = Color.White, fontSize = 14.sp)
                Text(" 🩵.", color = BGray, fontSize = 14.sp)
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
                icon = { Icon(Icons.Filled.Badge, contentDescription = null, tint = Color.White, modifier = it) },
                title = "PH Job Application Bio-Data",
                subtitle = "(Traditional)",
                onClick = { onChoose("ph_form") }
            )

            Spacer(Modifier.height(16.dp))

            BiodataChoiceCard(
                icon = { Icon(Icons.Filled.EditNote, contentDescription = null, tint = Color.White, modifier = it) },
                title = "Bio-Data (Black)",
                subtitle = "(Modern black)",
                onClick = { onChoose("black") }
            )

            Spacer(Modifier.height(16.dp))

            BiodataChoiceCard(
                icon = { Icon(Icons.Filled.EditNote, contentDescription = null, tint = Color.White, modifier = it) },
                title = "Bio-Data (Blue)",
                subtitle = "(Modern blue)",
                onClick = { onChoose("blue") }
            )

            Spacer(Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        1.dp,
                        Brush.linearGradient(listOf(Color(0xFF3B6FE0), Color(0xFFE0245E))),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Text("“", fontSize = 20.sp, color = Color(0xFF3B6FE0), modifier = Modifier.align(Alignment.TopStart))
                Text("”", fontSize = 20.sp, color = Color(0xFFE0245E), modifier = Modifier.align(Alignment.BottomEnd))
                Text(
                    buildAnnotatedString {
                        append("Walang kwenta pagiging matalino nyo kung tatawanan lang yan ng saltik Este ")
                        withStyle(SpanStyle(color = Color(0xFF3B6FE0), fontWeight = FontWeight.Bold)) {
                            append("Sal-Tech")
                        }
                    },
                    fontSize = 12.sp,
                    color = Color(0xFF9A9A9A),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center).padding(horizontal = 16.dp)
                )
            }
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
