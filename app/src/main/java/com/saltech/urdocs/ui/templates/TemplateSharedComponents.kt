package com.saltech.urdocs.ui.templates

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

fun uriToBitmapShared(context: android.content.Context, uriString: String): Bitmap? {
    if (uriString.isEmpty()) return null
    return try {
        if (uriString.startsWith("file://")) {
            BitmapFactory.decodeFile(Uri.parse(uriString).path)
        } else {
            val uri = Uri.parse(uriString)
            if (Build.VERSION.SDK_INT >= 28) {
                val src = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(src)
            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
        }
    } catch (e: Exception) { null }
}

fun saveAvatarBitmapShared(context: android.content.Context, bitmap: Bitmap): String {
    val dir = File(context.filesDir, "avatars").apply { mkdirs() }
    val file = File(dir, "avatar_${System.currentTimeMillis()}.png")
    FileOutputStream(file).use { out -> bitmap.compress(Bitmap.CompressFormat.PNG, 95, out) }
    return "file://" + file.absolutePath
}

/** Shared face-crop avatar picker (gallery only, no camera) used across all resume templates. */
@Composable
fun SharedAvatarPicker(
    uriValue: String,
    size: Dp,
    accent: Color,
    initial: String,
    onUriChange: (String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isProcessing by remember { mutableStateOf(false) }
    LaunchedEffect(uriValue) { bitmap = uriToBitmapShared(context, uriValue) }

    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            isProcessing = true
            scope.launch(Dispatchers.Default) {
                try {
                    val raw = if (Build.VERSION.SDK_INT >= 28) {
                        val src = ImageDecoder.createSource(context.contentResolver, uri)
                        ImageDecoder.decodeBitmap(src)
                    } else {
                        @Suppress("DEPRECATION")
                        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                    }
                    val cropped = try {
                        com.saltech.urdocs.ml.FaceCropHelper.cropTo2x2WithFaceBox(raw).first
                    } catch (e: Exception) { raw }
                    val savedPath = saveAvatarBitmapShared(context, cropped)
                    withContext(Dispatchers.Main) {
                        isProcessing = false
                        onUriChange(savedPath)
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) { isProcessing = false }
                }
            }
        }
    }

    Box(Modifier.size(size)) {
        Box(
            Modifier.size(size).clip(CircleShape).border(2.dp, accent, CircleShape)
                .background(Color.Transparent)
                .clickable { pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
            contentAlignment = Alignment.Center
        ) {
            when {
                isProcessing -> CircularProgressIndicator(color = accent, modifier = Modifier.size(size * 0.35f), strokeWidth = 2.dp)
                bitmap != null -> Image(bitmap!!.asImageBitmap(), contentDescription = "Avatar", modifier = Modifier.fillMaxSize().clip(CircleShape))
                initial.isNotEmpty() -> Text(initial.take(1), color = accent, fontSize = (size.value * 0.33f).sp, fontWeight = FontWeight.Bold)
            }
        }
        Box(
            Modifier.size(size * 0.27f).align(Alignment.BottomEnd).clip(CircleShape).background(accent)
                .border(2.dp, Color(0xFF050505), CircleShape)
                .clickable { pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add photo", tint = Color.Black, modifier = Modifier.size(size * 0.15f))
        }
    }
}

/** Auto-shrinks name font size so long full names don't get clipped. */
fun autoShrinkNameFontSize(name: String, base: Int = 19): androidx.compose.ui.unit.TextUnit {
    return when {
        name.length > 26 -> (base - 7).sp
        name.length > 20 -> (base - 5).sp
        name.length > 15 -> (base - 2.5f).sp
        else -> base.sp
    }
}
