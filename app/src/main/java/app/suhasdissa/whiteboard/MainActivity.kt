package app.suhasdissa.whiteboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import app.suhasdissa.whiteboard.ui.theme.WhiteboardAppTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhiteboardAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(

                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(stringResource(id = R.string.app_name))
                                },
                                actions = {}
                            )
                        }
                    ) { paddingValues: PaddingValues ->
                        DrawingApp(paddingValues)
                    }
                }
            }
        }
    }
}