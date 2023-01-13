package app.suhasdissa.whiteboard.ui.menu

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import app.suhasdissa.whiteboard.R
import app.suhasdissa.whiteboard.data.PathProperties

@Composable
fun PropertiesMenuDialog(pathOption: PathProperties, onDismiss: () -> Unit) {

    var strokeWidth by remember { mutableStateOf(pathOption.strokeWidth) }

    Dialog(onDismissRequest = onDismiss) {
        ElevatedCard {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = stringResource(R.string.brush_settings_dialog),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(start = 12.dp, top = 12.dp)
                )
                Canvas(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    val path = Path()
                    path.moveTo(0f, size.height / 2)
                    path.lineTo(size.width, size.height / 2)

                    drawPath(
                        color = pathOption.color,
                        path = path,
                        style = Stroke(
                            width = strokeWidth,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }

                Text(
                    text = stringResource(R.string.brush_size) +" ${strokeWidth.toInt()}",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                Slider(
                    value = strokeWidth,
                    onValueChange = {
                        strokeWidth = it
                        pathOption.strokeWidth = strokeWidth
                    },
                    valueRange = 1f..100f,
                    onValueChangeFinished = {}
                )
            }
        }
    }
}
