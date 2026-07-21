package com.saltech.urdocs.ui.theme

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale

/**
 * Reusable "press" effect -- gumagamit sa lahat ng clickable cards/buttons.
 * Bumabaon nang konti (scale down) pag pinindot, bumabalik nang bouncy pag binitawan.
 * Gamit: Modifier.pressScale(interactionSource).clickable { ... }
 */
@Composable
fun Modifier.pressScale(interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }): Modifier {
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "pressScale"
    )
    return this.scale(scale)
}
