package uz.veolia.cabinet.data.remote.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateAccrualRequest(
    @SerialName("acc")
    val acc: String?,
    @SerialName("acc_sum")
    val accSum: String?,
    @SerialName("acc_type")
    val accType: String?,
    @SerialName("consumer")
    val consumer: String?
)