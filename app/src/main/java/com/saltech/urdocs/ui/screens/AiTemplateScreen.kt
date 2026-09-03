package com.saltech.urdocs.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.net.Uri
import android.util.Base64
import android.view.View
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.ByteArrayOutputStream

private suspend fun neutralizeViewportHeight(webView: WebView): Unit =
    suspendCancellableCoroutine { cont ->
        // Gumagamit ng computed style (hindi CSSOM/stylesheet scan) kaya gumagana
        // kahit external .css file ang pinagmulan ng vh rule (na minsan naka-block
        // ng WebView security policy sa file:// URLs).
        val js = """
            (function(){
              var vh = window.innerHeight;
              var all = document.querySelectorAll('*');
              for (var i=0; i<all.length; i++){
                var el = all[i];
                var cs = window.getComputedStyle(el);
                var mh = parseFloat(cs.minHeight);
                var h = parseFloat(cs.height);
                if (mh && mh >= vh * 0.85) { el.style.setProperty('min-height','auto','important'); }
                if (h && h >= vh * 0.85 && cs.position !== 'fixed') { el.style.setProperty('height','auto','important'); }
              }
            })();
        """.trimIndent()
        webView.evaluateJavascript(js) {
            if (cont.isActive) cont.resume(Unit) { }
        }
    }

private suspend fun getDocumentHeightPx(webView: WebView, density: Float): Int =
    suspendCancellableCoroutine { cont ->
        webView.evaluateJavascript("document.body.scrollHeight.toString()") { result ->
            val cssHeight = result?.replace("\"", "")?.toFloatOrNull() ?: 0f
            if (cont.isActive) cont.resume((cssHeight * density).toInt()) { }
        }
    }

private data class ContentBox(val left: Int, val top: Int, val width: Int, val height: Int)

private suspend fun getContentBox(webView: WebView, density: Float): ContentBox =
    suspendCancellableCoroutine { cont ->
        val js = """
            (function(){
              var el = document.querySelector('.page') || document.body.firstElementChild || document.body;
              var r = el.getBoundingClientRect();
              return JSON.stringify({l:r.left, t:r.top, w:r.width, h:r.height});
            })();
        """.trimIndent()
        webView.evaluateJavascript(js) { result ->
            try {
                val clean = result?.replace("\\", "")?.trim('"') ?: "{}"
                val l = Regex(""""l":([\-0-9.]+)""").find(clean)?.groupValues?.get(1)?.toFloatOrNull() ?: 0f
                val t = Regex(""""t":([\-0-9.]+)""").find(clean)?.groupValues?.get(1)?.toFloatOrNull() ?: 0f
                val w = Regex(""""w":([\-0-9.]+)""").find(clean)?.groupValues?.get(1)?.toFloatOrNull() ?: 0f
                val h = Regex(""""h":([\-0-9.]+)""").find(clean)?.groupValues?.get(1)?.toFloatOrNull() ?: 0f
                val box = ContentBox(
                    left = (l * density).toInt().coerceAtLeast(0),
                    top = (t * density).toInt().coerceAtLeast(0),
                    width = (w * density).toInt().coerceAtLeast(1),
                    height = (h * density).toInt().coerceAtLeast(1)
                )
                if (cont.isActive) cont.resume(box) { }
            } catch (e: Exception) {
                if (cont.isActive) cont.resume(ContentBox(0, 0, webView.width, webView.height)) { }
            }
        }
    }

private suspend fun setZoom(webView: WebView, zoom: Float): Unit =
    suspendCancellableCoroutine { cont ->
        val js = "document.body.style.zoom = '" + zoom + "';"
        webView.evaluateJavascript(js) {
            if (cont.isActive) cont.resume(Unit) { }
        }
    }

private suspend fun captureFullWebView(webView: WebView, density: Float): Bitmap {
    val originalHeight = webView.height
    val originalLayerType = webView.layerType

    webView.setBackgroundColor(android.graphics.Color.WHITE)
    neutralizeViewportHeight(webView)
    delay(50)
    val rawHeight = getDocumentHeightPx(webView, density).coerceAtLeast(1)

    val targetHeight = (webView.width * 1.4142f).toInt().coerceAtLeast(1)
    var docHeight = rawHeight
    if (rawHeight > targetHeight) {
        val zoom = targetHeight.toFloat() / rawHeight.toFloat()
        setZoom(webView, zoom)
        delay(80)
        docHeight = getDocumentHeightPx(webView, density).coerceAtLeast(1)
    }

    webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    webView.measure(
        View.MeasureSpec.makeMeasureSpec(webView.width, View.MeasureSpec.EXACTLY),
        View.MeasureSpec.makeMeasureSpec(docHeight, View.MeasureSpec.EXACTLY)
    )
    webView.layout(0, 0, webView.width, docHeight)

    // give Chromium time to actually paint the newly expanded area
    delay(500)

    val fullBmp = Bitmap.createBitmap(webView.width, docHeight, Bitmap.Config.ARGB_8888)
    val fullCanvas = Canvas(fullBmp)
    fullCanvas.drawColor(android.graphics.Color.WHITE)
    webView.draw(fullCanvas)

    // sukatin totoong content box (.page) at i-crop dun -- para mawala black/blank bars sa gilid
    val box = getContentBox(webView, density)
    val cropWidth = box.width.coerceAtMost(fullBmp.width - box.left).coerceAtLeast(1)
    val cropHeight = box.height.coerceAtMost(fullBmp.height - box.top).coerceAtLeast(1)
    val cropped = try {
        Bitmap.createBitmap(fullBmp, box.left, box.top, cropWidth, cropHeight)
    } catch (e: Exception) {
        fullBmp
    }

    // restore
    webView.evaluateJavascript("document.body.style.zoom = '1';", null)
    webView.setBackgroundColor(android.graphics.Color.parseColor("#0A1931"))
    webView.setLayerType(originalLayerType, null)
    webView.measure(
        View.MeasureSpec.makeMeasureSpec(webView.width, View.MeasureSpec.EXACTLY),
        View.MeasureSpec.makeMeasureSpec(originalHeight, View.MeasureSpec.EXACTLY)
    )
    webView.layout(0, 0, webView.width, originalHeight)

    return cropped
}

private fun shrinkToA4(source: Bitmap): Bitmap {
    val targetWidth = 1600
    val scale = targetWidth.toFloat() / source.width
    val targetHeight = (source.height * scale).toInt().coerceAtLeast(1)

    return Bitmap.createScaledBitmap(source, targetWidth, targetHeight, true)
}

@Composable
fun AiTemplateScreen(htmlFileName: String, onBack: () -> Unit = {}) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var isProcessing by remember { mutableStateOf(false) }
    var isDownloading by remember { mutableStateOf(false) }

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
                    if (wv != null && wv.width > 0 && !isDownloading) {
                        isDownloading = true
                        scope.launch {
                            try {
                                val density = context.resources.displayMetrics.density
                                val full = captureFullWebView(wv, density)
                                val fitted = shrinkToA4(full)
                                saveBitmapToGallery(context, fitted, htmlFileName.removeSuffix(".html"))
                            } finally {
                                isDownloading = false
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF14161A))
            ) {
                Text(if (isDownloading) "\u23F3 Saving..." else "\u2B07 Download")
            }
        }
    }
}
