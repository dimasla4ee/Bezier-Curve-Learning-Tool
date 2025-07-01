package app

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.bezier_curve_learning_tool.generated.resources.Res
import com.example.bezier_curve_learning_tool.generated.resources.iconPng
import domain.MainViewModel
import domain.model.PointerDragState
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import presentation.app.AppCanvasContent
import presentation.app.AppSidebarContent
import presentation.components.AboutBezierCurves
import presentation.components.SidebarContainer
import resources.AppColors
import resources.AppDimensions
import java.awt.Dimension

@Composable
@Preview
fun App() {
    val viewModel = remember { MainViewModel() }
    val drawerState = DrawerState(DrawerValue.Open)
    val pointerDragState by remember { mutableStateOf(PointerDragState()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Base)
    ) {
        SidebarContainer(
            modifier = Modifier.padding(AppDimensions.MediumPadding),
            drawerState = drawerState,
            drawerElevation = 0.dp,
            sidebarContent = { AppSidebarContent(viewModel, drawerState) },
            content = { AppCanvasContent(viewModel, drawerState, pointerDragState) }
        )

        if (viewModel.settings.showTheory) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Black.copy(alpha = 0.4f)
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize().padding(100.dp),
                    color = AppColors.Surface,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    AboutBezierCurves {
                        viewModel.settings.updateShowTheory(it)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
fun main() = application {
    Window(
        alwaysOnTop = true,
        onCloseRequest = ::exitApplication,
        title = "Bezier Curve Learning Tool",
        icon = painterResource(Res.drawable.iconPng)
    ) {
        window.minimumSize = Dimension(900, 600)

        App()
    }
}

