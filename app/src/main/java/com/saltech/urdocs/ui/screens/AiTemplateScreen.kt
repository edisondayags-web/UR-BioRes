package com.saltech.urdocs.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
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
import java.io.File

private fun exportWebViewToBitmap(
    context: android.content.Context,
    webView: WebView,
    onDone: (Bitmap?) -> Unit
) {
    val fullHeight = (webView.contentHeight * webView.scale).toInt().coerceAtLeast(webView.height)
    val width = webView.width.coerceAtLeast(1)

    val dpi = 150
    val widthMils = ((width.toFloat() / dpi) * 1000).toInt().coerceAtLeast(1)
    val heightMils = ((fullHeight.toFloat() / dpi) * 1000).toInt().coerceAtLeast(1)

    val mediaSize = PrintAttributes.MediaSize("resume", "resume", widthMils, heightMils)
    val attributes = PrintAttributes.Builder()
        .setMediaSize(mediaSize)
        .setResolution(PrintAttributes.Resolution("res", "res", dpi, dpi))
        .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
        .build()

    val adapter = webView.createPrintDocumentAdapter("resume_export")

    adapter.onLayout(null, attributes, CancellationSignal(), object : PrintDocumentAdapter.LayoutResultCallback() {
        override fun onLayoutFinished(info: PrintDocumentInfo?, changed: Boolean) {
            try {
                val file = File(context.cacheDir, "export_${System.currentTimeMillis()}.pdf")
                val writePfd = ParcelFileDescriptor.open(
                    file,
                    ParcelFileDescriptor.MODE_CREATE or ParcelFileDescriptor.MODE_READ_WRITE or ParcelFileDescriptor.MODE_TRUNCATE
                )
                adapter.onWrite(arrayOf(PageRange.ALL_PAGES), writePfd, CancellationSignal(), object : PrintDocumentAdapter.WriteResultCallback() {
                    override fun onWriteFinished(pages: Array<out PageRange>?) {
                        try {
                            writePfd.close()
                            val readPfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                            val renderer = PdfRenderer(readPfd)
                            val page = renderer.openPage(0)
                            val bmp = Bitmap.createBitmap(width, fullHeight, Bitmap.Config.ARGB_8888)
                            val canvas = android.graphics.Canvas(bmp)
                            canvas.drawColor(android.graphics.Color.WHITE)
                            page.render(bmp, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                            page.close()
                            renderer.close()
                            readPfd.close()
                            file.delete()
                            onDone(bmp)
                        } catch (e: Exception) {
                            onDone(null)
                        }
                    }
                })
            } catch (e: Exception) {
                onDone(null)
            }
        }
    }, null)
}

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
                        exportWebViewToBitmap(context, wv) { bmp ->
                            if (bmp != null) {
                                saveBitmapToGallery(context, bmp, htmlFileName.removeSuffix(".html"))
                            }
                        }
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
