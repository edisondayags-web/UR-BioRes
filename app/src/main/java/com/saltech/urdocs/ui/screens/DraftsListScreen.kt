package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.saltech.urdocs.drafts.DraftManager
import com.saltech.urdocs.drafts.DraftEntry
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DraftsListScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var drafts by remember { mutableStateOf(DraftManager.getAllDrafts(context)) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text("Mga Draft", style = MaterialTheme.typography.titleLarge)
        }
        Spacer(Modifier.height(12.dp))
        if (drafts.isEmpty()) {
            Text("Wala ka pang naka-save na draft.")
        } else {
            LazyColumn {
                items(drafts) { draft: DraftEntry ->
                    Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                        Row(
                            Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(draft.label, style = MaterialTheme.typography.titleMedium)
                                Text(
                                    draft.type + " • " + SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()).format(Date(draft.timestamp)),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            IconButton(onClick = {
                                DraftManager.deleteDraft(context, draft.id)
                                drafts = DraftManager.getAllDrafts(context)
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}
