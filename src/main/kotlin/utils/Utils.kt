package utils

import androidx.compose.ui.geometry.Offset

fun combinations(n: Int, r: Int): Int = factorial(n) / (factorial(r) * factorial(n - r))

fun factorial(n: Int): Int {
    var factorial = 1
    for (i in 1..n) {
        factorial *= i
    }
    return factorial
}

fun Offset.toScale(
    origin: Offset,
    scale: Float
): Offset = Offset(origin.x + x * scale, origin.y + y * -scale)