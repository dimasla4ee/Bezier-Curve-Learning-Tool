package presentation.graph

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path

enum class BezierCurveType(val controlPoints: Int) {
    LINEAR(2),
    QUADRATIC(3),
    CUBIC(4);

    companion object {
        fun fromControlPoints(size: Int): BezierCurveType? = entries.firstOrNull { it.controlPoints == size }

        fun getPath(controlPoints: List<Offset>): Path? {
            val bezierType = BezierCurveType.fromControlPoints(controlPoints.size) ?: return null
            val path = Path().apply {
                moveTo(
                    controlPoints.first().x,
                    controlPoints.first().y
                )
            }

            when (bezierType) {
                LINEAR -> {
                    path.lineTo(
                        x = controlPoints.last().x,
                        y = controlPoints.last().y
                    )
                }

                QUADRATIC -> {
                    path.quadraticTo(
                        controlPoints[1].x, controlPoints[1].y,
                        controlPoints[2].x, controlPoints[2].y
                    )
                }

                CUBIC -> {
                    path.cubicTo(
                        controlPoints[1].x, controlPoints[1].y,
                        controlPoints[2].x, controlPoints[2].y,
                        controlPoints[3].x, controlPoints[3].y,
                    )
                }
            }

            return path
        }
    }
}