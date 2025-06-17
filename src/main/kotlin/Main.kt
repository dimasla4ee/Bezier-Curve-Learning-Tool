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
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isPrimaryPressed
import androidx.compose.ui.input.pointer.isTertiaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import data.MainViewModel
import kotlinx.coroutines.launch
import resources.AppColors
import resources.AppDimensions
import ui.BezierCurveGraph
import ui.DismissibleDrawerCard
import ui.IconButton
import ui.PointCard
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
                        contentDescription = "Добавить точку",
                        onClick = { viewModel.addPoint() }
                    )

                    IconButton(
                        imageVector = Icons.AutoMirrored.Filled.MenuOpen,
                        contentDescription = "Свернуть меню",
                        onClick = { coroutineScope.launch { drawerState.close() } }
                    )
                }

                Divider(Modifier.fillMaxWidth(), thickness = 1.5.dp, color = AppColors.Divider)

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
                    modifier = Modifier
                        .onSizeChanged { viewModel.updateCanvasSize(it) }
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent()
                                    val change = event.changes.first()
                                    isDraggingGraph = event.buttons.isTertiaryPressed

                                    when (event.type) {
                                        PointerEventType.Press -> {
                                            if (event.buttons.isPrimaryPressed) {
                                                viewModel.addPointAt(change.position)
                                            }
                                        }

                                        PointerEventType.Move -> {
                                            if (isDraggingGraph) {
                                                val delta = change.position - change.previousPosition
                                                viewModel.updatePanningOffset(delta)
                                            }
                                        }

                                        PointerEventType.Scroll -> {
                                            val delta = change.scrollDelta.y
                                            viewModel.updateScale(delta)
                                        }
                                    }
                                }
                            }
                        },
                    gridColor = AppColors.Divider,
                    cellSize = viewModel.scaledCellSize,
                    panOffset = viewModel.panningOffset,
                    controlPoints = viewModel.getControlPointOffsets(),
                    graphPoints = viewModel.getGraphPointOffsets()
                )

                if (drawerState.isClosed) {
                    IconButton(
                        modifier = Modifier.absolutePadding(
                            left = AppDimensions.MediumPadding,
                            top = AppDimensions.MediumPadding
                        ),
                        imageVector = Icons.AutoMirrored.Filled.MenuOpen,
                        contentDescription = "Раскрыть меню",
                        onClick = { coroutineScope.launch { drawerState.open() } }
                    )
                }
            }
        }
    }
}