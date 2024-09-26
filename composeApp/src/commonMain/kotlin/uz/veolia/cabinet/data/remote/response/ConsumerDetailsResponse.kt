package uz.veolia.cabinet.data.remote.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConsumerDetailsResponse(
    @SerialName("body")
    val body: ConsumerDetailsBody? = null,
    @SerialName("code")
    val code: Int? = null,
    @SerialName("msg")
    val msg: String? = null
)

@Serializable
data class ConsumerDetailsBody(
    @SerialName("CONSUMER")
    val consumer: String? = null,
    @SerialName("CONSUMER_ADDRESS")
    val consumerAddress: ConsumerAddress? = null,
    @SerialName("CONSUMER_CABINET")
    val consumerCabinet: ConsumerCabinet? = null,
    @SerialName("CONSUMER_METERS")
    val consumerMeters: ConsumerMeters? = null,
    @SerialName("CONSUMER_OBJECTS")
    val consumerObjects: ConsumerObjects? = null,
    @SerialName("CONSUMER_PROFILES")
    val consumerProfiles: ConsumerProfiles? = null,
    @SerialName("CONSUMER_STATES")
    val consumerStates: ConsumerState? = null,
    @SerialName("STATUS")
    val status: Boolean?,
    @SerialName("TYPE")
    val type: String?
)

@Serializable
data class ConsumerCabinet(
    @SerialName("CADASTRE")
    val cadastre: String? = null,
    @SerialName("CONSUMER")
    val consumer: String? = null,
    @SerialName("INN")
    val inn: String? = null,
    @SerialName("PASSPORT")
    val passport: String? = null,
    @SerialName("PASSWORD")
    val password: String? = null,
    @SerialName("PINFL")
    val pinfl: String? = null
)

@Serializable
data class ConsumerMeters(
    @SerialName("METER")
    val meter: String? = null,
    @SerialName("METER_DATE")
    val meterDate: String? = null
)

@Serializable
data class ConsumerObjects(
    @SerialName("AREA")
    val area: String? = null,
    @SerialName("CADASTRE")
    val cadastre: String? = null,
    @SerialName("ROOM")
    val room: String? = null,
    @SerialName("ROOMER")
    val roomer: String? = null
)

@Serializable
data class ConsumerState(
    @SerialName("BALANCE")
    val balance: String? = null,
    @SerialName("BALANCE_DATE")
    val balanceDate: String? = null,
    @SerialName("BASIC")
    val basic: String? = null,
    @SerialName("PENYA")
    val penya: String? = null,
    @SerialName("SHTRAF")
    val shtraf: String? = null
)
