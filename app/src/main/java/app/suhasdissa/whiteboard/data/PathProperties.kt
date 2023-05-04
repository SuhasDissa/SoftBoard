package app.suhasdissa.whiteboard.data

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Shader
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

class PathProperties(
    var path: Path = Path(),
    var strokeWidth: Float = 10f,
    var color: Color = Color.Blue,
    var drawMode: DrawMode = DrawMode.Pen
) {
    fun draw(scope: DrawScope, bitmap: Bitmap? = null) {
        when (drawMode) {
            DrawMode.Pen -> {

                if (bitmap != null) {
                    val brush = ShaderBrush(
                        shader = BitmapShader(
                            bitmap,
                            Shader.TileMode.REPEAT,
                            Shader.TileMode.REPEAT
                        )
                    )
                    scope.drawPath(
                        path, brush, style = Stroke(
                            width = strokeWidth,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                } else {
                    scope.drawPath(
                        color = color,
                        path = path,
                        style = Stroke(
                            width = strokeWidth,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }
            }
            DrawMode.Eraser -> {
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
}