package uz.veolia.cabinet.data.remote.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateAccrualResponse(
    @SerialName("code")
    val code: Int? = null,
    @SerialName("msg")
    val msg: String? = null,
    @SerialName("body")
    val createAccrualBody: CreateAccrualBody? = null
)

@Serializable
data class CreateAccrualBody(
    @SerialName("BALANCE")
    val balance: String?
)