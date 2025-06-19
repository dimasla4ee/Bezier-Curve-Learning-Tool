package app

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import domain.MainViewModel
import kotlinx.coroutines.launch
import presentation.cards.InterpolationSidebarCard
import presentation.cards.PointSidebarCard
import presentation.cards.PreferencesSidebarCard
import presentation.components.DismissibleDrawerCard
import presentation.components.IconButton
import presentation.graph.BezierCurveGraph
import presentation.graph.POINT_RADIUS
import resources.AppColors
import resources.AppDimensions
import utils.modelToScreen
import utils.screenToModel
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
    val coroutineScope = rememberCoroutineScope()
    var isDraggingGraph by remember { mutableStateOf(false) }
    var isDraggingPoint by remember { mutableStateOf(false) }
    var draggedPointIndex: Int? by remember { mutableStateOf(null) }

    Surface(Modifier.fillMaxSize(), color = AppColors.Base) {
        DismissibleDrawerCard(
            modifier = Modifier.padding(AppDimensions.MediumPadding),
            drawerState = drawerState,
            drawerElevation = 0.dp,
            drawerContent = {
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
                        onClick = { viewModel.points.addPoint() }
                    )

                    IconButton(
                        imageVector = Icons.AutoMirrored.Filled.MenuOpen,
                        tooltipText = "Свернуть меню",
                        onClick = { coroutineScope.launch { drawerState.close() } }
                    )
                }

                Divider(Modifier.fillMaxWidth(), thickness = 1.5.dp, color = AppColors.Divider)

                PreferencesSidebarCard(
                    modifier = Modifier
                        .width(249.dp)
                        .height(50.dp),
                    showFormula = viewModel.settings.showFormula,
                    showSupportLine = viewModel.settings.showSupportLine,
                    onShowFormulaChange = { viewModel.settings.updateShowFormula(it) },
                    onShowSupportLineChange = { viewModel.settings.updateShowSupportLine(it) }
                )

                Divider(Modifier.fillMaxWidth(), thickness = 1.5.dp, color = AppColors.Divider)

                InterpolationSidebarCard(
                    modifier = Modifier
                        .width(249.dp)
                        .height(50.dp),
                    value = viewModel.points.interpolation,
                    onValueChange = { viewModel.points.updateInterpolation(it) }
                )

                Divider(Modifier.fillMaxWidth(), color = AppColors.Divider)

                viewModel.points.controlPoints.forEachIndexed { index, point ->
                    PointSidebarCard(
                        modifier = Modifier
                            .width(249.dp)
                            .height(50.dp),
                        point = point,
                        pointIndex = index,
                        onXChange = { viewModel.points.updateX(index, it) },
                        onYChange = { viewModel.points.updateY(index, it) },
                        onClickClose = { viewModel.points.removePoint(index) }
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
                    modifier = Modifier
                        .onSizeChanged { viewModel.graph.updateCanvasSize(it) }
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent()
                                    val change = event.changes.first()
                                    val logicalPoint = screenToModel(
                                        change.position,
                                        viewModel.graph.origin,
                                        viewModel.graph.scaledCellSize
                                    )

                                    isDraggingGraph = event.buttons.isTertiaryPressed
                                    isDraggingPoint = event.buttons.isPrimaryPressed

                                    when (event.type) {
                                        PointerEventType.Press -> {
                                            if (event.buttons.isPrimaryPressed) {
                                                draggedPointIndex = viewModel.points.pointPressed(
                                                    logicalPoint,
                                                    0.2f
                                                )
                                                println(logicalPoint)
                                                if (draggedPointIndex == null) {
                                                    viewModel.points.addPointAt(logicalPoint)
                                                }
                                            } else if (event.buttons.isSecondaryPressed) {
                                                viewModel.points.removePointAt(logicalPoint)
                                            }
                                        }

                                        PointerEventType.Move -> {
                                            if (isDraggingGraph) {
                                                val delta = change.position - change.previousPosition
                                                viewModel.graph.updatePanningOffset(delta)
                                            } else if (isDraggingPoint) {
                                                viewModel.points.movePoint(draggedPointIndex, logicalPoint)
                                            }
                                        }

                                        PointerEventType.Scroll -> {
                                            val delta = change.scrollDelta.y
                                            viewModel.graph.updateScale(delta)
                                        }
                                    }
                                }
                            }
                        },
                    gridColor = AppColors.Divider,
                    cellSize = viewModel.graph.scaledCellSize,
                    panOffset = viewModel.graph.panningOffset,
                    controlPoints = viewModel.points.getControlPointOffsets().map { position ->
                        modelToScreen(
                            position,
                            viewModel.graph.origin,
                            viewModel.graph.scaledCellSize
                        )
                    },
                    pointRadius = POINT_RADIUS,
                    graphPoints = viewModel.points.getGraphPointOffsets().map { position ->
                        modelToScreen(
                            position,
                            viewModel.graph.origin,
                            viewModel.graph.scaledCellSize
                        )
                    }
                )

                if (drawerState.isClosed) {
                    IconButton(
                        modifier = Modifier.absolutePadding(
                            left = AppDimensions.MediumPadding,
                            top = AppDimensions.MediumPadding
                        ),
                        imageVector = Icons.AutoMirrored.Filled.MenuOpen,
                        tooltipText = "Раскрыть меню",
                        onClick = { coroutineScope.launch { drawerState.open() } }
                    )
                }
            }
        }
    }
}