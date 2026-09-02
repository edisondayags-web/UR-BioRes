package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saltech.urdocs.R

private val RPink = Color(0xFF3B6FE0)
private val RGreen = Color(0xFF0B1530)
private val RGray = Color(0xFF9A9A9A)
private val RGradient = Brush.horizontalGradient(listOf(RPink, RGreen))

@Composable
fun ResumeChoiceScreen(onChoose: (String) -> Unit, onBack: () -> Unit = {}) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Transparent)) {
        PremiumWaveBackground()
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 24.dp, start = 8.dp)
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
        }

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
                Icon(Icons.Filled.Description, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
                Spacer(Modifier.width(10.dp))
                Text("san dito luv", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                Spacer(Modifier.width(6.dp))
                Text("\uD83D\uDC99", fontSize = 22.sp)
            }
            Spacer(Modifier.height(10.dp))
            Row {
                Text("Tap ", color = RGray, fontSize = 14.sp)
                Text("ka ", color = Color.White, fontSize = 14.sp)
                Text(" lang ", color = RGray, fontSize = 14.sp)
                Text("dyan", color = Color.White, fontSize = 14.sp)
                Text(" Luv.", color = RGray, fontSize = 14.sp)
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
                        .background(Brush.horizontalGradient(listOf(Color.Transparent, RPink)))
                )
                Icon(
                    Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = RGreen,
                    modifier = Modifier.padding(horizontal = 10.dp).size(18.dp)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(Brush.horizontalGradient(listOf(RGreen, Color.Transparent)))
                )
            }

            Spacer(Modifier.height(20.dp))

            ResumeChoiceCard(
                icon = { Icon(Icons.Filled.Description, contentDescription = null, tint = Color.White, modifier = it) },
                title = "HTML",
                subtitle = "(Ito luv maganda din Dito)",
                onClick = { onChoose("ai_html") }
            )

            Spacer(Modifier.height(16.dp))

            ResumeChoiceCard(
                icon = { Icon(Icons.Filled.Work, contentDescription = null, tint = Color.White, modifier = it) },
                title = "Chronological Resume",
                subtitle = "(Ito tech/CV style at ATS friendly)",
                onClick = { onChoose("chronological") }
            )


            Spacer(Modifier.height(16.dp))

            ResumeChoiceCard(
                icon = { Icon(Icons.Filled.GridView, contentDescription = null, tint = Color.White, modifier = it) },
                title = "More Templates",
                subtitle = "(Ito luv dark theme to)",
                onClick = { onChoose("gallery") }
            )
        }
    }
}

@Composable
private fun ResumeChoiceCard(
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
            .border(1.5.dp, RGradient, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black.copy(alpha = 0.4f))
                .border(1.5.dp, RGradient, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            icon(Modifier.size(26.dp))
        }
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(subtitle, color = RGray, fontSize = 13.sp)
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = RGreen, modifier = Modifier.size(24.dp))
    }
}
