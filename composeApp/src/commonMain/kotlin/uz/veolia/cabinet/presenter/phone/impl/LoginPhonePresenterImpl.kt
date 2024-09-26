package uz.veolia.cabinet.presenter.phone.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import uz.veolia.cabinet.data.remote.ResultData
import uz.veolia.cabinet.domain.repository.AppRepository
import uz.veolia.cabinet.presenter.phone.CommonPresenter
import uz.veolia.cabinet.ui.intent.LoginIntent
import uz.veolia.cabinet.ui.navigation.RootConfigPhone
import uz.veolia.cabinet.ui.uistate.LoginUiState
import uz.veolia.cabinet.util.extension.collectData

class LoginPhonePresenterImpl(
    componentContext: ComponentContext,
    private val navigator: StackNavigation<RootConfigPhone>
) : CommonPresenter<LoginIntent, LoginUiState>(componentContext), KoinComponent {
    private val repository by inject<AppRepository>()
    override val uiState = MutableValue(LoginUiState())

    override fun onEventDispatcher(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.Login -> {
                uiState.update { it.copy(loading = true) }
                repository.login(intent.login, intent.password).collectData(screenModelScope) {
                    uiState.update { it.copy(loading = false) }
                    when (this) {
                        is ResultData.Message -> {
                            uiState.update { it.copy(message = message) }
                        }
                        is ResultData.Success -> {
                            navigator.replaceCurrent(RootConfigPhone.Dashboard)
                        }
                        is ResultData.TimeOut -> {
                            uiState.update { it.copy(serverError = true) }
                        }
                    }
                }
            }
            LoginIntent.ToastHide -> uiState.update { it.copy(serverError = false, message = null) }
        }
    }
}
