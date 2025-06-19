package presentation.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import resources.AppColors

@Composable
fun PreferencesSidebarCard(
    modifier: Modifier = Modifier,
    showEquation: Boolean,
    showSupportLine: Boolean,
    onShowFormulaChange: (Boolean) -> Unit,
    onShowSupportLineChange: (Boolean) -> Unit
) {
    SidebarCard(
        modifier = modifier,
        leadingContainerColor = AppColors.SurfaceVariant,
        leadingContent = {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = Icons.Outlined.Settings,
                tint = AppColors.OnSurfaceVariant,
                contentDescription = "Settings"
            )
        },
        content = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        modifier = Modifier
                            .scale(0.5f)
                            .size(16.dp),
                        checked = showEquation,
                        onCheckedChange = { onShowFormulaChange(it) },
                    )
                    Text("Показать формулу", fontSize = 11.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        modifier = Modifier
                            .scale(0.5f)
                            .size(16.dp),
                        checked = showSupportLine,
                        onCheckedChange = { onShowSupportLineChange(it) },
                    )
                    Text("Показать вспомогательные линии", fontSize = 11.sp)
                }
            }
        }
    )
}