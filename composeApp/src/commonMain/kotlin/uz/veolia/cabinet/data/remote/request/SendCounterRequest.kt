package uz.veolia.cabinet.data.remote.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendCounterRequest(
    @SerialName("consumer")
    val consumer: String,
    @SerialName("meters")
    val meters: List<SendCounterItem>
)


@Serializable
data class SendCounterItem(
    @SerialName("file_id")
    val fileId: String? = null,
    @SerialName("file_link")
    val fileLink: String? = null,
    @SerialName("file_name")
    val fileName: String? = null,
    @SerialName("meter")
    val meter: String? = ""
)