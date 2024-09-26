package uz.veolia.cabinet.ui.uistate

import uz.veolia.cabinet.data.remote.response.GetAccrualItem
import uz.veolia.cabinet.util.extension.getCurrentYear

data class AccrualUiState(
    val year: String = getCurrentYear().toString(),
    val list: List<GetAccrualItem> = listOf(),
    val loading : Boolean = false,
    val message : String? = null,
    val serverError : Boolean = false,
    val name : String = ""
)
