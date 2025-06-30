package presentation.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import resources.AppColors
import resources.AppDimensions
import resources.AppStyles
import resources.alignCenter

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
                style = AppStyles.REGULAR_ITALIC_SERIF,
                color = AppColors.OnSurfaceVariant
            )
        },
        content = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppDimensions.SmallPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "0",
                    style = AppStyles.SMALL_ITALIC_SERIF.alignCenter()
                )

                Column(
                    modifier = Modifier.weight(14f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = String.format("%.2f", value),
                        style = AppStyles.REGULAR_ITALIC_SERIF
                    )
                    Slider(
                        enabled = sliderEnabled,
                        value = value,
                        onValueChange = { onValueChange(it) },
                    )
                }

                Text(
                    modifier = Modifier.weight(1f),
                    text = "1",
                    style = AppStyles.SMALL_ITALIC_SERIF.alignCenter()
                )
            }
        }
    )
}