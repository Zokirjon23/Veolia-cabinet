package uz.veolia.cabinet

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.app_name
import org.jetbrains.compose.resources.stringResource
import uz.veolia.cabinet.data.local.ApplicationComponent
import uz.veolia.cabinet.ui.navigation.RootComponentPhone
import uz.veolia.cabinet.ui.navigation.RootComponentPhoneImpl
import javax.swing.SwingUtilities

fun main() = application {
    ApplicationComponent.init()
    val lifecycle = LifecycleRegistry()
    val root =
        runOnUiThread {
            RootComponentPhoneImpl(
                componentContext = DefaultComponentContext(lifecycle = lifecycle),
            )
        }
    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.app_name),
    ) {
        App(root)
    }
}

internal fun <T> runOnUiThread(block: () -> T): T {
    if (SwingUtilities.isEventDispatchThread()) {
        return block()
    }

    var error: Throwable? = null
    var result: T? = null

    SwingUtilities.invokeAndWait {
        try {
            result = block()
        } catch (e: Throwable) {
            error = e
        }
    }

    error?.also { throw it }

    @Suppress("UNCHECKED_CAST")
    return result as T
}