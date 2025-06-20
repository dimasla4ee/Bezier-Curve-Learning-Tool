package domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/** ViewModel for managing UI settings. */
class SettingsViewModel {

    /** Flag indicating whether the curve equation should be shown. */
    var showEquation by mutableStateOf(true)
        private set

    /** Flag indication whether the support lines should be drawn. */
    var showSupportLine by mutableStateOf(true)
        private set

    var playAnimation by mutableStateOf(false)
        private set

    fun updatePlayAnimation(newValue: Boolean) {
        playAnimation = newValue
    }

    /** Updates the [showEquation] flag */
    fun updateShowEquation(newValue: Boolean) {
        showEquation = newValue
    }

    /** Updates the [showSupportLine] flag */
    fun updateShowSupportLine(newValue: Boolean) {
        showSupportLine = newValue
    }
}
