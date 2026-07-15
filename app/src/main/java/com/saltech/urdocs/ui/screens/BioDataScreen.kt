package com.saltech.urdocs.ui.screens

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.layer.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.saltech.urdocs.util.GallerySaver
import kotlinx.coroutines.launch

@Composable
fun BioDataScreen(
    processedSelfie: Bitmap? = null,
    onTakeSelfie: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var civilStatus by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .drawWithContent {
                graphicsLayer.record { this@drawWithContent.drawContent() }
                drawLayer(graphicsLayer)
            }
            .padding(20.dp)
    ) {
        Text("📝 Bio-Data Maker", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (processedSelfie != null) {
            Image(
                bitmap = processedSelfie.asImageBitmap(),
                contentDescription = "2x2 ID Photo",
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        OutlinedButton(onClick = onTakeSelfie, modifier = Modifier.fillMaxWidth()) {
            Text(if (processedSelfie == null) "📸 Kumuha ng 2x2 Selfie" else "📸 Palitan ang Selfie")
        }

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(fullName, { fullName = it }, label = { Text("Buong Pangalan") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(birthDate, { birthDate = it }, label = { Text("Petsa ng Kapanganakan") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(civilStatus, { civilStatus = it }, label = { Text("Civil Status") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(address, { address = it }, label = { Text("Address") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(contactNumber, { contactNumber = it }, label = { Text("Contact Number") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                scope.launch {
                    val bitmap = graphicsLayer.toImageBitmap().asAndroidBitmap()
                    val saved = GallerySaver.saveBitmap(context, bitmap, "BioData_${System.currentTimeMillis()}")
                    Toast.makeText(
                        context,
                        if (saved) "Na-save sa Gallery (Pictures/UR Docs)!" else "Hindi na-save, subukan ulit.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("💾 I-save sa Gallery")
        }
    }
}
