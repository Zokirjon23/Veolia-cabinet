package uz.veolia.cabinet.presenter.phone

import androidx.paging.PagingData
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.StateFlow
import uz.veolia.cabinet.data.remote.response.PaymentItem
import uz.veolia.cabinet.ui.intent.HistoryPaymentIntent
import uz.veolia.cabinet.ui.uistate.HistoryPaymentUiState

interface HistoryPaymentPhonePresenter {
    val payment: StateFlow<PagingData<PaymentItem>>
    val uiState: Value<HistoryPaymentUiState>
    fun onEventDispatcher(intent: HistoryPaymentIntent)
}
