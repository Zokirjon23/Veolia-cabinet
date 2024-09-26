package uz.veolia.cabinet.ui.uistate

data class AddConsumerUiState(val loading : Boolean = false,val message : String? = null,val serverError : Boolean = false,val success : String? = null)
