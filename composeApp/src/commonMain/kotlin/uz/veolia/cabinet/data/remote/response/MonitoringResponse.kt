package uz.veolia.cabinet.data.remote.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MonitoringResponse(
    @SerialName("code")
    val code: Int? = null,
    @SerialName("msg")
    val msg: String? = null,
    @SerialName("body")
    val body: MonitoringBody? = null
)

@Serializable
data class MonitoringBody(
    @SerialName("count")
    val count: Int? = null,
    @SerialName("list")
    val list: List<MonitoringItem>? = null
)

@Serializable
data class MonitoringItem(
    @SerialName("ID")
    val id: Int? = null,
    @SerialName("CREATED_AT")
    val createdAt: String? = null,
    @SerialName("UPDATED_AT")
    val updatedAt: String? = null,
    @SerialName("CONSUMER")
    val consumer: String? = null,
    @SerialName("PAYMENT_STATE")
    val paymentState: String? = null,
    @SerialName("PAYMENT_CODE")
    val paymentCode: String? = null,
    @SerialName("AMOUNT_VALUE")
    val amountValue: String? = null,
    @SerialName("SETTLEMENT_CODE")
    val settlementCode: String? = null,
    @SerialName("PAY_SERVICE")
    val payService: PayService? = null
)

@Serializable
data class PayService(
    @SerialName("CREATED_AT")
    val createdAt: String? = null,
    @SerialName("UPDATED_AT")
    val updatedAt: String? = null,
    @SerialName("ID")
    val id: Int? = null,
    @SerialName("SERVICE_ID")
    val serviceId: String? = null,
    @SerialName("NAME")
    val name: String? = null,
    @SerialName("STATUS")
    val status: Boolean? = null
)

