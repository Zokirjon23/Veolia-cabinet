package uz.veolia.cabinet.presenter.phone

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
import uz.veolia.cabinet.ui.intent.AboutConsumerIntent
import uz.veolia.cabinet.ui.navigation.RootConfigPhone
import uz.veolia.cabinet.ui.uistate.AboutConsumerUiState
import uz.veolia.cabinet.util.extension.collectData

class AboutConsumerPhonePresenterImpl(
    private val consumer: String,
    componentContext: ComponentContext,
    private val navigation: StackNavigation<RootConfigPhone>,
    private val onUpdate: () -> Unit,
) : CommonPresenter<AboutConsumerIntent, AboutConsumerUiState>(componentContext), KoinComponent {
    override val uiState = MutableValue(AboutConsumerUiState())
    private val repository by inject<AppRepository>()
    override fun onEventDispatcher(intent: AboutConsumerIntent) {
        when (intent) {
            is AboutConsumerIntent.SwitchChange -> uiState.update { it.copy(switch = intent.state) }
            is AboutConsumerIntent.UpdateDetails -> {
                uiState.update { it.copy(loading = true) }
                repository.updateConsumerDetails(
                    intent.address.ifEmpty { null },
                    intent.area.ifEmpty { null },
                    intent.cadastre.ifEmpty { null },
                    consumer,
                    intent.name.ifEmpty { null },
                    intent.rooms.ifEmpty { null },
                    intent.roomer.ifEmpty { null },
                    intent.activeSelect == 0,
                    intent.type
                ).collectData(screenModelScope) {
                    uiState.update { it.copy(loading = false) }
                    when (this) {
                        is ResultData.Message -> {
                            uiState.update { it.copy(message = message) }
                            if (code == 401)
                                navigation.replaceAll(RootConfigPhone.Login)
                        }

                        is ResultData.Success -> {
                            onUpdate()
                            uiState.update { it.copy(switch = false, success = true) }
                            screenModelScope.launch {
                                delay(800)
                                navigation.pop()
                            }
                        }

                        is ResultData.TimeOut -> uiState.update { it.copy(serverError = true) }
                    }
                }
            }

            AboutConsumerIntent.Back -> navigation.pop()
            AboutConsumerIntent.ToastHide -> uiState.update {
                it.copy(
                    message = null,
                    serverError = false,
                    success = false
                )
            }
        }
    }
}
