package com.saltech.urdocs.ui.templates.biodata

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit

@Composable
fun EditableText_Bio(
    value: String,
    color: Color,
    fontSize: TextUnit,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(color = color, fontSize = fontSize),
        cursorBrush = SolidColor(color),
        maxLines = 1,
        modifier = modifier
    )
}
