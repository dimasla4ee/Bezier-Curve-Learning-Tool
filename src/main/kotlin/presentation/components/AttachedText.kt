package presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AttachedText(
    modifier: Modifier = Modifier,
    primaryText: String,
    secondaryText: String,
    verticalShift: Dp = 0.dp,
    textStyle: TextStyle = LocalTextStyle.current,
    indexStyle: TextStyle = LocalTextStyle.current
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = primaryText,
            style = textStyle
        )

        Text(
            modifier = Modifier.offset(y = verticalShift),
            text = secondaryText,
            style = indexStyle
        )
    }
}