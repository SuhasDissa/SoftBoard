package app.suhasdissa.whiteboard

import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.whiteboard.ui.MainCanvas
import app.suhasdissa.whiteboard.ui.WhiteboardViewModel
import app.suhasdissa.whiteboard.ui.components.SaveDialog
import app.suhasdissa.whiteboard.ui.menu.MainToolbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawingApp(vm: WhiteboardViewModel = viewModel()) {
    var showSaveDialog by remember { mutableStateOf(false) }
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(stringResource(id = R.string.app_name))
        }, actions = {
            IconButton(onClick = { vm.paths.clear() }) {
                Icon(Icons.Filled.Delete, contentDescription = null)
            }
            IconButton(onClick = {
                showSaveDialog = true
            }) {
                Icon(Icons.Filled.Save, contentDescription = null)
            }
        })
    }, bottomBar = {
        MainToolbar()
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            MainCanvas()
        }
    }

    if (showSaveDialog) {
        SaveDialog(onDismissRequest = { showSaveDialog = false })
    }
}
