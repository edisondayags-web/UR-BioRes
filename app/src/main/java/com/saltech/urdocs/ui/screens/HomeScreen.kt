package com.saltech.urdocs.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.saltech.urdocs.data.GeminiRepository
import com.saltech.urdocs.model.LetterType
import com.saltech.urdocs.ui.components.PremiumThinkingIndicator
import kotlinx.coroutines.launch

private val HcBlue = Color(0xFF4C8DFF)
private val HcBlueDeep = Color(0xFF16255E)
private val HcDrawerBg = Color(0xFF060B18)
private val HcGray = Color(0xFF9A9A9A)
private val HcInputBg = Color(0xFF0C0F16)

@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val listState = rememberLazyListState()
    val repository = remember { GeminiRepository() }

    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    var history by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    var inputText by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(false) }
    var showComingSoon by remember { mutableStateOf(false) }

    val email = remember { FirebaseAuth.getInstance().currentUser?.email }

    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    fun go(route: String) {
        scope.launch { drawerState.close() }
        onNavigate(route)
    }

    fun send(text: String) {
        if (text.isBlank() || isTyping) return
        val clean = text.trim()
        messages = messages + ChatMessage(clean, true)
        history = history + ("user" to clean)
        inputText = ""
        scope.launch {
            isTyping = true
            val reply = try {
                repository.chat(history, LetterType.CUSTOM)
            } catch (e: Exception) {
                "Nagka-error luv, subukan ulit."
            }
            history = history + ("model" to reply)
            isTyping = false
            val shown = reply
                .replace("###LETTER_START###", "")
                .replace("###LETTER_END###", "")
                .replace("###ASK_LANGUAGE###", "")
                .trim()
            messages = messages + ChatMessage(if (shown.isBlank()) "..." else shown, false)
        }
    }

    LaunchedEffect(messages.size, isTyping) {
        val last = messages.size - 1 + if (isTyping) 1 else 0
        if (last >= 0) listState.animateScrollToItem(last)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = HcDrawerBg,
                modifier = Modifier.width(300.dp)
            ) {
                Column(modifier = Modifier.fillMaxHeight()) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Profile header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { go("settings") }
                                .padding(start = 20.dp, end = 20.dp, top = 40.dp, bottom = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, HcBlue, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Outlined.Person, contentDescription = null, tint = HcBlue, modifier = Modifier.size(32.dp))
                            }
                            Spacer(Modifier.width(14.dp))
                            Column {
                                Text("Profile", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Text(
                                    if (email.isNullOrBlank()) "Guest" else email,
                                    color = HcBlue,
                                    fontSize = 13.sp
                                )
                            }
                        }
                        HorizontalDivider(color = HcBlue.copy(alpha = 0.35f))
                        Spacer(Modifier.height(8.dp))

                        DrawerItem(Icons.Outlined.Edit, "Drafts", "(save if no payment)") { go("drafts") }
                        DrawerItem(Icons.Outlined.Settings, "Settings") { go("settings") }
                        DrawerItem(Icons.Outlined.Description, "RESUME") { go("resume") }
                        DrawerItem(Icons.Outlined.Badge, "BIODATA") { go("biodata") }
                        DrawerItem(Icons.Outlined.Mail, "LETTERS") { go("letters") }
                        DrawerItem(Icons.Outlined.Mic, "INTERVIEW") { go("interview") }
                        DrawerItem(Icons.Outlined.Search, "JOB RESEARCHER") { go("job_researcher") }

                        Spacer(Modifier.height(16.dp))
                        Text(
                            "YOUR CHATS",
                            color = HcBlue,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                        )
                        DrawerItem(Icons.Outlined.Add, "New chat") {
                            messages = emptyList()
                            history = emptyList()
                            inputText = ""
                            scope.launch { drawerState.close() }
                        }
                        val firstUserMsg = messages.firstOrNull { it.isUser }?.text
                        if (firstUserMsg != null) {
                            DrawerItem(Icons.Outlined.ChatBubbleOutline, firstUserMsg.take(26)) {
                                scope.launch { drawerState.close() }
                            }
                        }
                    }

                    // Upgrade card
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(HcBlueDeep.copy(alpha = 0.5f))
                            .border(1.dp, HcBlue, RoundedCornerShape(14.dp))
                            .clickable { showComingSoon = true }
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Star, contentDescription = null, tint = HcBlue, modifier = Modifier.size(30.dp))
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text("Upgrade to Pro 99", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("Unlock templates like resume • biodata • letter • etc", color = HcBlue, fontSize = 11.sp)
                            }
                        }
                    }
                    Text(
                        "DEVELOPER: EDISON SUCLATAN DAYAGUIT",
                        color = HcGray.copy(alpha = 0.6f),
                        fontSize = 9.sp,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 16.dp)
                    )
                }
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
            PremiumWaveBackground()

            if (messages.isEmpty() && !isTyping) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 90.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(28.dp, Alignment.CenterVertically)
                ) {
                    HomeChip(Icons.Outlined.Search, "Search a job near me", Color(0xFF4C8DFF)) { onNavigate("job_researcher") }
                    HomeChip(Icons.Outlined.Description, "Make a resume", Color(0xFF7C6BFF)) { onNavigate("resume") }
                    HomeChip(Icons.Outlined.Assignment, "Make me a biodata", Color(0xFFC0508F)) { onNavigate("biodata") }
                    HomeChip(Icons.Outlined.Mail, "Make me a letter", Color(0xFFFF4D6D)) { onNavigate("letters") }
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 100.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    itemsIndexed(messages) { _, msg -> HomeChatBubble(msg) }
                    if (isTyping) {
                        item {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                HomeAvatar()
                                Spacer(Modifier.width(10.dp))
                                PremiumThinkingIndicator()
                            }
                        }
                    }
                }
            }

            // Hamburger
            Box(
                modifier = Modifier
                    .padding(start = 16.dp, top = 40.dp)
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.06f))
                    .border(1.dp, Brush.verticalGradient(listOf(HcBlue, Color.Black)), RoundedCornerShape(12.dp))
                    .clickable { scope.launch { drawerState.open() } },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = HcBlue)
            }

            // Input bar
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 14.dp)
                    .shadow(18.dp, RoundedCornerShape(28.dp), ambientColor = HcBlue, spotColor = HcBlue)
                    .clip(RoundedCornerShape(28.dp))
                    .background(HcInputBg)
                    .border(1.2.dp, Brush.horizontalGradient(listOf(HcBlue, HcBlueDeep)), RoundedCornerShape(28.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.AttachFile, contentDescription = null, tint = HcBlue, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Type your message...", color = HcGray, fontSize = 13.sp) },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = { send(inputText) }),
                    maxLines = 5,
                    enabled = !isTyping
                )
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                            if (inputText.isNotBlank() && !isTyping)
                                Brush.verticalGradient(listOf(HcBlue, Color(0xFF1E4FD6)))
                            else
                                Brush.verticalGradient(listOf(Color(0xFF2A2A2A), Color(0xFF2A2A2A)))
                        )
                        .clickable(enabled = inputText.isNotBlank() && !isTyping) { send(inputText) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }

            if (showComingSoon) {
                AlertDialog(
                    onDismissRequest = { showComingSoon = false },
                    title = { Text("Coming Soon pato Luv❤️🩵") },
                    text = { Text("under maintenance pa luv sorry") },
                    confirmButton = {
                        TextButton(onClick = { showComingSoon = false }) { Text("OK") }
                    }
                )
            }
        }
    }
}

@Composable
private fun HomeChip(icon: ImageVector, label: String, accent: Color, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.78f)
            .height(56.dp)
            .shadow(14.dp, RoundedCornerShape(50), ambientColor = accent, spotColor = accent)
            .clip(RoundedCornerShape(50))
            .background(HcInputBg)
            .border(1.5.dp, accent, RoundedCornerShape(50))
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text(label, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun DrawerItem(icon: ImageVector, title: String, subtitle: String? = null, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = HcBlue, modifier = Modifier.size(26.dp))
        Spacer(Modifier.width(16.dp))
        Column {
            Text(title, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            if (subtitle != null) Text(subtitle, color = HcBlue, fontSize = 11.sp)
        }
    }
}

@Composable
private fun HomeAvatar() {
    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .border(1.dp, Brush.verticalGradient(listOf(HcBlue, Color.Black)), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = HcBlue, modifier = Modifier.size(16.dp))
    }
}

@Composable
private fun HomeChatBubble(msg: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!msg.isUser) {
            HomeAvatar()
            Spacer(Modifier.width(10.dp))
        }
        Column(
            modifier = if (msg.isUser) {
                Modifier
                    .widthIn(max = 260.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Brush.verticalGradient(listOf(Color(0xFF1E4FD6), Color(0xFF0D1440))))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            } else {
                Modifier.widthIn(max = 280.dp).padding(vertical = 4.dp)
            }
        ) {
            Text(msg.text, color = Color.White, fontSize = 14.sp)
        }
    }
}
