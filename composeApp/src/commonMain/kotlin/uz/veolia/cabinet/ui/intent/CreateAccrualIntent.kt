package uz.veolia.cabinet.ui.intent

sealed interface CreateAccrualIntent {
    data object ToastHide : CreateAccrualIntent
    data object Back : CreateAccrualIntent
    class Create(val consumer : String?,val tabPos : Int,val typePos : Int,val balance : String) : CreateAccrualIntent
}
