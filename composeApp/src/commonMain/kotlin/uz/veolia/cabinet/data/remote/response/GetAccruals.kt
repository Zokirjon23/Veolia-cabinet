package uz.veolia.cabinet.data.remote.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetAccruals(
    @SerialName("body")
    val list: List<GetAccrualItem>? = null,
    @SerialName("code")
    val code: Int?= null,
    @SerialName("msg")
    val msg: String?= null
)

@Serializable
data class GetAccrualItem(
    @SerialName("ACC_LIST")
    val accList: List<AccItem>?= null,
    @SerialName("BALANCE")
    val balance: String?= null,
    @SerialName("CREDIT")
    val credit: String?= null,
    @SerialName("DEBET")
    val debet: String?= null,
    @SerialName("PAYMENT")
    val payment: String?= null,
    @SerialName("PERIOD")
    val period: String?= null,
    @SerialName("STATE")
    val state: String?= null
)


@Serializable
data class AccItem(
    @SerialName("CREATED_AT")
    val createdAt: String?= null,
    @SerialName("CREDIT")
    val credit: String?= null,
    @SerialName("DEBET")
    val debet: String?= null,
    @SerialName("PERIOD")
    val period: String?= null,
    @SerialName("STATE")
    val state: String?= null,
    @SerialName("TYPE")
    val type: String?= null
)