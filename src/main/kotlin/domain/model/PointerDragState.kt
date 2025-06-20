package domain.model

data class PointerDragState(
    var isDraggingGraph: Boolean = false,
    var isDraggingPoint: Boolean = false,
    var draggedPointIndex: Int? = null
)
