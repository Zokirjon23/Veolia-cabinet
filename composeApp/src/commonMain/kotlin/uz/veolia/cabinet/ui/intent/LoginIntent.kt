package uz.veolia.cabinet.ui.intent
sealed interface LoginIntent {
    data object ToastHide : LoginIntent
    data class Login(val login : String,val password : String) :LoginIntent
}