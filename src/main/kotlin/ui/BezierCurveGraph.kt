package ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import utils.toScale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BezierCurveGraph(
    modifier: Modifier = Modifier,
    controlPoints: List<Offset?>,           // Initial control points
    graphPoints: List<Offset>,              // Calculated Bezier curve points
    cellSize: Float,
    panOffset: Offset,
    gridColor: Color = Color.Gray,
    gridStrokeWidth: Float = Stroke.HairlineWidth,
    axisStyle: AxisStyle = AxisStyle.Axis
) {
    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        drawGrid(
            cellSize = cellSize,
            panOffset = panOffset,
            color = gridColor,
            strokeWidth = gridStrokeWidth,
            axisStyle = axisStyle
        )

        if (!controlPoints.contains(null)) {
            drawCurve(graphPoints.map { it.toScale(center + panOffset, cellSize) })
        }

        drawPoints(
            points = controlPoints.filterNotNull().map { it.toScale(center + panOffset, cellSize) },
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
            color = Color.Black,
            start = Offset(0f, center.y + panOffset.y),
            end = Offset(size.width, center.y + panOffset.y),
            strokeWidth = strokeWidth
        )
        drawLine(
            color = Color.Black,
            start = Offset(center.x + panOffset.x, 0f),
            end = Offset(center.x + panOffset.x, size.height),
            strokeWidth = strokeWidth
        )
    }
}

fun DrawScope.drawCurve(
    curvePoints: List<Offset>
) {
    for (i in 0..<curvePoints.lastIndex) {
        drawLine(
            color = Color.Blue,
            start = curvePoints[i],
            end = curvePoints[i + 1]
        )
    }
}
