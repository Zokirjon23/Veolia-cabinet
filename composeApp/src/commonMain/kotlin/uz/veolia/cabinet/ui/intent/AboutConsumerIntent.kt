package uz.veolia.cabinet.ui.intent

sealed interface AboutConsumerIntent{
    data object Back : AboutConsumerIntent
    data object ToastHide : AboutConsumerIntent
    class UpdateDetails(
        val consumer: String?,
        val activeSelect: Int,
        val name: String,
        val address: String,
        val area: String,
        val rooms: String,
        val roomer: String,
        val cadastre: String,
        val type: String?
    ) : AboutConsumerIntent
    class SwitchChange(val state: Boolean) : AboutConsumerIntent
}
