package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.saltech.urdocs.model.LetterRequest
import com.saltech.urdocs.model.LetterType
import com.saltech.urdocs.viewmodel.LettersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LettersScreen(
    viewModel: LettersViewModel = viewModel()
) {
    var selectedType by remember { mutableStateOf(LetterType.LEAVE) }
    var fullName by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var dateNeeded by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("✉️ Letter Generator", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            OutlinedTextField(
                value = selectedType.label,
                onValueChange = {},
                readOnly = true,
                label = { Text("Klase ng Letter") },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                LetterType.entries.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.label) },
                        onClick = { selectedType = type; expanded = false }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = fullName, onValueChange = { fullName = it },
            label = { Text("Buong Pangalan") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = position, onValueChange = { position = it },
            label = { Text("Position (optional)") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = company, onValueChange = { company = it },
            label = { Text("Company / Office (optional)") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = reason, onValueChange = { reason = it },
            label = { Text("Rason") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = dateNeeded, onValueChange = { dateNeeded = it },
            label = { Text("Petsa (hal. July 20, 2026)") }, modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                viewModel.generate(
                    LetterRequest(
                        type = selectedType,
                        fullName = fullName,
                        position = position,
                        company = company,
                        reason = reason,
                        dateNeeded = dateNeeded
                    )
                )
            },
            enabled = !uiState.isLoading && fullName.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (uiState.isLoading) "Ginagawa..." else "Generate Letter")
        }

        uiState.error?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text("⚠️ $it", color = MaterialTheme.colorScheme.error)
        }

        uiState.generatedLetter?.let { letter ->
            Spacer(modifier = Modifier.height(20.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = letter,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
