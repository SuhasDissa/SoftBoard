package app.suhasdissa.whiteboard.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.ColorUtils.HSLToColor
import androidx.core.graphics.ColorUtils.colorToHSL
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.whiteboard.R
import app.suhasdissa.whiteboard.ui.WhiteboardViewModel
import app.suhasdissa.whiteboard.ui.menu.items.HueBar

@Composable
fun ColorSelectionDialog(

    onDismiss: () -> Unit,
    vm: WhiteboardViewModel = viewModel()
) {
    val initialHSL by remember { mutableStateOf(floatArrayOf(0f, 0f, 0f)) }
    colorToHSL(vm.currentPath.color.toArgb(), initialHSL)
    var hue by remember { mutableFloatStateOf(initialHSL[0]) } // [0,360)
    var saturation by remember { mutableFloatStateOf(initialHSL[1]) } // [0,1]
    var lightness by remember { mutableFloatStateOf(initialHSL[2]) } // [0,1]

    val color = Color(HSLToColor(floatArrayOf(hue, saturation, lightness)))

    Dialog(onDismissRequest = onDismiss) {
        ElevatedCard {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.color_picker_dialog),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top = 12.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp, vertical = 20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .background(
                                vm.currentPath.color,
                                shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.old_color), color = Color.White)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .background(
                                color,
                                shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.new_color), color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(R.string.hue_slider))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        HueBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp)
                        )
                        Slider(
                            modifier = Modifier,
                            value = hue,
                            onValueChange = { hue = it },
                            valueRange = 0f..360f,
                            onValueChangeFinished = {}
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(R.string.saturation_slider))
                    Spacer(modifier = Modifier.width(8.dp))
                    Slider(
                        modifier = Modifier.weight(1f),
                        value = saturation,
                        onValueChange = { saturation = it },
                        valueRange = 0f..1f,
                        onValueChangeFinished = {}
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(R.string.lightness_slider))
                    Spacer(modifier = Modifier.width(8.dp))
                    Slider(
                        modifier = Modifier.weight(1f),
                        value = lightness,
                        onValueChange = { lightness = it },
                        valueRange = 0f..1f,
                        onValueChangeFinished = {}
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

                // Buttons

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Text(text = stringResource(R.string.cancel_button))
                    }
                    TextButton(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        onClick = {
                            vm.currentPath.color = color
                            onDismiss()
                        }
                    ) {
                        Text(text = stringResource(R.string.ok_button))
                    }
                }
            }
        }
    }
}
