package com.saltech.urdocs.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.lerp
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
import androidx.core.content.ContextCompat
import com.saltech.urdocs.ui.theme.UrGray
import com.saltech.urdocs.ui.theme.UrPink
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
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

// ===== Subtle Ambient Animated Background Composable =====
@Composable
fun AnimatedBackground(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "bg_anim")

    val floatX1 by infiniteTransition.animateFloat(
        initialValue = -30f,
        targetValue = 40f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 7000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatX1"
    )

    val floatY1 by infiniteTransition.animateFloat(
        initialValue = -20f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 9000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatY1"
    )

    val floatX2 by infiniteTransition.animateFloat(
        initialValue = 30f,
        targetValue = -40f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatX2"
    )

    val alphaGlow by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.60f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alphaGlow"
    )

    Canvas(modifier = modifier.fillMaxSize().background(Color.Black)) {
        val width = size.width
        val height = size.height

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF1E52D6).copy(alpha = alphaGlow),
                    Color(0xFF0D256B).copy(alpha = alphaGlow * 0.4f),
                    Color.Transparent
                ),
                center = Offset(width * 0.1f + floatX1, height * 0.15f + floatY1),
                radius = width * 0.85f
            )
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFE0245E).copy(alpha = alphaGlow * 0.85f),
                    Color(0xFF7A0C2E).copy(alpha = alphaGlow * 0.35f),
                    Color.Transparent
                ),
                center = Offset(width * 0.9f + floatX2, height * 0.85f - floatY1),
                radius = width * 0.9f
            )
        )
    }
}

// ===== Waveform: live during recording, idle dots otherwise =====
@Composable
private fun WaveformDisplay(amplitudes: List<Float>, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxWidth().height(36.dp)) {
        val barWidth = 4.dp.toPx()
        val gap = 3.dp.toPx()
        val midY = size.height / 2
        val maxBars = (size.width / (barWidth + gap)).toInt().coerceAtLeast(1)
        val data = if (amplitudes.isEmpty()) {
            List(maxBars) { 0.08f }
        } else if (amplitudes.size > maxBars) {
            amplitudes.takeLast(maxBars)
        } else {
            amplitudes
        }
        data.forEachIndexed { i, amp ->
            val barHeight = (amp * size.height).coerceAtLeast(4f)
            val x = i * (barWidth + gap)
            val colorT = i.toFloat() / data.size.coerceAtLeast(1)
            val color = lerp(Color(0xFF2A5CE0), Color(0xFFE0245E), colorT)
            drawLine(
                color = color,
                start = Offset(x, midY - barHeight / 2),
                end = Offset(x, midY + barHeight / 2),
                strokeWidth = barWidth,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun InterviewSessionScreen(
    mode: String,
    onBack: () -> Unit
) {
    val isAsync = mode.endsWith("_async")

    // ===== Async / Video mode =====
    if (isAsync) {
        val asyncQaList = remember(mode) { if (mode.startsWith("local")) LOCAL_QA else INTL_QA }
        var qIndex by remember { mutableStateOf(0) }
        var phase by remember { mutableStateOf("prep") }
        var secondsLeft by remember { mutableStateOf(15) }
        var asyncStarted by remember { mutableStateOf(false) }

        val context = LocalContext.current

        // ----- TTS: reads the red question text aloud, once per question -----
        val tts = remember { mutableStateOf<TextToSpeech?>(null) }
        var ttsReady by remember { mutableStateOf(false) }
        var lastSpokenQIndex by remember { mutableStateOf(-1) }

        DisposableEffect(Unit) {
            val t = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    tts.value?.language = Locale.US
                    tts.value?.setSpeechRate(1.1f)
                    ttsReady = true
                }
            }
            tts.value = t
            onDispose { t.stop(); t.shutdown() }
        }

        LaunchedEffect(qIndex, phase, ttsReady, asyncStarted) {
            if (asyncStarted && ttsReady && phase == "prep" && lastSpokenQIndex != qIndex) {
                lastSpokenQIndex = qIndex
                tts.value?.speak(asyncQaList[qIndex].question, TextToSpeech.QUEUE_FLUSH, null, "async_q_$qIndex")
            }
        }

        // ----- Recording permission -----
        var hasRecordPermission by remember {
            mutableStateOf(
                ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
            )
        }
        val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            hasRecordPermission = granted
        }

        // ----- Recording / playback state -----
        var isRecording by remember { mutableStateOf(false) }
        var isPlaying by remember { mutableStateOf(false) }
        var hasRecording by remember(qIndex) { mutableStateOf(false) }
        val mediaRecorder = remember { mutableStateOf<MediaRecorder?>(null) }
        val mediaPlayer = remember { mutableStateOf<MediaPlayer?>(null) }
        val audioFile = remember(qIndex) { File(context.cacheDir, "answer_$qIndex.3gp") }
        val amplitudes = remember { mutableStateListOf<Float>() }

        fun stopPlayback() {
            try {
                mediaPlayer.value?.stop()
                mediaPlayer.value?.release()
            } catch (e: Exception) {
                // ignore - player may already be stopped
            }
            mediaPlayer.value = null
            isPlaying = false
        }

        fun stopRecording() {
            try {
                mediaRecorder.value?.stop()
                mediaRecorder.value?.release()
            } catch (e: Exception) {
                // ignore - recorder may already be stopped
            }
            mediaRecorder.value = null
            isRecording = false
            hasRecording = true
        }

        fun startRecording() {
            if (!hasRecordPermission) {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                return
            }
            stopPlayback()
            amplitudes.clear()
            try {
                val recorder = MediaRecorder().apply {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                    setOutputFile(audioFile.absolutePath)
                    prepare()
                    start()
                }
                mediaRecorder.value = recorder
                isRecording = true
            } catch (e: Exception) {
                isRecording = false
            }
        }

        fun playRecording() {
            if (!hasRecording) return
            try {
                val player = MediaPlayer().apply {
                    setDataSource(audioFile.absolutePath)
                    prepare()
                    setOnCompletionListener { isPlaying = false }
                    start()
                }
                mediaPlayer.value = player
                isPlaying = true
            } catch (e: Exception) {
                isPlaying = false
            }
        }

        fun seekPlayback(deltaMs: Int) {
            mediaPlayer.value?.let { p ->
                try {
                    val newPos = (p.currentPosition + deltaMs).coerceIn(0, p.duration)
                    p.seekTo(newPos)
                } catch (e: Exception) {
                    // ignore
                }
            }
        }

        // Live amplitude polling while recording, for the waveform
        LaunchedEffect(isRecording) {
            while (isRecording) {
                val amp = try { mediaRecorder.value?.maxAmplitude ?: 0 } catch (e: Exception) { 0 }
                amplitudes.add((amp / 32767f).coerceIn(0f, 1f))
                if (amplitudes.size > 60) amplitudes.removeAt(0)
                delay(80)
            }
        }

        DisposableEffect(Unit) {
            onDispose {
                try { mediaRecorder.value?.release() } catch (e: Exception) { }
                try { mediaPlayer.value?.release() } catch (e: Exception) { }
            }
        }

        LaunchedEffect(qIndex, phase, asyncStarted) {
            if (!asyncStarted) return@LaunchedEffect
            if (phase == "prep") {
                secondsLeft = 15
                while (secondsLeft > 0) {
                    delay(1000)
                    secondsLeft -= 1
                }
                phase = "recording"
            }
            // "recording" phase is now manual (REC/mic button) - no auto timer/auto-advance here.
        }

        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedBackground()

            if (phase == "done") {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Tapos na luv 💙", color = UrPink, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("Back para lumabas", color = UrGray, fontSize = 14.sp)
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
                    .padding(horizontal = 32.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Question ${qIndex + 1} / ${asyncQaList.size}",
                        color = UrGray,
                        fontSize = 13.sp
                    )
                    Spacer(Modifier.height(16.dp))

                    // Waveform sits above REC/PLAY - live while recording, idle otherwise.
                    WaveformDisplay(amplitudes = amplitudes, modifier = Modifier.padding(horizontal = 40.dp))
                    Spacer(Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(40.dp)) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(
                                onClick = { if (isRecording) stopRecording() else startRecording() },
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(50))
                                    .border(1.5.dp, Color(0xFFE0245E), RoundedCornerShape(50))
                            ) {
                                Icon(
                                    imageVector = if (isRecording) Icons.Filled.Stop else Icons.Filled.FiberManualRecord,
                                    contentDescription = "Record",
                                    tint = Color(0xFFE0245E)
                                )
                            }
                            Text("REC", color = Color(0xFFE0245E), fontSize = 11.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(
                                onClick = { if (isPlaying) stopPlayback() else playRecording() },
                                enabled = hasRecording,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(50))
                                    .border(1.5.dp, Color(0xFF2A5CE0), RoundedCornerShape(50))
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                    contentDescription = "Play",
                                    tint = Color(0xFF2A5CE0)
                                )
                            }
                            Text("PLAY", color = Color(0xFF2A5CE0), fontSize = 11.sp)
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(28.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = asyncQaList[qIndex].question,
                        color = Color(0xFFE0245E),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    when (phase) {
                        "prep" -> {
                            Text("Prepare your answer...", color = UrGray, fontSize = 15.sp)
                            Spacer(Modifier.height(10.dp))
                            Text("$secondsLeft", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 40.sp)
                            Spacer(Modifier.height(24.dp))
                        }
                        "recording" -> {
                            Text(
                                if (isRecording) "Nagre-record..." else "Handa ka na? I-tap ang mic",
                                color = UrGray,
                                fontSize = 13.sp
                            )
                            Spacer(Modifier.height(16.dp))
                        }
                        "review" -> {
                            Text("Sample Answer:", color = UrGray, fontSize = 13.sp)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = asyncQaList[qIndex].answer,
                                color = Color(0xFF2A5CE0),
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 22.sp
                            )
                            Spacer(Modifier.height(20.dp))
                        }
                    }

                    // Bottom controls: BACK 2s / TAP TO SPEAK (mic) / FORWARD 2s - kept small.
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(28.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(
                                onClick = { seekPlayback(-2000) },
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(50))
                                    .border(1.dp, Color(0xFF2A5CE0), RoundedCornerShape(50))
                            ) {
                                Icon(
                                    Icons.Filled.FastRewind,
                                    contentDescription = "Back 2s",
                                    tint = Color(0xFF2A5CE0),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text("2s", color = Color(0xFF2A5CE0), fontSize = 9.sp)
                        }

                        IconButton(
                            onClick = {
                                if (phase == "recording") {
                                    if (isRecording) {
                                        stopRecording()
                                        phase = "review"
                                    } else {
                                        startRecording()
                                    }
                                }
                            },
                            enabled = phase == "recording",
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(50))
                                .border(
                                    2.dp,
                                    Brush.horizontalGradient(listOf(Color(0xFFE0245E), Color(0xFF2A5CE0))),
                                    RoundedCornerShape(50)
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Mic,
                                contentDescription = "Tap to speak",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(
                                onClick = { seekPlayback(2000) },
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(50))
                                    .border(1.dp, Color(0xFFE0245E), RoundedCornerShape(50))
                            ) {
                                Icon(
                                    Icons.Filled.FastForward,
                                    contentDescription = "Forward 2s",
                                    tint = Color(0xFFE0245E),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text("2s", color = Color(0xFFE0245E), fontSize = 9.sp)
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    Text("TAP TO SPEAK", color = UrGray, fontSize = 11.sp)

                    if (phase == "review") {
                        Spacer(Modifier.height(20.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF1D3FB5))
                                .clickable {
                                    stopPlayback()
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
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    val context = LocalContext.current
    val tts = remember { mutableStateOf<TextToSpeech?>(null) }
    var ttsReady by remember { mutableStateOf(false) }
    val spokenIndices = remember { mutableStateListOf<Int>() }
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp

    DisposableEffect(Unit) {
        val t = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.value?.language = Locale.US
                tts.value?.setSpeechRate(1.10f)
                tts.value?.setPitch(1.0f)
                ttsReady = true
            }
        }
        tts.value = t
        onDispose { t.stop(); t.shutdown() }
    }

    val screenHeightPx = with(density) { screenHeightDp.toPx() }
    val earlyTriggerPx = screenHeightPx * 0.85f

    fun speakIfDue(index: Int) {
        if (index !in spokenIndices) {
            spokenIndices.add(index)
            val params = Bundle().apply {
                putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f)
            }
            if (index == 0) {
                tts.value?.playSilentUtterance(200L, TextToSpeech.QUEUE_FLUSH, "silence_0")
                tts.value?.speak(qaList[0].question, TextToSpeech.QUEUE_ADD, params, "q_0")
            } else {
                tts.value?.speak(qaList[index].question, TextToSpeech.QUEUE_ADD, params, "q_$index")
            }
        }
    }

    var countdownDone by remember { mutableStateOf(false) }
    var startRequested by remember { mutableStateOf(false) }

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
                    coroutineScope.launch {
                        scrollState.scrollBy(-dragAmount.y)
                    }
                }
            }
    ) {
        AnimatedBackground()

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

        if (!countdownDone) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        enabled = true,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
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
