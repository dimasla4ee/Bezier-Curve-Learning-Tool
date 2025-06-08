package data

import androidx.compose.ui.geometry.Offset

data class EditablePoint(
    var x: String,
    var y: String
) {
    fun toOffset(): Offset? {
        val tempX = x.replace(',', '.').toFloatOrNull() ?: return null
        val tempY = y.replace(',', '.').toFloatOrNull() ?: return null
        return Offset(tempX, tempY)
    }
}

