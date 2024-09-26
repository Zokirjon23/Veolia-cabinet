package uz.veolia.cabinet.data.remote.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConsumerGetRequest(
    @SerialName("district")
    val district: Int? = null,
    @SerialName("keyword")
    val keyword: String? = null,
    @SerialName("page")
    val page: Int? = null,
    @SerialName("size")
    val size: Int? = null,
    @SerialName("status")
    val status: Boolean? = null,
    @SerialName("type")
    val type: String? = null
)