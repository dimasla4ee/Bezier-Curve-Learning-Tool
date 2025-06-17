package presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import domain.model.EditablePoint
import resources.AppColors
import resources.AppDimensions

@Composable
fun PointCard(
    modifier: Modifier = Modifier,
    pointIndex: Int,
    point: EditablePoint,
    onXChange: (String) -> Unit,
    onYChange: (String) -> Unit,
    onClickClose: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isError = point.toOffset() == null

    val headerColor = when {
        isFocused -> AppColors.Primary
        isError -> AppColors.Error
        else -> AppColors.SurfaceVariant
    }
    val textColor = if (isFocused || isError) AppColors.OnPrimary else AppColors.OnSurfaceVariant

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            Modifier
                .background(headerColor)
                .fillMaxHeight()
                .padding(horizontal = AppDimensions.SmallPadding)
                .width(18.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
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
        }

        Row(
            Modifier
                .background(headerColor.copy(alpha = 0.2f))
                .padding(horizontal = AppDimensions.SmallPadding)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
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
            Tooltip("Удалить точку") {
                Icon(
                    modifier = Modifier
                        .size(AppDimensions.IconButtonSize)
                        .clip(RoundedCornerShape(AppDimensions.SmallRadius))
                        .clickable { onClickClose() }
                        .padding(AppDimensions.TinyPadding),
                    imageVector = Icons.Default.Close,
                    tint = AppColors.OnSurfaceVariant,
                    contentDescription = "Delete point"
                )
            }
        }
    }
}



