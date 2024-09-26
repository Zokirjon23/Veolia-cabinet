package uz.veolia.cabinet.ui.intent

sealed interface PersonalDataIntent {
    data object Back : PersonalDataIntent
}
