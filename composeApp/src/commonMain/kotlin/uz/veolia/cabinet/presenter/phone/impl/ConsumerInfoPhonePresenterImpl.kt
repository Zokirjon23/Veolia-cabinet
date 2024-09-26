package uz.veolia.cabinet.presenter.phone.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import uz.veolia.cabinet.data.remote.ResultData
import uz.veolia.cabinet.domain.repository.AppRepository
import uz.veolia.cabinet.presenter.phone.CommonPresenter
import uz.veolia.cabinet.ui.intent.ConsumerInfoIntent
import uz.veolia.cabinet.ui.navigation.RootConfigPhone
import uz.veolia.cabinet.ui.uistate.ConsumerInfoUiState
import uz.veolia.cabinet.util.extension.collectData


class ConsumerInfoPhonePresenterImpl(
    private val consumer: String,
    componentContext: ComponentContext,
    private val navigator: StackNavigation<RootConfigPhone>
) : CommonPresenter<ConsumerInfoIntent, ConsumerInfoUiState>(componentContext), KoinComponent {
    override val uiState = MutableValue(ConsumerInfoUiState(consumer = consumer))
    private val repository by inject<AppRepository>()

    init {
        doOnDestroy { screenModelScope.cancel() }
        uiState.update { it.copy(loading = true) }
        repository.getConsumeDetails(consumer).collectData(screenModelScope) {
            uiState.update { it.copy(loading = false) }
            when (this) {
                is ResultData.Message -> {
                    if (code == 401)
                        navigator.replaceAll(RootConfigPhone.Login)
                    uiState.update { it.copy(message = message) }
                }

                is ResultData.Success -> {
                    uiState.update { it.copy(consumerDetailsBody = data) }
                }

                is ResultData.TimeOut -> uiState.update { it.copy(serverError = true) }
            }
        }
    }

    override fun onEventDispatcher(intent: ConsumerInfoIntent) {
        when (intent) {
            ConsumerInfoIntent.ToastHide -> uiState.update {
                it.copy(
                    message = null,
                    serverError = false,
                )
            }

            ConsumerInfoIntent.Back -> screenModelScope.launch { navigator.pop() }
            is ConsumerInfoIntent.OnPageSelect -> {}
            is ConsumerInfoIntent.Error -> uiState.update { it.copy(message = intent.error) }
            is ConsumerInfoIntent.OnItemClicked -> {
                when (intent.name) {
                    "О потребителе" -> navigator.push(
                        RootConfigPhone.AboutConsumer(
                            consumer,
                            uiState.value.consumerDetailsBody
                        ) { update() }
                    )

                    "Данные от ЛК" -> navigator.push(
                        RootConfigPhone.PersonalData(
                            uiState.value.consumerDetailsBody?.consumer ?: "",
                            uiState.value.consumerDetailsBody?.consumerCabinet?.password ?: "",
                            uiState.value.consumerDetailsBody?.consumerCabinet?.passport ?: "",
                            uiState.value.consumerDetailsBody?.consumerAddress?.address ?: "",
                            uiState.value.consumerDetailsBody?.consumerCabinet?.inn ?: "",
                            uiState.value.consumerDetailsBody?.consumerCabinet?.cadastre ?: "",
                            uiState.value.consumerDetailsBody?.consumerProfiles?.name ?: ""
                        )
                    )

                    "История оплат" -> navigator.push(
                        RootConfigPhone.HistoryPayment(
                            consumer,
                            uiState.value.consumerDetailsBody?.consumerProfiles?.name ?: ""
                        )
                    )

                    "История начислений" -> navigator.push(
                        RootConfigPhone.Accrual(
                            consumer,
                            uiState.value.consumerDetailsBody?.consumerProfiles?.name ?: "",
                            uiState.value.consumerDetailsBody?.consumerStates?.balance ?: ""
                        ) { _ ->
                            update()
                        }
                    )

                    "История показаний" -> navigator.push(
                        RootConfigPhone.HistoryCounter(
                            consumer,
                            uiState.value.consumerDetailsBody?.consumerProfiles?.name ?: ""
                        )
                    )
                }
            }
        }
    }

    private fun update() {
        repository.getConsumeDetails(consumer).collectData(screenModelScope) {
            when (this) {
                is ResultData.Message -> {
                    if (code == 401)
                        navigator.replaceAll(RootConfigPhone.Login)
                    uiState.update { it.copy(message = message) }
                }

                is ResultData.Success -> {
                    uiState.update { it.copy(consumerDetailsBody = data) }
                }

                is ResultData.TimeOut -> uiState.update { it.copy(serverError = true) }
            }
        }
    }
}
