package uz.veolia.cabinet

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.PredictiveBackGestureIcon
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.PredictiveBackGestureOverlay
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import uz.veolia.cabinet.data.local.ApplicationComponent
import uz.veolia.cabinet.ui.navigation.RootComponentPhone
import uz.veolia.cabinet.ui.navigation.RootComponentPhoneImpl

@OptIn(ExperimentalDecomposeApi::class)
fun MainViewController() = ComposeUIViewController {
    val root = remember { RootComponentPhoneImpl(DefaultComponentContext(LifecycleRegistry())) }
    val back = remember { BackDispatcher() }
    PredictiveBackGestureOverlay(
        backDispatcher = back,
        backIcon = { progress, _ ->
            PredictiveBackGestureIcon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                progress = progress,
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) {
        App(root)
    }
}

fun initialize() {
    ApplicationComponent.init()
}