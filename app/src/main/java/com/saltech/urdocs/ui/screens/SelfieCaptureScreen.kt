package com.saltech.urdocs.ui.screens

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.view.WindowManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

/**
 * Selfie capture LANG -- kumuha, isara agad ang camera (unbindAll), tapos
 * ibalik ang RAW bitmap sa caller. Ang guide box ay LIVE na sumusunod sa
 * mukha papuntang dibdib (head-to-chest, tulad ng totoong 2x2 ID photo),
 * gamit ang UseCaseGroup + ViewPort para magkatugma ang "nakikita" ng
 * Preview at ng face-detection analysis (kung hindi ito ma-align, ang
 * box ay maling lokasyon ang tatarget, gaya ng dating nangyari).
 */
@Composable
fun SelfieCaptureScreen(
    onProcessed: (Bitmap) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if (!hasCameraPermission) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Kailangan ng camera permission para makakuha ng selfie.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }) {
                Text("Payagan ang Camera")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = onCancel) { Text("Cancel") }
        }
        return
    }

    var isProcessing by remember { mutableStateOf(false) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var camera by remember { mutableStateOf<Camera?>(null) }
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var countdown by remember { mutableStateOf<Int?>(null) }
    var flashOn by remember { mutableStateOf(false) }
    var showFlashPop by remember { mutableStateOf(false) }
    var faceBoxNormalized by remember { mutableStateOf<Rect?>(null) }

    val faceDetector = remember {
        FaceDetection.getClient(
            FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .build()
        )
    }
    val analysisExecutor = remember { Executors.newSingleThreadExecutor() }

    LaunchedEffect(flashOn) {
        val window = (context as? Activity)?.window ?: return@LaunchedEffect
        val params = window.attributes
        params.screenBrightness = if (flashOn) 1f else WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
        window.attributes = params
    }

    LaunchedEffect(flashOn, camera) {
        val cam = camera ?: return@LaunchedEffect
        if (cam.cameraInfo.hasFlashUnit()) {
            cam.cameraControl.enableTorch(flashOn)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            val window = (context as? Activity)?.window
            if (window != null) {
                val params = window.attributes
                params.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
                window.attributes = params
            }
            cameraProvider?.unbindAll()
            analysisExecutor.shutdown()
            faceDetector.close()
        }
    }

    fun startCaptureSequence() {
        val capture = imageCapture ?: return
        scope.launch {
            for (i in 3 downTo 1) {
                countdown = i
                delay(1000)
            }
            countdown = null

            if (flashOn) {
                showFlashPop = true
                delay(180)
            }

            isProcessing = true
            capture.takePicture(
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        showFlashPop = false
                        val bitmap = image.toBitmap()
                        image.close()

                        // ISARA AGAD ANG CAMERA -- patay na talaga.
                        cameraProvider?.unbindAll()
                        isProcessing = false

                        // RAW bitmap lang ang ibinabalik -- ang caller
                        // (Bio-Data/Resume) na ang bahalang mag-process.
                        onProcessed(bitmap)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        showFlashPop = false
                        isProcessing = false
                        errorMessage = "Capture error: ${exception.message}"
                    }
                }
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)

                // Ang buong pag-bind ay ginagawa PAGKATAPOS ma-layout ang
                // previewView (post{}), para available na ang tamang
                // viewPort -- ito ang nag-a-align sa "nakikita" ng Preview
                // at ng ImageAnalysis (face detection) sa parehong FOV/crop.
                previewView.post {
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        val provider = cameraProviderFuture.get()
                        cameraProvider = provider

                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        val capture = ImageCapture.Builder().build()
                        imageCapture = capture

                        val analysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                        analysis.setAnalyzer(analysisExecutor) { imageProxy ->
                            val mediaImage = imageProxy.image
                            if (mediaImage != null) {
                                val rotation = imageProxy.imageInfo.rotationDegrees
                                val inputImage = InputImage.fromMediaImage(mediaImage, rotation)
                                faceDetector.process(inputImage)
                                    .addOnSuccessListener { faces ->
                                        val face = faces.maxByOrNull {
                                            it.boundingBox.width().toLong() * it.boundingBox.height().toLong()
                                        }
                                        if (face != null) {
                                            val box = face.boundingBox
                                            val imgWidth = if (rotation == 90 || rotation == 270) imageProxy.height else imageProxy.width
                                            val imgHeight = if (rotation == 90 || rotation == 270) imageProxy.width else imageProxy.height
                                            if (imgWidth > 0 && imgHeight > 0) {
                                                val leftN = box.left.toFloat() / imgWidth
                                                val topN = box.top.toFloat() / imgHeight
                                                val rightN = box.right.toFloat() / imgWidth
                                                val bottomN = box.bottom.toFloat() / imgHeight
                                                faceBoxNormalized = Rect(
                                                    (1f - rightN).coerceIn(0f, 1f),
                                                    topN.coerceIn(0f, 1f),
                                                    (1f - leftN).coerceIn(0f, 1f),
                                                    bottomN.coerceIn(0f, 1f)
                                                )
                                            }
                                        } else {
                                            faceBoxNormalized = null
                                        }
                                    }
                                    .addOnCompleteListener { imageProxy.close() }
                            } else {
                                imageProxy.close()
                            }
                        }

                        val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                        provider.unbindAll()

                        val viewPort = previewView.viewPort
                        if (viewPort != null) {
                            val useCaseGroup = UseCaseGroup.Builder()
                                .addUseCase(preview)
                                .addUseCase(capture)
                                .addUseCase(analysis)
                                .setViewPort(viewPort)
                                .build()
                            camera = provider.bindToLifecycle(lifecycleOwner, cameraSelector, useCaseGroup)
                        } else {
                            // Fallback kung wala pang viewPort (bihira lang mangyari)
                            camera = provider.bindToLifecycle(
                                lifecycleOwner, cameraSelector, preview, capture, analysis
                            )
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                }
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        // Guide box na LIVE na sumusunod sa mukha -- pero HEAD-TO-CHEST na
        // ang saklaw (hindi lang mukha/noo), tulad ng totoong 2x2 ID photo.
        Canvas(modifier = Modifier.fillMaxSize()) {
            val dim = Color.Black.copy(alpha = 0.45f)
            val face = faceBoxNormalized

            if (face != null) {
                val faceLeftPx = face.left * size.width
                val faceTopPx = face.top * size.height
                val faceRightPx = face.right * size.width
                val faceBottomPx = face.bottom * size.height
                val faceHeightPx = faceBottomPx - faceTopPx
                val centerX = (faceLeftPx + faceRightPx) / 2f

                // Head-to-chest: ang box height ay ~2.8x ng face height
                // (may espasyo sa itaas ng ulo, hanggang dibdib sa ibaba),
                // parisukat (2x2).
                val boxHeight = (faceHeightPx * 2.8f).coerceIn(size.height * 0.25f, size.height * 0.95f)
                val boxWidth = boxHeight
                val boxTop = (faceTopPx - faceHeightPx * 0.45f).coerceAtLeast(0f)

                var left = centerX - boxWidth / 2f
                left = left.coerceIn(0f, (size.width - boxWidth).coerceAtLeast(0f))
                val top = boxTop.coerceAtMost((size.height - boxHeight).coerceAtLeast(0f))
                val right = left + boxWidth
                val bottom = top + boxHeight

                drawRect(color = dim, topLeft = Offset(0f, 0f), size = Size(size.width, top))
                drawRect(color = dim, topLeft = Offset(0f, bottom), size = Size(size.width, (size.height - bottom).coerceAtLeast(0f)))
                drawRect(color = dim, topLeft = Offset(0f, top), size = Size(left, boxHeight))
                drawRect(color = dim, topLeft = Offset(right, top), size = Size((size.width - right).coerceAtLeast(0f), boxHeight))

                drawRect(
                    color = Color.White,
                    topLeft = Offset(left, top),
                    size = Size(boxWidth, boxHeight),
                    style = Stroke(width = 3.dp.toPx())
                )
            } else {
                drawRect(color = dim, topLeft = Offset.Zero, size = size)
            }
        }

        Text(
            text = if (faceBoxNormalized != null) "Handa na -- pindutin ang Kuhanan"
                   else "Ilagay ang mukha mo sa loob ng camera",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 32.dp)
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        )

        if (countdown != null) {
            Text(
                text = "$countdown",
                color = Color.White,
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(Color.Black.copy(alpha = 0.35f))
                    .padding(horizontal = 28.dp, vertical = 12.dp)
            )
        }

        if (showFlashPop) {
            Box(modifier = Modifier.fillMaxSize().background(Color.White)) { }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = { flashOn = !flashOn },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (flashOn) "💡 Flash: ON" else "💡 Flash: OFF")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { startCaptureSequence() },
                enabled = !isProcessing && countdown == null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    when {
                        countdown != null -> "📸 $countdown..."
                        isProcessing -> "Kumukuha..."
                        else -> "📸 Kuhanan"
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = onCancel, modifier = Modifier.fillMaxWidth()) {
                Text("Cancel")
            }
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
                    .background(Color.White)
                    .padding(8.dp)
            )
        }
    }
}
