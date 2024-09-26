package uz.veolia.cabinet.presenter.phone

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import uz.veolia.cabinet.util.extension.coroutineScope

abstract class CommonPresenter<I, out U : Any>(componentContext: ComponentContext) :
    ComponentContext by componentContext {
    abstract val uiState: Value<U>
    val screenModelScope = coroutineScope(Dispatchers.Main + SupervisorJob())
    abstract fun onEventDispatcher(intent: I)
}