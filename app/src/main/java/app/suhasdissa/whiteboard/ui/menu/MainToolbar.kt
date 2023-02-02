package app.suhasdissa.whiteboard.ui.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import app.suhasdissa.whiteboard.R
import app.suhasdissa.whiteboard.data.DrawMode

@Composable
fun MainToolbar(
    modifier: Modifier = Modifier,
    drawMode: DrawMode,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onDrawModeChanged: (DrawMode) -> Unit
) {
    var currentDrawMode = drawMode

    ElevatedCard(
        modifier = modifier
            .padding(8.dp)
            .fillMaxHeight()
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = {
                currentDrawMode = when (currentDrawMode) {
                    DrawMode.Eraser -> {
                        DrawMode.Pen
                    }
                    DrawMode.Pen -> {
                        DrawMode.Eraser
                    }
                }
                onDrawModeChanged(currentDrawMode)
            }) {
                when (currentDrawMode) {
                    DrawMode.Pen -> {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_eraser_black_24dp),
                            contentDescription = null,
                            tint = if (currentDrawMode == DrawMode.Eraser) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    DrawMode.Eraser -> {
                        Icon(Icons.Filled.Brush, contentDescription = null)
                    }
                }
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
}
