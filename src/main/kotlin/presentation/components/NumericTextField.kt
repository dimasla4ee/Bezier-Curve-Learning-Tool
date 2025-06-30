package presentation.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import resources.AppStyles

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
        textStyle = AppStyles.SMALL_ITALIC_SERIF,
        singleLine = true,
        interactionSource = interactionSource
    )
}