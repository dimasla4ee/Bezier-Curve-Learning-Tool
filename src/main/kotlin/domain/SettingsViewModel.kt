package domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class SettingsViewModel {
    var showFormula by mutableStateOf(true)
        private set

    var showSupportLine by mutableStateOf(true)
        private set

    fun updateShowFormula(newValue: Boolean) {
        showFormula = newValue
    }

    fun updateShowSupportLine(newValue: Boolean) {
        showSupportLine = newValue
    }
}
