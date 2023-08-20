package app.suhasdissa.whiteboard.ui.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.whiteboard.R
import app.suhasdissa.whiteboard.data.DrawMode
import app.suhasdissa.whiteboard.ui.WhiteboardViewModel
import app.suhasdissa.whiteboard.ui.components.ColorButton
import app.suhasdissa.whiteboard.ui.components.ToggleIconButton
import app.suhasdissa.whiteboard.ui.menu.items.rainbowColors

@Composable
fun MainToolbar(
    vm: WhiteboardViewModel = viewModel()
) {
    var showColorDialog by remember { mutableStateOf(false) }
    var showProperties by remember { mutableStateOf(false) }
    var drawMode by remember { mutableStateOf(vm.currentPath.drawMode) }
    var showBrushes by remember { mutableStateOf(drawMode == DrawMode.Pen) }
    fun switchDrawMode(newDrawMode: DrawMode) {
        showBrushes = (newDrawMode == DrawMode.Pen)
        drawMode = newDrawMode
        vm.currentPath.drawMode = drawMode
    }
    Surface(color = MaterialTheme.colorScheme.primaryContainer) {
        Column {
            AnimatedVisibility(visible = showProperties) {
                PropertiesBar()
            }
            AnimatedVisibility(visible = showBrushes) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ToggleIconButton(
                        onClick = { showProperties = !showProperties },
                        isToggled = showProperties,
                        imageVector = Icons.Default.Tune,
                        contentDescription = R.string.change_brush_width
                    )
                    IconButton(
                        onClick = { showColorDialog = true },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = "Custom Color"
                        )
                    }
                    LazyRow(contentPadding = PaddingValues(vertical = 8.dp)) {
                        items(rainbowColors) {
                            ColorButton(color = it)
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ToggleIconButton(
                    onClick = {
                        switchDrawMode(DrawMode.None)
                    },
                    isToggled = (drawMode == DrawMode.None),
                    imageVector = Icons.Default.PanTool,
                    contentDescription = R.string.pan_mode
                )
                ToggleIconButton(
                    onClick = {
                        switchDrawMode(DrawMode.Pen)
                    },
                    isToggled = (drawMode == DrawMode.Pen),
                    imageVector = Icons.Default.Draw,
                    contentDescription = R.string.pan_mode
                )
                ToggleIconButton(
                    onClick = {
                        switchDrawMode(DrawMode.Eraser)
                    },
                    isToggled = (drawMode == DrawMode.Eraser),
                    painter = painterResource(id = R.drawable.ic_eraser_black_24dp),
                    contentDescription = R.string.pan_mode
                )
                IconButton(onClick = {
                    if (vm.paths.isNotEmpty()) {
                        vm.pathsUndone.add(vm.paths.last())
                        vm.paths.removeLast()
                    }
                }) {
                    Icon(Icons.Filled.Undo, contentDescription = null)
                }

                IconButton(onClick = {
                    if (vm.pathsUndone.isNotEmpty()) {
                        vm.paths.add(vm.pathsUndone.last())
                        vm.pathsUndone.removeLast()
                    }
                }) {
                    Icon(Icons.Filled.Redo, contentDescription = null)
                }
            }
        }
    }
    if (showColorDialog) {
        ColorSelectionDialog(onDismiss = { showColorDialog = !showColorDialog })
    }
}

@Preview(showBackground = true)
@Composable
private fun MainToolbarPreview() {
    MainToolbar()
}
