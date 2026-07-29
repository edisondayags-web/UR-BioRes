package com.saltech.urdocs.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import com.saltech.urdocs.ui.components.PremiumThinkingIndicator
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.saltech.urdocs.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saltech.urdocs.data.GeminiRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

private val JrBlue = Color(0xFF4C8DFF)
private val JrBlueDeep = Color(0xFF16255E)
private val JrBlueDark = Color(0xFF060B18)
private val JrGray = Color(0xFF9A9A9A)
private val JrUserBubble = Color(0xFF1E4FD6)

data class JrChatMessage(
    val text: String,
    val isUser: Boolean,
    val time: String = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
)

@Composable
fun JobResearcherScreen(
    onBack: () -> Unit,
    repository: GeminiRepository = remember { GeminiRepository() }
) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Filled.WorkOutline, contentDescription = null, tint = JrBlue, modifier = Modifier.size(64.dp))
            Spacer(Modifier.height(16.dp))
            Text(
                "coming soon pa to luv🩵",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
        Box(
            modifier = Modifier
                .padding(16.dp)
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.06f))
                .border(1.dp, Brush.verticalGradient(listOf(JrBlue, Color.Black)), RoundedCornerShape(12.dp))
                .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = JrBlue)
        }
    }
}

@Composable
private fun JobResearcherScreenOriginal(
    onBack: () -> Unit,
    repository: GeminiRepository = remember { GeminiRepository() }
) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var messages by remember { mutableStateOf(listOf<JrChatMessage>()) }
    var history by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    var inputText by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(false) }
    var started by remember { mutableStateOf(false) }

    fun addMessage(text: String, isUser: Boolean) {
        messages = messages + JrChatMessage(text = text, isUser = isUser)
    }

    fun handleUserInput(text: String) {
        if (text.isBlank() || isTyping) return
        addMessage(text, isUser = true)
        val updatedHistory = history + ("user" to text)
        history = updatedHistory
        inputText = ""
        isTyping = true
        scope.launch {
            val reply = repository.chatOpen(updatedHistory)
            addMessage(reply, isUser = false)
            history = history + ("model" to reply)
            isTyping = false
        }
    }

    LaunchedEffect(Unit) {
        if (!started) {
            started = true
        }
    }

    LaunchedEffect(messages.size, isTyping) {
        val lastIndex = messages.size - 1 + if (isTyping) 1 else 0
        if (lastIndex >= 0) {
            listState.animateScrollToItem(lastIndex)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        JrAnimatedChatBackground(isTyping)

        AnimatedVisibility(
            visible = messages.isEmpty() && !isTyping,
            enter = fadeIn(animationSpec = tween(500)),
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 24.dp, end = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(painter = painterResource(R.drawable.ic_brain_thinking), contentDescription = null, modifier = Modifier.size(64.dp))
                Spacer(Modifier.height(16.dp))
                Text(
                    "Hello luv have\nA great day",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 110.dp, bottom = 72.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(messages, key = { it.time + it.text.hashCode() }) { msg ->
                var visible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) { visible = true }
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(animationSpec = tween(400, easing = LinearOutSlowInEasing))
                ) {
                    JrChatBubble(msg)
                }
            }
            if (isTyping) {
                item { JrThinkingBubble() }
            }
        }

        Box(
            modifier = Modifier
                .padding(16.dp)
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.06f))
                .border(1.dp, Brush.verticalGradient(listOf(JrBlue, Color.Black)), RoundedCornerShape(12.dp))
                .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = JrBlue)
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp)
                .shadow(18.dp, RoundedCornerShape(28.dp), ambientColor = JrBlue, spotColor = JrBlue)
                .clip(RoundedCornerShape(28.dp))
                .background(Color(0xFF0C0F16))
                .border(BorderStroke(1.2.dp, Brush.horizontalGradient(listOf(JrBlue, JrBlueDeep))), RoundedCornerShape(28.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.AttachFile, contentDescription = null, tint = JrBlue, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Type your message...", color = JrGray, fontSize = 13.sp) },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Brush.verticalGradient(listOf(JrBlue, JrBlueDeep)))
                    .clickable { handleUserInput(inputText) },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
private fun JrChatBubble(msg: JrChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!msg.isUser) {
            JrAssistantAvatar()
            Spacer(Modifier.width(10.dp))
        }
        Column(
            modifier = if (msg.isUser) {
                Modifier.widthIn(max = 260.dp).clip(RoundedCornerShape(18.dp))
                    .background(Brush.verticalGradient(listOf(JrUserBubble, Color(0xFF0D1440))))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            } else {
                Modifier.widthIn(max = 280.dp).padding(vertical = 4.dp)
            }
        ) {
            Text(msg.text, color = Color.White, fontSize = 14.sp)
            Spacer(Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(msg.time, color = if (msg.isUser) Color(0xFFB9CBFF).copy(alpha = 0.7f) else JrGray, fontSize = 10.sp)
            }
        }
    }
}

@Composable
private fun JrThinkingBubble() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        JrAssistantAvatar()
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier) {
            JrThinkingIndicator()
        }
    }
}

@Composable
private fun JrAnimatedChatBackground(isTyping: Boolean) {
    val transition = rememberInfiniteTransition(label = "bgPulse")
    val pulse by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (isTyping) 1400 else 3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bgPulseValue"
    )
    val baseIntensity = if (isTyping) 0.25f else 0.12f
    val intensity = baseIntensity + pulse * 0.08f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Black,
                        JrBlueDark,
                        JrBlueDeep.copy(alpha = intensity),
                        JrBlue.copy(alpha = intensity * 0.9f)
                    )
                )
            )
    )
}

@Composable
private fun JrThinkingIndicator() {
    PremiumThinkingIndicator()
}

@Composable
private fun JrWavingText(text: String, color: Color) {
    val transition = rememberInfiniteTransition(label = "wave")
    Row {
        text.forEachIndexed { i, ch ->
            val offsetY by transition.animateFloat(
                initialValue = 0f, targetValue = -4f,
                animationSpec = infiniteRepeatable(
                    animation = tween(400, delayMillis = i * 60, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = "wave$i"
            )
            Text(
                ch.toString(),
                color = color,
                fontSize = 12.sp,
                modifier = Modifier.offset(y = offsetY.dp)
            )
        }
    }
}

@Composable
private fun JrTypingDots() {
    val transition = rememberInfiniteTransition(label = "typing")
    Row {
        repeat(3) { i ->
            val scaleAnim by transition.animateFloat(
                initialValue = 0.6f, targetValue = 1.3f,
                animationSpec = infiniteRepeatable(
                    animation = tween(400, delayMillis = i * 120),
                    repeatMode = RepeatMode.Reverse
                ), label = "dot$i"
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .size(6.dp)
                    .scale(scaleAnim)
                    .clip(CircleShape)
                    .background(JrBlue)
            )
        }
    }
}

@Composable
private fun JrAssistantAvatar() {
    Box(
        modifier = Modifier.size(34.dp).clip(CircleShape)
            .border(1.dp, Brush.verticalGradient(listOf(JrBlue, Color.Black)), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Filled.WorkOutline, contentDescription = null, tint = JrBlue, modifier = Modifier.size(16.dp))
    }
}
