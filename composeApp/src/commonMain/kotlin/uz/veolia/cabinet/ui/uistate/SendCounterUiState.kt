package uz.veolia.cabinet.ui.uistate

import uz.veolia.cabinet.data.remote.request.SendCounterItem
import uz.veolia.cabinet.data.remote.response.CounterItem

data class SendCounterUiState(
    val list: List<SendCounterItem> = listOf(SendCounterItem()),
    val loading: Boolean = false,
    val message: String? = null,
    val serverError: Boolean = false,
    val success : String? = null,
    val lastMeter : String? = null,
    val name : String? = null
)
