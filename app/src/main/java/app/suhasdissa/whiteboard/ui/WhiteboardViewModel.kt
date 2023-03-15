package app.suhasdissa.whiteboard.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import app.suhasdissa.whiteboard.data.DrawMode
import app.suhasdissa.whiteboard.data.MotionEvent
import app.suhasdissa.whiteboard.data.PathProperties

class WhiteboardViewModel : ViewModel() {
    var paths = mutableStateListOf<PathProperties>()
    var pathsUndone = mutableStateListOf<PathProperties>()
    var motionEvent by mutableStateOf(MotionEvent.Idle)
    var drawMode by mutableStateOf(DrawMode.Pen)
    var currentPath by mutableStateOf(PathProperties())
    var canvasScale by mutableStateOf(value = 1f)

    var currentPosition by mutableStateOf(Offset.Unspecified)
    var canvasTranslate by mutableStateOf(Offset(x = 0f, y = 0f))
    val canvasPivot by mutableStateOf(Offset(x = 0f, y = 0f))
}