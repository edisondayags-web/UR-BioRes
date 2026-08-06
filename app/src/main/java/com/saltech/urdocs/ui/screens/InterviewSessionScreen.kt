package com.saltech.urdocs.ui.screens

import android.media.MediaPlayer
import android.media.MediaRecorder
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File
import java.util.Locale

enum class RecordingState {
    IDLE,       // Bago mag-"Go"
    RECORDING,  // Nagre-record
    PAUSED,     // Naka-pause ang recording
    STOPPED     // Tapos na mag-record, ready for Playback
}

@Composable
fun AsyncInterviewSessionContent(
    questionText: String,
    questionIndex: Int,
    totalQuestions: Int,
    onNextQuestion: () -> Unit
) {
    val context = LocalContext.current

    // State Holders
    var recordingState by remember { mutableStateOf(RecordingState.IDLE) }
    var isPlayingAudio by remember { mutableStateOf(false) }
    var audioFile by remember { mutableStateOf<File?>(null) }

    // Media Controllers
    var mediaRecorder by remember { mutableStateOf<MediaRecorder?>(null) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    
    // Text-To-Speech Engine
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    // Initialize TTS and Speak Question automatically
    DisposableEffect(questionText) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
                tts?.speak(questionText, TextToSpeech.QUEUE_FLUSH, null, "QuestionTTS")
            }
        }
        onDispose {
            tts?.stop()
            tts?.shutdown()
            mediaRecorder?.release()
            mediaPlayer?.release()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070B19))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Bar: Question Counter
        Text(
            text = "Question $questionIndex / $totalQuestions",
            color = Color.Gray,
            fontSize = 16.sp
        )

        // Question Display + Speaker Button
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = questionText,
                color = Color(0xFFFF4D6D),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Re-play Question TTS Voice
            IconButton(
                onClick = {
                    tts?.speak(questionText, TextToSpeech.QUEUE_FLUSH, null, "QuestionTTS")
                },
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.1f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Listen Question",
                    tint = Color.White
                )
            }
        }

        // Recording & Audio Playback Controls Box
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            when (recordingState) {
                RecordingState.IDLE -> {
                    // Step 1: "Go" Button to start recording
                    Button(
                        onClick = {
                            val outputFile = File(context.cacheDir, "response_$questionIndex.mp3")
                            audioFile = outputFile
                            mediaRecorder = MediaRecorder(context).apply {
                                setAudioSource(MediaRecorder.AudioSource.MIC)
                                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                                setOutputFile(outputFile.absolutePath)
                                prepare()
                                start()
                            }
                            recordingState = RecordingState.RECORDING
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676)),
                        shape = CircleShape,
                        modifier = Modifier.size(100.dp)
                    ) {
                        Text("GO", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Tap GO to start recording", color = Color.Gray, fontSize = 14.sp)
                }

                RecordingState.RECORDING, RecordingState.PAUSED -> {
                    // Step 2: Active REC Indicator & Pause/Stop controls
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Pause / Resume Button
                        IconButton(
                            onClick = {
                                if (recordingState == RecordingState.RECORDING) {
                                    mediaRecorder?.pause()
                                    recordingState = RecordingState.PAUSED
                                } else {
                                    mediaRecorder?.resume()
                                    recordingState = RecordingState.RECORDING
                                }
                            },
                            modifier = Modifier
                                .size(60.dp)
                                .background(Color(0xFFFFB703), CircleShape)
                        ) {
                            Icon(
                                imageVector = if (recordingState == RecordingState.RECORDING) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Pause/Resume",
                                tint = Color.Black
                            )
                        }

                        // Stop Recording Button
                        IconButton(
                            onClick = {
                                mediaRecorder?.apply {
                                    stop()
                                    release()
                                }
                                mediaRecorder = null
                                recordingState = RecordingState.STOPPED
                            },
                            modifier = Modifier
                                .size(70.dp)
                                .background(Color(0xFFFF0055), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stop,
                                contentDescription = "Stop",
                                tint = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (recordingState == RecordingState.RECORDING) "🔴 Recording..." else "⏸️ Paused",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }

                RecordingState.STOPPED -> {
                    // Step 3: Play recorded boses & Next Question
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Playback Recorded Voice Button
                        Button(
                            onClick = {
                                if (isPlayingAudio) {
                                    mediaPlayer?.stop()
                                    mediaPlayer?.release()
                                    mediaPlayer = null
                                    isPlayingAudio = false
                                } else {
                                    audioFile?.let { file ->
                                        mediaPlayer = MediaPlayer().apply {
                                            setDataSource(file.absolutePath)
                                            prepare()
                                            start()
                                            setOnCompletionListener {
                                                isPlayingAudio = false
                                            }
                                        }
                                        isPlayingAudio = true
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = if (isPlayingAudio) Icons.Default.Stop else Icons.Default.PlayArrow,
                                contentDescription = "Play Voice"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (isPlayingAudio) "Stop Playback" else "Listen to My Voice")
                        }

                        // Re-record Button
                        OutlinedButton(
                            onClick = {
                                recordingState = RecordingState.IDLE
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Retake", color = Color.White)
                        }
                    }
                }
            }
        }

        // Bottom Action Button
        Button(
            onClick = {
                // Stop any playback before proceeding
                mediaPlayer?.stop()
                recordingState = RecordingState.IDLE
                onNextQuestion()
            },
            enabled = recordingState == RecordingState.STOPPED || recordingState == RecordingState.IDLE,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7))
        ) {
            Text("Next Question", fontSize = 16.sp)
        }
    }
}
