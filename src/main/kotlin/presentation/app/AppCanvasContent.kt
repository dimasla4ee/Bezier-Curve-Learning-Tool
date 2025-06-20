package presentation.app

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DrawerState
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import domain.MainViewModel
import domain.model.GraphSettings
import domain.model.PointerDragState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import presentation.components.IconButton
import presentation.graph.BezierCurveGraph
import presentation.graph.POINT_RADIUS
import resources.AppColors
import resources.AppDimensions
import utils.modelToScreen
import utils.screenToModel

@Composable
fun AppCanvasContent(
    viewModel: MainViewModel,
    drawerState: DrawerState,
    pointerDragState: PointerDragState
) {
    val coroutineScope = rememberCoroutineScope()
    val graph = remember { viewModel.graph }
    val points = remember { viewModel.points }
    val settings = remember { viewModel.settings }
    val interpolatedPoint = modelToScreen(
        points.findBezierPoint(),
        graph.origin,
        graph.scaledCellSize
    )
    val graphSettings = GraphSettings(
        interpolatedPoint = interpolatedPoint,
        quadraticSupportLinePoints = points.getQuadraticSupportLinePoints()?.map {
            modelToScreen(
                it,
                graph.origin,
                graph.scaledCellSize
            )
        },
        cubicSupportLinePoints = points.getCubicSupportLinePoints()?.map {
            modelToScreen(
                it,
                graph.origin,
                graph.scaledCellSize
            )
        },
        showEquation = settings.showEquation,
        showSupportLine = settings.showSupportLine
    )
    val playAnimation by remember { settings::playAnimation }

    LaunchedEffect(playAnimation) {
        if (playAnimation) {
            var t = points.interpolation
            var direction = 1

            while (viewModel.settings.playAnimation) {
                delay(16L)

                t += 0.01f * direction

                if (t >= 1f) {
                    t = 1f
                    direction = -1
                } else if (t <= 0f) {
                    t = 0f
                    direction = 1
                }

                points.updateInterpolation(t)
            }
        }
    }


    Surface(
        shape = RoundedCornerShape(AppDimensions.MediumRadius),
        border = BorderStroke(1.dp, AppColors.SurfaceDivider)
    ) {
        BezierCurveGraph(
            modifier = Modifier
                .onSizeChanged { graph.updateCanvasSize(it) }
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes.first()
                            val logicalPoint = screenToModel(
                                change.position,
                                graph.origin,
                                graph.scaledCellSize
                            )

                            pointerDragState.isDraggingGraph = event.buttons.isTertiaryPressed
                            pointerDragState.isDraggingPoint = event.buttons.isPrimaryPressed

                            when (event.type) {
                                PointerEventType.Press -> {
                                    when {
                                        event.buttons.isPrimaryPressed -> {
                                            pointerDragState.draggedPointIndex = points.pointPressed(
                                                logicalPoint,
                                                0.2f
                                            )
                                            if (pointerDragState.draggedPointIndex == null) {
                                                points.addPointAt(logicalPoint)
                                            }
                                        }

                                        event.buttons.isSecondaryPressed -> {
                                            points.removePointAt(logicalPoint)
                                        }
                                    }
                                }

                                PointerEventType.Move -> {
                                    when {
                                        pointerDragState.isDraggingGraph -> {
                                            val delta = change.position - change.previousPosition
                                            graph.updatePanningOffset(delta)
                                        }

                                        pointerDragState.isDraggingPoint -> {
                                            points.movePoint(pointerDragState.draggedPointIndex, logicalPoint)
                                        }
                                    }
                                }

                                PointerEventType.Scroll -> {
                                    val delta = change.scrollDelta.y
                                    graph.updateScale(delta)
                                }
                            }
                        }
                    }
                },
            settings = graphSettings,
            gridColor = AppColors.Divider,
            cellSize = graph.scaledCellSize,
            panOffset = graph.panningOffset,
            controlPoints = points.getControlPointOffsets().map { position ->
                modelToScreen(
                    position,
                    graph.origin,
                    graph.scaledCellSize
                )
            },
            pointRadius = POINT_RADIUS
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