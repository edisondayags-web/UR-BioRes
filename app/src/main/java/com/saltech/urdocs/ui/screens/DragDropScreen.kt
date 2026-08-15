package com.saltech.urdocs.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.UUID

// ===================== DATA MODEL (state-backed so drags actually redraw) =====================

private enum class ElementType { TEXT, RECTANGLE, CIRCLE, LINE, STAR, IMAGE }

private class DroppedElement(
    val id: String = UUID.randomUUID().toString(),
    val type: ElementType,
    x: Float,
    y: Float,
    width: Float = 100f,
    height: Float = 60f,
    text: String = "Text",
    colorArgb: Long = 0xFF2A5CE0
) {
    var x by mutableStateOf(x)
    var y by mutableStateOf(y)
    var width by mutableStateOf(width)
    var height by mutableStateOf(height)
    var text by mutableStateOf(text)
    var colorArgb by mutableStateOf(colorArgb)

    fun copyAt(newX: Float, newY: Float) = DroppedElement(
        type = type, x = newX, y = newY, width = width, height = height, text = text, colorArgb = colorArgb
    )
}

private data class DrawerCategory(val label: String, val icon: String, val type: ElementType)

private val drawerCategories = listOf(
    DrawerCategory("Text", "🅰️", ElementType.TEXT),
    DrawerCategory("Rectangle", "⬜", ElementType.RECTANGLE),
    DrawerCategory("Circle", "⚪", ElementType.CIRCLE),
    DrawerCategory("Line", "➖", ElementType.LINE),
    DrawerCategory("Star", "⭐", ElementType.STAR),
    DrawerCategory("Image", "🖼️", ElementType.IMAGE),
)

private val swatchColors = listOf(
    0xFF2A5CE0, 0xFFE0245E, 0xFF00C853, 0xFFFFB300, 0xFF8E24AA, 0xFF000000, 0xFF757575
)

// ===================== MAIN SCREEN =====================

@Composable
fun DragDropScreen(onBack: () -> Unit = {}) {
    var scale by remember { mutableStateOf(1f) }
    var canvasOffset by remember { mutableStateOf(Offset.Zero) }

    var drawerOpen by remember { mutableStateOf(false) }
    var lastInteraction by remember { mutableStateOf(System.currentTimeMillis()) }

    val elements = remember { mutableStateListOf<DroppedElement>() }
    var selectedId by remember { mutableStateOf<String?>(null) }
    val selectedElement = elements.find { it.id == selectedId }

    LaunchedEffect(drawerOpen, lastInteraction) {
        if (drawerOpen) {
            delay(4000)
            if (System.currentTimeMillis() - lastInteraction >= 4000) {
                drawerOpen = false
            }
        }
    }

    fun addElement(type: ElementType) {
        val newEl = DroppedElement(
            type = type,
            x = 100f,
            y = 150f,
            text = if (type == ElementType.TEXT) "Double tap to edit" else ""
        )
        elements.add(newEl)
        selectedId = newEl.id
    }

    fun deleteSelected() {
        elements.removeAll { it.id == selectedId }
        selectedId = null
    }

    fun duplicateSelected() {
        val original = elements.find { it.id == selectedId } ?: return
        val copy = original.copyAt(original.x + 20f, original.y + 20f)
        elements.add(copy)
        selectedId = copy.id
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
                        canvasOffset += pan
                        lastInteraction = System.currentTimeMillis()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(width = 350.dp, height = 495.dp)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = canvasOffset.x,
                        translationY = canvasOffset.y
                    )
                    .background(Color.White)
                    .border(1.dp, Color.Gray.copy(alpha = 0.4f))
                    .pointerInput(Unit) {
                        detectTapGestures { selectedId = null }
                    }
            ) {
                elements.forEach { el ->
                    key(el.id) {
                        DraggableElement(
                            element = el,
                            isSelected = el.id == selectedId,
                            scale = scale,
                            onSelect = { selectedId = el.id; lastInteraction = System.currentTimeMillis() },
                            onMove = { dx, dy ->
                                el.x += dx / scale
                                el.y += dy / scale
                                lastInteraction = System.currentTimeMillis()
                            },
                            onResize = { dw, dh ->
                                el.width = (el.width + dw / scale).coerceAtLeast(24f)
                                el.height = (el.height + dh / scale).coerceAtLeast(24f)
                                lastInteraction = System.currentTimeMillis()
                            }
                        )
                    }
                }
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

        // ===== SELECTED ELEMENT TOOLBAR (Delete / Duplicate) =====
        if (selectedElement != null) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 12.dp, end = 12.dp)
                    .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = { duplicateSelected() }) {
                    Icon(Icons.Filled.ContentCopy, contentDescription = "Duplicate", tint = Color.White)
                }
                IconButton(onClick = { deleteSelected() }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color(0xFFFF4D4D))
                }
            }
        }

        // ===== ">" DRAWER TAB =====
        if (!drawerOpen) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(top = 220.dp)
                    .background(Color(0xFF2A2A2A), RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                    .pointerInput(Unit) {
                        detectTapGestures {
                            drawerOpen = true
                            lastInteraction = System.currentTimeMillis()
                        }
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
                        "Elements — tap to add",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(drawerCategories) { cat ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .padding(6.dp)
                                    .pointerInput(cat.type) {
                                        detectTapGestures {
                                            addElement(cat.type)
                                            lastInteraction = System.currentTimeMillis()
                                        }
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

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .background(Color(0xFF2A2A2A), RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                        .pointerInput(Unit) {
                            detectTapGestures { drawerOpen = false }
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
                .padding(bottom = 80.dp)
                .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        )

        // ===== PROPERTIES PANEL (bottom sheet-style, shows when element selected) =====
        AnimatedVisibility(
            visible = selectedElement != null,
            enter = slideInVertically(animationSpec = tween(200)) { it },
            exit = slideOutVertically(animationSpec = tween(200)) { it },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            selectedElement?.let { el ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF202020), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .padding(12.dp)
                ) {
                    if (el.type == ElementType.TEXT) {
                        OutlinedTextField(
                            value = el.text,
                            onValueChange = { el.text = it },
                            label = { Text("Text", color = Color.White.copy(alpha = 0.6f)) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF2A5CE0),
                                unfocusedBorderColor = Color.Gray
                            )
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                    if (el.type != ElementType.TEXT) {
                        Text("Color", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                        Spacer(Modifier.height(6.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(swatchColors) { c ->
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(Color(c), CircleShape)
                                        .then(
                                            if (el.colorArgb == c) Modifier.border(2.dp, Color.White, CircleShape)
                                            else Modifier
                                        )
                                        .clickable { el.colorArgb = c }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ===================== DRAGGABLE ELEMENT RENDERER =====================

@Composable
private fun DraggableElement(
    element: DroppedElement,
    isSelected: Boolean,
    scale: Float,
    onSelect: () -> Unit,
    onMove: (Float, Float) -> Unit,
    onResize: (Float, Float) -> Unit
) {
    val density = LocalDensity.current
    val widthDp = with(density) { element.width.toDp() }
    val heightDp = with(density) { element.height.toDp() }
    val xDp = with(density) { element.x.toDp() }
    val yDp = with(density) { element.y.toDp() }

    Box(
        modifier = Modifier
            .offset(x = xDp, y = yDp)
            .size(width = widthDp, height = heightDp)
            .then(
                if (isSelected) Modifier.border(2.dp, Color(0xFF2A5CE0)) else Modifier
            )
            .pointerInput(element.id) {
                detectTapGestures { onSelect() }
            }
            .pointerInput(element.id) {
                detectDragGestures(
                    onDragStart = { onSelect() },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onMove(dragAmount.x, dragAmount.y)
                    }
                )
            }
    ) {
        when (element.type) {
            ElementType.TEXT -> Text(
                element.text,
                color = Color.Black,
                fontSize = 14.sp,
                modifier = Modifier.padding(4.dp)
            )
            ElementType.RECTANGLE -> Box(
                Modifier
                    .fillMaxSize()
                    .background(Color(element.colorArgb).copy(alpha = 0.85f))
            )
            ElementType.CIRCLE -> Box(
                Modifier
                    .fillMaxSize()
                    .background(Color(element.colorArgb).copy(alpha = 0.85f), CircleShape)
            )
            ElementType.LINE -> Box(
                Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .align(Alignment.CenterStart)
                    .background(Color(element.colorArgb))
            )
            ElementType.STAR -> Text("⭐", fontSize = 32.sp, modifier = Modifier.align(Alignment.Center))
            ElementType.IMAGE -> Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text("🖼️", fontSize = 24.sp)
            }
        }

        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(18.dp)
                    .background(Color(0xFF2A5CE0), CircleShape)
                    .pointerInput(element.id) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            onResize(dragAmount.x, dragAmount.y)
                        }
                    }
            )
        }
    }
}
