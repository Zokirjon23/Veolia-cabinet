package uz.veolia.cabinet.presenter.phone

import androidx.paging.PagingData
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.StateFlow
import uz.veolia.cabinet.data.remote.response.MonitoringItem
import uz.veolia.cabinet.ui.intent.MonitoringTabIntent
import uz.veolia.cabinet.ui.uistate.MonitoringTabUiState

interface MonitoringTabPhonePresenter {
    val uiState : Value<MonitoringTabUiState>
    val monitoring: StateFlow<PagingData<MonitoringItem>>

    fun onEventDispatcher(intent: MonitoringTabIntent)
}