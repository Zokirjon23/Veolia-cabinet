package uz.veolia.cabinet.data.remote.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConsumerCreateRequest(
    @SerialName("address")
    val address: String?,
    @SerialName("area")
    val area: String?,
    @SerialName("cadastre")
    val cadastre: String?,
    @SerialName("consumer")
    val consumer: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("room")
    val room: String?,
    @SerialName("roomer")
    val roomer: String?,
    @SerialName("status")
    val status: Boolean?,
    @SerialName("type")
    val type: String?
)