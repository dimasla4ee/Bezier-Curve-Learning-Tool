import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import data.MainViewModel
import kotlinx.coroutines.launch
import resources.AppColors
import resources.AppDimensions
import ui.BezierCurveGraph
import ui.DismissibleDrawerCard
import ui.PointCard
import ui.Tooltip
import java.awt.Dimension

fun main() = singleWindowApplication(
    title = "Bezier Curve Learning Tool"
) {
    window.minimumSize = Dimension(900, 600)

    App()
}

@Composable
@Preview
fun App() {
    val viewModel = remember { MainViewModel() }
    val drawerState = DrawerState(DrawerValue.Open)
    val scope = rememberCoroutineScope()

    Surface(Modifier.fillMaxSize(), color = AppColors.Base) {
        DismissibleDrawerCard(
            modifier = Modifier.padding(AppDimensions.MediumPadding),
            drawerState = drawerState,
            drawerElevation = 0.dp,
            drawerContent = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppDimensions.SmallRadius, vertical = AppDimensions.SmallRadius),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Tooltip("Добавить точку") {
                        Icon(
                            modifier = Modifier
                                .size(AppDimensions.IconButtonSize)
                                .clip(RoundedCornerShape(AppDimensions.SmallRadius))
                                .clickable { viewModel.addPoint() }
                                .padding(AppDimensions.TinyPadding),
                            imageVector = Icons.Default.Add,
                            tint = AppColors.OnSurfaceVariant,
                            contentDescription = "Add point"
                        )
                    }

                    Tooltip("Свернуть меню") {
                        Icon(
                            modifier = Modifier
                                .size(AppDimensions.IconButtonSize)
                                .clip(RoundedCornerShape(AppDimensions.SmallRadius))
                                .clickable { scope.launch { drawerState.close() } }
                                .padding(AppDimensions.TinyPadding),
                            imageVector = Icons.AutoMirrored.Filled.MenuOpen,
                            tint = AppColors.OnSurfaceVariant,
                            contentDescription = "Collapse drawer"
                        )
                    }
                }

                Divider(
                    Modifier
                        .fillMaxWidth()
                        .height(1.5.dp),
                    color = AppColors.Divider
                )

                viewModel.controlPoints.forEachIndexed { index, point ->
                    PointCard(
                        modifier = Modifier
                            .width(249.dp)
                            .height(50.dp),
                        point = point,
                        pointIndex = index,
                        onXChange = { viewModel.updateX(index, it) },
                        onYChange = { viewModel.updateY(index, it) },
                        onClickClose = { viewModel.removePoint(index) }
                    )
                    Divider(Modifier.fillMaxWidth(), color = AppColors.Divider)
                }
            },
        ) {
            Surface(
                shape = RoundedCornerShape(AppDimensions.MediumRadius),
                border = BorderStroke(1.dp, AppColors.SurfaceDivider)
            ) {
                BezierCurveGraph(
                    gridColor = AppColors.Divider,
                    controlPoints = viewModel.getControlPointsAsOffset(),
                    scale = viewModel.scale,
                    offset = viewModel.panningOffset,
                    onDrag = { viewModel.updatePanningOffset(it) },
                    onScroll = { viewModel.updateScale(it) },
                    graphPoints = viewModel.getGraphPointsAsOffset()
                )

                if (drawerState.isClosed) {
                    Tooltip("Раскрыть меню") {
                        Icon(
                            modifier = Modifier
                                .absolutePadding(left = AppDimensions.MediumPadding, top = AppDimensions.MediumPadding)
                                .size(AppDimensions.IconButtonSize)
                                .clip(RoundedCornerShape(AppDimensions.SmallRadius))
                                .clickable { scope.launch { drawerState.open() } }
                                .padding(AppDimensions.TinyPadding),
                            imageVector = Icons.AutoMirrored.Filled.MenuOpen,
                            tint = AppColors.OnSurfaceVariant,
                            contentDescription = "Open drawer"
                        )
                    }
                }
            }
        }
    }
}