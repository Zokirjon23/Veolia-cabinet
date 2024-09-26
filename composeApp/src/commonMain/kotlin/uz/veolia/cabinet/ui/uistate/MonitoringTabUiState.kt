package uz.veolia.cabinet.ui.uistate

import uz.veolia.cabinet.util.extension.getCurrentDayInMillis

data class MonitoringTabUiState(
    val loading: Boolean = false, val region: List<Int> = listOf(),
    val filterRegion: Int? = null,
    val name: String? = null,
    val search: String = "",
    val warningText: String? = null,
    val isMustSelect: Boolean = false,
    val serviceId: String? = null,
    val startDate : Long = getCurrentDayInMillis(),
    val endDate : Long = getCurrentDayInMillis()
)