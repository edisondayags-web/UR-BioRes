package com.saltech.urdocs.ui.screens

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saltech.urdocs.ui.theme.UrGray
import com.saltech.urdocs.ui.theme.UrPink
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

private data class QA(val question: String, val answer: String)

// Local (BPO) track - researched: mix of universal HR questions +
// BPO-specific ones (night shift, CSAT/FCR/QA familiarity), each with a sample answer.
private val LOCAL_QA = listOf(
    QA("Tell me something about yourself.",
        "I'm hardworking, a fast learner, and I always give my best in every task given to me."),
    QA("Why do you want to work in the BPO industry?",
        "I enjoy helping people and solving problems, and I like the fast-paced, dynamic environment of a call center."),
    QA("What are your strengths and weaknesses?",
        "My strength is staying patient under pressure. My weakness is I used to overthink, but I've learned to focus on taking action instead."),
    QA("Are you willing to work night shifts or a graveyard schedule?",
        "Yes, I'm flexible and willing to work any shift, including nights, to meet the needs of the business."),
    QA("How do you handle an angry or difficult customer?",
        "I stay calm, listen carefully, and focus on finding a solution instead of taking it personally."),
    QA("What do you know about CSAT, FCR, and QA in a call center setting?",
        "CSAT measures customer satisfaction, FCR means resolving an issue on the first call, and QA checks call quality against company standards."),
    QA("Tell me about a time you performed well under pressure.",
        "During a high-volume shift, I stayed organized and prioritized urgent tasks, which helped me meet all my targets."),
    QA("Where do you see yourself five years from now?",
        "I see myself growing within the company, taking on more responsibilities, and becoming a team lead or specialist."),
    QA("Do you have any questions for us?",
        "Yes — what does success look like in this role during the first three months?")
)

// International/corporate track - researched: standard global HR/behavioral set.
private val INTL_QA = listOf(
    QA("Tell me about yourself.",
        "I'm focused, motivated, and always eager to learn. I love contributing to meaningful work and growing through new challenges."),
    QA("What do you know about our company, and why do you want to work here?",
        "I've researched your company's mission and values, and I believe my skills align well with what you're building."),
    QA("What is your greatest strength, and what is your greatest weakness?",
        "My greatest strength is adaptability. My weakness is I used to overthink, but now I focus on taking action and trusting my preparation."),
    QA("Tell me about a time you failed or made a mistake. How did you handle it?",
        "I once missed a deadline early in my career. I learned from it by improving how I plan and communicate timelines."),
    QA("What motivates you in your professional life?",
        "I'm motivated by solving problems and seeing the impact of my work on the team's success."),
    QA("Where do you see yourself in five years?",
        "I see myself growing, continuing to learn, and leading exciting new projects that make an impact."),
    QA("What are your salary expectations?",
        "I'm looking for a fair offer based on the role and my experience, and I'm open to discussing details."),
    QA("Do you have any questions for us?",
        "Yes — what does a typical day look like for someone in this role?")
)

@Composable
fun InterviewSessionScreen(
    mode: String,
    onBack: () -> Unit
) {
    val isAsync = mode.endsWith("_async")

    // ===== Async / Video mode not built yet - show placeholder =====
    if (isAsync) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
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

    // ===== Traditional mode: continuous auto-scroll, drag to scroll/pause =====
    val qaList = remember(mode) { if (mode.startsWith("local")) LOCAL_QA else INTL_QA }
    val scrollState = rememberScrollState()
    var isPaused by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    // TTS setup - engine only here. Actual speak() calls happen per-question below,
    // one at a time, only after the PREVIOUS question+answer has fully scrolled off screen.
    val context = LocalContext.current
    val tts = remember { mutableStateOf<TextToSpeech?>(null) }
    var ttsReady by remember { mutableStateOf(false) }
    val spokenIndices = remember { mutableStateListOf<Int>() }
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp

    DisposableEffect(Unit) {
        val t = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Content is all in English, so use an English voice throughout -
                // a Filipino voice was spelling out small English words like "us" letter by letter.
                tts.value?.language = Locale.US

                // 1.10x - readable pace, not too fast (1.35 felt rushed), not default-slow either.
                tts.value?.setSpeechRate(1.10f)

                // Name-based "male" voice matching is unreliable across devices/engines,
                // so always apply a lower pitch instead - consistently reads as a deeper,
                // more male-sounding voice regardless of which underlying voice is picked.
                tts.value?.setPitch(0.62f)

                ttsReady = true
            }
        }
        tts.value = t
        onDispose { t.stop(); t.shutdown() }
    }

    // Trigger line: middle of the screen. Questions 2+ speak the instant they reach here.
    val midLinePx = with(density) { (screenHeightDp / 2).toPx() }
    // The very first question speaks as soon as it enters the screen at all (bottom edge) -
    // giving it a head start so it doesn't get cut off/overlapped by the next question later.
    val screenHeightPx = with(density) { screenHeightDp.toPx() }

    fun speakIfDue(index: Int) {
        if (index !in spokenIndices) {
            spokenIndices.add(index)
            val params = Bundle().apply {
                putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f)
            }
            tts.value?.speak(qaList[index].question, TextToSpeech.QUEUE_FLUSH, params, null)
        }
    }

    // The first question speaks reliably as soon as the TTS engine is ready - it's essentially
    // on-screen from the start (short spacer), so it no longer depends on catching the right
    // scroll-position frame, which was sometimes missed.
    LaunchedEffect(ttsReady) {
        if (ttsReady) {
            speakIfDue(0)
        }
    }

    LaunchedEffect(scrollState.maxValue) {
        while (scrollState.value < scrollState.maxValue) {
            if (!isPaused) {
                scrollState.scrollBy(8.5f)
            }
            delay(16)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { isPaused = true },
                    onDragEnd = { isPaused = false },
                    onDragCancel = { isPaused = false }
                ) { change, dragAmount ->
                    coroutineScope.launch {
                        scrollState.scrollBy(-dragAmount.y)
                    }
                }
            }
    ) {
        // Reserved top space - banner ad goes here later (ads pass)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 90.dp)
                .verticalScroll(scrollState, enabled = false)
                .padding(horizontal = 32.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            // Widened from 140.dp - previous gap was too tight, next question was arriving
            // before the user finished reading the current answer.
            verticalArrangement = Arrangement.spacedBy(260.dp)
        ) {
            // Starts the list a bit below the screen (not a full screen-height away) so the
            // first text appears sooner - a full screen of blank black felt like a frozen app.
            Spacer(Modifier.height(290.dp))

            qaList.forEachIndexed { index, qa ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.onGloballyPositioned { coords ->
                        val top = coords.boundsInWindow().top
                        val bottom = coords.boundsInWindow().bottom
                        if (ttsReady) {
                            val due = if (index == 0) {
                                top <= screenHeightPx
                            } else {
                                top <= midLinePx && bottom >= midLinePx
                            }
                            if (due) speakIfDue(index)
                        }
                    }
                ) {
                    Text(
                        text = qa.question,
                        color = Color(0xFFE0245E),
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = qa.answer,
                        color = Color(0xFF2A5CE0),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp
                    )
                }
            }
            // Matches the top spacer - gives enough scroll room to carry the LAST
            // question fully off the top of the screen instead of stopping while it's still visible.
            Spacer(Modifier.height(screenHeightDp))
        }

        // Fade masks top/bottom so text appears/disappears smoothly
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(90.dp)
                .background(Brush.verticalGradient(listOf(Color.Black, Color.Transparent)))
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(70.dp)
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black)))
        )

        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart).padding(top = 24.dp, start = 8.dp)
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = UrPink)
        }
    }
}
