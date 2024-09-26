package uz.veolia.cabinet.data.remote.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetCounterResponse(
    @SerialName("body")
    val counterListBody: CounterListBody?= null,
    @SerialName("code")
    val code: Int?= null,
    @SerialName("msg")
    val msg: String?= null
)

@Serializable
data class CounterListBody(
    @SerialName("count")
    val count: Int?= null,
    @SerialName("list")
    val list: List<CounterItem>?= null
)

@Serializable
data class CounterItem(
    @SerialName("CONSUMER")
    val consumer: String?= null,
    @SerialName("COUNT")
    val count: Int?= null,
    @SerialName("CREATED_AT")
    val createdAt: String?= null,
    @SerialName("ID")
    val id: Int?= null,
    @SerialName("METER")
    val meter: String?= null,
    @SerialName("METER_DETAILS")
    val meterDetails: List<CounterDetails?>?= null,
    @SerialName("MOUNTED")
    val mounted: String?= null,
    @SerialName("STATE")
    val state: String?= null,
    @SerialName("SYSTEM")
    val system: String?= null,
    @SerialName("UPDATED_AT")
    val updatedAt: String?= null
)

@Serializable
data class CounterDetails(
    @SerialName("FILE_ID")
    val fileId: Int?= null,
    @SerialName("FILE_LINK")
    val fileLink: String?= null,
    @SerialName("FILE_NAME")
    val fileName: String?= null,
    @SerialName("ID")
    val id: Int?= null,
    @SerialName("METER")
    val meter: String?= null,
    @SerialName("PARENT")
    val parent: Int?= null
)