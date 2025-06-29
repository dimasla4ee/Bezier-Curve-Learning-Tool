package presentation.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import resources.AppColors
import resources.AppDimensions

@Composable
fun InterpolationSidebarCard(
    modifier: Modifier = Modifier,
    sliderEnabled: Boolean,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    SidebarCard(
        modifier = modifier,
        leadingContainerColor = AppColors.SurfaceVariant,
        leadingContent = {
            Text(
                text = "t",
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Serif,
                fontSize = AppDimensions.RegularText,
                color = AppColors.OnSurfaceVariant
            )
        },
        content = {
            Text(
                text = "0",
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Serif,
                fontSize = AppDimensions.SmallText
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = String.format("%.2f", value),
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Serif,
                    fontSize = AppDimensions.RegularText
                )
                Slider(
                    enabled = sliderEnabled,
                    value = value,
                    onValueChange = { onValueChange(it) },
                )
            }

            Text(
                text = "1",
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Serif,
                fontSize = AppDimensions.SmallText
            )
        }
    )
}