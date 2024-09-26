package uz.veolia.cabinet.presenter.phone.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import uz.veolia.cabinet.data.remote.ResultData
import uz.veolia.cabinet.domain.repository.AppRepository
import uz.veolia.cabinet.presenter.phone.CommonPresenter
import uz.veolia.cabinet.ui.intent.CreateAccrualIntent
import uz.veolia.cabinet.ui.navigation.RootConfigPhone
import uz.veolia.cabinet.ui.uistate.CreateAccrualUiState
import uz.veolia.cabinet.util.extension.collectData

class CreateAccrualPhonePresenterImpl(
    consumer: String,
    balance: String,
    name: String,
    componentContext: ComponentContext,
    private val navigator: StackNavigation<RootConfigPhone>,
    private val callBack: (String) -> Unit,
) : CommonPresenter<CreateAccrualIntent, CreateAccrualUiState>(componentContext),
    KoinComponent {
    override val uiState =
        MutableValue(CreateAccrualUiState(consumer = consumer, balance = balance, name = name))
    private val repository by inject<AppRepository>()

    override fun onEventDispatcher(intent: CreateAccrualIntent) {
        when (intent) {
            is CreateAccrualIntent.Create -> {
                uiState.update { it.copy(loading = true) }
                repository.createAccruals(
                    intent.consumer,
                    acc = if (intent.tabPos == 0) "DEBET" else "CREDIT",
                    "0${intent.typePos + 1}",
                    intent.balance
                ).collectData(screenModelScope) {
                    uiState.update { it.copy(loading = false) }
                    when (this) {
                        is ResultData.Message -> {
                            if (code == 401)
                                navigator.replaceAll(RootConfigPhone.Login)
                            uiState.update { it.copy(message = message) }
                        }

                        is ResultData.Success -> {
                            uiState.update { it.copy(success = true, balance = data) }
                            callBack(data)
                            screenModelScope.launch {
                                delay(1000)
                                navigator.pop()
                            }
                        }

                        is ResultData.TimeOut -> uiState.update { it.copy(serverError = true) }
                    }
                }
            }

            CreateAccrualIntent.ToastHide -> uiState.update {
                it.copy(
                    message = null,
                    serverError = false,
                    success = false
                )
            }

            CreateAccrualIntent.Back -> screenModelScope.launch {
                navigator.pop()
            }
        }
    }
}
