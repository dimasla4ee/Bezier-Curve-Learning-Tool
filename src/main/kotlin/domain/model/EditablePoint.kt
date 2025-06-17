package domain.model

import androidx.compose.ui.geometry.Offset

/**
 * Represents a control point as user-editable string inputs.
 *
 * @property xInput the X coordinate as entered by the user (in string form, using comma as decimal separator).
 * @property yInput the Y coordinate as entered by the user (in string form, using comma as decimal separator).
 * @property offset the parsed [Offset] value if the input is valid, or `null` otherwise.
 * @property isValid indicates whether both [xInput] and [yInput] can be successfully parsed into a valid coordinate.
 */
data class EditablePoint(
    var xInput: String,
    var yInput: String,
    val offset: Offset? = null,
    val isValid: Boolean = false
)