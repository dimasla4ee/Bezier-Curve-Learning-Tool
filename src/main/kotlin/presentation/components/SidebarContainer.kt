package presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DrawerDefaults
import androidx.compose.material.DrawerState
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import resources.AppColors
import resources.AppDimensions

@Composable
fun SidebarContainer(
    drawerContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    drawerElevation: Dp = DrawerDefaults.Elevation,
    content: @Composable () -> Unit
) {
    Row(
        modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (drawerState.isOpen) {
            Surface(
                modifier = Modifier.fillMaxHeight().width(250.dp),
                elevation = drawerElevation,
                shape = RoundedCornerShape(AppDimensions.MediumRadius),
                border = BorderStroke(1.dp, AppColors.SurfaceDivider)
            ) {
                Column { drawerContent() }
            }
        }

        Box(Modifier.fillMaxSize()) {
            content()
        }
    }
}