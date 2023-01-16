package app.suhasdissa.whiteboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
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
import app.suhasdissa.whiteboard.data.PathProperties
import app.suhasdissa.whiteboard.gesture.MotionEvent
import app.suhasdissa.whiteboard.ui.menu.MainToolbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawingApp() {
    val paths = remember { mutableStateListOf<Pair<Path, PathProperties>>() }
    val pathsUndone = remember { mutableStateListOf<Pair<Path, PathProperties>>() }
    var motionEvent by remember { mutableStateOf(MotionEvent.Idle) }
    var currentPosition by remember { mutableStateOf(Offset.Unspecified) }
    var previousPosition by remember { mutableStateOf(Offset.Unspecified) }
    var drawMode by remember { mutableStateOf(DrawMode.Draw) }
    var currentPath by remember { mutableStateOf(Path()) }
    var currentPathProperty by remember { mutableStateOf(PathProperties()) }
    var canvasTranslate by remember { mutableStateOf(Offset(0f, 0f)) }
    var canvasScale by remember { mutableStateOf(1f) }
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
                        motionEvent = MotionEvent.Down
                        currentPosition = (downEvent.position - canvasTranslate) / canvasScale
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
                                canvasScale *= event.calculateZoom()
                                val offset = event.calculatePan()
                                canvasTranslate += offset
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
                            currentPath.moveTo(currentPosition.x, currentPosition.y)
                            previousPosition = currentPosition
                        }
                        MotionEvent.Move -> {
                            currentPath.quadraticBezierTo(
                                previousPosition.x,
                                previousPosition.y,
                                (previousPosition.x + currentPosition.x) / 2,
                                (previousPosition.y + currentPosition.y) / 2
                            )
                            previousPosition = currentPosition
                        }
                        MotionEvent.Up -> {
                            currentPath.lineTo(currentPosition.x, currentPosition.y)
                            paths.add(Pair(currentPath, currentPathProperty))
                            currentPath = Path()
                            currentPathProperty = PathProperties(
                                strokeWidth = currentPathProperty.strokeWidth,
                                color = currentPathProperty.color,
                                eraseMode = currentPathProperty.eraseMode
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
                        scale(canvasScale, canvasScale, Offset(0f, 0f))
                    }) {
                        paths.forEach {
                            val path = it.first
                            val property = it.second
                            if (!property.eraseMode) {
                                drawPath(
                                    color = property.color, path = path, style = Stroke(
                                        width = property.strokeWidth,
                                        cap = StrokeCap.Round,
                                        join = StrokeJoin.Round
                                    )
                                )
                            } else {
                                // Source
                                drawPath(
                                    color = Color.Transparent, path = path, style = Stroke(
                                        width = currentPathProperty.strokeWidth,
                                        cap = StrokeCap.Round,
                                        join = StrokeJoin.Round
                                    ), blendMode = BlendMode.Clear
                                )
                            }
                        }
                        if (motionEvent != MotionEvent.Idle) {
                            if (!currentPathProperty.eraseMode) {
                                drawPath(
                                    color = currentPathProperty.color,
                                    path = currentPath,
                                    style = Stroke(
                                        width = currentPathProperty.strokeWidth,
                                        cap = StrokeCap.Round,
                                        join = StrokeJoin.Round
                                    )
                                )
                            } else {
                                drawPath(
                                    color = Color.Transparent, path = currentPath, style = Stroke(
                                        width = currentPathProperty.strokeWidth,
                                        cap = StrokeCap.Round,
                                        join = StrokeJoin.Round
                                    ), blendMode = BlendMode.Clear
                                )
                                drawCircle(
                                    center = currentPosition,
                                    color = Color.Black,
                                    radius = currentPathProperty.strokeWidth / 2,
                                    style = Stroke(
                                        width = 2f
                                    )
                                )
                            }
                        }
                    }
                }
            }
            MainToolbar(modifier = Modifier,
                pathProperties = currentPathProperty,
                drawMode = drawMode,
                onUndo = {
                    if (paths.isNotEmpty()) {
                        val lastItem = paths.last()
                        val lastPath = lastItem.first
                        val lastPathProperty = lastItem.second
                        paths.remove(lastItem)
                        pathsUndone.add(Pair(lastPath, lastPathProperty))
                    }
                },
                onRedo = {
                    if (pathsUndone.isNotEmpty()) {
                        val lastPath = pathsUndone.last().first
                        val lastPathProperty = pathsUndone.last().second
                        pathsUndone.removeLast()
                        paths.add(Pair(lastPath, lastPathProperty))
                    }
                },
                onDrawModeChanged = {
                    motionEvent = MotionEvent.Idle
                    drawMode = it
                    currentPathProperty.eraseMode = (drawMode == DrawMode.Erase)
                })
        }
    }

}