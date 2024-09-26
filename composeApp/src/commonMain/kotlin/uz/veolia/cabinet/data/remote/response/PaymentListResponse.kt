package uz.veolia.cabinet.data.remote.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentListResponse(
    @SerialName("code")
    val code: Int? = null,
    @SerialName("msg")
    val msg: String? = null,
    @SerialName("body")
    val body: Body? = null
)

@Serializable
data class Body(
    @SerialName("count")
    val count: Int? = null,
    @SerialName("list")
    val list: List<PaymentItem>? = null
)


@Serializable
data class PaymentItem(
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
    @SerialName("PAY_SERVICE")
    val paymentService: PaymentService? = null,
    @SerialName("SETTLEMENT_CODE")
    val settlementCode: String? = null
)

@Serializable
data class PaymentService(
    @SerialName("CREATED_AT")
    val createdAt: String? = null,
    @SerialName("ID")
    val id: Int? = null,
    @SerialName("NAME")
    val name: String? = null,
    @SerialName("SERVICE_ID")
    val serviceId: String? = null,
    @SerialName("STATUS")
    val status: Boolean? = null,
    @SerialName("UPDATED_AT")
    val updatedAt: String? = null
)
