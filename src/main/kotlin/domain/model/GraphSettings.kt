package domain.model

import androidx.compose.ui.geometry.Offset

data class GraphSettings(
    var interpolatedPoint: Offset = Offset.Zero,
    var quadraticSupportLinePoints: List<Offset>? = null,
    var cubicSupportLinePoints: List<Offset>? = null,
    var showEquation: Boolean = true,
    var showSupportLine: Boolean = true
)
