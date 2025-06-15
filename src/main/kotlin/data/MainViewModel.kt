package data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import utils.binomialCoefficients
import kotlin.math.pow

class MainViewModel {

    companion object {
        const val MAX_SCALE = 2f
        const val MIN_SCALE = 0.2f
        const val SCALE_MULTIPLIER = 0.05f
        const val POINTS_CAP = 4
        const val POINT_PER_POINT = 15
    }

    var scale by mutableStateOf(0.5f)
        private set

    var panningOffset by mutableStateOf(Offset(0f, 0f))
        private set

    val controlPoints = mutableStateListOf<EditablePoint>()

    val graphPoints: MutableList<Offset>
        get() {
            val parsedControlPoints = getControlPointsAsOffset()
            if (parsedControlPoints.contains(null)) {
                return mutableListOf()
            }

            val points = parsedControlPoints.filterNotNull()
            if (points.size < 2) {
                return mutableListOf()
            }

            val n = points.lastIndex
            val steps = (POINT_PER_POINT * n).coerceAtLeast(2)

            val result = mutableStateListOf<Offset>()

            for (tStep in 0..steps) {
                val t = tStep / steps.toFloat()
                val point = findBezierPoint(t, points)

                result.add(point)
            }

            return result
        }

    fun findBezierPoint(t: Float, points: List<Offset>): Offset {
        var point = Offset.Zero
        val n = points.lastIndex

        for (i in 0..n) {
            point += points[i] * binomialCoefficients(n, i).toFloat() * t.pow(i) * (1 - t).pow(n - i)
        }

        return point
    }

    fun addPoint() {
        if (controlPoints.size < POINTS_CAP) {
            controlPoints.add(
                EditablePoint("", "")
            )
        }
    }

    fun removePoint(index: Int) {
        controlPoints.removeAt(index)
    }

    fun updateX(index: Int, newValue: String) {
        if (checkPattern(newValue)) {
            controlPoints[index] = controlPoints[index].copy(xInput = newValue)
        }
    }

    fun updateY(index: Int, newValue: String) {
        if (checkPattern(newValue)) {
            controlPoints[index] = controlPoints[index].copy(yInput = newValue)
        }
    }

    private fun checkPattern(str: String): Boolean = Regex("^-?\\d{0,2}(,\\d{0,2})?$").matches(str)

    fun getControlPointsAsOffset(): List<Offset?> = controlPoints.map { it.toOffset() }

    fun getGraphPointsAsOffset(): List<Offset> = graphPoints

    fun updateScale(delta: Float) {
        val newValue = scale - delta * SCALE_MULTIPLIER
        scale = newValue.coerceIn(MIN_SCALE..MAX_SCALE)
    }

    fun updatePanningOffset(newValue: Offset) {
        panningOffset = newValue
    }
}