package app.suhasdissa.whiteboard.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.suhasdissa.whiteboard.data.CanvasState
import app.suhasdissa.whiteboard.data.ExportFormat
import app.suhasdissa.whiteboard.data.MotionEvent
import app.suhasdissa.whiteboard.data.PathProperties
import app.suhasdissa.whiteboard.util.StorageHelper
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WhiteboardViewModel : ViewModel() {
    var paths = mutableStateListOf<PathProperties>()
    var pathsUndone = mutableStateListOf<PathProperties>()
    var motionEvent by mutableStateOf(MotionEvent.Idle)
    var currentPath by mutableStateOf(PathProperties())

    var currentPosition by mutableStateOf(Offset.Unspecified)

    val canvasState = CanvasState()

    fun saveBitmap(format: ExportFormat) {
        if (canvasState.size != null && paths.isNotEmpty()) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val bitmap =
                        Bitmap.createBitmap(
                            canvasState.size!!.width.toInt(),
                            canvasState.size!!.height.toInt(),
                            Bitmap.Config.ARGB_8888
                        )
                    val canvas = Canvas(bitmap)
                    format.backgroundColor?.let { bgColor ->
                        canvas.drawPaint(bgColor)
                    }
                    canvas.translate(canvasState.translation.x, canvasState.translation.y)
                    canvas.scale(canvasState.scale, canvasState.scale)
                    val outputStream =
                        FileOutputStream(StorageHelper.getOutputFile(format.extension))

                    paths.forEach { path ->
                        path.drawNative(canvas)
                    }

                    bitmap.compress(format.format, 100, outputStream)

                    outputStream.flush()
                    outputStream.close()
                }
            }
        }
    }
}
