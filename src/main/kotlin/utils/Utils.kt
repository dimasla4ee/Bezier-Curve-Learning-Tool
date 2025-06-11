package utils

import androidx.compose.ui.geometry.Offset

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
    return (1..n).reduce { previousValue, currentValue ->
        previousValue * currentValue
    }
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
    Offset(screenCenterWithPan.x + x * cellSize, screenCenterWithPan.y + y * -cellSize)