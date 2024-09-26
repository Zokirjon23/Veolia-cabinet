package uz.veolia.cabinet.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetLastCounterResponse(
    @SerialName("code")
    val code: Int?= null,
    @SerialName("msg")
    val msg: String?= null,
    @SerialName("body")
    val body : CounterItem?= null
)