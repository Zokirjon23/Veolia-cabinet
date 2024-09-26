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
import uz.veolia.cabinet.data.remote.request.SendCounterItem
import uz.veolia.cabinet.domain.repository.AppRepository
import uz.veolia.cabinet.presenter.phone.CommonPresenter
import uz.veolia.cabinet.ui.intent.SendCounterIntent
import uz.veolia.cabinet.ui.navigation.RootConfigPhone
import uz.veolia.cabinet.ui.uistate.SendCounterUiState
import uz.veolia.cabinet.util.extension.collectData

class SendCounterPhonePresenterImpl(
    private val consumerId: String,
    name: String?,
    last: String?,
    componentContext: ComponentContext,
    private val navigator: StackNavigation<RootConfigPhone>,
    private val callBack: () -> Unit,
) : CommonPresenter<SendCounterIntent, SendCounterUiState>(componentContext),
    KoinComponent {
    override val uiState = MutableValue(SendCounterUiState(lastMeter = last, name = name))
    private val repository by inject<AppRepository>()

    init {
        repository.getLastCounters(consumerId).collectData(screenModelScope) {
            when (this) {
                is ResultData.Message -> {
                    if (code == 401)
                        navigator.replaceAll(RootConfigPhone.Login)
                    uiState.update { it.copy(message = message) }
                }

                is ResultData.Success -> uiState.update { it.copy(lastMeter = data) }
                is ResultData.TimeOut -> uiState.update { it.copy(serverError = true) }
            }
        }
    }

    override fun onEventDispatcher(intent: SendCounterIntent) {
        when (intent) {
            SendCounterIntent.OpenCameraScreen -> screenModelScope.launch {
//                navigator.push(CameraScreen())
            }

            is SendCounterIntent.Send -> screenModelScope.launch {
                repository.sendCounters(consumerId, intent.list)
                    .collectData(this) {
                        when (this) {
                            is ResultData.Message -> {
                                if (code == 401)
                                    navigator.replaceAll(RootConfigPhone.Login)
                                uiState.update { it.copy(message = message) }
                            }

                            is ResultData.Success -> screenModelScope.launch {
                                uiState.update { it.copy(success = data) }
                                callBack()
                                screenModelScope.launch {
                                    delay(1000)
                                    navigator.pop()
                                }
                            }

                            is ResultData.TimeOut -> uiState.update { it.copy(serverError = true) }
                        }
                    }
            }

            SendCounterIntent.ToastHide -> uiState.update {
                it.copy(
                    serverError = false,
                    message = null,
                    success = null
                )
            }

            SendCounterIntent.Back -> screenModelScope.launch { navigator.pop() }
            SendCounterIntent.Add -> {
                val list = uiState.value.list.toMutableList()
                list.add(SendCounterItem())
                uiState.update { it.copy(list = list) }
            }

            is SendCounterIntent.Delete -> {
                val list = uiState.value.list.toMutableList()
                list.removeAt(intent.pos)
                uiState.update { it.copy(list = list) }
            }

//            is SendCounterIntent.ImageUpload -> screenModelScope.launch {
//                uiState.update { it.copy(loading = true) }
//                repository.uploadImage(intent.imageByteArray).collectData(screenModelScope) {
//                    uiState.update { it.copy(loading = false) }
//                    when (this) {
//                        is ResultData.Message -> {
//                            if (code == 401)
//                                navigator.replaceAll(RootConfigPhone.Login)
//                            uiState.update { it.copy(message = message) }
//                        }
//
//                        is ResultData.Success -> {
//                            val list = uiState.value.list.toMutableList()
//                            val counter = list[intent.index].meter
//                            list[intent.index] = data.copy(meter = counter)
//                            uiState.update { it.copy(list = list) }
//                        }
//
//                        is ResultData.TimeOut -> uiState.update { it.copy(serverError = true) }
//                    }
//                }
//            }

            is SendCounterIntent.OnInputChange -> {
                val list = uiState.value.list.toMutableList()
                val data = list[intent.index]
                list[intent.index] = data.copy(meter = intent.data)
                uiState.update { it.copy(list = list) }
            }

            is SendCounterIntent.DeleteItem -> {
                val list = uiState.value.list.toMutableList()
                val data = list[intent.index]
                list[intent.index] = data.copy(fileId = null, fileLink = null, fileName = null)
                uiState.update { it.copy(list = list) }
            }
        }
    }
}
