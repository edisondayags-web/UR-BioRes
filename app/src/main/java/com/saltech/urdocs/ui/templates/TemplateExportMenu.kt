package com.saltech.urdocs.ui.templates

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import android.widget.Toast
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.launch
import com.google.zxing.common.BitMatrix
import java.io.File
import java.io.FileOutputStream

fun saveBitmapToCache(context: Context, bitmap: Bitmap, name: String): Uri {
    val dir = File(context.cacheDir, "exports")
    if (!dir.exists()) dir.mkdirs()
    val file = File(dir, "$name.png")
    FileOutputStream(file).use { out -> bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) }
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}

fun shareImageUri(context: Context, uri: Uri) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Share Resume"))
}

fun generateQrBitmap(text: String, size: Int = 512): Bitmap {
    val matrix: BitMatrix = MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, size, size)
    val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
    for (x in 0 until size) for (y in 0 until size) {
        bmp.setPixel(x, y, if (matrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
    }
    return bmp
}

fun generateBarcodeBitmap(text: String, width: Int = 600, height: Int = 200): Bitmap {
    val matrix: BitMatrix = MultiFormatWriter().encode(text, BarcodeFormat.CODE_128, width, height)
    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
    for (x in 0 until width) for (y in 0 until height) {
        bmp.setPixel(x, y, if (matrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
    }
    return bmp
}

@Composable
fun TemplateExportMenu(
    graphicsLayer: GraphicsLayer,
    resumeName: String,
    onHome: () -> Unit
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var showQr by remember { mutableStateOf(false) }
    var showBarcode by remember { mutableStateOf(false) }

    suspend fun captureBitmap(): Bitmap {
        val imageBitmap = graphicsLayer.toImageBitmap()
        return imageBitmap.asAndroidBitmap()
    }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("Download") }, onClick = {
                expanded = false
                scope.launch {
                    val bmp = captureBitmap()
                    val uri = saveBitmapToCache(context, bmp, resumeName)
                    Toast.makeText(context, "Saved to cache: $uri", Toast.LENGTH_SHORT).show()
                }
            })
            DropdownMenuItem(text = { Text("Send To") }, onClick = {
                expanded = false
                scope.launch {
                    val bmp = captureBitmap()
                    val uri = saveBitmapToCache(context, bmp, resumeName)
                    shareImageUri(context, uri)
                }
            })
            DropdownMenuItem(text = { Text("Convert to QR Code") }, onClick = {
                expanded = false
                showQr = true
            })
            DropdownMenuItem(text = { Text("Convert to Barcode") }, onClick = {
                expanded = false
                showBarcode = true
            })
            DropdownMenuItem(text = { Text("Home") }, onClick = {
                expanded = false
                onHome()
            })
        }
    }

    if (showQr) {
        val qrBmp = remember { generateQrBitmap(resumeName) }
        AlertDialog(
            onDismissRequest = { showQr = false },
            confirmButton = { TextButton(onClick = { showQr = false }) { Text("Close") } },
            text = {
                androidx.compose.foundation.Image(
                    qrBmp.asImageBitmap(), contentDescription = "QR Code",
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f)
                )
            }
        )
    }

    if (showBarcode) {
        val bcBmp = remember { generateBarcodeBitmap(resumeName) }
        AlertDialog(
            onDismissRequest = { showBarcode = false },
            confirmButton = { TextButton(onClick = { showBarcode = false }) { Text("Close") } },
            text = {
                androidx.compose.foundation.Image(
                    bcBmp.asImageBitmap(), contentDescription = "Barcode",
                    modifier = Modifier.fillMaxWidth().height(150.dp)
                )
            }
        )
    }
}
