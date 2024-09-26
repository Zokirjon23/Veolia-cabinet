package uz.veolia.cabinet.presenter.phone

import androidx.paging.PagingData
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.StateFlow
import uz.veolia.cabinet.data.remote.response.CounterItem
import uz.veolia.cabinet.ui.intent.HistoryCounterIntent
import uz.veolia.cabinet.ui.uistate.HistoryCounterUiState

interface HistoryCounterPhonePresenter {
    val meters: StateFlow<PagingData<CounterItem>>
    val uiState : Value<HistoryCounterUiState>
    fun onEventDispatcher(intent : HistoryCounterIntent)
}
