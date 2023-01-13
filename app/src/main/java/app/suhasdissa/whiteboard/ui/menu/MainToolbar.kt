package app.suhasdissa.whiteboard.ui.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import app.suhasdissa.whiteboard.R
import app.suhasdissa.whiteboard.data.DrawMode
import app.suhasdissa.whiteboard.data.PathProperties

@Composable
fun MainToolbar(
    modifier: Modifier = Modifier,
    pathProperties: PathProperties,
    drawMode: DrawMode,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onDrawModeChanged: (DrawMode) -> Unit
) {
    val properties by rememberUpdatedState(newValue = pathProperties)

    var showColorDialog by remember { mutableStateOf(false) }
    var showPropertiesDialog by remember { mutableStateOf(false) }
    var currentDrawMode = drawMode

    ElevatedCard(
        modifier = modifier
            .padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(
                onClick = {
                    currentDrawMode = if (currentDrawMode == DrawMode.Erase) {
                        DrawMode.Draw
                    } else {
                        DrawMode.Erase
                    }
                    onDrawModeChanged(currentDrawMode)
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_eraser_black_24dp),
                    contentDescription = null,
                    tint = if (currentDrawMode == DrawMode.Erase) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = { showColorDialog = !showColorDialog }) {
                Icon(Icons.Filled.WaterDrop, contentDescription = null)
            }

            IconButton(onClick = { showPropertiesDialog = !showPropertiesDialog }) {
                Icon(Icons.Outlined.Circle, contentDescription = null)
            }

            IconButton(onClick = {
                onUndo()
            }) {
                Icon(Icons.Filled.Undo, contentDescription = null)
            }

            IconButton(onClick = {
                onRedo()
            }) {
                Icon(Icons.Filled.Redo, contentDescription = null)
            }
        }
    }


    if (showColorDialog) {
        ColorSelectionDialog(
            properties.color,
            onDismiss = { showColorDialog = !showColorDialog },
            onNegativeClick = { showColorDialog = !showColorDialog },
            onPositiveClick = { color: Color ->
                showColorDialog = !showColorDialog
                properties.color = color
            }
        )
    }

    if (showPropertiesDialog) {
        PropertiesMenuDialog(properties) {
            showPropertiesDialog = !showPropertiesDialog
        }
    }
}
