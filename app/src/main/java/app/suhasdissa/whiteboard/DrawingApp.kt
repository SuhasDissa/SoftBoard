package app.suhasdissa.whiteboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.suhasdissa.whiteboard.data.DrawMode
import app.suhasdissa.whiteboard.data.MotionEvent
import app.suhasdissa.whiteboard.data.PathProperties
import app.suhasdissa.whiteboard.ui.menu.MainToolbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawingApp() {
    val paths = remember { mutableStateListOf<PathProperties>() }
    val pathsUndone = remember { mutableStateListOf<PathProperties>() }
    var motionEvent by remember { mutableStateOf(MotionEvent.Idle) }
    var currentPosition by remember { mutableStateOf(Offset.Unspecified) }
    var previousPosition by remember { mutableStateOf(Offset.Unspecified) }
    var drawMode by remember { mutableStateOf(DrawMode.Draw) }
    var currentPath by remember { mutableStateOf(PathProperties()) }
    var canvasTranslate by remember { mutableStateOf(Offset(x = 0f, y = 0f)) }
    var canvasScale by remember { mutableStateOf(value = 1f) }
    var canvasPivot by remember { mutableStateOf(Offset(x=0f,y=0f)) }
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

            val drawModifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val downEvent = awaitFirstDown()
                        currentPosition = (downEvent.position - canvasTranslate) / canvasScale
                        motionEvent = MotionEvent.Down
                        if (downEvent.pressed != downEvent.previousPressed) downEvent.consume()
                        var canvasMoved = false
                        do {
                            val event = awaitPointerEvent()
                            if (event.changes.size == 1) {
                                if (canvasMoved) break
                                currentPosition =
                                    (event.changes[0].position - canvasTranslate) / canvasScale
                                motionEvent = MotionEvent.Move
                                if (event.changes[0].positionChange() != Offset.Zero) event.changes[0].consume()
                            } else if (event.changes.size > 1) {
                                //canvasPivot = event.calculateCentroid(true)
                                val zoom = event.calculateZoom()
                                canvasScale *= zoom
                                val offset = event.calculatePan()
                                canvasTranslate += offset //- (canvasPivot / zoom*2f)
                                canvasMoved = true
                            }
                        } while (event.changes.any { it.pressed })
                        motionEvent = MotionEvent.Up
                    }
                }
            ElevatedCard(
                Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Canvas(modifier = drawModifier) {
                    when (motionEvent) {
                        MotionEvent.Down -> {
                            currentPath.path.moveTo(currentPosition.x, currentPosition.y)
                            previousPosition = currentPosition
                        }
                        MotionEvent.Move -> {
                            if(previousPosition != Offset.Unspecified) {
                                currentPath.path.quadraticBezierTo(
                                    x1 = previousPosition.x,
                                    y1 = previousPosition.y,
                                    x2 = (previousPosition.x + currentPosition.x) / 2,
                                    y2 = (previousPosition.y + currentPosition.y) / 2
                                )
                            }
                            previousPosition = currentPosition
                        }
                        MotionEvent.Up -> {
                            currentPath.path.lineTo(currentPosition.x, currentPosition.y)
                            paths.add(currentPath)
                            currentPath = PathProperties(
                                path = Path(),
                                strokeWidth = currentPath.strokeWidth,
                                color = currentPath.color,
                                eraseMode = currentPath.eraseMode
                            )
                            pathsUndone.clear()
                            // reset states
                            currentPosition = Offset.Unspecified
                            previousPosition = Offset.Unspecified
                            motionEvent = MotionEvent.Idle
                        }
                        else -> Unit
                    }
                    withTransform({
                        translate(canvasTranslate.x, canvasTranslate.y)
                        scale(canvasScale, canvasScale, canvasPivot)
                    }) {
                        paths.forEach { path ->
                            path.draw(this)
                        }
                        if (motionEvent != MotionEvent.Idle) {
                            currentPath.draw(this)
                            drawCircle(
                                center = currentPosition,
                                color = Color.Gray,
                                radius = currentPath.strokeWidth / 2,
                                style = Stroke(
                                    width = 1f
                                )
                            )
                        }
                    }
                }
            }
            MainToolbar(modifier = Modifier,
                pathProperties = currentPath,
                drawMode = drawMode,
                onUndo = {
                    if (paths.isNotEmpty()) {
                        pathsUndone.add(paths.last())
                        paths.removeLast()
                    }
                },
                onRedo = {
                    if (pathsUndone.isNotEmpty()) {
                        paths.add(pathsUndone.last())
                        pathsUndone.removeLast()
                    }
                },
                onDrawModeChanged = { mode ->
                    motionEvent = MotionEvent.Idle
                    drawMode = mode
                    currentPath.eraseMode = (drawMode == DrawMode.Erase)
                })
        }
    }

}