package jimmy.assesment.placeholder.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import jimmy.assesment.placeholder.ui.posts.PostsScreen
import jimmy.assesment.placeholder.ui.theme.PlaceholderTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlaceholderTheme {
                PostsScreen()
            }
        }
    }
}
