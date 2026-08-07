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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Selfie capture -- SIMPLE na, static guide box lang (walang live face
 * tracking). Ang totoong 2x2 crop ay ML Kit pa rin ang bahala PAGKATAPOS
 * kumuha (sa Bio-Data/Resume screen) -- kaya hindi kailangan ang box na
 * "sumusunod" sa mukha para gumana nang tama. Mas simple = mas stable.
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
            Text("permission lang to luv para sa camera🩵.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }) {
                Text("allow mo lang luv🩵")
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
                        cameraProvider?.unbindAll()
                        isProcessing = false
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
        PremiumWaveBackground()
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                cameraProviderFuture.addListener({
                    val provider = cameraProviderFuture.get()
                    cameraProvider = provider
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    val capture = ImageCapture.Builder().build()
                    imageCapture = capture

                    val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                    provider.unbindAll()
                    camera = provider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, preview, capture
                    )
                }, ContextCompat.getMainExecutor(ctx))
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            val boxSize = size.width * 0.7f
            val left = (size.width - boxSize) / 2f
            val top = size.height * 0.32f
            val right = left + boxSize
            val bottom = top + boxSize
            val dim = Color.Black.copy(alpha = 0.45f)

            drawRect(color = dim, topLeft = Offset(0f, 0f), size = Size(size.width, top))
            drawRect(color = dim, topLeft = Offset(0f, bottom), size = Size(size.width, size.height - bottom))
            drawRect(color = dim, topLeft = Offset(0f, top), size = Size(left, boxSize))
            drawRect(color = dim, topLeft = Offset(right, top), size = Size(size.width - right, boxSize))

            drawRect(
                color = Color.White,
                topLeft = Offset(left, top),
                size = Size(boxSize, boxSize),
                style = Stroke(width = 3.dp.toPx())
            )
        }

        Text(
            text = "luv mas recommened kung mag upload ka nalang ako na bahala mag edit para maging white background image mo🩵",
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
                        isProcessing -> "Taking..."
                        else -> "📸 Take"
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
