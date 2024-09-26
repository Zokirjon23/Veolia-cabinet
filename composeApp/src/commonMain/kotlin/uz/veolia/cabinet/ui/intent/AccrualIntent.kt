package uz.veolia.cabinet.ui.intent

sealed interface AccrualIntent {
    data object OpenCreateAccrualScreen : AccrualIntent
    data object Back : AccrualIntent
    class AccrualDateChange(val year: Int) : AccrualIntent
}
