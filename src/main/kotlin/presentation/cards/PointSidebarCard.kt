package presentation.cards

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import domain.model.EditablePoint
import presentation.components.IconButton
import presentation.components.IndexedText
import presentation.components.NumericTextField
import presentation.components.Tooltip
import resources.AppColors
import resources.AppDimensions

@Composable
fun PointSidebarCard(
    modifier: Modifier = Modifier,
    pointIndex: Int,
    point: EditablePoint,
    onXChange: (String) -> Unit,
    onYChange: (String) -> Unit,
    onClickClose: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isError = !point.isValid

    val leadingContainerColor = when {
        isFocused -> AppColors.Primary
        isError -> AppColors.Error
        else -> AppColors.SurfaceVariant
    }
    val textColor = if (isFocused || isError) AppColors.OnPrimary else AppColors.OnSurfaceVariant

    SidebarCard(
        modifier = modifier,
        leadingContainerColor = leadingContainerColor,
        leadingContent = {
            if (isError) {
                Tooltip("Введите вещественное значение для x и y") {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Outlined.Warning,
                        tint = textColor,
                        contentDescription = "Error"
                    )
                }
            } else {
                IndexedText(
                    text = "P",
                    index = pointIndex,
                    color = textColor,
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Serif,
                    fontSize = AppDimensions.RegularText,
                    indexFontSize = AppDimensions.SmallText
                )
            }
        },
        content = {
            Text(
                "x =",
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Serif,
                fontSize = AppDimensions.RegularText
            )
            NumericTextField(
                modifier = Modifier.width(AppDimensions.TextFieldWidth),
                value = point.xInput,
                interactionSource = interactionSource,
                onValueChange = onXChange
            )
            Text(
                "y = ",
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Serif,
                fontSize = AppDimensions.RegularText
            )
            NumericTextField(
                modifier = Modifier.width(AppDimensions.TextFieldWidth),
                value = point.yInput,
                interactionSource = interactionSource,
                onValueChange = onYChange
            )
            IconButton(
                imageVector = Icons.Default.Close,
                tooltipText = "Удалить точку",
                onClick = { onClickClose() }
            )
        }
    )
}