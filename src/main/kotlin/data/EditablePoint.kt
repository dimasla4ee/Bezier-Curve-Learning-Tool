package data

import androidx.compose.ui.geometry.Offset

/**
 * Represents a control point in editable string form.
 *
 * The [xInput] and [yInput] values are stored as strings to allow partial or invalid input during editing.
 */
data class EditablePoint(
    var xInput: String,
    var yInput: String
) {
    /**
     * Attempts to convert this editable point into a [Offset].
     *
     * This method supports both dot (`.`) and comma (`,`) as decimal separators.
     *
     * @return an [Offset] if both [xInput] and [yInput] are valid float representations, or `null` otherwise.
     */
    fun toOffset(): Offset? {
        val xParsed = xInput.replace(',', '.').toFloatOrNull() ?: return null
        val yParsed = yInput.replace(',', '.').toFloatOrNull() ?: return null
        return Offset(xParsed, yParsed)
    }
}

