package app.suhasdissa.whiteboard.data

import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

class PathProperties(
    var path: Path = Path(),
    var strokeWidth: Float = 10f,
    var color: Color = Color.Blue,
    var eraseMode: Boolean = false
){
    fun draw(scope: DrawScope){
        if (!eraseMode) {
            scope.drawPath(
                color = color,
                path = path,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        } else {
            scope.drawPath(
                color = Color.Transparent,
                path = path,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                ),
                blendMode = BlendMode.Clear
            )
        }
    }
}