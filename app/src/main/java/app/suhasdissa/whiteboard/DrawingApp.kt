package app.suhasdissa.whiteboard

import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.whiteboard.ui.MainCanvas
import app.suhasdissa.whiteboard.ui.WhiteboardViewModel
import app.suhasdissa.whiteboard.ui.menu.MainToolbar
import app.suhasdissa.whiteboard.ui.menu.PropertiesBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawingApp(vm: WhiteboardViewModel = viewModel()) {

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(stringResource(id = R.string.app_name))
        }, actions = {
            IconButton(onClick = { vm.paths.clear() }) {
                Icon(Icons.Filled.Delete, contentDescription = null)
            }
            IconButton(onClick = {}) {
                Icon(Icons.Filled.Menu, contentDescription = null)
            }
        })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box {
                Box(
                    Modifier
                        .zIndex(1f)
                        .fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    MainCanvas()
                }
                Box(
                    Modifier
                        .zIndex(2f)
                        .fillMaxSize(), contentAlignment = Alignment.CenterStart
                ) {
                    MainToolbar(modifier = Modifier)
                }
                Box(
                    Modifier
                        .zIndex(2f)
                        .fillMaxSize(), contentAlignment = Alignment.BottomCenter
                ) {
                    PropertiesBar()
                }
            }
        }
    }
}