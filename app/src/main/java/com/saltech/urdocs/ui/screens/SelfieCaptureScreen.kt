package com.saltech.urdocs.ui.screens

import android.graphics.Bitmap
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.Alignment
import androidx.core.content.ContextCompat
import com.saltech.urdocs.ml.BackgroundHelper
import com.saltech.urdocs.ml.FaceCropHelper
import kotlinx.coroutines.launch

/**
 * Selfie -> 2x2 ID photo pipeline:
 * 1. CameraX captures front-facing selfie
 * 2. FaceCropHelper (ML Kit face detection) auto-crops sa square framing
 * 3. BackgroundHelper (ML Kit selfie segmentation) papalitan ng puting bg
 * 4. Resulta ipapasa pabalik sa caller (Resume/BioData screen)
 */
@Composable
fun SelfieCaptureScreen(
    onProcessed: (Bitmap) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    var isProcessing by remember { mutableStateOf(false) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }

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
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, preview, capture
                    )
                }, ContextCompat.getMainExecutor(ctx))
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            Button(
                onClick = {
                    val capture = imageCapture ?: return@Button
                    isProcessing = true
                    capture.takePicture(
                        ContextCompat.getMainExecutor(context),
                        object : ImageCapture.OnImageCapturedCallback() {
                            override fun onCaptureSuccess(image: ImageProxy) {
                                val bitmap = image.toBitmap()
                                image.close()
                                scope.launch {
                                    try {
                                        val cropped = FaceCropHelper.cropTo2x2(bitmap)
                                        val whiteBg = BackgroundHelper.replaceWithWhiteBackground(cropped)
                                        isProcessing = false
                                        onProcessed(whiteBg)
                                    } catch (e: Exception) {
                                        isProcessing = false
                                        // TODO: show error snackbar; for now, ibalik yung
                                        // original bitmap para hindi ma-stuck ang user.
                                        onProcessed(bitmap)
                                    }
                                }
                            }

                            override fun onError(exception: ImageCaptureException) {
                                isProcessing = false
                            }
                        }
                    )
                },
                enabled = !isProcessing,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isProcessing) "Pina-process..." else "📸 Kuhanan")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = onCancel, modifier = Modifier.fillMaxWidth()) {
                Text("Cancel")
            }
        }
    }
}

// NOTE: `ImageProxy.toBitmap()` ay extension function na kailangan i-implement
// (YUV_420_888 -> Bitmap conversion) o gamitin ang built-in na helper mula sa
// camera-core kung available sa bersyon na ginagamit. Placeholder muna dito;
// susunod na session natin i-flesh out ang exact conversion code.
