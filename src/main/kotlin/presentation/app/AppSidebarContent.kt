package presentation.app

import androidx.compose.foundation.layout.*
import androidx.compose.material.DrawerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.MainViewModel
import kotlinx.coroutines.launch
import presentation.cards.InterpolationSidebarCard
import presentation.cards.PointSidebarCard
import presentation.cards.PreferencesSidebarCard
import presentation.components.HorizontalDivider
import presentation.components.IconButton
import resources.AppDimensions

@Composable
fun AppSidebarContent(
    viewModel: MainViewModel,
    drawerState: DrawerState
) {
    val coroutineScope = rememberCoroutineScope()
    val points = remember { viewModel.points }
    val settings = remember { viewModel.settings }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = AppDimensions.SmallRadius,
                vertical = AppDimensions.SmallRadius
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            imageVector = Icons.Default.Add,
            tooltipText = "Добавить точку",
            onClick = { points.addPoint() }
        )

        IconButton(
            imageVector = Icons.AutoMirrored.Filled.MenuOpen,
            tooltipText = "Свернуть меню",
            onClick = { coroutineScope.launch { drawerState.close() } }
        )
    }

    HorizontalDivider(thickness = 1.5.dp)

    PreferencesSidebarCard(
        modifier = Modifier
            .width(249.dp)
            .height(50.dp),
        showEquation = settings.showEquation,
        showSupportLine = settings.showSupportLine,
        onShowFormulaChange = { settings.updateShowEquation(it) },
        onShowSupportLineChange = { settings.updateShowSupportLine(it) }
    )

    HorizontalDivider()

    InterpolationSidebarCard(
        modifier = Modifier
            .width(249.dp)
            .height(50.dp),
        value = points.interpolation,
        onValueChange = { points.updateInterpolation(it) }
    )

    HorizontalDivider()

    points.controlPoints.forEachIndexed { index, point ->
        PointSidebarCard(
            modifier = Modifier
                .width(249.dp)
                .height(50.dp),
            point = point,
            pointIndex = index,
            onXChange = { points.updateX(index, it) },
            onYChange = { points.updateY(index, it) },
            onClickClose = { points.removePoint(index) }
        )
        HorizontalDivider()
    }
}