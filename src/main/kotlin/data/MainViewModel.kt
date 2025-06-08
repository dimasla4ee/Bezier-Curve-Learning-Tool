package data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import utils.combinations
import kotlin.math.pow

class MainViewModel {

    companion object {
        const val MAX_SCALE = 2f
        const val MIN_SCALE = 0.2f
        const val SCALE_MULTIPLIER = 0.05f
        const val POINTS_CAP = 4
    }

    var scale by mutableStateOf(0.5f)
        private set

    var panningOffset = mutableStateOf(Offset(0f, 0f))
        private set

    val controlPoints = mutableStateListOf<EditablePoint>()

    val graphPoints: MutableList<Offset>
        get() {
            val graphPoints = mutableStateListOf<Offset>()
            val parsedControlPoints = getControlPointsAsOffset()

            if (parsedControlPoints.contains(null)) {
                return graphPoints
            }

            for (t in 1..100) {
                var b = Offset(0f, 0f)
                val n = parsedControlPoints.size - 1
                for (i in 0..n) {
                    b += parsedControlPoints[i]!! * (combinations(n, i)
                            * (t / 100f).pow(i) * (1 - t / 100f).pow(n - i))
                }
                graphPoints.add(b)
            }
            return graphPoints
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
            controlPoints[index] = controlPoints[index].copy(x = newValue)
        }
    }

    fun updateY(index: Int, newValue: String) {
        if (checkPattern(newValue)) {
            controlPoints[index] = controlPoints[index].copy(y = newValue)
        }
    }

    private fun checkPattern(str: String): Boolean = Regex("^-?\\d{0,2}(,\\d{0,2})?$").matches(str)

    fun getControlPointsAsOffset(): List<Offset?> = controlPoints.map { it.toOffset() }

    fun getGraphPointsAsOffset(): List<Offset> = graphPoints

    fun updateScale(delta: Float) {
        val newValue = scale - delta * SCALE_MULTIPLIER
        if (newValue in MIN_SCALE..MAX_SCALE) {
            scale = newValue
        }
    }
}