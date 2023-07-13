package app.suhasdissa.whiteboard.data

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import android.os.Build
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb

class PathProperties(
    var path: Path = Path(),
    var strokeWidth: Float = 10f,
    var color: Color = Color.Blue,
    var drawMode: DrawMode = DrawMode.Pen
) {
    private val paint: Paint
        get() {
            return if (drawMode == DrawMode.Pen) {
                Paint().apply {
                    color = this@PathProperties.color.toArgb()
                    style = Paint.Style.STROKE
                    strokeWidth = this@PathProperties.strokeWidth
                    strokeCap = Paint.Cap.ROUND
                    strokeJoin = Paint.Join.ROUND
                }
            } else {
                Paint().apply {
                    color = Color.Transparent.toArgb()
                    style = Paint.Style.STROKE
                    strokeWidth = this@PathProperties.strokeWidth
                    strokeCap = Paint.Cap.ROUND
                    strokeJoin = Paint.Join.ROUND
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                        blendMode = android.graphics.BlendMode.CLEAR
                }
            }
        }

    private val androidPath
        get() = path.asAndroidPath()

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

            DrawMode.None -> {}
        }
    }

    fun drawNative(canvas: Canvas) {
        when (drawMode) {
            DrawMode.Pen -> {
                canvas.drawPath(
                    androidPath,
                    paint
                )
            }

            DrawMode.Eraser -> {
                canvas.drawPath(
                    androidPath,
                    paint
                )
            }

            DrawMode.None -> {}
        }
    }
}