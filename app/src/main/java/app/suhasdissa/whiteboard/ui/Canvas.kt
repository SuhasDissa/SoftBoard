package app.suhasdissa.whiteboard.ui


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.whiteboard.data.MotionEvent
import app.suhasdissa.whiteboard.data.PathProperties

@Composable
fun MainCanvas(
    vm: WhiteboardViewModel = viewModel()
) {
    //val context = LocalContext.current

    // TODO : Textured Brushes
    // val bitmap = remember { BitmapFactory.decodeResource(context.resources, R.drawable.chalk100) }

    val drawModifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .pointerInput(Unit) {
            awaitEachGesture {
                val downEvent = awaitFirstDown()
                vm.currentPosition = (downEvent.position - vm.canvasTranslate) / vm.canvasScale
                vm.motionEvent = MotionEvent.Down
                if (downEvent.pressed != downEvent.previousPressed) downEvent.consume()
                var canvasMoved = false
                do {
                    val event = awaitPointerEvent()
                    if (event.changes.size == 1) {
                        if (canvasMoved) break
                        vm.currentPosition =
                            (event.changes[0].position - vm.canvasTranslate) / vm.canvasScale
                        vm.motionEvent = MotionEvent.Move
                        if (event.changes[0].positionChange() != Offset.Zero) event.changes[0].consume()
                    } else if (event.changes.size > 1) {
                        //canvasPivot = event.calculateCentroid(true)
                        val zoom = event.calculateZoom()
                        vm.canvasScale *= zoom
                        val offset = event.calculatePan()
                        vm.canvasTranslate += offset //- (canvasPivot / zoom*2f)
                        canvasMoved = true
                    }
                } while (event.changes.any { it.pressed })
                vm.motionEvent = MotionEvent.Up

            }
        }
    Canvas(modifier = drawModifier) {
        withTransform({
            translate(vm.canvasTranslate.x, vm.canvasTranslate.y)
            scale(vm.canvasScale, vm.canvasScale, vm.canvasPivot)
        }) {
            with(drawContext.canvas.nativeCanvas) {
                val checkPoint = saveLayer(null, null)
                when (vm.motionEvent) {
                    MotionEvent.Idle -> Unit
                    MotionEvent.Down -> {
                        vm.paths.add(vm.currentPath)
                        vm.currentPath.path.moveTo(
                            vm.currentPosition.x, vm.currentPosition.y
                        )
                    }

                    MotionEvent.Move -> {
                        vm.currentPath.path.lineTo(
                            vm.currentPosition.x, vm.currentPosition.y
                        )
                        drawCircle(
                            center = vm.currentPosition,
                            color = Color.Gray,
                            radius = vm.currentPath.strokeWidth / 2,
                            style = Stroke(
                                width = 1f
                            )
                        )
                    }

                    MotionEvent.Up -> {
                        vm.currentPath.path.lineTo(
                            vm.currentPosition.x, vm.currentPosition.y
                        )
                        vm.currentPath = PathProperties(
                            path = Path(),
                            strokeWidth = vm.currentPath.strokeWidth,
                            color = vm.currentPath.color,
                            drawMode = vm.currentPath.drawMode
                        )
                        vm.pathsUndone.clear()
                        vm.currentPosition = Offset.Unspecified
                        vm.motionEvent = MotionEvent.Idle
                    }
                }
                vm.paths.forEach { path ->
                    path.draw(this@withTransform /*, bitmap*/)
                }
                restoreToCount(checkPoint)
            }
        }
    }
}