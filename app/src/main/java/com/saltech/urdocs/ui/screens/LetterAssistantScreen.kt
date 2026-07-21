package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saltech.urdocs.data.GeminiRepository
import com.saltech.urdocs.model.LetterType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.animation.core.*

private val UrPink = Color(0xFFFF2E7E)
private val UrGreen = Color(0xFF39FF6A)
private val UrGray = Color(0xFF9A9A9A)
private val UrBubbleDark = Color(0xFF161616)

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val time: String = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
)

@Composable
fun LetterAssistantScreen(
    letterType: LetterType,
    onBack: () -> Unit,
    repository: GeminiRepository = remember { GeminiRepository() }
) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    var history by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    var inputText by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(false) }
    var started by remember { mutableStateOf(false) }

    fun addMessage(text: String, isUser: Boolean) {
        messages = messages + ChatMessage(text = text, isUser = isUser)
    }

    fun sendToGemini(userText: String?) {
        scope.launch {
            isTyping = true
            if (userText != null) {
                history = history + ("user" to userText)
            }
            delay(600 + (200..900).random().toLong())
            val reply = repository.chat(history)
            history = history + ("model" to reply)
            isTyping = false
            addMessage(reply, isUser = false)
        }
    }

    fun handleUserInput(text: String) {
        if (text.isBlank() || isTyping) return
        addMessage(text, isUser = true)
        inputText = ""
        sendToGemini(text)
    }

    LaunchedEffect(Unit) {
        if (!started) {
            started = true
            sendToGemini("Gusto ko ng ${letterType.label}.")
        }
    }

    LaunchedEffect(messages.size, isTyping) {
        if (messages.isNotEmpty() || isTyping) {
            listState.animateScrollToItem((messages.size - 1 + if (isTyping) 1 else 0).coerceAtLeast(0))
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, UrPink, RoundedCornerShape(10.dp))
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = UrPink)
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row {
                    Text(letterType.label.substringBefore(" "), color = UrPink, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(" Letter ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Assistant", color = UrGreen, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Text("I'm here to help you create your ${letterType.label.lowercase()}.", color = UrGray, fontSize = 11.sp)
            }
            Box(
                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).border(1.dp, UrGreen, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Description, contentDescription = null, tint = UrGreen, modifier = Modifier.size(20.dp))
            }
        }
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Brush.horizontalGradient(listOf(UrPink, UrGreen))))

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            AnimatedChatBackground(isTyping)
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
            items(messages) { msg -> ChatBubble(msg) }
            if (isTyping) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AssistantAvatar()
                        Spacer(Modifier.width(10.dp))
                        Box(
                            modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(UrBubbleDark).padding(14.dp)
                        ) {
                            TypingDots()
                        }
                    }
                }
            }
        }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF0A0A0A))
                .border(BorderStroke(1.5.dp, Brush.linearGradient(listOf(UrGreen, UrPink))), RoundedCornerShape(24.dp))
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.AttachFile, contentDescription = null, tint = UrPink, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Type your message...", color = UrGray, fontSize = 13.sp) },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                enabled = !isTyping
            )
            Box(
                modifier = Modifier.size(38.dp).clip(CircleShape).background(UrPink)
                    .clickable(enabled = inputText.isNotBlank() && !isTyping) { handleUserInput(inputText) },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun TypingDots() {
    val transition = rememberInfiniteTransition(label = "typing")
    Row {
        repeat(3) { i ->
            val alpha by transition.animateFloat(
                initialValue = 0.3f, targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = androidx.compose.animation.core.tween(500, delayMillis = i * 150),
                    repeatMode = RepeatMode.Reverse
                ), label = "dot$i"
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(UrGreen.copy(alpha = alpha))
            )
        }
    }
}

@Composable
private fun AssistantAvatar() {
    Box(
        modifier = Modifier.size(34.dp).clip(CircleShape).border(1.dp, UrGreen, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = UrGreen, modifier = Modifier.size(16.dp))
    }
}

@Composable
private fun ChatBubble(msg: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!msg.isUser) {
            AssistantAvatar()
            Spacer(Modifier.width(10.dp))
        }
        Column(
            modifier = Modifier.widthIn(max = 260.dp).clip(RoundedCornerShape(16.dp))
                .background(if (msg.isUser) UrGreen else UrBubbleDark).padding(14.dp)
        ) {
            Text(msg.text, color = if (msg.isUser) Color.Black else Color.White, fontSize = 14.sp)
            Spacer(Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                Text(msg.time, color = if (msg.isUser) Color(0xFF0A3D1F) else UrGray, fontSize = 10.sp)
                if (msg.isUser) {
                    Spacer(Modifier.width(4.dp))
                    Icon(Icons.Filled.DoneAll, contentDescription = null, tint = Color(0xFF0A3D1F), modifier = Modifier.size(12.dp))
                }
            }
        }
    }
}

@Composable
private fun AnimatedChatBackground(isTyping: Boolean) {
    val transition = rememberInfiniteTransition(label = "bgShift")
    val shift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = androidx.compose.animation.core.tween(
                durationMillis = if (isTyping) 2500 else 6000,
                easing = androidx.compose.animation.core.LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bgShiftValue"
    )
    val baseAlpha = if (isTyping) 0.18f else 0.08f
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        UrPink.copy(alpha = baseAlpha * shift),
                        Color.Black,
                        UrGreen.copy(alpha = baseAlpha * (1f - shift))
                    ),
                    start = androidx.compose.ui.geometry.Offset(0f, shift * 1000f),
                    end = androidx.compose.ui.geometry.Offset(1000f, (1f - shift) * 1000f)
                )
            )
    )
}
