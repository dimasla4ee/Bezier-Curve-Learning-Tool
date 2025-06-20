package app

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import domain.MainViewModel
import domain.model.PointerDragState
import presentation.app.AppCanvasContent
import presentation.app.AppSidebarContent
import presentation.components.SidebarContainer
import resources.AppColors
import resources.AppDimensions
import java.awt.Dimension

fun main() = singleWindowApplication(
    title = "Bezier Curve Learning Tool"
) {
    window.minimumSize = Dimension(900, 600)

    App()
}

@Composable
@Preview
fun App() {
    val viewModel = remember { MainViewModel() }
    val drawerState = DrawerState(DrawerValue.Open)
    val pointerDragState by remember { mutableStateOf(PointerDragState()) }

    Surface(Modifier.fillMaxSize(), color = AppColors.Base) {
        SidebarContainer(
            modifier = Modifier.padding(AppDimensions.MediumPadding),
            drawerState = drawerState,
            drawerElevation = 0.dp,
            sidebarContent = { AppSidebarContent(viewModel, drawerState) },
            content = { AppCanvasContent(viewModel, drawerState, pointerDragState) }
        )
    }
}