package com.saltech.urdocs.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

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
        content = content
    )
}
