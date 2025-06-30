package presentation.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
        Box(
            modifier = Modifier
                .background(leadingContainerColor)
                .fillMaxHeight()
                .defaultMinSize(minWidth = 34.dp),
            contentAlignment = Alignment.Center
        ) {
            leadingContent()
        }

        Box(
            modifier = Modifier
                .background(leadingContainerColor.copy(alpha = 0.2f))
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}