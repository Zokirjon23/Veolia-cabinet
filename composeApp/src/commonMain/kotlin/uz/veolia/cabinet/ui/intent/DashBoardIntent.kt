package uz.veolia.cabinet.ui.intent

sealed interface DashBoardIntent {
    data object AddConsumer : DashBoardIntent
    data class BottomTabSelected(val index: Int) : DashBoardIntent
}
