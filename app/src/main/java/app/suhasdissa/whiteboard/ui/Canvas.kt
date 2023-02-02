@file:Suppress("NAME_SHADOWING")

package app.suhasdissa.whiteboard.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import app.suhasdissa.whiteboard.data.MotionEvent
import app.suhasdissa.whiteboard.data.PathProperties

@Composable
fun MainCanvas(
    motionEvent: MotionEvent,
    paths: SnapshotStateList<PathProperties>,
    currentPath: PathProperties,
    pathsUndone: SnapshotStateList<PathProperties>,
    canvasScale: Float,
    onScaleChange: (Float) -> Unit,
    onPathPropertyChange: (PathProperties) -> Unit,
    onMotionEventChange: (MotionEvent) -> Unit
) {

    var currentPosition by remember { mutableStateOf(Offset.Unspecified) }
    var canvasTranslate by remember { mutableStateOf(Offset(x = 0f, y = 0f)) }
    val canvasPivot by remember { mutableStateOf(Offset(x = 0f, y = 0f)) }
    var canvasScale by remember { mutableStateOf(canvasScale) }
    var currentPath by remember { mutableStateOf(currentPath) }
    var motionEvent by remember { mutableStateOf(motionEvent) }

    val drawModifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .pointerInput(Unit) {
            awaitEachGesture {
                val downEvent = awaitFirstDown()
                currentPosition = (downEvent.position - canvasTranslate) / canvasScale
                motionEvent = MotionEvent.Down
                onMotionEventChange(motionEvent)
                if (downEvent.pressed != downEvent.previousPressed) downEvent.consume()
                var canvasMoved = false
                do {
                    val event = awaitPointerEvent()
                    if (event.changes.size == 1) {
                        if (canvasMoved) break
                        currentPosition =
                            (event.changes[0].position - canvasTranslate) / canvasScale
                        motionEvent = MotionEvent.Move
                        onMotionEventChange(motionEvent)
                        if (event.changes[0].positionChange() != Offset.Zero) event.changes[0].consume()
                    } else if (event.changes.size > 1) {
                        //canvasPivot = event.calculateCentroid(true)
                        val zoom = event.calculateZoom()
                        canvasScale *= zoom
                        onScaleChange(canvasScale)
                        val offset = event.calculatePan()
                        canvasTranslate += offset //- (canvasPivot / zoom*2f)
                        canvasMoved = true
                    }
                } while (event.changes.any { it.pressed })
                motionEvent = MotionEvent.Up
                onMotionEventChange(motionEvent)

            }
        }
    Canvas(modifier = drawModifier) {

        withTransform({
            translate(canvasTranslate.x, canvasTranslate.y)
            scale(canvasScale, canvasScale, canvasPivot)
        }) {
            with(drawContext.canvas.nativeCanvas) {
                val checkPoint = saveLayer(null, null)
                when (motionEvent) {
                    MotionEvent.Idle -> Unit
                    MotionEvent.Down -> {
                        paths.add(currentPath)
                        currentPath.path.moveTo(
                            currentPosition.x, currentPosition.y
                        )
                    }
                    MotionEvent.Move -> {
                        currentPath.path.lineTo(
                            currentPosition.x, currentPosition.y
                        )
                        drawCircle(
                            center = currentPosition,
                            color = Color.Gray,
                            radius = currentPath.strokeWidth / 2,
                            style = Stroke(
                                width = 1f
                            )
                        )
                    }
                    MotionEvent.Up -> {
                        currentPath.path.lineTo(
                            currentPosition.x, currentPosition.y
                        )
                        currentPath = PathProperties(
                            path = Path(),
                            strokeWidth = currentPath.strokeWidth,
                            color = currentPath.color,
                            drawMode = currentPath.drawMode
                        )
                        onPathPropertyChange(currentPath)
                        pathsUndone.clear()
                        currentPosition = Offset.Unspecified
                        motionEvent = MotionEvent.Idle
                        onMotionEventChange(motionEvent)
                    }
                }
                paths.forEach { path ->
                    path.draw(this@withTransform)
                }
                restoreToCount(checkPoint)
            }
        }
    }
}