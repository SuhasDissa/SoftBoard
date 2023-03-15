package app.suhasdissa.whiteboard.ui.menu

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Slider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.whiteboard.ui.WhiteboardViewModel

@Composable
fun PropertiesBar(vm: WhiteboardViewModel = viewModel()) {
    var showColorDialog by remember { mutableStateOf(false) }
    var brushSize by remember { mutableStateOf(vm.currentPath.strokeWidth) }
    ElevatedCard(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(0.8f)
    ) {
        Row(Modifier.fillMaxWidth()) {
            Canvas(modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 20.dp)
                .size(40.dp)
                .clickable { showColorDialog = !showColorDialog }) {
                drawCircle(
                    center = Offset(size.width / 2, size.height / 2),
                    color = vm.currentPath.color,
                    radius = brushSize * vm.canvasScale / 2,
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

            if (showColorDialog) {
                ColorSelectionDialog(onDismiss = { showColorDialog = !showColorDialog })
            }
        }
    }
}