package app.suhasdissa.whiteboard.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.ColorUtils.HSLToColor
import androidx.core.graphics.ColorUtils.colorToHSL
import app.suhasdissa.whiteboard.ui.menu.items.HueBar

@Composable
fun ColorSelectionDialog(
    initialColor: Color,
    onDismiss: () -> Unit,
    onNegativeClick: () -> Unit,
    onPositiveClick: (Color) -> Unit
) {
    val initialHSL by remember { mutableStateOf(floatArrayOf(0f, 0f, 0f)) }
    colorToHSL(initialColor.toArgb(), initialHSL)
    var hue by remember { mutableStateOf(initialHSL[0]) } // [0,360)
    var saturation by remember { mutableStateOf(initialHSL[1]) } //[0,1]
    var lightness by remember { mutableStateOf(initialHSL[2]) }// [0,1]

    val color = Color(HSLToColor(floatArrayOf(hue, saturation, lightness)))

    Dialog(onDismissRequest = onDismiss) {
        BoxWithConstraints(
            Modifier
                .shadow(1.dp, RoundedCornerShape(8.dp))
                .background(Color.White)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {

                Text(
                    text = "Color",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 12.dp)
                )

                // Initial and Current Colors
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
                                initialColor,
                                shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                            ), contentAlignment = Alignment.Center
                    ) {
                        Text("Old Color",color = Color.White)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .background(
                                color, shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                            ), contentAlignment = Alignment.Center
                    ) {
                        Text("New Color",color=Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(text = "Hue")
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        HueBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp)
                        )
                        Slider(modifier = Modifier,
                            value = hue,
                            onValueChange = { hue = it },
                            valueRange = 0f..360f,
                            onValueChangeFinished = {})
                    }

                }
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(text = "Saturation")
                    Spacer(modifier = Modifier.width(8.dp))
                    Slider(modifier = Modifier.weight(1f),
                        value = saturation,
                        onValueChange = { saturation = it },
                        valueRange = 0f..1f,
                        onValueChangeFinished = {})
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(text = "Lightness")
                    Spacer(modifier = Modifier.width(8.dp))
                    Slider(modifier = Modifier.weight(1f),
                        value = lightness,
                        onValueChange = { lightness = it },
                        valueRange = 0f..1f,
                        onValueChangeFinished = {})
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
                        onClick = onNegativeClick, modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Text(text = "CANCEL")
                    }
                    TextButton(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        onClick = {
                            onPositiveClick(color)
                        },
                    ) {
                        Text(text = "OK")
                    }
                }
            }
        }
    }
}