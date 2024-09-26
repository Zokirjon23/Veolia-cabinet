package uz.veolia.cabinet.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uz.veolia.cabinet.data.remote.ResultData
import uz.veolia.cabinet.data.remote.request.SendCounterItem
import uz.veolia.cabinet.data.remote.response.ConsumerDetailsBody
import uz.veolia.cabinet.data.remote.response.ConsumerItem
import uz.veolia.cabinet.data.remote.response.CounterItem
import uz.veolia.cabinet.data.remote.response.GetAccrualItem
import uz.veolia.cabinet.data.remote.response.MonitoringItem
import uz.veolia.cabinet.data.remote.response.PaymentItem
import uz.veolia.cabinet.data.remote.response.TokenPayload

interface AppRepository {
    suspend fun hasAuth(): Boolean
    fun login(login: String, parol: String): Flow<ResultData<Unit>>
    fun getConsumers(
        keyword: String,
        status: Boolean?,
        type: String?,
        region: Int?
    ): Flow<PagingData<ConsumerItem>>

    fun getConsumeDetails(consumer: String): Flow<ResultData<ConsumerDetailsBody?>>
    fun createConsumer(
        id: String,
        type: String,
        status: Boolean,
        name: String,
        address: String,
        cadastre: String,
        area: String,
        room: String,
        rommer: String,
    ) : Flow<ResultData<String>>

    fun getMeterHistory(
        consumer : String
    ): Flow<PagingData<CounterItem>>

    fun sendCounters(consumer: String, list: List<SendCounterItem>): Flow<ResultData<String>>
    fun getLastCounters(consumer: String): Flow<ResultData<String>>
    fun uploadImage(imageByteArray: ByteArray?) : Flow<ResultData<SendCounterItem>>
    fun updateConsumerDetails(
        address: String?,
        area: String?,
        cadastre: String?,
        consumer: String?,
        name: String?,
        room: String?,
        roomer: String?,
        status: Boolean?,
        type: String?
    ): Flow<ResultData<ConsumerDetailsBody?>>

    fun getAccruals(
        consumer: String?,
        year: Int?
    ): Flow<ResultData<List<GetAccrualItem>>>
    fun createAccruals(
        consumer: String?,
        acc: String?,
        type: String?,
        accSum: String?
    ): Flow<ResultData<String>>

    fun isExpired(): Boolean
    suspend fun tokenPayload(): TokenPayload?
    fun logOut()
    fun getPaymentList(consumer: String): Flow<PagingData<PaymentItem>>
    fun getMonitoring(
        keyword: String,
        state: String?,
        district: Int?,
        service: String?,
        startDate: Long,
        endDate: Long,
    ): Flow<PagingData<MonitoringItem>>
}