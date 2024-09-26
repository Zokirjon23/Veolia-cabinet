package uz.veolia.cabinet.ui.intent

sealed interface ConsumerInfoIntent {
    class OnPageSelect(val page: Int) : ConsumerInfoIntent
    class OnItemClicked(val name: String) : ConsumerInfoIntent
    class Error(val error: String?) : ConsumerInfoIntent
    data object ToastHide : ConsumerInfoIntent
    data object Back : ConsumerInfoIntent
}