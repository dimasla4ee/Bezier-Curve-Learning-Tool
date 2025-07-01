package presentation.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.mahozad.multiplatform.wavyslider.WaveDirection
import ir.mahozad.multiplatform.wavyslider.material3.WavySlider
import presentation.components.Tooltip
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
            Tooltip(
                tooltipText = "Параметр интерполяции",
                content = {
                    Text(
                        text = "t",
                        style = AppStyles.REGULAR_ITALIC_SERIF,
                        color = AppColors.OnSurfaceVariant
                    )
                }
            )
        },
        content = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppDimensions.SmallPadding),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "0",
                    style = AppStyles.SMALL_SERIF.alignCenter()
                )

                Column(
                    modifier = Modifier.weight(14f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = String.format("%.2f", value).replace('.', ','),
                        style = AppStyles.SMALL_SERIF
                    )

                    WavySlider(
                        value = value,
                        onValueChange = onValueChange,
                        enabled = sliderEnabled,
                        waveHeight = 0.dp,
                        waveVelocity = 0.dp to WaveDirection.TAIL,
                        waveThickness = 6.dp,
                        trackThickness = 6.dp,
                        colors = SliderDefaults.colors(
                            thumbColor = AppColors.Primary,
                            activeTrackColor = AppColors.Primary
                        )
                    )
                }

                Text(
                    modifier = Modifier.weight(1f),
                    text = "1",
                    style = AppStyles.SMALL_SERIF.alignCenter()
                )
            }
        }
    )
}