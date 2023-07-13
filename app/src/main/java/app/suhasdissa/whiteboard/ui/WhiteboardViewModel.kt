package app.suhasdissa.whiteboard.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.suhasdissa.whiteboard.data.MotionEvent
import app.suhasdissa.whiteboard.data.PathProperties
import app.suhasdissa.whiteboard.util.StorageHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileOutputStream


class WhiteboardViewModel : ViewModel() {
    var paths = mutableStateListOf<PathProperties>()
    var pathsUndone = mutableStateListOf<PathProperties>()
    var motionEvent by mutableStateOf(MotionEvent.Idle)
    var currentPath by mutableStateOf(PathProperties())
    var canvasScale by mutableFloatStateOf(value = 1f)

    var canvasSize: Size? = null

    var currentPosition by mutableStateOf(Offset.Unspecified)
    var canvasTranslate by mutableStateOf(Offset(x = 0f, y = 0f))
    val canvasPivot by mutableStateOf(Offset(x = 0f, y = 0f))

    fun saveBitmap() {

        if (canvasSize != null && paths.isNotEmpty()) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val bitmap =
                        Bitmap.createBitmap(
                            canvasSize!!.width.toInt(),
                            canvasSize!!.height.toInt(),
                            Bitmap.Config.ARGB_8888
                        )
                    val canvas = Canvas(bitmap)
                    canvas.translate(canvasTranslate.x, canvasTranslate.y)
                    canvas.scale(canvasScale, canvasScale)
                    val outputStream = FileOutputStream(StorageHelper.getOutputFile("png"))

                    paths.forEach { path ->
                        path.drawNative(canvas)
                    }

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

                    outputStream.flush()
                    outputStream.close()

                }
            }
        }
    }
}