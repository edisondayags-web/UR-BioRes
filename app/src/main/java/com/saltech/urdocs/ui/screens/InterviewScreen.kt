package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saltech.urdocs.ui.theme.UrGray
import com.saltech.urdocs.ui.theme.UrPink
import com.saltech.urdocs.ui.theme.pressScale

private data class InterviewChoice(
    val title: String,
    val subtitle: String,
    val color: Color,
    val mode: String
)

@Composable
fun InterviewScreen(
    onBack: () -> Unit,
    onSelect: (String) -> Unit
) {
    val choices = listOf(
        InterviewChoice("LOCAL (BPO) • TRADITIONAL", "Live Q&A, kausap ka", Color(0xFF4C8DFF), "local_traditional"),
        InterviewChoice("LOCAL (BPO) • ASYNC VIDEO", "Record answer, may time limit", Color(0xFF4CFFA0), "local_async"),
        InterviewChoice("INTERNATIONAL • TRADITIONAL", "Live Q&A, kausap ka", Color(0xFFFFA84C), "intl_traditional"),
        InterviewChoice("INTERNATIONAL • ASYNC VIDEO", "Record answer, may time limit", Color(0xFFB44CFF), "intl_async"),
    )

    Box(modifier = Modifier.fillMaxSize()) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 24.dp, start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = UrPink
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 72.dp, start = 20.dp, end = 20.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = "Piliin ang gusto mong practice luv🩵",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            choices.forEach { choice ->
                InterviewChoiceCard(choice = choice, onClick = { onSelect(choice.mode) })
            }
        }
    }
}

@Composable
private fun InterviewChoiceCard(choice: InterviewChoice, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .pressScale(interactionSource)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black.copy(alpha = 0.55f))
            .border(2.dp, choice.color, RoundedCornerShape(16.dp))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .padding(horizontal = 20.dp, vertical = 22.dp)
    ) {
        Text(
            text = choice.title,
            color = choice.color,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = choice.subtitle,
            color = UrGray,
            fontSize = 13.sp
        )
    }
}
