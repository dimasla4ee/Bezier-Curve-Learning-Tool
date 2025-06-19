package domain

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import domain.MainViewModel.Companion.POINTS_CAP
import domain.model.EditablePoint
import presentation.graph.*
import utils.binomialCoefficients
import utils.recalculate
import utils.toEditablePoint
import utils.toScale
import kotlin.math.pow

class MainViewModel {

    companion object {
        /** Maximum number of control points. */
        const val POINTS_CAP = 4

        /** Number of curve steps per control point (controls smoothness). */
        const val STEPS_PER_POINT = 15
    }

    var showFormula by mutableStateOf(true)
        private set

    fun updateShowFormula(newValue: Boolean) {
        showFormula = newValue
    }

    var showSupportLine by mutableStateOf(true)
        private set

    fun updateShowSupportLine(newValue: Boolean) {
        showSupportLine = newValue
    }

    /** Current zoom level applied to the graph view. */
    var scale by mutableStateOf(0.5f)
        private set

    /** Current pan offset applied to the graph view. */
    var panningOffset by mutableStateOf(Offset(0f, 0f))
        private set

    var interpolation by mutableFloatStateOf(0f)
        private set

    /** Current cell size used for rendering, adjusted by the zoom scale */
    var scaledCellSize by mutableStateOf(BASE_CELL_SIZE * scale)
        private set

    /** Current size of the canvas in pixels */
    private var canvasSize = mutableStateOf(IntSize.Companion.Zero)

    /** Center point of the canvas in pixels */
    val canvasCenter: Offset
        get() = Offset(canvasSize.value.width / 2f, canvasSize.value.height / 2f)

    /** List of editable control points as entered by the user. */
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

    /** Adds a new empty control point to the list, up to the defined [POINTS_CAP]. */
    fun addPoint() {
        if (controlPoints.size < POINTS_CAP) {
            controlPoints.add(
                EditablePoint("", "")
            )
        }
    }

    /**
     * Adds a new control point at the specified [position] on the canvas.
     * The position is interpreted relative to the graph's coordinate system.
     *
     * @param position the screen-space position where the point should be added.
     */
    fun addPointAt(position: Offset) {
        if (controlPoints.size >= POINTS_CAP) return

        val point = (position - canvasCenter - panningOffset) / scaledCellSize
        controlPoints.add(point.toEditablePoint())
    }

    /** Removes a control point at the specified [index]. */
    fun removePoint(index: Int) {
        controlPoints.removeAt(index)
    }

    /**
     * Removes a control point located near the given [position], if within hit radius.
     *
     * @param position the screen-space position to check for a nearby point.
     */
    fun removePointAt(position: Offset) {
        val pointIndex = pointPressed(position, POINT_RADIUS + 1f) ?: return
        removePoint(pointIndex)
    }

    /**
     * Determines if a control point is pressed within a specified radius.
     *
     * @param position the screen-space position to check for a nearby point.
     * @param radius the radius within which a point is considered pressed.
     * @return the index of the pressed point, or null if no point is within the radius.
     */
    fun pointPressed(position: Offset, radius: Float): Int? {
        val parsedControlPoints = getControlPointOffsets()
        parsedControlPoints.forEachIndexed { index, offset ->
            val distance = (position - offset).getDistance()
            if (distance <= radius) return index
        }
        return null
    }

    /**
     * Moves a control point to a new position.
     *
     * Does nothing if index is invalid.
     *
     * @param index the index of the point to move.
     * @param position the new position of the point in screen-space coordinates.
     */
    fun movePoint(index: Int?, position: Offset) {
        if (index == null || index !in controlPoints.indices) return

        val coordinates = (position - canvasCenter - panningOffset) / scaledCellSize
        controlPoints[index] = coordinates.toEditablePoint()
    }

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

    /** Validates [str] to match a decimal pattern (supports comma as decimal separator). */
    private fun matchesDecimalPattern(str: String): Boolean = Regex("^-?\\d{0,2}(,\\d{0,2})?$").matches(str)

    /** Converts the list of editable control points to a list of [Offset] without nulls. */
    fun getControlPointOffsets(): List<Offset> =
        controlPoints.mapNotNull { point ->
            point.offset?.toScale(panningOffset + canvasCenter, scaledCellSize)
        }

    /** Returns the list of [Offset] for calculated Bézier curve points. */
    fun getGraphPointOffsets(): List<Offset> = graphPoints

    /**
     * Updates the current zoom level based on scroll delta input.
     *
     * @param delta the vertical scroll delta used to modify zoom level.
     */
    fun updateScale(delta: Float) {
        val newValue = scale - delta * SCALE_MULTIPLIER
        scale = newValue.coerceIn(MIN_SCALE..MAX_SCALE)
        scaledCellSize = BASE_CELL_SIZE * scale
    }

    /**
     * Updates the pan offset used to shift the graph.
     *
     * @param delta the difference between the previous and new mouse positions.
     */
    fun updatePanningOffset(delta: Offset) {
        panningOffset += delta
    }

    /** Updates the internal record of the canvas size. */
    fun updateCanvasSize(newSize: IntSize) {
        canvasSize.value = newSize
    }

    fun updateInterpolation(newValue: Float) {
        interpolation = newValue
    }
}