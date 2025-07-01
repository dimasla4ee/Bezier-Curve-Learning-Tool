package resources

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign

object AppStyles {
    val REGULAR_SERIF = TextStyle.Default.copy(
        textAlign = TextAlign.Left,
        fontFamily = FontFamily.Serif,
        fontSize = AppDimensions.RegularText
    )

    val SMALL_SERIF = REGULAR_SERIF.copy(
        fontSize = AppDimensions.SmallText
    )

    val TINY_SERIF = REGULAR_SERIF.copy(
        fontSize = AppDimensions.TinyText
    )

    val REGULAR_ITALIC_SERIF = REGULAR_SERIF.copy(
        fontStyle = FontStyle.Italic
    )

    val SMALL_ITALIC_SERIF = SMALL_SERIF.copy(
        fontStyle = FontStyle.Italic
    )
}

fun TextStyle.alignCenter(): TextStyle = copy(textAlign = TextAlign.Center)