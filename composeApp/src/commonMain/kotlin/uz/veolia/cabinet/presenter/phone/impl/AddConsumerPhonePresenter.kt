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
import uz.veolia.cabinet.ui.intent.AddConsumerIntent
import uz.veolia.cabinet.ui.navigation.RootConfigPhone
import uz.veolia.cabinet.ui.uistate.AddConsumerUiState
import uz.veolia.cabinet.util.extension.collectData

class AddConsumerPhonePresenter(
    componentContext: ComponentContext,
    private val navigation: StackNavigation<RootConfigPhone>,
) : CommonPresenter<AddConsumerIntent, AddConsumerUiState>(componentContext), KoinComponent {
    override val uiState = MutableValue(AddConsumerUiState())
    private val repository by inject<AppRepository>()
    override fun onEventDispatcher(intent: AddConsumerIntent) {
        when (intent) {
            AddConsumerIntent.Back -> screenModelScope.launch { navigation.pop() }
            is AddConsumerIntent.Create -> {
                uiState.update { it.copy(loading = true) }
                repository.createConsumer(
                    intent.id,
                    if (intent.typePos == 0) "1" else "3",
                    intent.statusPos == 0,
                    intent.name,
                    intent.address,
                    intent.cadastre,
                    intent.area,
                    intent.room,
                    intent.rommer
                ).collectData(screenModelScope) {
                    uiState.update { it.copy(loading = false) }
                    when (this) {
                        is ResultData.Message -> {
                            if (code == 401)
                                navigation.replaceAll(RootConfigPhone.Login)
                            uiState.update { it.copy(message = message) }
                        }

                        is ResultData.Success -> {
                            uiState.update { it.copy(success = data) }
                            delay(1500)
                            navigation.pop()
                        }

                        is ResultData.TimeOut -> uiState.update { it.copy(serverError = true) }
                    }
                }
            }

            AddConsumerIntent.ToastHide -> uiState.update {
                it.copy(
                    message = null,
                    serverError = false,
                    success = null
                )
            }
        }
    }
}
