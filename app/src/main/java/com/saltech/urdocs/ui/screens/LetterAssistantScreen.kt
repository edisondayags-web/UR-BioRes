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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.saltech.urdocs.model.LetterRequest
import com.saltech.urdocs.model.LetterType
import com.saltech.urdocs.viewmodel.LettersViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

private val UrPink = Color(0xFFFF2E7E)
private val UrGreen = Color(0xFF39FF6A)
private val UrGray = Color(0xFF9A9A9A)
private val UrBubbleDark = Color(0xFF161616)

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val time: String = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
)

private enum class ChatStep { ASK_REASON, ASK_NAME_POSITION, GENERATING, DONE }

private fun quickRepliesFor(type: LetterType): List<Pair<String, String>> = when {
    type.name.contains("LEAVE") -> listOf(
        "Sick Leave" to "heart", "Vacation Leave" to "beach",
        "Personal Leave" to "person", "Other" to "dots"
    )
    type.name.contains("EXCUSE") -> listOf(
        "Medical" to "heart", "Family Emergency" to "person",
        "Traffic/Weather" to "beach", "Other" to "dots"
    )
    type.name.contains("RESIGNATION") -> listOf(
        "Immediate" to "dots", "2 Weeks Notice" to "beach",
        "1 Month Notice" to "person", "Other" to "dots"
    )
    else -> listOf("Standard Request" to "person", "Other" to "dots")
}

@Composable
private fun QuickReplyIcon(key: String, tint: Color) {
    val icon = when (key) {
        "heart" -> Icons.Filled.Favorite
        "beach" -> Icons.Filled.BeachAccess
        "person" -> Icons.Filled.Person
        else -> Icons.Filled.MoreHoriz
    }
    Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
}

@Composable
fun LetterAssistantScreen(
    letterType: LetterType,
    onBack: () -> Unit,
    viewModel: LettersViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage(
                    text = "Hi! I'll help you create a professional ${letterType.label}.\n\nAno ang klase/rason nito?",
                    isUser = false
                )
            )
        )
    }
    var step by remember { mutableStateOf(ChatStep.ASK_REASON) }
    var showQuickReplies by remember { mutableStateOf(true) }
    var inputText by remember { mutableStateOf("") }
    var collectedReason by remember { mutableStateOf("") }

    fun addMessage(text: String, isUser: Boolean) {
        messages = messages + ChatMessage(text = text, isUser = isUser)
    }

    fun handleUserInput(text: String) {
        if (text.isBlank()) return
        addMessage(text, isUser = true)
        showQuickReplies = false
        inputText = ""

        when (step) {
            ChatStep.ASK_REASON -> {
                collectedReason = text
                step = ChatStep.ASK_NAME_POSITION
                addMessage(
                    "Got it! Gagawa ako ng ${letterType.label.lowercase()} para dito: \"$text\".\n\nPakisulat ang buong pangalan at position mo (hal. \"Juan Dela Cruz, Staff\").",
                    isUser = false
                )
            }
            ChatStep.ASK_NAME_POSITION -> {
                val parts = text.split(",")
                val fullName = parts.getOrNull(0)?.trim() ?: text.trim()
                val position = parts.getOrNull(1)?.trim() ?: ""
                step = ChatStep.GENERATING
                addMessage("Perfect, ginagawa na ang letter mo... ✨", isUser = false)

                scope.launch {
                    viewModel.generate(
                        LetterRequest(
                            type = letterType,
                            fullName = fullName,
                            position = position,
                            company = "",
                            reason = collectedReason,
                            dateNeeded = "",
                            extraDetails = ""
                        )
                    )
                }
            }
            else -> {}
        }
    }

    LaunchedEffect(uiState.generatedLetter) {
        uiState.generatedLetter?.let { letter ->
            addMessage(letter, isUser = false)
            step = ChatStep.DONE
        }
    }
    LaunchedEffect(uiState.error) {
        uiState.error?.let { err ->
            addMessage("Pasensya na, may problema: $err\n\nSubukan mo ulit.", isUser = false)
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

        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(messages) { msg -> ChatBubble(msg) }
            if (step == ChatStep.GENERATING && uiState.isLoading) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AssistantAvatar()
                        Spacer(Modifier.width(10.dp))
                        CircularProgressIndicator(color = UrGreen, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                        Spacer(Modifier.width(8.dp))
                        Text("Gumagawa ng letter...", color = UrGray, fontSize = 12.sp)
                    }
                }
            }
            if (showQuickReplies && step == ChatStep.ASK_REASON) {
                val opts = quickRepliesFor(letterType)
                items(opts.chunked(2)) { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(start = 44.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowItems.forEach { (label, iconKey) ->
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .border(1.dp, UrPink, RoundedCornerShape(20.dp))
                                    .clickable { handleUserInput(label) }
                                    .padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                QuickReplyIcon(iconKey, UrPink)
                                Spacer(Modifier.width(6.dp))
                                Text(label, color = Color.White, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        LaunchedEffect(messages.size) {
            if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1)
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
                enabled = step != ChatStep.GENERATING && step != ChatStep.DONE
            )
            Box(
                modifier = Modifier.size(38.dp).clip(CircleShape).background(UrPink)
                    .clickable(enabled = inputText.isNotBlank()) { handleUserInput(inputText) },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
            }
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
