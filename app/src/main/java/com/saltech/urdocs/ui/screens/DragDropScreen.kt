package com.saltech.urdocs.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.max
import kotlin.math.min

private data class DrawerCategory(val label: String, val icon: String)

private val drawerCategories = listOf(
    DrawerCategory("Elements", "🧩"),
    DrawerCategory("Icons", "⭐"),
    DrawerCategory("Fonts", "🅰️"),
    DrawerCategory("Signs", "➡️"),
    DrawerCategory("Badges", "🏷️"),
    DrawerCategory("Shapes", "⬜"),
    DrawerCategory("Images", "🖼️"),
    DrawerCategory("Backgrounds", "🎨"),
)

@Composable
fun DragDropScreen(onBack: () -> Unit = {}) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    var drawerOpen by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(drawerCategories.first()) }
    var lastInteraction by remember { mutableStateOf(System.currentTimeMillis()) }

    // Auto-hide drawer after 4 seconds of no interaction
    LaunchedEffect(drawerOpen, lastInteraction) {
        if (drawerOpen) {
            delay(4000)
            if (System.currentTimeMillis() - lastInteraction >= 4000) {
                drawerOpen = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        // ===== BONDPAPER CANVAS (pinch zoom + pan) =====
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(0.5f, 5f)
                        offset += pan
                        lastInteraction = System.currentTimeMillis()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(width = 350.dp, height = 495.dp) // A4-ish ratio bondpaper
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    )
                    .background(Color.White)
                    .border(1.dp, Color.Gray.copy(alpha = 0.4f))
            ) {
                // Empty bondpaper — dropped elements will render here later
            }
        }

        // ===== TOP BAR (back button) =====
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
        }

        // ===== ">" DRAWER TAB =====
        if (!drawerOpen) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(top = 220.dp)
                    .background(Color(0xFF2A2A2A), RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                    .clickableTab {
                        drawerOpen = true
                        lastInteraction = System.currentTimeMillis()
                    }
                    .padding(vertical = 16.dp, horizontal = 6.dp)
            ) {
                Icon(Icons.Filled.ChevronRight, contentDescription = "Open elements drawer", tint = Color.White)
            }
        }

        // ===== ELEMENTS DRAWER (slides in from left) =====
        AnimatedVisibility(
            visible = drawerOpen,
            enter = slideInHorizontally(animationSpec = tween(250)) { -it },
            exit = slideOutHorizontally(animationSpec = tween(250)) { -it },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = 60.dp, bottom = 60.dp)
            ) {
                Column(
                    modifier = Modifier
                        .width(260.dp)
                        .fillMaxHeight()
                        .background(Color(0xFF202020), RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        "Elements",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Category chips row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF151515), RoundedCornerShape(10.dp))
                            .padding(4.dp)
                    ) {
                        // Simple scrollable-less first few categories; can be made scrollable later
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 8.dp)
                    ) {
                        items(drawerCategories) { cat ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .padding(6.dp)
                                    .clickableTab {
                                        selectedCategory = cat
                                        lastInteraction = System.currentTimeMillis()
                                    }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(Color(0xFF2A2A2A), RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(cat.icon, fontSize = 20.sp)
                                }
                                Spacer(Modifier.height(4.dp))
                                Text(cat.label, color = Color.White.copy(alpha = 0.8f), fontSize = 9.sp)
                            }
                        }
                    }
                }

                // Close tab attached to drawer edge
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .background(Color(0xFF2A2A2A), RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                        .clickableTab {
                            drawerOpen = false
                        }
                        .padding(vertical = 16.dp, horizontal = 6.dp)
                ) {
                    Icon(Icons.Filled.ChevronRight, contentDescription = "Close drawer", tint = Color.White)
                }
            }
        }

        // ===== ZOOM INDICATOR =====
        Text(
            "${(scale * 100).toInt()}%",
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 11.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

private fun Modifier.clickableTab(onClick: () -> Unit): Modifier = this.then(
    Modifier.pointerInput(Unit) {
        detectTapGesturesSimple(onClick)
    }
)

private suspend fun androidx.compose.ui.input.pointer.PointerInputScope.detectTapGesturesSimple(onClick: () -> Unit) {
    androidx.compose.foundation.gestures.detectTapGestures(onTap = { onClick() })
}
