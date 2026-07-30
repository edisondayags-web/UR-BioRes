package com.saltech.urdocs.letters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Resignation Letter — fillable A4-style template.
 * Same pattern as BioDataScreen: plain editable text fields, no AI/API call.
 * All text (including the body paragraphs) is editable so the user can
 * reword anything before downloading.
 */

data class ResignationLetterState(
    var fullNameTop: String = "",
    var position: String = "",
    var date: String = "",
    var supervisorName: String = "",
    var companyName: String = "",
    var bodyIntro: String = "I am writing to formally resign from my position as [Your Position], effective [Last Working Day].",
    var bodyThanks: String = "Thank you for the opportunity and support during my time here. I am willing to help with turnover to ensure a smooth transition.",
    var signatureName: String = "",
)

@Composable
fun ResignationLetterScreen(
    state: ResignationLetterState = remember { ResignationLetterState() },
    onDownload: (ResignationLetterState) -> Unit,
    onUpload: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize()) {

        // ---- A4-style scrollable letter body ----
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .background(Color.White)
                .padding(24.dp)
        ) {
            Text(
                text = "RESIGNATION LETTER",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            EditableLine(label = "Full Name", value = state.fullNameTop) { state.fullNameTop = it }
            EditableLine(label = "Position", value = state.position) { state.position = it }

            Spacer(modifier = Modifier.height(16.dp))
            EditableLine(label = "Date", value = state.date) { state.date = it }

            Spacer(modifier = Modifier.height(16.dp))
            EditableLine(label = "Supervisor's Name", value = state.supervisorName) { state.supervisorName = it }
            EditableLine(label = "Company Name", value = state.companyName) { state.companyName = it }

            Spacer(modifier = Modifier.height(20.dp))
            Text("Dear ${state.supervisorName.ifBlank { "[Supervisor's Name]" }},", color = Color.Black)

            Spacer(modifier = Modifier.height(12.dp))
            EditableParagraph(value = state.bodyIntro) { state.bodyIntro = it }

            Spacer(modifier = Modifier.height(12.dp))
            EditableParagraph(value = state.bodyThanks) { state.bodyThanks = it }

            Spacer(modifier = Modifier.height(28.dp))
            Text("Sincerely,", color = Color.Black)
            Spacer(modifier = Modifier.height(32.dp))

            // Left = Full Name, Right = Signature (side by side, not stacked)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    BasicTextField(
                        value = state.signatureName,
                        onValueChange = { state.signatureName = it },
                        textStyle = TextStyle(color = Color.Black, fontSize = 14.sp)
                    )
                    Divider(color = Color.Black, thickness = 1.dp)
                    Text("Full Name", fontSize = 11.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.width(24.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Spacer(modifier = Modifier.height(24.dp)) // blank space for signature
                    Divider(color = Color.Black, thickness = 1.dp)
                    Text("Signature", fontSize = 11.sp, color = Color.Gray)
                }
            }
        }

        // ---- Bottom action bar, same as Bio-Data screen ----
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(onClick = onUpload) {
                Text("Upload")
            }
            Button(onClick = { onDownload(state) }) {
                Text("Download")
            }
        }
    }
}

@Composable
private fun EditableLine(label: String, value: String, onChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(bottom = 10.dp)) {
        BasicTextField(
            value = value,
            onValueChange = onChange,
            textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
            modifier = Modifier.fillMaxWidth()
        )
        Divider(color = Color.Black, thickness = 1.dp)
        Text(label, fontSize = 11.sp, color = Color.Gray)
    }
}

@Composable
private fun EditableParagraph(value: String, onChange: (String) -> Unit) {
    BasicTextField(
        value = value,
        onValueChange = onChange,
        textStyle = TextStyle(color = Color.Black, fontSize = 14.sp, lineHeight = 20.sp),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF7F7F7))
            .padding(8.dp)
    )
}
