package uz.veolia.cabinet.ui.uistate

data class ConsumerTabUiState(
    val loading: Boolean = false,
    val search: String = "",
    val name: String? = null,
    val filterStatus: Boolean? = null,
    val filterRegion: Int? = null,
    val region: List<Int> = listOf(),
    val filterPersonType: String? = null,
    val warningText : String? = null,
    val isMustSelect : Boolean = false
)
