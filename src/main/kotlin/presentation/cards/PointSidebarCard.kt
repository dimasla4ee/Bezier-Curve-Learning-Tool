package presentation.cards

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.model.EditablePoint
import presentation.components.AttachedText
import presentation.components.IconButton
import presentation.components.NumericTextField
import presentation.components.Tooltip
import resources.AppColors
import resources.AppDimensions
import resources.AppStyles

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
                AttachedText(
                    primaryText = "P",
                    secondaryText = pointIndex.toString(),
                    textStyle = AppStyles.REGULAR_ITALIC_SERIF.copy(textColor),
                    indexStyle = AppStyles.TINY_SERIF.copy(textColor),
                    verticalShift = 2.dp
                )
            }
        },
        content = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppDimensions.SmallPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NumericTextFieldWithPrefix(
                    prefix = "x =",
                    value = point.xInput,
                    interactionSource = interactionSource,
                    onChange = onXChange
                )

                NumericTextFieldWithPrefix(
                    prefix = "y =",
                    value = point.yInput,
                    interactionSource = interactionSource,
                    onChange = onYChange
                )

                IconButton(
                    imageVector = Icons.Default.Close,
                    tooltipText = "Удалить точку",
                    onClick = { onClickClose() }
                )
            }
        }
    )
}

@Composable
fun NumericTextFieldWithPrefix(
    prefix: String,
    value: String,
    interactionSource: MutableInteractionSource,
    onChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxHeight()
            .wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = prefix,
            style = AppStyles.REGULAR_ITALIC_SERIF
        )
        NumericTextField(
            modifier = Modifier.width(AppDimensions.TextFieldWidth),
            value = value,
            interactionSource = interactionSource,
            onValueChange = onChange
        )
    }
}