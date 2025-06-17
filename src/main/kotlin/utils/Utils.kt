package utils

import androidx.compose.ui.geometry.Offset
import domain.model.EditablePoint

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
 * Converts this [Offset] to an [EditablePoint] suitable for UI input.
 *
 * The result uses localized formatting:
 * - both X and Y values are formatted to two decimal places;
 * - the decimal point is replaced with a comma for display;
 * - the Y value is negated to match the graph's coordinate system.
 *
 * @return a valid [EditablePoint] with prefilled inputs and calculated offset.
 */
fun Offset.toEditablePoint(): EditablePoint {
    String.format("%.2f", x)

    return EditablePoint(
        String.format("%.2f", x).replace('.', ','),
        String.format("%.2f", -y).replace('.', ','),
        offset = copy(y = -y),
        isValid = true
    )
}

/**
 * Recalculates the numeric [Offset] and validity state based on the current input strings.
 *
 * This method:
 * - attempts to parse [EditablePoint.xInput] and [EditablePoint.yInput] as floats (supports comma as decimal separator);
 * - updates the [EditablePoint.offset] if parsing succeeds;
 * - negates the Y coordinate to match the visual graph direction;
 * - sets [EditablePoint.isValid] to `true` only if both coordinates are successfully parsed.
 *
 * @receiver the current [EditablePoint] instance.
 * @return a new [EditablePoint] with updated offset and validity status.
 */
fun EditablePoint.recalculate(): EditablePoint {
    val x = xInput.replace(',', '.').toFloatOrNull()
    val y = yInput.replace(',', '.').toFloatOrNull()

    return if (x != null && y != null) {
        copy(offset = Offset(x, y), isValid = true)
    } else {
        copy(offset = null, isValid = false)
    }
}