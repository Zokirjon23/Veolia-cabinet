package uz.veolia.cabinet.data.remote.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConsumerGetResponse(
    @SerialName("body")
    val body: ConsumerGetBody? = null,
    @SerialName("code")
    val code: Int? = null,
    @SerialName("msg")
    val msg: String? = null
)

@Serializable
data class ConsumerGetBody(
    @SerialName("count")
    val count: Int?= null,
    @SerialName("list")
    val list: List<ConsumerItem>?= null
)

@Serializable
data class ConsumerItem(
    @SerialName("CONSUMER")
    val consumer: String?= null,
    @SerialName("CONSUMER_ADDRESS")
    val consumerAddress: ConsumerAddress?= null,
    @SerialName("CONSUMER_OBJECTS")
    val consumerObject: ConsumerObject?= null,
    @SerialName("CONSUMER_PROFILES")
    val consumerProfiles: ConsumerProfiles?= null,
    @SerialName("CONSUMER_STATES")
    val consumerStates: ConsumerStates?= null,
    @SerialName("CREATED_AT")
    val createdAt: String?= null,
    @SerialName("STATUS")
    val status: Boolean?= null,
    @SerialName("TYPE")
    val type: String?= null,
    @SerialName("UPDATED_AT")
    val updateAt: String?= null
)

@Serializable
data class ConsumerAddress(
    @SerialName("ADDRESS")
    val address: String?= null
)

@Serializable
data class ConsumerObject(
    @SerialName("CADASTRE")
    val cadastre: String?= null
)

@Serializable
data class ConsumerProfiles(
    @SerialName("NAME")
    val name: String?= null
)

@Serializable
data class ConsumerStates(
    @SerialName("BALANCE")
    val balance: String?= null
)


