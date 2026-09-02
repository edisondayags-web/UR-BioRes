package com.saltech.urdocs.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.util.Base64
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.saltech.urdocs.ml.BackgroundHelper
import com.saltech.urdocs.ml.FaceCropHelper
import com.saltech.urdocs.ui.templates.saveBitmapToGallery
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@Composable
fun AiTemplateScreen(htmlFileName: String, onBack: () -> Unit = {}) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var isProcessing by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            isProcessing = true
            scope.launch {
                try {
                    val input = context.contentResolver.openInputStream(uri)
                    val original = BitmapFactory.decodeStream(input)
                    input?.close()
                    if (original != null) {
                        val cropped = FaceCropHelper.cropTo2x2(original)
                        val whiteBg = BackgroundHelper.replaceWithWhiteBackground(cropped)
                        val stream = ByteArrayOutputStream()
                        whiteBg.compress(Bitmap.CompressFormat.PNG, 90, stream)
                        val base64 = Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP)
                        val js = """
                            (function(){
                              var el = document.querySelector('.avatar-circle, .photo-box, .photo-circle');
                              if(el){
                                el.innerHTML = '';
                                el.style.backgroundImage = 'url(data:image/png;base64,$base64)';
                                el.style.backgroundSize = 'cover';
                                el.style.backgroundPosition = 'center';
                              }
                            })();
                        """.trimIndent()
                        webViewRef?.evaluateJavascript(js, null)
                    }
                } catch (e: Exception) {
                    // silently ignore for now
                } finally {
                    isProcessing = false
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                WebView(ctx).apply {
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                    settings.useWideViewPort = true
                    settings.loadWithOverviewMode = true
                    setBackgroundColor(android.graphics.Color.parseColor("#0A1931"))
                    loadUrl("file:///android_asset/templates/$htmlFileName")
                    webViewRef = this
                }
            }
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { photoPickerLauncher.launch("image/*") },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1AB5A3))
            ) {
                Text(if (isProcessing) "\u23F3 Processing..." else "\u2B06 Upload")
            }
            Button(
                onClick = {
                    val wv = webViewRef
                    if (wv != null && wv.width > 0) {
                        val fullHeight = (wv.contentHeight * wv.scale).toInt().coerceAtLeast(wv.height)
                        val bmp = Bitmap.createBitmap(wv.width, fullHeight, Bitmap.Config.ARGB_8888)
                        val canvas = Canvas(bmp)
                        canvas.drawColor(android.graphics.Color.WHITE)
                        wv.draw(canvas)
                        saveBitmapToGallery(context, bmp, htmlFileName.removeSuffix(".html"))
                    }
                },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF14161A))
            ) {
                Text("\u2B07 Download")
            }
        }
    }
}
