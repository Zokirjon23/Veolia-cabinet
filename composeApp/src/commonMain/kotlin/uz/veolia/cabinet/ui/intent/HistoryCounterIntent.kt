package uz.veolia.cabinet.ui.intent

sealed interface HistoryCounterIntent {
    data object LogOut : HistoryCounterIntent
    data object Back : HistoryCounterIntent
    class SendCounter(val count: String?,val name: String?) : HistoryCounterIntent
}
