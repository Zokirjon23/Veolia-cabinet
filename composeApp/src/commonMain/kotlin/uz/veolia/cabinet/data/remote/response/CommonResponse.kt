package uz.veolia.cabinet.data.remote.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommonResponse(
    @SerialName("code")
    val code: Int? = null,
    @SerialName("msg")
    val msg: String?= null
)