package uz.veolia.cabinet.presenter.phone

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import uz.veolia.cabinet.ui.intent.DashBoardIntent
import uz.veolia.cabinet.ui.uistate.DashBoardUiState

interface DashBoardPhonePresenter {
    val uiState: StateFlow<DashBoardUiState>

    val stack: Value<ChildStack<*, Child>>
    fun onEventDispatch(intent: DashBoardIntent)

    sealed interface Child {
        class ConsumerPhoneTabChild(val presenter: ConsumerTabPhonePresenter) :
            Child

        class MonitoringPhoneTabChild(val presenter: MonitoringTabPhonePresenter) :
            Child

        data object NoAccess : Child
    }

    @Serializable
    sealed interface Config {

        @Serializable
        data object ConsumerPhoneTabConfig : Config

        @Serializable
        data object MonitoringPhoneTabConfig : Config

        @Serializable
        data object NoAccess : Config
    }
}