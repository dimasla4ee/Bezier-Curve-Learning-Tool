package ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import utils.toScale

@Composable
fun BezierCurveGraph(
    modifier: Modifier = Modifier,
    controlPoints: List<Offset?>,
    graphPoints: List<Offset>,
    gridColor: Color = Color.Gray,
    gridStrokeWidth: Float = Stroke.HairlineWidth,
    axisStyle: AxisStyle = AxisStyle.Axis
) {
    val scale = 50f

    Canvas(modifier.fillMaxSize()) {
        drawGrid(scale, gridColor, gridStrokeWidth, axisStyle)

        if (!controlPoints.contains(null)) {
            drawCurve(graphPoints.map { it.toScale(center, scale) })
        }
        drawPoints(
            points = controlPoints.filterNotNull().map { it.toScale(center, scale) },
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
    color: Color = Color.Gray,
    strokeWidth: Float = Stroke.HairlineWidth,
    axisStyle: AxisStyle = AxisStyle.Axis
) {
    var x = center.x - (center.x / scale).toInt() * scale
    while (x < size.width) {
        drawLine(
            color = color,
            start = Offset(x, 0f),
            end = Offset(x, size.height),
            strokeWidth = strokeWidth
        )
        x += scale
    }

    var y = center.y - (center.y / scale).toInt() * scale
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
            start = center.copy(x = 0f),
            end = center.copy(x = size.width),
            strokeWidth = strokeWidth
        )
        drawLine(
            color = Color.Black,
            start = center.copy(y = 0f),
            end = center.copy(y = size.height),
            strokeWidth = strokeWidth
        )
    }
}

fun DrawScope.drawCurve(
    points: List<Offset>,
) {
    for (i in 0..<points.lastIndex) {
        drawLine(
            color = Color.Blue,
            start = points[i],
            end = points[i + 1]
        )
    }
}
