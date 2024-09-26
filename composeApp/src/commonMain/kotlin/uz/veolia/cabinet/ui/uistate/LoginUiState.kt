package uz.veolia.cabinet.ui.uistate

data class LoginUiState(
    val loading: Boolean = false,
    val message: String? = null,
    val serverError: Boolean = false
)