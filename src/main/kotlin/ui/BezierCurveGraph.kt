package ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isTertiaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import utils.toScale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BezierCurveGraph(
    modifier: Modifier = Modifier,
    controlPoints: List<Offset?>,
    graphPoints: List<Offset>,
    scale: Float,
    offset: Offset,
    onDrag: (Offset) -> Unit,
    onScroll: (Float) -> Unit,
    gridColor: Color = Color.Gray,
    gridStrokeWidth: Float = Stroke.HairlineWidth,
    axisStyle: AxisStyle = AxisStyle.Axis
) {
    val cellSize = 100f
    val scaledCellSize = cellSize * scale
    var isDragging by remember { mutableStateOf(false) }
    var dragOffset by mutableStateOf(offset)

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val change = event.changes.first()
                        isDragging = event.buttons.isTertiaryPressed

                        when (event.type) {
                            PointerEventType.Move -> {
                                if (isDragging) {
                                    val delta = change.position - change.previousPosition
                                    dragOffset += delta
                                    onDrag(dragOffset)
                                }
                            }

                            PointerEventType.Scroll -> {
                                val delta = change.scrollDelta.y
                                onScroll(delta)
                            }
                        }
                    }
                }
            }
    ) {
        drawGrid(scaledCellSize, offset, gridColor, gridStrokeWidth, axisStyle)

        if (!controlPoints.contains(null)) {
            drawCurve(
                graphPoints.map { graphPoint ->
                    graphPoint.toScale(
                        origin = center + offset,
                        scale = scaledCellSize
                    )
                }
            )
        }

        drawPoints(
            points = controlPoints.filterNotNull().map { it.toScale(center + offset, scaledCellSize) },
            pointMode = PointMode.Points,
            strokeWidth = 6f,
            color = Color.Red
        )
    }
}

enum class AxisStyle {
    NoAxis,
    Axis
}

fun DrawScope.drawGrid(
    scale: Float,
    offset: Offset,
    color: Color = Color.Gray,
    strokeWidth: Float = Stroke.HairlineWidth,
    axisStyle: AxisStyle = AxisStyle.Axis
) {
    var x = (center.x + offset.x) - ((center.x + offset.x) / scale).toInt() * scale
    var y = (center.y + offset.y) - ((center.y + offset.y) / scale).toInt() * scale

    while (x < size.width) {
        drawLine(
            color = color,
            start = Offset(x, 0f),
            end = Offset(x, size.height),
            strokeWidth = strokeWidth
        )
        x += scale
    }

    while (y < size.height) {
        drawLine(
            color = color,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = strokeWidth
        )
        y += scale
    }

    if (axisStyle != AxisStyle.NoAxis) {
        drawLine(
            color = Color.Black,
            start = Offset(0f, center.y + offset.y),
            end = Offset(size.width, center.y + offset.y),
            strokeWidth = strokeWidth
        )
        drawLine(
            color = Color.Black,
            start = Offset(center.x + offset.x, 0f),
            end = Offset(center.x + offset.x, size.height),
            strokeWidth = strokeWidth
        )
    }
}

fun DrawScope.drawCurve(
    points: List<Offset>
) {
    for (i in 0..<points.lastIndex) {
        drawLine(
            color = Color.Blue,
            start = points[i],
            end = points[i + 1]
        )
    }
}
