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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.saltech.urdocs.util.DocumentRenderer
import com.saltech.urdocs.util.GallerySaver
import com.saltech.urdocs.util.ResumeData
import com.saltech.urdocs.util.ResumeStyle

@Composable
private fun FormField(label: String, value: String, minLines: Int = 1, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        minLines = minLines
    )
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun ResumeScreen(
    style: String,
    processedSelfie: Bitmap? = null,
    onTakeSelfie: () -> Unit
) {
    val resumeStyle = if (style == "modern") ResumeStyle.MODERN else ResumeStyle.CORPORATE
    val context = LocalContext.current

    var fullName by remember { mutableStateOf("") }
    var jobTitle by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var civilStatus by remember { mutableStateOf("") }
    var nationality by remember { mutableStateOf("Filipino") }
    var objective by remember { mutableStateOf("") }
    var education by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf("") }
    var trainings by remember { mutableStateOf("") }
    var references by remember { mutableStateOf("") }

    var previewBitmap by remember { mutableStateOf<Bitmap?>(null) }

    if (previewBitmap != null) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            Text("👁️ Preview", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            Image(
                bitmap = previewBitmap!!.asImageBitmap(),
                contentDescription = "Resume preview",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val saved = GallerySaver.saveBitmap(context, previewBitmap!!, "Resume_${System.currentTimeMillis()}")
                    Toast.makeText(
                        context,
                        if (saved) "Na-save sa Gallery (Pictures/UR Docs)!" else "Hindi na-save, subukan ulit.",
                        Toast.LENGTH_LONG
                    ).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("⬇️ I-download / I-save sa Gallery") }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = { previewBitmap = null }, modifier = Modifier.fillMaxWidth()) {
                Text("✏️ Bumalik sa Form")
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("📄 Resume Maker (${if (resumeStyle == ResumeStyle.MODERN) "Modern" else "Corporate"})", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (processedSelfie != null) {
            Image(bitmap = processedSelfie.asImageBitmap(), contentDescription = "2x2 ID Photo", modifier = Modifier.size(120.dp))
            Spacer(modifier = Modifier.height(8.dp))
        }
        OutlinedButton(onClick = onTakeSelfie, modifier = Modifier.fillMaxWidth()) {
            Text(if (processedSelfie == null) "📸 Kumuha ng 2x2 Selfie" else "📸 Palitan ang Selfie")
        }
        Spacer(modifier = Modifier.height(16.dp))

        FormField("Buong Pangalan", fullName) { fullName = it }
        FormField("Job Title / Profession (para sa Modern)", jobTitle) { jobTitle = it }
        FormField("Address", address) { address = it }
        FormField("Contact Number", contactNumber) { contactNumber = it }
        FormField("Email Address", email) { email = it }
        FormField("Date of Birth", birthDate) { birthDate = it }
        FormField("Age", age) { age = it }
        FormField("Civil Status", civilStatus) { civilStatus = it }
        FormField("Nationality", nationality) { nationality = it }
        FormField("Career Objective", objective, minLines = 2) { objective = it }
        FormField("Educational Background", education, minLines = 2) { education = it }
        FormField("Work Experience", experience, minLines = 3) { experience = it }
        FormField("Skills", skills, minLines = 2) { skills = it }
        FormField("Trainings / Seminars (optional)", trainings, minLines = 2) { trainings = it }
        FormField("Character References (optional)", references, minLines = 2) { references = it }

        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (fullName.isBlank()) {
                    Toast.makeText(context, "Lagyan muna ng Buong Pangalan.", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                val data = ResumeData(
                    fullName = fullName, jobTitle = jobTitle, address = address,
                    contactNumber = contactNumber, email = email, birthDate = birthDate,
                    age = age, civilStatus = civilStatus, nationality = nationality,
                    objective = objective, education = education, experience = experience,
                    skills = skills, trainings = trainings, references = references
                )
                previewBitmap = DocumentRenderer.renderResume(data, processedSelfie, resumeStyle)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("✅ Tapos na")
        }
    }
}
