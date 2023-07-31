package app.suhasdissa.whiteboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.unit.dp
import app.suhasdissa.whiteboard.gesture.MotionEvent
import app.suhasdissa.whiteboard.gesture.dragMotionEvent
import app.suhasdissa.whiteboard.model.PathProperties
import app.suhasdissa.whiteboard.ui.menu.DrawingPropertiesMenu

@Composable
fun DrawingApp(paddingValues: PaddingValues) {

    /**
     * Paths that are added, this is required to have paths with different options and paths
     *  ith erase to keep over each other
     */
    val paths = remember { mutableStateListOf<Pair<Path, PathProperties>>() }

    /**
     * Paths that are undone via button. These paths are restored if user pushes
     * redo button if there is no new path drawn.
     *
     * If new path is drawn after this list is cleared to not break paths after undoing previous
     * ones.
     */
    val pathsUndone = remember { mutableStateListOf<Pair<Path, PathProperties>>() }

    /**
     * Canvas touch state. [MotionEvent.Idle] by default, [MotionEvent.Down] at first contact,
     * [MotionEvent.Move] while dragging and [MotionEvent.Up] when first pointer is up
     */
    var motionEvent by remember { mutableStateOf(MotionEvent.Idle) }

    /**
     * Current position of the pointer that is pressed or being moved
     */
    var currentPosition by remember { mutableStateOf(Offset.Unspecified) }

    /**
     * Previous motion event before next touch is saved into this current position.
     */
    var previousPosition by remember { mutableStateOf(Offset.Unspecified) }

    /**
     * Draw mode, erase mode or touch mode to
     */
    var drawMode by remember { mutableStateOf(DrawMode.Draw) }

    /**
     * Path that is being drawn between [MotionEvent.Down] and [MotionEvent.Up]. When
     * pointer is up this path is saved to **paths** and new instance is created
     */
    var currentPath by remember { mutableStateOf(Path()) }

    /**
     * Properties of path that is currently being drawn between
     * [MotionEvent.Down] and [MotionEvent.Up].
     */
    var currentPathProperty by remember { mutableStateOf(PathProperties()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        val drawModifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .dragMotionEvent(onDragStart = { pointerInputChange ->
                motionEvent = MotionEvent.Down
                currentPosition = pointerInputChange.position
                if (pointerInputChange.pressed != pointerInputChange.previousPressed) pointerInputChange.consume()

            }, onDrag = { pointerInputChange ->
                motionEvent = MotionEvent.Move
                currentPosition = pointerInputChange.position

                if (drawMode == DrawMode.Touch) {
                    val change = pointerInputChange.positionChange()
                    println("DRAG: $change")
                    paths.forEach { entry ->
                        val path: Path = entry.first
                        path.translate(change)
                    }
                    currentPath.translate(change)
                }
                if (pointerInputChange.positionChange() != Offset.Zero) pointerInputChange.consume()

            }, onDragEnd = { pointerInputChange ->
                motionEvent = MotionEvent.Up
                if (pointerInputChange.pressed != pointerInputChange.previousPressed) pointerInputChange.consume()
            })
        ElevatedCard(
            Modifier
                .background(Color.White)
                .weight(1f)
                .padding(8.dp)
        ) {
            Canvas(modifier = drawModifier) {

                when (motionEvent) {

                    MotionEvent.Down -> {
                        if (drawMode != DrawMode.Touch) {
                            currentPath.moveTo(currentPosition.x, currentPosition.y)
                        }

                        previousPosition = currentPosition

                    }
                    MotionEvent.Move -> {

                        if (drawMode != DrawMode.Touch) {
                            currentPath.quadraticBezierTo(
                                previousPosition.x,
                                previousPosition.y,
                                (previousPosition.x + currentPosition.x) / 2,
                                (previousPosition.y + currentPosition.y) / 2

                            )
                        }

                        previousPosition = currentPosition
                    }

                    MotionEvent.Up -> {
                        if (drawMode != DrawMode.Touch) {
                            currentPath.lineTo(currentPosition.x, currentPosition.y)

                            // Pointer is up save current path
//                        paths[currentPath] = currentPathProperty
                            paths.add(Pair(currentPath, currentPathProperty))

                            // Since paths are keys for map, use new one for each key
                            // and have separate path for each down-move-up gesture cycle
                            currentPath = Path()

                            // Create new instance of path properties to have new path and properties
                            // only for the one currently being drawn
                            currentPathProperty = PathProperties(
                                strokeWidth = currentPathProperty.strokeWidth,
                                color = currentPathProperty.color,
                                strokeCap = currentPathProperty.strokeCap,
                                strokeJoin = currentPathProperty.strokeJoin,
                                eraseMode = currentPathProperty.eraseMode
                            )
                        }

                        // Since new path is drawn no need to store paths to undone
                        pathsUndone.clear()

                        // If we leave this state at MotionEvent.Up it causes current path to draw
                        // line from (0,0) if this composable recomposes when draw mode is changed
                        currentPosition = Offset.Unspecified
                        previousPosition = currentPosition
                        motionEvent = MotionEvent.Idle
                    }
                    else -> Unit
                }

                with(drawContext.canvas.nativeCanvas) {

                    val checkPoint = saveLayer(null, null)

                    paths.forEach {

                        val path = it.first
                        val property = it.second

                        if (!property.eraseMode) {
                            drawPath(
                                color = property.color, path = path, style = Stroke(
                                    width = property.strokeWidth,
                                    cap = property.strokeCap,
                                    join = property.strokeJoin
                                )
                            )
                        } else {

                            // Source
                            drawPath(
                                color = Color.Transparent, path = path, style = Stroke(
                                    width = currentPathProperty.strokeWidth,
                                    cap = currentPathProperty.strokeCap,
                                    join = currentPathProperty.strokeJoin
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
                                    cap = currentPathProperty.strokeCap,
                                    join = currentPathProperty.strokeJoin
                                )
                            )
                        } else {
                            drawPath(
                                color = Color.Transparent, path = currentPath, style = Stroke(
                                    width = currentPathProperty.strokeWidth,
                                    cap = currentPathProperty.strokeCap,
                                    join = currentPathProperty.strokeJoin
                                ), blendMode = BlendMode.Clear
                            )
                        }
                    }
                    restoreToCount(checkPoint)
                }

                // 🔥🔥 This is for debugging
//            canvasText.clear()
//
//            paths.forEach {
//                val path = it.first
//                val property = it.second
//
//                canvasText.append(
//                    "pHash: ${path.hashCode()}, " +
//                            "propHash: ${property.hashCode()}, " +
//                            "Mode: ${property.eraseMode}\n"
//                )
//            }
//
//            canvasText.append(
//                "🔥 pHash: ${currentPath.hashCode()}, " +
//                        "propHash: ${currentPathProperty.hashCode()}, " +
//                        "Mode: ${currentPathProperty.eraseMode}\n"
//            )
//
//            drawText(text = canvasText.toString(), x = 0f, y = 60f, paint)
            }
        }
        DrawingPropertiesMenu(modifier = Modifier,
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
            onPathPropertiesChange = {
                motionEvent = MotionEvent.Idle
            },
            onDrawModeChanged = {
                motionEvent = MotionEvent.Idle
                drawMode = it
                currentPathProperty.eraseMode = (drawMode == DrawMode.Erase)
            })
    }
}