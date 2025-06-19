package presentation.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import resources.AppDimensions

@Composable
fun SidebarCard(
    modifier: Modifier = Modifier,
    leadingContainerColor: Color,
    leadingContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            Modifier
                .background(leadingContainerColor)
                .fillMaxHeight()
                .padding(horizontal = AppDimensions.SmallPadding)
                .width(18.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingContent()
        }

        Row(
            Modifier
                .background(leadingContainerColor.copy(alpha = 0.2f))
                .padding(horizontal = AppDimensions.SmallPadding)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            content()
        }
    }
}