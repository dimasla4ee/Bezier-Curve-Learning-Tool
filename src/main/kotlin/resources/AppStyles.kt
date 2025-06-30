package resources

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign

object AppStyles {
    val REGULAR_ITALIC_SERIF = TextStyle.Default.copy(
        textAlign = TextAlign.Left,
        fontStyle = FontStyle.Italic,
        fontFamily = FontFamily.Serif,
        fontSize = AppDimensions.RegularText
    )

    val SMALL_ITALIC_SERIF = REGULAR_ITALIC_SERIF.copy(
        fontSize = AppDimensions.SmallText
    )
}

fun TextStyle.alignCenter(): TextStyle = copy(textAlign = TextAlign.Center)