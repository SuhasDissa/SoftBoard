package app.suhasdissa.whiteboard.ui.menu.items

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val rainbowColors = listOf(
    Color(0xFFFF0040),
    Color(0xFFFF00FF),
    Color(0xFF8000FF),
    Color(0xFF0000FF),
    Color(0xFF0080FF),
    Color(0xFF00FFFF),
    Color(0xFF00FF80),
    Color(0xFF00FF00),
    Color(0xFF80FF00),
    Color(0xFFFFFF00),
    Color(0xFFFF8000),
    Color(0xFFFF0000)
)

@Composable
fun HueBar(modifier: Modifier = Modifier) {

    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        drawRoundRect(
            brush = Brush.horizontalGradient(colors = rainbowColors.asReversed()),
            size = Size(canvasWidth, canvasHeight),
            cornerRadius = CornerRadius(.5f, .5f)
        )
    }
}