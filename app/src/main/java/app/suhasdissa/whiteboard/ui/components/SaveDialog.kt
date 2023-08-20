package app.suhasdissa.whiteboard.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.whiteboard.data.ExportFormat
import app.suhasdissa.whiteboard.ui.WhiteboardViewModel

@Composable
fun SaveDialog(
    onDismissRequest: () -> Unit,
    whiteboardViewModel: WhiteboardViewModel = viewModel()
) {
    var exportFormat by remember { mutableStateOf(ExportFormat.PNG) }
    val context = LocalContext.current
    Dialog(onDismissRequest) {
        Surface(
            modifier = Modifier.width(300.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text("Select Export Format", style = MaterialTheme.typography.titleLarge)
                ChipSelector(
                    title = "Export Format",
                    selectedValue = exportFormat,
                    onSelectionChanged = { exportFormat = it }
                )

                Row(Modifier.fillMaxWidth()) {
                    Button(onClick = { onDismissRequest() }) {
                        Text("Cancel")
                    }
                    Button(onClick = {
                        whiteboardViewModel.saveBitmap(exportFormat)
                        Toast.makeText(context, "File Save Successful", Toast.LENGTH_LONG).show()
                        onDismissRequest()
                    }) {
                        Text("Save")
                    }
                }
            }
        }
    }
}
