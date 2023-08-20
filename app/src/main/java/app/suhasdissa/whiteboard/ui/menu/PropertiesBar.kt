package app.suhasdissa.whiteboard.ui.menu

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.whiteboard.ui.WhiteboardViewModel

@Composable
fun PropertiesBar(vm: WhiteboardViewModel = viewModel()) {
    var brushSize by remember { mutableFloatStateOf(vm.currentPath.strokeWidth) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Canvas(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 20.dp)
                .size(40.dp)
        ) {
            drawCircle(
                center = Offset(size.width / 2, size.height / 2),
                color = vm.currentPath.color,
                radius = brushSize * vm.canvasState.scale / 2
            )
        }
        Slider(
            modifier = Modifier.weight(1f),
            value = brushSize,
            onValueChange = { size ->
                brushSize = size
                vm.currentPath.strokeWidth = brushSize
            },
            valueRange = 1f..300f
        )
    }
}
