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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.suhasdissa.whiteboard.data.PathProperties

@Composable
fun PropertiesBar(currentPath:PathProperties,canvasScale:Float){
    var showColorDialog by remember { mutableStateOf(false) }
    ElevatedCard(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(0.8f)
    ) {
        Row(Modifier.fillMaxWidth()) {
            var strokeWidth by remember { mutableStateOf(currentPath.strokeWidth) }
            var currentColor by remember { mutableStateOf(currentPath.color) }
            Canvas(modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 20.dp)
                .size(40.dp)
                .clickable { showColorDialog = !showColorDialog }) {
                drawCircle(
                    center = Offset(size.width / 2, size.height / 2),
                    color = currentColor,
                    radius = currentPath.strokeWidth * canvasScale / 2,
                )
            }
            Slider(
                modifier = Modifier.weight(1f),
                value = strokeWidth,
                onValueChange = {
                    strokeWidth = it
                    currentPath.strokeWidth = strokeWidth
                },
                valueRange = 1f..300f
            )

            if (showColorDialog) {
                ColorSelectionDialog(currentColor,
                    onDismiss = { showColorDialog = !showColorDialog },
                    onNegativeClick = { showColorDialog = !showColorDialog },
                    onPositiveClick = { color: Color ->
                        showColorDialog = !showColorDialog
                        currentColor = color
                        currentPath.color = currentColor
                    })
            }
        }
    }
}