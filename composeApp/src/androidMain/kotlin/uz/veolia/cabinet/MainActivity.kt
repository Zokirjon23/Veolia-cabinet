package uz.veolia.cabinet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.retainedComponent
import uz.veolia.cabinet.ui.navigation.RootComponentPhone
import uz.veolia.cabinet.ui.navigation.RootComponentPhoneImpl

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = retainedComponent { RootComponentPhoneImpl(it) }
        enableEdgeToEdge()
        setContent {
            App(root)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {

}