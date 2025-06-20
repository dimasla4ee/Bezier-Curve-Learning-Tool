package presentation.graph

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

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

        /**
         * Returns a Composable that draws the equation corresponding to this Bézier curve,
         * including the evaluated B(t) result at the provided interpolation value.
         *
         * @param t interpolation parameter from 0 to 1
         * @param result point B(t) on the curve
         */
        @Composable
        fun Formula(
            modifier: Modifier = Modifier,
            controlPoints: List<Offset>,
            t: Float,
            result: Offset,
            fontSize: TextUnit = 12.sp
        ) {
            val bezierType = BezierCurveType.fromControlPoints(controlPoints.size) ?: return

            val roundedX = String.format("%.2f", result.x)
            val roundedY = String.format("%.2f", result.y)
            val roundedT = String.format("%.2f", t)

            val expression = when (bezierType) {
                LINEAR -> "B($roundedT) = (1 - t)P₀ + tP₁"
                QUADRATIC -> "B($roundedT) = (1 - t)²P₀ + 2t(1 - t)P₁ + t²P₂"
                CUBIC -> "B($roundedT) = (1 - t)³P₀ + 3t(1 - t)²P₁ + 3t²(1 - t)P₂ + t³P₃"
            }

            val resultStr = " = (${roundedX}; ${roundedY})"

            Text(
                modifier = modifier,
                text = expression + resultStr,
                fontSize = fontSize,
                fontFamily = FontFamily.Monospace,
                fontStyle = FontStyle.Italic,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
