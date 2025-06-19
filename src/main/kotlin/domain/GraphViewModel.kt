package domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import presentation.graph.BASE_CELL_SIZE
import presentation.graph.MAX_SCALE
import presentation.graph.MIN_SCALE
import presentation.graph.SCALE_MULTIPLIER

class GraphViewModel {

    /** Current zoom level applied to the graph view. */
    var scale by mutableStateOf(0.5f)
        private set

    /** Current pan offset applied to the graph view. */
    var panningOffset by mutableStateOf(Offset(0f, 0f))
        private set


    /** Current cell size used for rendering, adjusted by the zoom scale */
    var scaledCellSize by mutableStateOf(BASE_CELL_SIZE * scale)
        private set

    /** Current size of the graph canvas in pixels */
    private var canvasSize = mutableStateOf(IntSize.Companion.Zero)

    /** Center point of the canvas in pixels */
    val canvasCenter: Offset
        get() = Offset(canvasSize.value.width / 2f, canvasSize.value.height / 2f)

    val origin: Offset
        get() = canvasCenter + panningOffset

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
}