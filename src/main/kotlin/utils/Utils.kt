package utils

import androidx.compose.ui.geometry.Offset
import domain.model.EditablePoint
import java.text.DecimalFormat

/**
 * Calculates binomial coefficient "n choose r" which represents the number of ways
 * to choose unordered subset of `r` elements from a fixed set of `n` elements.
 *
 * @param n the total number of items.
 * @param r the number of items to choose.
 * @return the binomial coefficients C(n, r).
 */
fun binomialCoefficients(n: Int, r: Int): Int = factorial(n) / (factorial(r) * factorial(n - r))

/**
 * Returns the factorial of a given non-negative integer `n` (i.e., n!).
 */
fun factorial(n: Int): Int {
    require(n >= 0)
    var factorial = 1
    for (i in 1..n) {
        factorial *= i
    }
    return factorial
}

/**
 * Converts a point in graph coordinates to screen coordinates
 * based on the screen center offset with pan and cell size.
 *
 * @param screenCenterWithPan the screen center offset including pan effect.
 * @param cellSize width and height of a cell in pixels.
 * @return a new [Offset] representing the point in screen coordinates.
 */
fun Offset.toScale(screenCenterWithPan: Offset, cellSize: Float): Offset =
    Offset(
        screenCenterWithPan.x + x * cellSize,
        screenCenterWithPan.y + y * -cellSize
    )

/**
 * Converts a screen-space [Offset] to an [EditablePoint] suitable for UI input fields.
 *
 * This function formats the X and Y values using the provided [decimalFormat], and applies
 * a sign inversion to the Y-axis to match the mathematical coordinate system used in the graph.
 * Additionally, it replaces the decimal point (`.`) with a comma (`,`) for localization.
 *
 * @receiver the [Offset] representing a point in logical or graph space.
 * @param decimalFormat the formatter used to round and convert numeric values to strings.
 * @return an [EditablePoint] with X and Y coordinates as formatted strings.
 */
fun Offset.toEditablePoint(decimalFormat: DecimalFormat): EditablePoint =
    EditablePoint(
        decimalFormat.format(x).replace('.', ','),
        decimalFormat.format(-y).replace('.', ',')
    )