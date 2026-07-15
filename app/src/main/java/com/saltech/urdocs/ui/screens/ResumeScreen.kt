package com.saltech.urdocs.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

/**
 * MVP form para sa Resume. Ang [processedSelfie] ay yung result na galing sa
 * SelfieCaptureScreen (2x2 crop + white background) -- ipinapasa via
 * NavGraph/back-stack savedStateHandle sa susunod na iteration.
 */
@Composable
fun ResumeScreen(
    processedSelfie: Bitmap? = null,
    onTakeSelfie: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var objective by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var education by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("📄 Resume Maker", style = MaterialTheme.typography.titleLarge)
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
        OutlinedTextField(address, { address = it }, label = { Text("Address") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(objective, { objective = it }, label = { Text("Career Objective") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(experience, { experience = it }, label = { Text("Work Experience") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(education, { education = it }, label = { Text("Education") }, modifier = Modifier.fillMaxWidth(), minLines = 2)

        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { /* TODO: export to PDF -- next milestone */ }, modifier = Modifier.fillMaxWidth()) {
            Text("I-generate ang Resume (PDF) -- TODO")
        }
    }
}
