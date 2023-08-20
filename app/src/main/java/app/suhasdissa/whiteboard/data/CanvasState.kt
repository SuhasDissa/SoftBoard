package app.suhasdissa.whiteboard.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

class CanvasState {
    var scale by mutableFloatStateOf(1f)
    var translation by mutableStateOf(Offset(0f, 0f))
    var pivot by mutableStateOf(Offset(0f, 0f))
    var size: Size? = null
}
