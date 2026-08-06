package com.saltech.urdocs.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.saltech.urdocs.ui.theme.UrGray
import com.saltech.urdocs.ui.theme.UrPink
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

private data class TranscriptEntry(val isAi: Boolean, val text: String)

private enum class SessionState { SPEAKING, LISTENING, PROCESSING, DONE }

// Local (BPO) track questions - researched: mix of universal HR questions +
// BPO-specific ones (night shift, CSAT/FCR/QA familiarity).
private val LOCAL_QUESTIONS = listOf(
    "Tell me something about yourself.",
    "Why do you want to work in the BPO industry?",
    "What are your strengths and weaknesses?",
    "Are you willing to work night shifts or a graveyard schedule?",
    "How do you handle an angry or difficult customer?",
    "What do you know about CSAT, FCR, and QA in a call center setting?",
    "Tell me about a time you performed well under pressure.",
    "Where do you see yourself five years from now?",
    "Do you have any questions for us?"
)

// International/corporate track - researched: standard global HR/behavioral set.
private val INTL_QUESTIONS = listOf(
    "Tell me about yourself.",
    "What do you know about our company, and why do you want to work here?",
    "What is your greatest strength, and what is your greatest weakness?",
    "Tell me about a time you failed or made a mistake. How did you handle it?",
    "What motivates you in your professional life?",
    "Where do you see yourself in five years?",
    "What are your salary expectations?",
    "Do you have any questions for us?"
)

@Composable
fun InterviewSessionScreen(
    mode: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val isAsync = mode.endsWith("_async")
    val questions = remember(mode) {
        if (mode.startsWith("local")) LOCAL_QUESTIONS else INTL_QUESTIONS
    }

    // ===== Async / Video mode not built yet - show placeholder =====
    if (isAsync) {
        Box(modifier = Modifier.fillMaxSize()) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.TopStart).padding(top = 24.dp, start = 8.dp)
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = UrPink)
            }
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("ASYNC VIDEO", color = UrPink, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(Modifier.height(8.dp))
                Text("Coming soon luv 💙", color = UrGray, fontSize = 14.sp)
            }
        }
        return
    }

    // ===== Traditional (live TTS + STT) mode =====
    var hasAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasAudioPermission = granted }

    LaunchedEffect(Unit) {
        if (!hasAudioPermission) permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    if (!hasAudioPermission) {
        Box(modifier = Modifier.fillMaxSize()) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.TopStart).padding(top = 24.dp, start = 8.dp)
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = UrPink)
            }
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Kailangan namin ng mic access para makinig sa sagot mo luv🩵",
                    color = Color.White, fontSize = 15.sp
                )
                Spacer(Modifier.height(12.dp))
                Button(onClick = { permissionLauncher.launch(Manifest.permission.RECORD_AUDIO) }) {
                    Text("Payagan ang Mic")
                }
            }
        }
        return
    }

    var currentIndex by remember { mutableStateOf(0) }
    var sessionState by remember { mutableStateOf(SessionState.SPEAKING) }
    val transcript = remember { mutableStateListOf<TranscriptEntry>() }
    val listState = rememberLazyListState()

    // ===== TTS setup =====
    val tts = remember {
        arrayOfNulls<TextToSpeech>(1)
    }
    var ttsReady by remember { mutableStateOf(false) }
    DisposableEffect(Unit) {
        val engine = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts[0]?.language = Locale.US
                ttsReady = true
            }
        }
        tts[0] = engine
        onDispose {
            tts[0]?.stop()
            tts[0]?.shutdown()
        }
    }

    // ===== SpeechRecognizer setup =====
    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }
    DisposableEffect(Unit) {
        onDispose { speechRecognizer.destroy() }
    }

    fun startListening() {
        sessionState = SessionState.LISTENING
        val intent = android.content.Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
        }
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                val text = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull()
                    ?: "(hindi na-gets ng mic, subukan ulit)"
                transcript.add(TranscriptEntry(isAi = false, text = text))
                sessionState = SessionState.PROCESSING
                coroutineScope.launch {
                    delay(600)
                    if (currentIndex < questions.lastIndex) {
                        currentIndex += 1
                        val nextQ = questions[currentIndex]
                        transcript.add(TranscriptEntry(isAi = true, text = nextQ))
                        sessionState = SessionState.SPEAKING
                        tts[0]?.speak(nextQ, TextToSpeech.QUEUE_FLUSH, null, "q$currentIndex")
                    } else {
                        val closing = "Salamat sa pag-practice luv! Tapos na ang mock interview na ito."
                        transcript.add(TranscriptEntry(isAi = true, text = closing))
                        sessionState = SessionState.DONE
                        tts[0]?.speak(closing, TextToSpeech.QUEUE_FLUSH, null, "done")
                    }
                }
            }
            override fun onError(error: Int) {
                sessionState = SessionState.PROCESSING
                transcript.add(TranscriptEntry(isAi = false, text = "(walang na-record, i-tap ulit ang mic)"))
                sessionState = SessionState.SPEAKING
            }
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
        speechRecognizer.startListening(intent)
    }

    // Ask the first question once TTS is ready
    LaunchedEffect(ttsReady) {
        if (ttsReady && transcript.isEmpty()) {
            val firstQ = questions[0]
            transcript.add(TranscriptEntry(isAi = true, text = firstQ))
            tts[0]?.speak(firstQ, TextToSpeech.QUEUE_FLUSH, null, "q0")
        }
    }

    LaunchedEffect(transcript.size) {
        if (transcript.isNotEmpty()) listState.animateScrollToItem(transcript.size - 1)
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        IconButton(onClick = onBack, modifier = Modifier.padding(top = 8.dp, start = 8.dp)) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = UrPink)
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(transcript) { entry -> TranscriptBubble(entry) }
            item { Spacer(Modifier.height(80.dp)) }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            when (sessionState) {
                SessionState.SPEAKING -> Text("AI is speaking...", color = UrGray, fontSize = 13.sp)
                SessionState.PROCESSING -> Text("Processing...", color = UrGray, fontSize = 13.sp)
                SessionState.DONE -> Text("Tapos na — Back para lumabas", color = UrGray, fontSize = 13.sp)
                SessionState.LISTENING -> Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0245E).copy(alpha = 0.85f))
                        .clickable { speechRecognizer.stopListening() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Mic, contentDescription = "Listening", tint = Color.White)
                }
            }
        }

        // Manual mic trigger once AI finished speaking (simple: allow tap anytime except while listening/done)
        if (sessionState == SessionState.SPEAKING) {
            LaunchedEffect(currentIndex) {
                delay(2500) // rough wait for TTS to likely finish; refined later with UtteranceProgressListener
                if (sessionState == SessionState.SPEAKING) startListening()
            }
        }
    }
}

@Composable
private fun TranscriptBubble(entry: TranscriptEntry) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (entry.isAi) Arrangement.Start else Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(if (entry.isAi) Color(0xFF1D3FB5).copy(alpha = 0.35f) else Color(0xFF2A2A2A))
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .widthIn(max = 280.dp)
        ) {
            Text(entry.text, color = Color.White, fontSize = 15.sp)
        }
    }
}
