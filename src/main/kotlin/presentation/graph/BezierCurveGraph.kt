package presentation.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import domain.model.GraphSettings

@Composable
fun BezierCurveGraph(
    modifier: Modifier = Modifier,
    settings: GraphSettings,
    controlPoints: List<Offset>,    // Points to be drawn
    pointRadius: Float,
    cellSize: Float,
    panOffset: Offset,
    gridColor: Color = Color.Gray,
    strokeWidth: Float = Stroke.HairlineWidth,
    axisStyle: AxisStyle = AxisStyle.Axis
) {
    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        drawGrid(
            cellSize = cellSize,
            panOffset = panOffset,
            color = gridColor,
            strokeWidth = strokeWidth,
            axisStyle = axisStyle
        )

        drawCurve(
            curvePoints = controlPoints,
            strokeWidth = 2f
        )

        if (settings.showSupportLine && controlPoints.size > 2) {
            connectPoints(controlPoints, Color.Blue.copy(alpha = 0.4f))
            if (settings.quadraticSupportLinePoints != null) {
                connectPoints(settings.quadraticSupportLinePoints!!, Color.Red.copy(alpha = 0.4f))
            }
            if (settings.cubicSupportLinePoints != null) {
                connectPoints(settings.cubicSupportLinePoints!!, Color.Green.copy(alpha = 0.8f))
            }
        }

        controlPoints.forEach { pos ->
            drawCircle(
                color = Color.Red,
                radius = pointRadius,
                center = pos
            )
        }

        if (settings.showSupportLine && controlPoints.size > 1) {
            drawCircle(
                color = Color.Blue,
                radius = pointRadius + 1f,
                center = settings.interpolatedPoint
            )
        }
    }
}

enum class AxisStyle {
    NoAxis,
    Axis
}

fun DrawScope.connectPoints(
    points: List<Offset>,
    color: Color,
    strokeWidth: Float = Stroke.HairlineWidth
) {
    for (i in 0..<points.lastIndex) {
        drawLine(
            color = color,
            strokeWidth = strokeWidth,
            start = points[i],
            end = points[i + 1]
        )
    }
}

fun DrawScope.drawGrid(
    cellSize: Float,
    panOffset: Offset,
    color: Color = Color.Gray,
    strokeWidth: Float = Stroke.HairlineWidth,
    axisStyle: AxisStyle = AxisStyle.Axis
) {
    // Initial values for x and y to align axis with center
    var gridX = (center.x + panOffset.x) - ((center.x + panOffset.x) / cellSize).toInt() * cellSize
    var gridY = (center.y + panOffset.y) - ((center.y + panOffset.y) / cellSize).toInt() * cellSize

    // Draw grid
    while (gridX < size.width) {
        drawLine(
            color = color,
            start = Offset(gridX, 0f),
            end = Offset(gridX, size.height),
            strokeWidth = strokeWidth
        )
        gridX += cellSize
    }
    while (gridY < size.height) {
        drawLine(
            color = color,
            start = Offset(0f, gridY),
            end = Offset(size.width, gridY),
            strokeWidth = strokeWidth
        )
        gridY += cellSize
    }

    // Draw axis
    if (axisStyle != AxisStyle.NoAxis) {
        drawLine(
            color = Color.Black.copy(alpha = 0.4f),
            start = Offset(0f, center.y + panOffset.y),
            end = Offset(size.width, center.y + panOffset.y),
            strokeWidth = strokeWidth
        )
        drawLine(
            color = Color.Black.copy(alpha = 0.4f),
            start = Offset(center.x + panOffset.x, 0f),
            end = Offset(center.x + panOffset.x, size.height),
            strokeWidth = strokeWidth
        )
    }
}

fun DrawScope.drawCurve(
    curvePoints: List<Offset>,
    strokeWidth: Float = 1f,
    color: Color = Color.Blue
) {
    val path = BezierCurveType.getPath(curvePoints) ?: return

    drawPath(
        path = path,
        color = color,
        style = Stroke(strokeWidth)
    )
}