package uz.veolia.cabinet.data.remote.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConsumerDetailsRequest(
    @SerialName("consumer")
    val consumer: String?
)