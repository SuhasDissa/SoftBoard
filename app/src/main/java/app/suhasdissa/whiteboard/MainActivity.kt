package app.suhasdissa.whiteboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import app.suhasdissa.whiteboard.ui.theme.WhiteboardAppTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhiteboardAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(

                        topBar = {
                            TopAppBar(
                                title = {
                                    Text("Drawing App")
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