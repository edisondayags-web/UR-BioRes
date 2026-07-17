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
import com.saltech.urdocs.ml.BackgroundHelper
import com.saltech.urdocs.ml.FaceCropHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.saltech.urdocs.BuildConfig
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Selfie -> 2x2 ID photo pipeline:
 * 1. Kailangan muna ng CAMERA runtime permission (Android 6+).
 * 2. CameraX captures front-facing selfie.
 * 3. Guide box + dim vignette + 3-2-1 countdown = steady at maayos na
 *    framing bago kumuha (para hindi baluktot/"zigzag" ang resulta).
 * 4. Flash toggle = sustained screen brightness + white "pop" bago
 *    mag-shutter (front camera karamihan walang physical flash), plus
 *    totoong torch kung meron talaga ang device.
 * 5. FaceCropHelper (ML Kit face detection) auto-crops sa square framing.
 * 6. BackgroundHelper (ML Kit selfie segmentation) papalitan ng puting bg.
 * 7. Resulta ipapasa pabalik sa caller (Resume/BioData screen).
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
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var countdown by remember { mutableStateOf<Int?>(null) }
    var flashOn by remember { mutableStateOf(false) }
    var showFlashPop by remember { mutableStateOf(false) }

    // Sustained brightness boost habang naka-ON ang flash toggle.
    LaunchedEffect(flashOn) {
        val window = (context as? Activity)?.window ?: return@LaunchedEffect
        val params = window.attributes
        params.screenBrightness = if (flashOn) 1f else WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
        window.attributes = params
    }

    // Totoong torch kung meron ang device (bihira sa front camera, pero
    // libre i-try -- walang epekto kung wala talaga).
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
                        scope.launch {
                            try {
                                val cropped = FaceCropHelper.cropTo2x2(bitmap)
                                val whiteBg = BackgroundHelper.replaceWithWhiteBackground(cropped)
                                val enhanced = try { enhance2x2WithAI(whiteBg) } catch (e: Exception) { whiteBg }
                                isProcessing = false
                                onProcessed(enhanced)
                            } catch (e: Exception) {
                                isProcessing = false
                                errorMessage = "Processing error: ${e.javaClass.simpleName}: ${e.message}"
                            }
                        }
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
                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    val capture = ImageCapture.Builder().build()
                    imageCapture = capture

                    val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                    cameraProvider.unbindAll()
                    camera = cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, preview, capture
                    )
                }, ContextCompat.getMainExecutor(ctx))
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        // Guide box + dim vignette -- preview kung papaano magiging hitsura
        // ng 2x2 crop. Dito dapat kasya ang BUONG mukha, hindi lang bahagi.
        Canvas(modifier = Modifier.fillMaxSize()) {
            val boxSize = size.width * 0.65f
            val left = (size.width - boxSize) / 2f
            val top = size.height * 0.38f - boxSize / 2f
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
            text = "Ilagay ang buong mukha dito sa loob ng box",
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) { }
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
                        isProcessing -> "Pina-process..."
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

private suspend fun enhance2x2WithAI(bitmap: Bitmap): Bitmap = withContext(Dispatchers.IO) {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    val base64Image = Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP)

    val apiKey = BuildConfig.GEMINI_API_KEY
    val url = URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-image:generateContent?key=$apiKey")

    val requestBody = JSONObject().apply {
        put("contents", org.json.JSONArray().put(
            JSONObject().apply {
                put("parts", org.json.JSONArray()
                    .put(JSONObject().apply {
                        put("text", "Clean up this ID photo: fix lighting, remove noise/blur, make the background pure clean white, and make it look like a professional studio ID photo. Do NOT change the person's face, facial features, or expression.")
                    })
                    .put(JSONObject().apply {
                        put("inline_data", JSONObject().apply {
                            put("mime_type", "image/png")
                            put("data", base64Image)
                        })
                    })
                )
            }
        ))
    }

    val connection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "POST"
    connection.setRequestProperty("Content-Type", "application/json")
    connection.doOutput = true
    connection.outputStream.use { it.write(requestBody.toString().toByteArray()) }

    val responseCode = connection.responseCode
    if (responseCode != 200) {
        throw Exception("Gemini API error: $responseCode")
    }

    val responseText = connection.inputStream.bufferedReader().use { it.readText() }
    val json = JSONObject(responseText)
    val parts = json.getJSONArray("candidates")
        .getJSONObject(0)
        .getJSONObject("content")
        .getJSONArray("parts")

    var resultBitmap: Bitmap? = null
    for (i in 0 until parts.length()) {
        val part = parts.getJSONObject(i)
        if (part.has("inline_data")) {
            val b64 = part.getJSONObject("inline_data").getString("data")
            val bytes = Base64.decode(b64, Base64.NO_WRAP)
            resultBitmap = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            break
        }
    }

    resultBitmap ?: bitmap
}
