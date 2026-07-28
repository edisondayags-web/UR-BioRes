package com.saltech.urdocs.ui.screens

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saltech.urdocs.util.*
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

/** Maliit na underline-style field -- itsura ng "Label: ____" sa papel. */
@Composable
private fun PaperField(label: String, value: String, modifier: Modifier = Modifier, onChange: (String) -> Unit) {
    Column(modifier = modifier) {
        if (label.isNotEmpty()) {
            Text(label, color = Color.Black, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
        }
        TextField(
            value = value, onValueChange = onChange,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White, unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                focusedIndicatorColor = Color.Black, unfocusedIndicatorColor = Color.Gray
            ),
            singleLine = true
        )
    }
}

private class Row5(val a: MutableState<String>, val b: MutableState<String>, val c: MutableState<String>, val d: MutableState<String>, val e: MutableState<String>)
private class Row3(val a: MutableState<String>, val b: MutableState<String>, val c: MutableState<String>)
private class Row4(val a: MutableState<String>, val b: MutableState<String>, val c: MutableState<String>, val d: MutableState<String>)

@Composable
fun ResumeScreen(
    processedSelfie: Bitmap? = null,
    onTakeSelfie: () -> Unit
) {
    val context = LocalContext.current

    var fullName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var civilStatus by remember { mutableStateOf("") }
    var nationality by remember { mutableStateOf("Filipino") }
    var objective by remember { mutableStateOf("") }

    val educLevels = listOf("Elementary", "High School", "Senior High School", "College")
    val educRows = remember { List(4) { Row4(mutableStateOf(""), mutableStateOf(""), mutableStateOf(""), mutableStateOf("")) } }
    val workRows = remember { List(3) { Row5(mutableStateOf(""), mutableStateOf(""), mutableStateOf(""), mutableStateOf(""), mutableStateOf("")) } }
    val skillsRows = remember { List(5) { mutableStateOf("") } }
    val trainingRows = remember { List(3) { Row3(mutableStateOf(""), mutableStateOf(""), mutableStateOf("")) } }
    val referenceRows = remember { List(3) { Row4(mutableStateOf(""), mutableStateOf(""), mutableStateOf(""), mutableStateOf("")) } }

    var previewBitmap by remember { mutableStateOf<Bitmap?>(null) }

    if (previewBitmap != null) {
        Column(modifier = Modifier.fillMaxSize().background(Color.Black).verticalScroll(rememberScrollState()).padding(16.dp)) {
            Text("👁️ Preview", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            Image(bitmap = previewBitmap!!.asImageBitmap(), contentDescription = "Resume preview", modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val saved = GallerySaver.saveBitmap(context, previewBitmap!!, "Resume_${System.currentTimeMillis()}")
                    Toast.makeText(context, if (saved) "Na-save sa Gallery (Pictures/UR Docs)!" else "Hindi na-save, subukan ulit.", Toast.LENGTH_LONG).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("⬇️ I-download / I-save sa Gallery") }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = { previewBitmap = null }, modifier = Modifier.fillMaxWidth()) { Text("✏️ Bumalik sa Form") }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        AndroidView(
            factory = { ctx ->
                AdView(ctx).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = "ca-app-pub-3940256099942544/6300978111"
                    loadAd(AdRequest.Builder().build())
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text("RESUME", color = Color.Black, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))

        Box(modifier = Modifier.size(120.dp).border(2.dp, Color.Black)) {
            if (processedSelfie != null) {
                Image(bitmap = processedSelfie.asImageBitmap(), contentDescription = "2x2 Photo", modifier = Modifier.fillMaxSize())
            } else {
                Text("2x2 PICTURE", color = Color.Black, modifier = Modifier.padding(8.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(onClick = onTakeSelfie, modifier = Modifier.fillMaxWidth()) {
            Text(if (processedSelfie == null) "📸 Kumuha ng 2x2 Selfie" else "📸 Palitan ang Selfie")
        }
        Spacer(modifier = Modifier.height(16.dp))

        PaperField("FULL NAME", fullName) { fullName = it }
        PaperField("ADDRESS", address) { address = it }
        PaperField("CONTACT NUMBER", contactNumber) { contactNumber = it }
        PaperField("EMAIL ADDRESS", email) { email = it }
        PaperField("DATE OF BIRTH", birthDate) { birthDate = it }
        PaperField("AGE", age) { age = it }
        PaperField("CIVIL STATUS", civilStatus) { civilStatus = it }
        PaperField("NATIONALITY", nationality) { nationality = it }

        Spacer(modifier = Modifier.height(16.dp))
        Text("CAREER OBJECTIVE", color = Color.Black, fontWeight = FontWeight.Bold)
        PaperField("", objective) { objective = it }

        Spacer(modifier = Modifier.height(16.dp))
        Text("EDUCATIONAL BACKGROUND", color = Color.Black, fontWeight = FontWeight.Bold)
        educRows.forEachIndexed { i, row ->
            Text(educLevels[i], color = Color.Black, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
            PaperField("Name of School", row.a.value) { row.a.value = it }
            PaperField("Address", row.b.value) { row.b.value = it }
            Row {
                PaperField("Year Graduated", row.c.value, modifier = Modifier.weight(1f)) { row.c.value = it }
                Spacer(modifier = Modifier.width(8.dp))
                PaperField("Honors Received", row.d.value, modifier = Modifier.weight(1f)) { row.d.value = it }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("WORK EXPERIENCE", color = Color.Black, fontWeight = FontWeight.Bold)
        workRows.forEachIndexed { i, row ->
            Text("Entry ${i + 1}", color = Color.Black, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
            PaperField("Company Name", row.a.value) { row.a.value = it }
            PaperField("Position", row.b.value) { row.b.value = it }
            PaperField("Duties and Responsibilities", row.c.value) { row.c.value = it }
            Row {
                PaperField("Date Started", row.d.value, modifier = Modifier.weight(1f)) { row.d.value = it }
                Spacer(modifier = Modifier.width(8.dp))
                PaperField("Date Ended", row.e.value, modifier = Modifier.weight(1f)) { row.e.value = it }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("SKILLS", color = Color.Black, fontWeight = FontWeight.Bold)
        skillsRows.forEachIndexed { i, s -> PaperField("Skill ${i + 1}", s.value) { s.value = it } }

        Spacer(modifier = Modifier.height(16.dp))
        Text("TRAININGS / SEMINARS ATTENDED", color = Color.Black, fontWeight = FontWeight.Bold)
        trainingRows.forEachIndexed { i, row ->
            Text("Entry ${i + 1}", color = Color.Black, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
            PaperField("Title of Training/Seminar", row.a.value) { row.a.value = it }
            PaperField("Sponsor / Organization", row.b.value) { row.b.value = it }
            PaperField("Date Attended", row.c.value) { row.c.value = it }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("CHARACTER REFERENCES", color = Color.Black, fontWeight = FontWeight.Bold)
        referenceRows.forEachIndexed { i, row ->
            Text("Entry ${i + 1}", color = Color.Black, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
            PaperField("Name", row.a.value) { row.a.value = it }
            PaperField("Position", row.b.value) { row.b.value = it }
            PaperField("Company / Organization", row.c.value) { row.c.value = it }
            PaperField("Contact Number", row.d.value) { row.d.value = it }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                if (fullName.isBlank()) {
                    Toast.makeText(context, "Lagyan muna ng Full Name.", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                val data = ResumeFull(
                    fullName = fullName, address = address, contactNumber = contactNumber,
                    email = email, birthDate = birthDate, age = age, civilStatus = civilStatus,
                    nationality = nationality, objective = objective,
                    educRows = educRows.mapIndexed { i, r -> EducRowData(educLevels[i], r.a.value, r.b.value, r.c.value, r.d.value) },
                    workRows = workRows.map { WorkRowData(it.a.value, it.b.value, it.c.value, it.d.value, it.e.value) },
                    skills = skillsRows.map { it.value },
                    trainingRows = trainingRows.map { TrainingRowData(it.a.value, it.b.value, it.c.value) },
                    referenceRows = referenceRows.map { ReferenceRowData(it.a.value, it.b.value, it.c.value, it.d.value) }
                )
                previewBitmap = DocumentRenderer.renderResume(data, processedSelfie)
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("✅ Tapos na") }
    }
}
