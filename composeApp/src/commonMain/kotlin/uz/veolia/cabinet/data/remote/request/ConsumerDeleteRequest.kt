package uz.veolia.cabinet.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConsumerDeleteRequest(
    @SerialName("consumer")
    val consumer : String
)