package uz.veolia.cabinet.presenter.phone

import androidx.paging.PagingData
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.StateFlow
import uz.veolia.cabinet.data.remote.response.ConsumerItem
import uz.veolia.cabinet.ui.intent.ConsumerTabIntent
import uz.veolia.cabinet.ui.uistate.ConsumerTabUiState

interface ConsumerTabPhonePresenter {
    val consumer: StateFlow<PagingData<ConsumerItem>>
    val uiState : Value<ConsumerTabUiState>
    fun onEventDispatcher(intent: ConsumerTabIntent)
}