package uz.veolia.cabinet.ui.uistate

import uz.veolia.cabinet.data.remote.response.ConsumerDetailsBody

data class ConsumerInfoUiState(
    val message: String? = null,
    val serverError: Boolean = false,
    val consumerDetailsBody: ConsumerDetailsBody? = null,
//    val balance : String? = null,
//    val date : String? = null,
    val loading: Boolean = true,
    val consumer : String? = null,
    val type : String? = null
)