package uz.veolia.cabinet.ui.intent

sealed interface ConsumerTabIntent {
    class OnSearchChange(val search: String) : ConsumerTabIntent
    class ItemClicked(val consumer: String,val type: String? = null) : ConsumerTabIntent
    class Filter(val type: String?,val status: Boolean?,val region: Int?) : ConsumerTabIntent
    data object Clear : ConsumerTabIntent
    data object ToastHide : ConsumerTabIntent
    data object LogOut : ConsumerTabIntent
    data object ConsumerReload : ConsumerTabIntent
    data object Search : ConsumerTabIntent
    data object AddClicked : ConsumerTabIntent
}
