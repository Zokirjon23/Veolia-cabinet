package uz.veolia.cabinet.ui.intent

sealed interface HistoryPaymentIntent {
    data object LogOut : HistoryPaymentIntent
    data object Back : HistoryPaymentIntent
}
