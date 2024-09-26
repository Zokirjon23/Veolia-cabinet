package uz.veolia.cabinet.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class LoginResponse(
    @SerialName("code") val code: Int? = null,
    @SerialName("msg") val message: String? = null,
    @SerialName("access_token") val token: String? = null
)
