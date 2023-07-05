package app.suhasdissa.whiteboard.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.whiteboard.ui.WhiteboardViewModel

@Composable
fun ColorButton(
    color: Color,
    vm: WhiteboardViewModel = viewModel()
) {
    Canvas(modifier = Modifier
        .padding(8.dp)
        .size(24.dp)
        .clickable { vm.currentPath.color = color }) {
        drawCircle(
            center = Offset(size.width / 2, size.height / 2),
            color = color,
            radius = size.width / 2,
        )
    }
}