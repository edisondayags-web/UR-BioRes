package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BioDataMoreTemplatesScreen(
    onTemplateSelected: (String) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val templateNames = (1..20).map { "biodata_template_" + it.toString().padStart(2, '0') }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0B1530))) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(Modifier.width(8.dp))
                Text("Choose a Template", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(templateNames, key = { it }) { name ->
                    val resId = context.resources.getIdentifier(name, "drawable", context.packageName)
                    if (resId != 0) {
                        Image(
                            painter = painterResource(id = resId),
                            contentDescription = name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(6.dp)
                                .aspectRatio(0.75f)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                .clickable { onTemplateSelected(name) }
                        )
                    }
                }
            }
        }
    }
}
