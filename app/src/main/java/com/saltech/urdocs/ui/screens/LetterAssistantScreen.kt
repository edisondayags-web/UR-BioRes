package com.saltech.urdocs.ui.screens
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.draw.graphicsLayer
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.saltech.urdocs.R
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import com.saltech.urdocs.ui.components.PremiumThinkingIndicator
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
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas as ComposeCanvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.layer.rememberGraphicsLayer
import androidx.compose.ui.graphics.layer.toImageBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.draw.drawWithCache
import android.graphics.Picture

private val UrBlue = Color(0xFF4C8DFF)
private val UrBlueDeep = Color(0xFF16255E)
private val UrBlueDark = Color(0xFF060B18)
private val UrGray = Color(0xFF9A9A9A)
private val UrBubbleDark = Color(0xFF161616)
private val UrUserBubble = Color(0xFF1E4FD6)

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
    var generatedLetter by remember { mutableStateOf<String?>(null) }
    var awaitingLanguage by remember { mutableStateOf(false) }

    fun addMessage(text: String, isUser: Boolean) {
        messages = messages + ChatMessage(text = text, isUser = isUser)
    }

    fun sendToGemini(userText: String?) {
        scope.launch {
            isTyping = true
            if (userText != null) {
                history = history + ("user" to userText)
            }
            val reply = repository.chat(history, letterType)
            history = history + ("model" to reply)
            isTyping = false

            val startMarker = "###LETTER_START###"
            val endMarker = "###LETTER_END###"
            if (reply.trim().contains("###ASK_LANGUAGE###")) {
                awaitingLanguage = true
            } else if (reply.contains(startMarker) && reply.contains(endMarker)) {
                val letterText = reply.substringAfter(startMarker).substringBefore(endMarker).trim()
                val advice = reply.substringAfter(endMarker).trim()
                generatedLetter = letterText
                addMessage(if (advice.isNotBlank()) advice else "Handa na ang iyong letter! Tignan sa itaas.", isUser = false)
            } else {
                addMessage(reply, isUser = false)
            }
        }
    }

    fun pickLanguage(lang: String) {
        awaitingLanguage = false
        addMessage("Sa wikang $lang po.", isUser = true)
        history = history + ("user" to "Sa wikang $lang po.")
        sendToGemini(null)
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
        val lastIndex = messages.size - 1 + if (isTyping) 1 else 0
        if (lastIndex >= 0) {
            listState.animateScrollToItem(lastIndex)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // ---------- Full-bleed pulsing gradient background, Gemini-style: dark on top, blue glow at bottom ----------
        AnimatedChatBackground(isTyping)

        // ---------- Greeting header, only shows before the first reply arrives ----------
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

        // ---------- Chat list, grows from the top like a normal conversation, auto-scrolls as it overflows ----------
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
                    ChatBubble(msg)
                }
            }
            if (isTyping) {
                item { ThinkingBubble() }
            }
            if (awaitingLanguage && !isTyping) {
                item {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        AssistantAvatar()
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("Anong wika gusto mo para sa letter mo?", color = Color.White, fontSize = 14.sp)
                            Spacer(Modifier.height(10.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("Tagalog", "English").forEach { lang ->
                                    LanguageChip(lang) { pickLanguage(lang) }
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("Taglish", "Bisaya").forEach { lang ->
                                    LanguageChip(lang) { pickLanguage(lang) }
                                }
                            }
                        }
                    }
                }
            }
        }

        // ---------- Back button floats on top, rendered LAST so it always receives taps ----------
        Box(
            modifier = Modifier
                .padding(16.dp)
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.06f))
                .border(1.dp, Brush.verticalGradient(listOf(UrBlue, Color.Black)), RoundedCornerShape(12.dp))
                .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = UrBlue)
        }

        // ---------- Floating input box ----------
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp)
                .shadow(18.dp, RoundedCornerShape(28.dp), ambientColor = UrBlue, spotColor = UrBlue)
                .clip(RoundedCornerShape(28.dp))
                .background(Color(0xFF0C0F16))
                .border(BorderStroke(1.2.dp, Brush.horizontalGradient(listOf(UrBlue, UrBlueDeep))), RoundedCornerShape(28.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.AttachFile, contentDescription = null, tint = UrBlue, modifier = Modifier.size(20.dp))
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
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                maxLines = 5,
                enabled = !isTyping
            )
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(
                        if (inputText.isNotBlank() && !isTyping)
                            Brush.verticalGradient(listOf(UrBlue, Color.Black))
                        else
                            Brush.verticalGradient(listOf(Color(0xFF2A2A2A), Color(0xFF2A2A2A)))
                    )
                    .clickable(enabled = inputText.isNotBlank() && !isTyping) { handleUserInput(inputText) },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }

        generatedLetter?.let { letter ->
            LetterPaperPreview(letterText = letter, onDismiss = { generatedLetter = null })
        }
    }
}

@Composable
private fun ThinkingIndicator() {
    PremiumThinkingIndicator()
}

@Composable
private fun WavingText(text: String, color: Color) {
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
private fun TypingDots() {
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
                    .background(UrBlue)
            )
        }
    }
}

@Composable
private fun AssistantAvatar() {
    Box(
        modifier = Modifier.size(34.dp).clip(CircleShape)
            .border(1.dp, Brush.verticalGradient(listOf(UrBlue, Color.Black)), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = UrBlue, modifier = Modifier.size(16.dp))
    }
}

@Composable
private fun TypewriterText(fullText: String, color: Color, fontSize: androidx.compose.ui.unit.TextUnit) {
    var shownChars by remember(fullText) { mutableStateOf(0) }
    LaunchedEffect(fullText) {
        shownChars = 0
        while (shownChars < fullText.length) {
            shownChars += 3
            delay(12)
        }
        shownChars = fullText.length
    }
    Text(fullText.take(shownChars), color = color, fontSize = fontSize)
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
            modifier = if (msg.isUser) {
                Modifier.widthIn(max = 260.dp).clip(RoundedCornerShape(18.dp))
                    .background(Brush.verticalGradient(listOf(UrUserBubble, Color(0xFF0D1440))))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            } else {
                Modifier.widthIn(max = 280.dp).padding(vertical = 4.dp)
            }
        ) {
            if (msg.isUser) {
                Text(msg.text, color = Color.White, fontSize = 14.sp)
            } else {
                Text(msg.text, color = Color.White, fontSize = 14.sp)
            }
            Spacer(Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(msg.time, color = if (msg.isUser) Color(0xFFB9CBFF).copy(alpha = 0.7f) else UrGray, fontSize = 10.sp)
            }
        }
    }
}

@Composable
private fun ThinkingBubble() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        AssistantAvatar()
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier) {
            ThinkingIndicator()
        }
    }
}

@Composable
private fun AnimatedChatBackground(isTyping: Boolean) {
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
                        UrBlueDark,
                        UrBlueDeep.copy(alpha = intensity),
                        UrBlue.copy(alpha = intensity * 0.9f)
                    )
                )
            )
    )
}

@Composable
private fun LetterPaperPreview(letterText: String, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var saving by remember { mutableStateOf(false) }
    val picture = remember { android.graphics.Picture() }
    val paperWidthDp = 850.dp
    val paperHeightDp = 1250.dp
    var offset by remember { mutableStateOf(Offset.Zero) }

    var interstitialAd by remember { mutableStateOf<InterstitialAd?>(null) }
    LaunchedEffect(Unit) {
        InterstitialAd.load(
            context,
            "ca-app-pub-3134240485602899/5274307709",
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }
            }
        )
    }

    suspend fun saveToGallery() {
        val bitmap = Bitmap.createBitmap(picture.width.coerceAtLeast(1), picture.height.coerceAtLeast(1), Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        canvas.drawColor(android.graphics.Color.WHITE)
        picture.draw(canvas)
        val filename = "UR_Letter_${System.currentTimeMillis()}.png"
        val resolver = context.contentResolver
        val values = android.content.ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/URDocs")
        }
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        uri?.let {
            resolver.openOutputStream(it)?.use { out -> bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) }
            android.widget.Toast.makeText(context, "see your gellery luv🩵", android.widget.Toast.LENGTH_LONG).show()
        } ?: run {
            android.widget.Toast.makeText(context, "Hindi na-download, subukan ulit.", android.widget.Toast.LENGTH_LONG).show()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.9f)),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.fillMaxWidth(0.9f).fillMaxHeight(0.85f)) {
            AndroidView(
                factory = { ctx ->
                    AdView(ctx).apply {
                        val displayMetrics = ctx.resources.displayMetrics
                        val adWidthPixels = displayMetrics.widthPixels.toFloat()
                        val density = displayMetrics.density
                        val adWidth = (adWidthPixels / density).toInt()
                        setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(ctx, adWidth))
                        adUnitId = "ca-app-pub-3134240485602899/5923255956"
                        loadAd(AdRequest.Builder().build())
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            BoxWithConstraints(
                modifier = Modifier.weight(1f).background(Color.Transparent)
            ) {
                val fitScale = minOf(maxWidth / paperWidthDp, maxHeight / paperHeightDp)
                var scale by remember { mutableStateOf(fitScale) }

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                scale = (scale * zoom).coerceIn(fitScale, 4f)
                                offset = if (scale <= fitScale) Offset.Zero else offset + pan
                            }
                        }
                        .graphicsLayer(
                            scaleX = scale, scaleY = scale,
                            translationX = offset.x, translationY = offset.y
                        )
                        .requiredWidth(paperWidthDp)
                        .requiredHeight(paperHeightDp)

                        .drawWithContent {
                            val pictureCanvas = androidx.compose.ui.graphics.Canvas(picture.beginRecording(size.width.toInt().coerceAtLeast(1), size.height.toInt().coerceAtLeast(1)))
                            draw(this, layoutDirection, pictureCanvas, size) {
                                this@drawWithContent.drawContent()
                            }
                            picture.endRecording()
                            drawContent()
                        }
                        
                        .background(Color.White)
                        .padding(48.dp)
                ) {
                    SelectionContainer {
                        Text(letterText, color = Color.Black, fontSize = 16.sp, lineHeight = 24.sp)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(Color(0xFF2A2A2A))
                        .clickable { onDismiss() }.padding(horizontal = 20.dp, vertical = 12.dp)
                ) { Text("Isara", color = Color.White) }
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(UrBlue)
                        .clickable(enabled = !saving) {
                            saving = true
                            val activity = context as? android.app.Activity
                            if (activity != null && interstitialAd != null) {
                                interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {
                                        interstitialAd = null
                                        scope.launch {
                                            saveToGallery()
                                            saving = false
                                        }
                                    }
                                }
                                interstitialAd?.show(activity)
                            } else {
                                scope.launch {
                                    saveToGallery()
                                    saving = false
                                }
                            }
                        }.padding(horizontal = 20.dp, vertical = 12.dp)
                ) { Text(if (saving) "Sinesave..." else "I-Download", color = Color.White, fontWeight = FontWeight.Bold) }
            }
        }
    }
}
@Composable
private fun LanguageChip(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF0C0F16))
            .border(1.dp, Brush.horizontalGradient(listOf(UrBlue, UrBlueDeep)), RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 8.dp)
    ) {
        Text(label, color = Color.White, fontSize = 13.sp)
    }
}
