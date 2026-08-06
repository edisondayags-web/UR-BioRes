package com.saltech.urdocs.ui.screens

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saltech.urdocs.ui.theme.UrGray
import com.saltech.urdocs.ui.theme.UrPink
import kotlinx.coroutines.delay
import java.util.Locale

private data class QA(val question: String, val answer: String)

private val LOCAL_QA = listOf(
    QA("Tell me something about yourself.", "I'm hardworking, a fast learner, and I always give my best in every task given to me."),
    QA("Why do you want to work in the BPO industry?", "I enjoy helping people and solving problems, and I like the fast-paced, dynamic environment of a call center."),
    QA("What are your strengths and weaknesses?", "My strength is staying patient under pressure. My weakness is I used to overthink, but I've learned to focus on taking action instead."),
    QA("Are you willing to work night shifts or a graveyard schedule?", "Yes, I'm flexible and willing to work any shift, including nights, to meet the needs of the business."),
    QA("How do you handle an angry or difficult customer?", "I stay calm, listen carefully, and focus on finding a solution instead of taking it personally."),
    QA("What do you know about CSAT, FCR, and QA in a call center setting?", "CSAT measures customer satisfaction, FCR means resolving an issue on the first call, and QA checks call quality against company standards."),
    QA("Tell me about a time you performed well under pressure.", "During a high-volume shift, I stayed organized and prioritized urgent tasks, which helped me meet all my targets."),
    QA("Where do you see yourself five years from now?", "I see myself growing within the company, taking on more responsibilities, and becoming a team lead or specialist."),
    QA("Do you have any questions for us?", "Yes — what does success look like in this role during the first three months?")
)

private val INTL_QA = listOf(
    QA("Tell me about yourself.", "I'm focused, motivated, and always eager to learn. I love contributing to meaningful work and growing through new challenges."),
    QA("What do you know about our company, and why do you want to work here?", "I've researched your company's mission and values, and I believe my skills align well with what you're building."),
    QA("What is your greatest strength, and what is your greatest weakness?", "My greatest strength is adaptability. My weakness is I used to overthink, but now I focus on taking action and trusting my preparation."),
    QA("Tell me about a time you failed or made a mistake. How did you handle it?", "I once missed a deadline early in my career. I learned from it by improving how I plan and communicate timelines."),
    QA("What motivates you in your professional life?", "I'm motivated by solving problems and seeing the impact of my work on the team's success."),
    QA("Where do you see yourself in five years?", "I see myself growing, continuing to learn, and leading exciting new projects that make an impact."),
    QA("What are your salary expectations?", "I'm looking for a fair offer based on the role and my experience, and I'm open to discussing details."),
    QA("Do you have any questions for us?", "Yes — what does a typical day look like for someone in this role?")
)

@Composable
fun InterviewSessionScreen(
    mode: String,
    onBack: () -> Unit
) {
    val isAsync = mode.endsWith("_async")

    // ===== Async / Video mode =====
    if (isAsync) {
        val asyncQaList = remember(mode) { if (mode.startsWith("local")) LOCAL_QA else INTL_QA }
        var qIndex by remember { mutableIntStateOf(0) }
        var phase by remember { mutableStateOf("prep") }
        var secondsLeft by remember { mutableIntStateOf(15) }
        var asyncStarted by remember { mutableStateOf(false) }

        LaunchedEffect(qIndex, phase, asyncStarted) {
            if (!asyncStarted) return@LaunchedEffect
            if (phase == "prep" || phase == "recording") {
                val total = if (phase == "prep") 15 else 90
                secondsLeft = total
                while (secondsLeft > 0) {
                    delay(1000)
                    secondsLeft -= 1
                }
                phase = if (phase == "prep") "recording" else "review"
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(8.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }

            if (phase == "done") {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Tapos na luv 💙", color = UrPink, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("Pindutin ang back para lumabas", color = UrGray, fontSize = 14.sp)
                }
                return@Box
            }

            if (!asyncStarted) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { asyncStarted = true },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Go",
                            style = TextStyle(
                                brush = Brush.horizontalGradient(listOf(Color(0xFF2A5CE0), Color(0xFFE0245E))),
                                fontWeight = FontWeight.Bold,
                                fontSize = 64.sp
                            )
                        )
                        Spacer(Modifier.height(20.dp))
                        Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = Color(0xFFE0245E), modifier = Modifier.size(40.dp))
                        Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = Color(0xFF7B3FE4), modifier = Modifier.size(40.dp).offset(y = (-14).dp))
                        Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = Color(0xFF2A5CE0), modifier = Modifier.size(40.dp).offset(y = (-28).dp))
                    }
                }
                return@Box
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp)
                    .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
                        if (phase == "recording") phase = "review"
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Question ${qIndex + 1} / ${asyncQaList.size}",
                    color = UrGray,
                    fontSize = 13.sp
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = asyncQaList[qIndex].question,
                    color = Color(0xFFE0245E),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(28.dp))

                when (phase) {
                    "prep" -> {
                        Text("Prepare your answer...", color = UrGray, fontSize = 15.sp)
                        Spacer(Modifier.height(10.dp))
                        Text("$secondsLeft", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 40.sp)
                    }
                    "recording" -> {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color(0xFFE0245E))
                        )
                        Spacer(Modifier.height(10.dp))
                        Text("Recording... ${secondsLeft}s", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 28.sp)
                        Spacer(Modifier.height(6.dp))
                        Text("(i-tap ang screen kung tapos ka na)", color = UrGray, fontSize = 12.sp)
                    }
                    "review" -> {
                        Text("Sample Answer:", color = UrGray, fontSize = 13.sp)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = asyncQaList[qIndex].answer,
                            color = Color(0xFF2A5CE0),
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 26.sp
                        )
                        Spacer(Modifier.height(24.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF1D3FB5))
                                .clickable {
                                    if (qIndex < asyncQaList.lastIndex) {
                                        qIndex += 1
                                        phase = "prep"
                                    } else {
                                        phase = "done"
                                    }
                                }
                                .padding(horizontal = 28.dp, vertical = 14.dp)
                        ) {
                            Text(
                                if (qIndex < asyncQaList.lastIndex) "Susunod" else "Tapos na",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
        return
    }

    // ===== Traditional Teleprompter Mode =====
    val qaList = remember(mode) { if (mode.startsWith("local")) LOCAL_QA else INTL_QA }
    val scrollState = rememberScrollState()
    var isPaused by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val density = LocalDensity.current
    var ttsInstance by remember { mutableStateOf<TextToSpeech?>(null) }
    var ttsReady by remember { mutableStateOf(false) }
    val spokenIndices = remember { mutableSetOf<Int>() }
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp

    DisposableEffect(Unit) {
        var textToSpeech: TextToSpeech? = null
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale.US
                textToSpeech?.setSpeechRate(1.10f)
                textToSpeech?.setPitch(1.0f)
            }
            ttsReady = true
        }
        ttsInstance = textToSpeech

        onDispose {
            textToSpeech?.stop()
            textToSpeech?.shutdown()
        }
    }

    val screenHeightPx = with(density) { screenHeightDp.toPx() }
    // BAGONG TRIGGER POINT: 85% ng screen height (nasa ibabang bahagi pa lang ng screen ay babasahin na agad, hindi na maghihintay sa gitna)
    val earlyTriggerPx = screenHeightPx * 0.85f

    fun speakIfDue(index: Int) {
        if (index !in spokenIndices) {
            spokenIndices.add(index)
            val params = Bundle().apply {
                putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f)
            }
            
            if (index == 0) {
                // UNANG TANONG: QUEUE_FLUSH para linisin ang lumang sound at magsalita agad
                ttsInstance?.playSilentUtterance(200L, TextToSpeech.QUEUE_FLUSH, "silence_0")
                ttsInstance?.speak(qaList[0].question, TextToSpeech.QUEUE_ADD, params, "q_0")
            } else {
                // SUSUNOD NA MGA TANONG: QUEUE_ADD para hindi ma-cut/ma-abort ang kasalukuyang binabasang tanong
                ttsInstance?.speak(qaList[index].question, TextToSpeech.QUEUE_ADD, params, "q_$index")
            }
        }
    }

    var countdownDone by remember { mutableStateOf(false) }
    var startRequested by remember { mutableStateOf(false) }

    // INAYOS ANG "GO" BUTTON: Sa unang tap pa lang, kapag ready na ang TTS ay agad babasahin ang Unang Tanong (#1)
    LaunchedEffect(startRequested, ttsReady) {
        if (startRequested && ttsReady && !countdownDone) {
            countdownDone = true
            speakIfDue(0)
        }
    }

    LaunchedEffect(scrollState.maxValue, countdownDone) {
        if (!countdownDone) return@LaunchedEffect
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
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { isPaused = true },
                    onDragEnd = { isPaused = false },
                    onDragCancel = { isPaused = false }
                ) { change, dragAmount ->
                    change.consume()
                    scrollState.dispatchRawDelta(-dragAmount.y)
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 90.dp)
                .verticalScroll(scrollState, enabled = false)
                .padding(horizontal = 32.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(260.dp)
        ) {
            Spacer(Modifier.height(290.dp))

            qaList.forEachIndexed { index, qa ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.onGloballyPositioned { coords ->
                        val top = coords.boundsInWindow().top
                        if (ttsReady && countdownDone) {
                            val isPlaced = top > 0f
                            val due = if (index == 0) {
                                isPlaced && top <= screenHeightPx
                            } else {
                                // Babasahin na habang nasa ibaba pa lang ng screen (malayo pa sa gitna)
                                isPlaced && top <= earlyTriggerPx
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
            Spacer(Modifier.height(screenHeightDp))
        }

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
            modifier = Modifier
                .statusBarsPadding()
                .padding(8.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
        }

        if (!countdownDone) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        enabled = true,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        // Isang tap na lang dito sa Go button!
                        startRequested = true
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Go",
                        style = TextStyle(
                            brush = Brush.horizontalGradient(listOf(Color(0xFF2A5CE0), Color(0xFFE0245E))),
                            fontWeight = FontWeight.Bold,
                            fontSize = 64.sp
                        )
                    )
                    Spacer(Modifier.height(20.dp))
                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = Color(0xFFE0245E), modifier = Modifier.size(40.dp))
                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = Color(0xFF7B3FE4), modifier = Modifier.size(40.dp).offset(y = (-14).dp))
                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = Color(0xFF2A5CE0), modifier = Modifier.size(40.dp).offset(y = (-28).dp))
                }
            }
        }
    }
}
