package ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign

@Composable
fun NumericTextField(
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource,
    value: String,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = { onValueChange(it) },
        textStyle = TextStyle.Default.copy(
            textAlign = TextAlign.Left,
            fontStyle = FontStyle.Italic,
            fontFamily = FontFamily.Serif
        ),
        singleLine = true,
        interactionSource = interactionSource
    )
}