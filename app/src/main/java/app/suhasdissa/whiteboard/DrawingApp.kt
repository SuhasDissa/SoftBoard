package app.suhasdissa.whiteboard

import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import app.suhasdissa.whiteboard.data.DrawMode
import app.suhasdissa.whiteboard.data.MotionEvent
import app.suhasdissa.whiteboard.data.PathProperties
import app.suhasdissa.whiteboard.ui.MainCanvas
import app.suhasdissa.whiteboard.ui.menu.MainToolbar
import app.suhasdissa.whiteboard.ui.menu.PropertiesBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawingApp() {
    val paths = remember { mutableStateListOf<PathProperties>() }
    val pathsUndone = remember { mutableStateListOf<PathProperties>() }
    var motionEvent by remember { mutableStateOf(MotionEvent.Idle) }
    var drawMode by remember { mutableStateOf(DrawMode.Pen) }
    var currentPath by remember { mutableStateOf(PathProperties()) }
    var canvasScale by remember { mutableStateOf(value = 1f) }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(stringResource(id = R.string.app_name))
        }, actions = {
            IconButton(onClick = { paths.clear() }) {
                Icon(Icons.Filled.Delete, contentDescription = null)
            }
            IconButton(onClick = { }) {
                Icon(Icons.Filled.Menu, contentDescription = null)
            }
        })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box {
                Box(
                    Modifier
                        .zIndex(1f)
                        .fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    MainCanvas(motionEvent = motionEvent,
                        paths = paths,
                        currentPath = currentPath,
                        pathsUndone = pathsUndone,
                        canvasScale = canvasScale,
                        onScaleChange = { canvasScale = it },
                        onMotionEventChange = { motionEvent = it },
                        onPathPropertyChange = { currentPath = it })
                }
                Box(
                    Modifier
                        .zIndex(2f)
                        .fillMaxSize(), contentAlignment = Alignment.CenterStart
                ) {
                    MainToolbar(modifier = Modifier, drawMode = drawMode, onUndo = {
                        if (paths.isNotEmpty()) {
                            pathsUndone.add(paths.last())
                            paths.removeLast()
                        }
                    }, onRedo = {
                        if (pathsUndone.isNotEmpty()) {
                            paths.add(pathsUndone.last())
                            pathsUndone.removeLast()
                        }
                    }) { mode ->
                        motionEvent = MotionEvent.Idle
                        drawMode = mode
                        currentPath.drawMode = drawMode
                    }
                }
                Box(
                    Modifier
                        .zIndex(2f)
                        .fillMaxSize(), contentAlignment = Alignment.BottomCenter
                ) {
                    PropertiesBar(currentPath = currentPath, canvasScale = canvasScale)
                }
            }
        }
    }
}