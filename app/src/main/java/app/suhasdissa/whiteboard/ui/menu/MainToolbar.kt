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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.whiteboard.R
import app.suhasdissa.whiteboard.data.DrawMode
import app.suhasdissa.whiteboard.ui.WhiteboardViewModel

@Composable
fun MainToolbar(
    modifier: Modifier = Modifier, vm: WhiteboardViewModel = viewModel()
) {
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
            var drawMode by remember{mutableStateOf(vm.currentPath.drawMode)}
            IconButton(onClick = {
                drawMode = when (drawMode) {
                    DrawMode.Eraser -> {
                        DrawMode.Pen
                    }
                    DrawMode.Pen -> {
                        DrawMode.Eraser
                    }
                }
                vm.currentPath.drawMode = drawMode
            }) {
                when (drawMode) {
                    DrawMode.Pen -> {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_eraser_black_24dp),
                            contentDescription = null,
                            tint = if (drawMode == DrawMode.Eraser) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    DrawMode.Eraser -> {
                        Icon(Icons.Filled.Brush, contentDescription = null)
                    }
                }
            }
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
