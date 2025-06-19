package domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import domain.PointsViewModel.Companion.POINTS_CAP
import domain.model.EditablePoint
import utils.binomialCoefficients
import utils.recalculate
import utils.toEditablePoint
import kotlin.math.pow

/** ViewModel for handling logical point calculations */
class PointsViewModel {

    var interpolation by mutableFloatStateOf(0f)
        private set

    /** List of user-editable control points. */
    val controlPoints = mutableStateListOf<EditablePoint>()

    /**
     * Generates a list of points along the Bézier curve based on current control points.
     * Returns an empty list if any point in null or fewer than two are defined.
     */
    private val graphPoints: MutableList<Offset>
        get() {
            val parsedControlPoints = getControlPointOffsets()
            val containsInvalidPoints = parsedControlPoints.size != controlPoints.size
            val notEnoughPoints = parsedControlPoints.size < 2

            if (containsInvalidPoints || notEnoughPoints) {
                return mutableListOf()
            }

            val n = parsedControlPoints.lastIndex
            val steps = (STEPS_PER_POINT * n).coerceAtLeast(2)

            val result = mutableStateListOf<Offset>()

            for (tStep in 0..steps) {
                val t = tStep / steps.toFloat()
                val point = findBezierPoint(t, parsedControlPoints)

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
        var point = Offset.Companion.Zero
        val n = points.lastIndex

        for (i in 0..n) {
            point += points[i] * binomialCoefficients(n, i).toFloat() * t.pow(i) * (1 - t).pow(n - i)
        }

        return point
    }

    /** Returns the list of [Offset] for calculated Bézier curve points. */
    fun getGraphPointOffsets(): List<Offset> = graphPoints

    /**
     * Determines if a control point is pressed within a given radius.
     *
     * @param position the screen-space position to check for a nearby point.
     * @param radius the radius within which a point is considered pressed.
     * @return the index of the pressed point, or `null` if no point is within the radius.
     */
    fun pointPressed(position: Offset, radius: Float): Int? {
        val parsedControlPoints = getControlPointOffsets()
        parsedControlPoints.forEachIndexed { index, offset ->
            val distance = (position - offset).getDistance()
            if (distance <= radius) return index
        }
        return null
    }

    /** Adds a new empty control point to the list, up to the defined [POINTS_CAP]. */
    fun addPoint() {
        if (controlPoints.size < POINTS_CAP) {
            controlPoints.add(
                EditablePoint("", "")
            )
        }
    }

    /**
     * Adds a new control point at the given [position] in logical space (not screen-space).
     *
     * @param position the logical position where the point should be added.
     */
    fun addPointAt(position: Offset) {
        if (controlPoints.size >= POINTS_CAP) return

        controlPoints.add(position.toEditablePoint())
    }

    /** Removes a control point at the given [index]. */
    fun removePoint(index: Int) {
        controlPoints.removeAt(index)
    }

    /**
     * Removes a control point located near the given [position], if any exist within hit radius.
     *
     * @param position the logical position to check for a nearby point.
     */
    fun removePointAt(position: Offset) {
        val pointIndex = pointPressed(position, 0.2f) ?: return
        removePoint(pointIndex)
    }

    /**
     * Moves a control point at [index] to a new [position].
     *
     * Does nothing if index is invalid.
     *
     * @param index the index of the point to move.
     * @param position new logical position to assign to the point.
     */
    fun movePoint(index: Int?, position: Offset) {
        if (index == null || index !in controlPoints.indices) return

        controlPoints[index] = position.toEditablePoint()
    }

    /** Returns the list of control points as list of [Offset] without nulls. */
    fun getControlPointOffsets(): List<Offset> = controlPoints.mapNotNull { it.offset }

    /** Validates [str] to match a decimal pattern (supports comma as decimal separator). */
    private fun matchesDecimalPattern(str: String): Boolean = Regex("^-?\\d{0,2}(,\\d{0,2})?$").matches(str)

    /**
     * Updates the X value of a control point if the input string is valid.
     *
     * @param index index of the point to update.
     * @param newValue new X value in string format.
     */
    fun updateX(index: Int, newValue: String) {
        if (matchesDecimalPattern(newValue)) {
            val old = controlPoints[index]
            controlPoints[index] = old.copy(xInput = newValue).recalculate()
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
            val old = controlPoints[index]
            controlPoints[index] = old.copy(yInput = newValue).recalculate()
        }
    }

    /**
     * Updates the interpolation progress [interpolation].
     *
     * @param newValue a value typically between 0.0 and 1.0.
     */
    fun updateInterpolation(newValue: Float) {
        interpolation = newValue
    }

    companion object {
        /** Maximum number of control points. */
        const val POINTS_CAP = 4

        /** Number of curve steps per control point (controls smoothness). */
        const val STEPS_PER_POINT = 15
    }
}
