package presentation.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
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
        textStyle = AppStyles.SMALL_SERIF,
        singleLine = true,
        interactionSource = interactionSource,
        visualTransformation = object : VisualTransformation {

            override fun filter(text: AnnotatedString): TransformedText {
                val offsetMapping = object : OffsetMapping {
                    override fun originalToTransformed(offset: Int): Int = offset
                    override fun transformedToOriginal(offset: Int): Int = offset
                }

                val newText = AnnotatedString(
                    text.toString()
                        .replace('-', '−')
                        .replace('.', ',')
                )

                return TransformedText(newText, offsetMapping)
            }
        }
    )
}