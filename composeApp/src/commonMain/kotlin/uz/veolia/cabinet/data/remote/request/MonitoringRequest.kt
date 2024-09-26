package uz.veolia.cabinet.data.remote.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MonitoringRequest(
    @SerialName("keyword")
    val keyword: String? = null,
    @SerialName("state")
    val state: String? = null,
    @SerialName("page")
    val page: Int? = null,
    @SerialName("size")
    val size: Int? = null,
    @SerialName("district")
    val district: Int? = null,
    @SerialName("service")
    val service : String? = null,
    @SerialName("periodFrom")
    val periodFrom : String? = null,
    @SerialName("periodTo")
    val periodTo : String? = null,
)