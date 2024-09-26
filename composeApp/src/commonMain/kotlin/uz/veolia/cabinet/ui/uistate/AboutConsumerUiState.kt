package uz.veolia.cabinet.ui.uistate

import uz.veolia.cabinet.data.remote.response.ConsumerDetailsBody

data class AboutConsumerUiState(
    val message: String? = null,
    val serverError: Boolean = false,
    val consumerDetailsBody: ConsumerDetailsBody? = null,
    val loading: Boolean = false,
    val switch : Boolean = false,
    val success : Boolean = false
)