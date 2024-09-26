package uz.veolia.cabinet.presenter.phone.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import uz.veolia.cabinet.presenter.phone.DashBoardPhonePresenter
import uz.veolia.cabinet.ui.intent.DashBoardIntent
import uz.veolia.cabinet.ui.navigation.RootConfigPhone
import uz.veolia.cabinet.ui.navigation.consumerTabPresenterPhone
import uz.veolia.cabinet.ui.navigation.monitoringTabPresenterPhone
import uz.veolia.cabinet.ui.uistate.DashBoardUiState
import uz.veolia.cabinet.util.extension.coroutineScope

class DashBoardPhonePresenterImpl(
    componentContext: ComponentContext,
    private val navigator: StackNavigation<RootConfigPhone>,
) : DashBoardPhonePresenter, ComponentContext by componentContext {
    private val screenModelScope = coroutineScope(Dispatchers.Main + SupervisorJob())
    override val uiState = MutableStateFlow(DashBoardUiState())
    private val navigation = StackNavigation<DashBoardPhonePresenter.Config>()
    override val stack: Value<ChildStack<*, DashBoardPhonePresenter.Child>> =
        childStack(
            source = navigation,
            serializer = DashBoardPhonePresenter.Config.serializer(),
            initialConfiguration = DashBoardPhonePresenter.Config.ConsumerPhoneTabConfig,
            handleBackButton = true,
            childFactory = ::child,
        )

    private fun child(
        config: DashBoardPhonePresenter.Config,
        componentContext: ComponentContext
    ): DashBoardPhonePresenter.Child {
        return when (config) {
            DashBoardPhonePresenter.Config.ConsumerPhoneTabConfig -> DashBoardPhonePresenter.Child.ConsumerPhoneTabChild(
                consumerTabPresenterPhone(componentContext, navigator)
            )

            DashBoardPhonePresenter.Config.MonitoringPhoneTabConfig -> DashBoardPhonePresenter.Child.MonitoringPhoneTabChild(
                monitoringTabPresenterPhone(componentContext, navigator)
            )

            DashBoardPhonePresenter.Config.NoAccess -> DashBoardPhonePresenter.Child.NoAccess
        }
    }


    init {
        doOnDestroy { screenModelScope.cancel() }
    }

    override fun onEventDispatch(intent: DashBoardIntent) {
        when (intent) {
            is DashBoardIntent.BottomTabSelected -> {
                uiState.update { it.copy(bottomTab = intent.index) }
                navigation.bringToFront(if (intent.index == 0) DashBoardPhonePresenter.Config.ConsumerPhoneTabConfig else DashBoardPhonePresenter.Config.MonitoringPhoneTabConfig)
            }

            DashBoardIntent.AddConsumer -> navigator.push(RootConfigPhone.CreateConsumer)
        }
    }
}