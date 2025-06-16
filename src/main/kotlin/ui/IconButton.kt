package ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import resources.AppColors
import resources.AppDimensions

@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    tint: Color = AppColors.OnSurfaceVariant,
    contentDescription: String,
    onClick: () -> Unit
) {
    Tooltip(contentDescription) {
        Icon(
            modifier = modifier
                .size(AppDimensions.IconButtonSize)
                .clip(RoundedCornerShape(AppDimensions.SmallRadius))
                .clickable { onClick() }
                .padding(AppDimensions.TinyPadding),
            imageVector = imageVector,
            tint = tint,
            contentDescription = contentDescription
        )
    }
}