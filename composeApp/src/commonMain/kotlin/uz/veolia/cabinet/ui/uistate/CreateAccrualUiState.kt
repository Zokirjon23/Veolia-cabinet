package uz.veolia.cabinet.ui.uistate

data class CreateAccrualUiState(
    val loading: Boolean = false,
    val message: String? = null,
    val serverError: Boolean = false,
    val success: Boolean = false,
    val balance : String = "",
    val consumer: String = "",
    val name: String = "",
)
