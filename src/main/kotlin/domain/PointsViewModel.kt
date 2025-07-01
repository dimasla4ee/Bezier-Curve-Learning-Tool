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

    var interpolation by mutableFloatStateOf(0.5f)
        private set

    /** List of user-editable control points. */
    val controlPoints = mutableStateListOf<EditablePoint>()

    /**
     * Calculates a single point on a Bézier curve using De Casteljau algorithm.
     *
     * @param controlPoints list of control points defining the Bézier curve.
     * @return the point on the curve at position [interpolation].
     */
    fun findBezierPoint(controlPoints: List<Offset> = getControlPointOffsets()): Offset {
        var point = Offset.Companion.Zero
        val t = interpolation
        val n = controlPoints.lastIndex

        for (i in 0..n) {
            point += controlPoints[i] * binomialCoefficients(n, i).toFloat() * t.pow(i) * (1 - t).pow(n - i)
        }

        return point
    }

    fun getQuadraticSupportLinePoints(): List<Offset>? {
        return if (getControlPointOffsets().size > 2) List(getControlPointOffsets().size - 1) {
            findBezierPoint(getControlPointOffsets().subList(it, it + 2))
        } else null
    }

    fun getCubicSupportLinePoints(): List<Offset>? {
        return if (getControlPointOffsets().size > 3) List(getQuadraticSupportLinePoints()!!.size - 1) {
            findBezierPoint(getQuadraticSupportLinePoints()!!.subList(it, it + 2))
        } else null
    }

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

    /** Validates [str] to match a decimal pattern (supports both comma and dot as decimal separator). */
    private fun matchesDecimalPattern(str: String): Boolean = Regex("^-?\\d{0,2}([,.]\\d{0,2})?$").matches(str)

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

    }
}
