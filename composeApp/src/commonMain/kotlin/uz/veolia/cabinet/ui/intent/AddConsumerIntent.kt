package uz.veolia.cabinet.ui.intent

sealed interface AddConsumerIntent {
    data object ToastHide : AddConsumerIntent
    data object Back : AddConsumerIntent

    data class Create(
        val typePos: Int,
        val statusPos: Int,
        val id: String,
        val name: String,
        val address: String,
        val cadastre: String,
        val area: String,
        val room: String,
        val rommer : String,
    ) : AddConsumerIntent
}