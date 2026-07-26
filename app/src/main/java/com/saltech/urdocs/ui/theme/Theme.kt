package com.saltech.urdocs.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import com.saltech.urdocs.ui.components.MatrixRainBackground

private val UrDocsColorScheme = darkColorScheme(
    primary = UrPink,
    secondary = UrNeon,
    background = UrBlack,
    surface = UrGray,
    onPrimary = UrWhite,
    onBackground = UrWhite,
    onSurface = UrWhite
)

@Composable
fun UrDocsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = UrDocsColorScheme,
        typography = UrDocsTypography,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            MatrixRainBackground(modifier = Modifier.fillMaxSize())
            content()
        }
    }
}
