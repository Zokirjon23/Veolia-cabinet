package uz.veolia.cabinet.presenter.phone.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import uz.veolia.cabinet.data.remote.ResultData
import uz.veolia.cabinet.domain.repository.AppRepository
import uz.veolia.cabinet.presenter.phone.CommonPresenter
import uz.veolia.cabinet.ui.intent.AccrualIntent
import uz.veolia.cabinet.ui.navigation.RootConfigPhone
import uz.veolia.cabinet.ui.uistate.AccrualUiState
import uz.veolia.cabinet.util.extension.collectData

class AccrualPhonePresenterImpl(
    componentContext: ComponentContext,
    private val navigator: StackNavigation<RootConfigPhone>,
    private val consumer: String,
    private val balance: String,
    private val name: String,
    private val onBalanceChange: (String) -> Unit,
) : CommonPresenter<AccrualIntent, AccrualUiState>(componentContext), KoinComponent {
    override val uiState = MutableValue(AccrualUiState(name = name))
    private val repository by inject<AppRepository>()

    init {
        getAccrual()
    }

    override fun onEventDispatcher(intent: AccrualIntent) {
        when (intent) {
            is AccrualIntent.AccrualDateChange -> {
                uiState.update {
                    it.copy(
                        loading = true,
                        list = listOf(),
                        year = intent.year.toString()
                    )
                }
                repository.getAccruals(consumer, intent.year).collectData(screenModelScope) {
                    uiState.update { it.copy(loading = false) }
                    when (this) {
                        is ResultData.Message -> {
                            if (code == 401)
                                navigator.replaceAll(RootConfigPhone.Login)
                            uiState.update { it.copy(message = message) }
                        }

                        is ResultData.Success -> uiState.update { it.copy(list = data) }
                        is ResultData.TimeOut -> uiState.update { it.copy(serverError = true) }
                    }
                }
            }

            AccrualIntent.OpenCreateAccrualScreen -> navigator.push(
                RootConfigPhone.CreateAccrual(
                    consumer,
                    balance,
                    name,
                    callBack = { balance ->
                        onBalanceChange(balance)
                        getAccrual()
                    })
            )

            AccrualIntent.Back -> navigator.pop()
        }
    }

    private fun getAccrual() {
        uiState.update { it.copy(loading = true) }
        repository.getAccruals(consumer, uiState.value.year.toInt())
            .collectData(screenModelScope) {
                uiState.update { it.copy(loading = false) }
                when (this) {
                    is ResultData.Message -> {
                        uiState.update { it.copy(message = message) }
                        if (code == 401)
                            navigator.replaceAll(RootConfigPhone.Login)
                    }

                    is ResultData.Success -> uiState.update { it.copy(list = data) }
                    is ResultData.TimeOut -> uiState.update { it.copy(serverError = true) }
                }
            }
    }
}