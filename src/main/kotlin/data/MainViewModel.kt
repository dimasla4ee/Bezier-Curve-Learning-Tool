package data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import data.MainViewModel.Companion.POINTS_CAP
import utils.binomialCoefficients
import kotlin.math.pow

class MainViewModel {

    companion object {
        /** Maximum allowed zoom level. */
        const val MAX_SCALE = 2f

        /** Minimum allowed zoom level. */
        const val MIN_SCALE = 0.2f

        /** Zoom sensitivity multiplier. */
        const val SCALE_MULTIPLIER = 0.05f

        /** Maximum number of control points. */
        const val POINTS_CAP = 4

        /** Number of curve steps per control point (controls smoothness). */
        const val STEPS_PER_POINT = 15
    }

    /** Current zoom level applied to the graph view. */
    var scale by mutableStateOf(0.5f)
        private set

    /** Current pan offset applied to the graph view. */
    var panningOffset by mutableStateOf(Offset(0f, 0f))
        private set

    /** List of editable control points as entered by the user. */
    val controlPoints = mutableStateListOf<EditablePoint>()

    /**
     * Generates a list of points along the Bézier curve based on current control points.
     * Returns an empty list if any point in null or fewer than two are defined.
     */
    private val graphPoints: MutableList<Offset>
        get() {
            val parsedControlPoints = getControlPointOffsets()
            if (parsedControlPoints.contains(null)) {
                return mutableListOf()
            }

            val points = parsedControlPoints.filterNotNull()
            if (points.size < 2) {
                return mutableListOf()
            }

            val n = points.lastIndex
            val steps = (STEPS_PER_POINT * n).coerceAtLeast(2)

            val result = mutableStateListOf<Offset>()

            for (tStep in 0..steps) {
                val t = tStep / steps.toFloat()
                val point = findBezierPoint(t, points)

                result.add(point)
            }

            return result
        }

    /**
     * Calculates a single point on a Bézier curve at a given parameter [t] using De Casteljau algorithm.
     *
     * @param t normalized time parameter between 0 and 1.
     * @param points list of control points defining the Bézier curve.
     * @return the point on the curve at position [t].
     */
    fun findBezierPoint(t: Float, points: List<Offset>): Offset {
        var point = Offset.Zero
        val n = points.lastIndex

        for (i in 0..n) {
            point += points[i] * binomialCoefficients(n, i).toFloat() * t.pow(i) * (1 - t).pow(n - i)
        }

        return point
    }

    /** Adds a new empty control point to the list, up to the defined [POINTS_CAP]. */
    fun addPoint() {
        if (controlPoints.size < POINTS_CAP) {
            controlPoints.add(
                EditablePoint("", "")
            )
        }
    }

    /** Removes a control point at the specified [index]. */
    fun removePoint(index: Int) {
        controlPoints.removeAt(index)
    }

    /**
     * Updates the X value of a control point if the input string is valid.
     *
     * @param index index of the point to update.
     * @param newValue new X value in string format.
     */
    fun updateX(index: Int, newValue: String) {
        if (matchesDecimalPattern(newValue)) {
            controlPoints[index] = controlPoints[index].copy(xInput = newValue)
        }
    }

    /**
     * Updates the Y value of a control point if the input string is valid.
     *
     * @param index index of the point to update.
     * @param newValue new Y value in string format.
     */
    fun updateY(index: Int, newValue: String) {
        if (matchesDecimalPattern(newValue)) {
            controlPoints[index] = controlPoints[index].copy(yInput = newValue)
        }
    }

    /** Validates [str] to match a decimal pattern (supports comma as decimal separator). */
    private fun matchesDecimalPattern(str: String): Boolean = Regex("^-?\\d{0,2}(,\\d{0,2})?$").matches(str)

    /** Converts the list of editable control points to a list of [Offset], replacing invalid entries with null. */
    fun getControlPointOffsets(): List<Offset?> = controlPoints.map { it.toOffset() }

    /** Returns the list of Bézier curve points. */
    fun getGraphPointOffsets(): List<Offset> = graphPoints

    /**
     * Updates the current zoom level based on scroll delta input.
     *
     * @param delta the vertical scroll delta used to modify zoom level.
     */
    fun updateScale(delta: Float) {
        val newValue = scale - delta * SCALE_MULTIPLIER
        scale = newValue.coerceIn(MIN_SCALE..MAX_SCALE)
    }

    /**
     * Updates the pan offset used to shift the graph.
     */
    fun updatePanningOffset(newValue: Offset) {
        panningOffset = newValue
    }
}
