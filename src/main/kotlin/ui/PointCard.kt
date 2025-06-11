package ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp.Companion.Unspecified
import androidx.compose.ui.unit.dp
import data.EditablePoint
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

    val headerColor = if (isFocused) AppColors.Primary else AppColors.SurfaceVariant
    val textColor = if (isFocused) AppColors.OnPrimary else AppColors.OnSurfaceVariant
    val dividerWidth = if (isFocused) 1.dp else Unspecified

    Row(
        modifier = modifier.border(BorderStroke(dividerWidth, headerColor)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            Modifier
                .background(headerColor)
                .fillMaxHeight()
                .padding(horizontal = AppDimensions.SmallPadding)
                .wrapContentWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
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

        Row(
            Modifier
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



