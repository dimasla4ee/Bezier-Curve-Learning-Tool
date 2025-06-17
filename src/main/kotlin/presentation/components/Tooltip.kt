package presentation.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp.Companion.Hairline
import androidx.compose.ui.unit.dp
import resources.AppColors
import resources.AppDimensions

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun Tooltip(
    tooltipText: String,
    content: @Composable () -> Unit
) {
    TooltipArea(
        tooltip = {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = AppColors.Surface,
                border = BorderStroke(Hairline, AppColors.SurfaceDivider)
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = tooltipText,
                    fontSize = AppDimensions.SmallText,
                    fontWeight = FontWeight.Medium,
                    color = AppColors.OnSurface
                )
            }
        },
        content = content
    )
}