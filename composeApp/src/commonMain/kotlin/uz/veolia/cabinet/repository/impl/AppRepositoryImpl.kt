package uz.veolia.cabinet.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json
import uz.veolia.cabinet.data.local.LocalStorage
import uz.veolia.cabinet.data.remote.Api
import uz.veolia.cabinet.data.remote.ResultData
import uz.veolia.cabinet.data.remote.paging.ConsumerGetPaging
import uz.veolia.cabinet.data.remote.paging.GetMeterHistoryPaging
import uz.veolia.cabinet.data.remote.paging.MonitoringPaging
import uz.veolia.cabinet.data.remote.paging.PaymentHistoryPaging
import uz.veolia.cabinet.data.remote.request.ConsumerCreateRequest
import uz.veolia.cabinet.data.remote.request.ConsumerDetailsRequest
import uz.veolia.cabinet.data.remote.request.CreateAccrualRequest
import uz.veolia.cabinet.data.remote.request.DetailsUpdateRequest
import uz.veolia.cabinet.data.remote.request.SendCounterItem
import uz.veolia.cabinet.data.remote.request.SendCounterRequest
import uz.veolia.cabinet.data.remote.response.CommonResponse
import uz.veolia.cabinet.data.remote.response.ConsumerDetailsBody
import uz.veolia.cabinet.data.remote.response.ConsumerDetailsResponse
import uz.veolia.cabinet.data.remote.response.ConsumerItem
import uz.veolia.cabinet.data.remote.response.CounterItem
import uz.veolia.cabinet.data.remote.response.CreateAccrualResponse
import uz.veolia.cabinet.data.remote.response.GetAccrualItem
import uz.veolia.cabinet.data.remote.response.GetAccruals
import uz.veolia.cabinet.data.remote.response.GetLastCounterResponse
import uz.veolia.cabinet.data.remote.response.LoginResponse
import uz.veolia.cabinet.data.remote.response.MonitoringItem
import uz.veolia.cabinet.data.remote.response.PaymentItem
import uz.veolia.cabinet.data.remote.response.TokenPayload
import uz.veolia.cabinet.data.remote.response.UploadImageResponse
import uz.veolia.cabinet.data.model.role
import uz.veolia.cabinet.data.remote.response.toRequest
import uz.veolia.cabinet.data.model.toRole
import uz.veolia.cabinet.domain.repository.AppRepository
import uz.veolia.cabinet.util.extension.decodeJwtPayload
import uz.veolia.cabinet.util.extension.isConnection


class AppRepositoryImpl(private val localStorage: LocalStorage, private val api: Api) :
    AppRepository {

    override suspend fun hasAuth(): Boolean {
        return localStorage.getToken() != null
    }

    override fun login(login: String, parol: String): Flow<ResultData<Unit>> {
        return flow {
            val result = api.login(login, parol).body<LoginResponse>()
            if (result.token != null) {
                val jsonString = result.token.decodeJwtPayload()
                val json = Json {
                    ignoreUnknownKeys = true
                    this.isLenient = true
                }
                val data = json.decodeFromString<TokenPayload>(jsonString)
                data.resourceAccess?.cabinetClient?.roles?.let { list ->
                    role = list.map { s -> s.toRole() }
                }
                localStorage.setToken(result.token)
                emit(ResultData.Success(Unit))
            } else if (result.message != null) {
                emit(ResultData.Message(result.message))
            } else {
                emit(ResultData.TimeOut())
            }
        }.catch {
            if (it.isConnection()) {
                emit(ResultData.TimeOut())
            } else {
                emit(ResultData.Message(it.message ?: "Something went wrong"))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getConsumers(
        keyword: String,
        status: Boolean?,
        type: String?,
        region: Int?,
    ): Flow<PagingData<ConsumerItem>> {
        return Pager(
            config = PagingConfig(30),
            pagingSourceFactory = {
                ConsumerGetPaging(api, keyword, status, type, region, localStorage)
            }
        ).flow.flowOn(Dispatchers.IO)
    }

    override fun getMonitoring(
        keyword: String,
        state: String?,
        district: Int?,
        service: String?,
        startDate: Long,
        endDate: Long,
    ): Flow<PagingData<MonitoringItem>> {
        return Pager(
            config = PagingConfig(30),
            pagingSourceFactory = {
                MonitoringPaging(
                    api,
                    keyword,
                    state,
                    district,
                    service,
                    startDate,
                    endDate,
                    localStorage
                )
            }
        ).flow.flowOn(Dispatchers.IO)
    }

    override fun getConsumeDetails(consumer: String): Flow<ResultData<ConsumerDetailsBody?>> {
        return flow {
            val response = api.getConsumerDetail(ConsumerDetailsRequest(consumer))
            val result = response.body<ConsumerDetailsResponse>()
            if (response.status == HttpStatusCode.OK && result.code == 0) {
                emit(ResultData.Success(result.body))
            } else if (!result.msg.isNullOrEmpty()) {
                emit(ResultData.Message(result.msg, response.status.value))
            } else {
                emit(ResultData.TimeOut())
            }
        }.catch {
            if (it.isConnection()) {
                emit(ResultData.TimeOut())
            } else {
                emit(ResultData.Message(it.message ?: "Произошла ошибка"))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun createConsumer(
        id: String,
        type: String,
        status: Boolean,
        name: String,
        address: String,
        cadastre: String,
        area: String,
        room: String,
        rommer: String,
    ): Flow<ResultData<String>> {
        return flow {
            val request = ConsumerCreateRequest(
                address = address,
                area = area,
                cadastre = cadastre,
                consumer = id,
                name = name,
                room = room,
                roomer = rommer,
                status = status,
                type = type
            )
            val response = api.createConsumer(request)
            val result = response.body<CommonResponse>()
            if (response.status == HttpStatusCode.Created && result.code == 0) {
                emit(ResultData.Success(result.msg ?: "Успех"))
            } else if (!result.msg.isNullOrEmpty()) {
                emit(ResultData.Message(result.msg, response.status.value))
            } else {
                emit(ResultData.TimeOut())
            }
        }.catch {
            if (it.isConnection()) {
                emit(ResultData.TimeOut())
            } else {
                emit(ResultData.Message(it.message ?: "Произошла ошибка"))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getMeterHistory(
        consumer: String,
    ): Flow<PagingData<CounterItem>> {
        return Pager(
            config = PagingConfig(30),
            pagingSourceFactory = {
                GetMeterHistoryPaging(api, consumer, localStorage)
            }
        ).flow.flowOn(Dispatchers.IO)
    }

    override fun getPaymentList(
        consumer: String,
    ): Flow<PagingData<PaymentItem>> {
        return Pager(
            config = PagingConfig(30),
            pagingSourceFactory = {
                PaymentHistoryPaging(api, consumer, localStorage)
            }
        ).flow.flowOn(Dispatchers.IO)
    }

    override fun sendCounters(
        consumer: String,
        list: List<SendCounterItem>,
    ): Flow<ResultData<String>> {
        return flow {
            val request = SendCounterRequest(consumer, list)
            val response = api.sendCounters(request)
            val result = response.body<CommonResponse>()
            if (result.code == 0) {
                emit(ResultData.Success(result.msg ?: "Успех"))
            } else if (!result.msg.isNullOrEmpty()) {
                emit(ResultData.Message(result.msg, response.status.value))
            } else {
                emit(ResultData.TimeOut())
            }
        }.catch {
            if (it.isConnection()) {
                emit(ResultData.TimeOut())
            } else {
                emit(ResultData.Message(it.message ?: "Произошла ошибка"))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getLastCounters(consumer: String): Flow<ResultData<String>> {
        return flow {
            val response = api.getLastCounter(consumer)
            val result = response.body<GetLastCounterResponse>()
            if (result.code == 0 || result.code == -1) {
                emit(ResultData.Success(result.body?.meter ?: "0"))
            } else if (!result.msg.isNullOrEmpty()) {
                emit(ResultData.Message(result.msg, response.status.value))
            } else {
                emit(ResultData.TimeOut())
            }
        }.catch {
            if (it.isConnection()) {
                emit(ResultData.TimeOut())
            } else {
                emit(ResultData.Message(it.message ?: "Произошла ошибка"))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun uploadImage(imageByteArray: ByteArray?): Flow<ResultData<SendCounterItem>> {
        return flow {
            if (imageByteArray != null) {
                val response = api.uploadImage(imageByteArray)
                val result = response.body<UploadImageResponse>()
                if (result.fileDetails?.getOrNull(0) != null) {
                    emit(ResultData.Success(result.fileDetails[0]!!.toRequest()))
                } else if (result.error?.isNotEmpty() == true && result.error[0]?.msg != null) {
                    emit(ResultData.Message(result.error[0]?.msg ?: "", response.status.value))
                } else {
                    emit(ResultData.TimeOut())
                }
            } else
                emit(ResultData.Message("Image not found"))
        }.catch {
            if (it.isConnection()) {
                emit(ResultData.TimeOut())
            } else {
                emit(ResultData.Message(it.message ?: "Произошла ошибка"))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun updateConsumerDetails(
        address: String?,
        area: String?,
        cadastre: String?,
        consumer: String?,
        name: String?,
        room: String?,
        roomer: String?,
        status: Boolean?,
        type: String?,
    ): Flow<ResultData<ConsumerDetailsBody?>> {
        return flow {
            val response = api.updateConsumerDetails(
                DetailsUpdateRequest(
                    address,
                    area,
                    cadastre,
                    consumer,
                    name,
                    room,
                    roomer,
                    status,
                    type
                )
            )
            val result = response.body<ConsumerDetailsResponse>()
            if (response.status == HttpStatusCode.OK && result.code == 0) {
                emit(ResultData.Success(result.body))
            } else if (!result.msg.isNullOrEmpty()) {
                emit(ResultData.Message(result.msg, response.status.value))
            } else {
                emit(ResultData.TimeOut())
            }
        }.catch {
            if (it.isConnection()) {
                emit(ResultData.TimeOut())
            } else {
                emit(ResultData.Message(it.message ?: "Произошла ошибка"))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getAccruals(
        consumer: String?,
        year: Int?,
    ): Flow<ResultData<List<GetAccrualItem>>> {
        return flow {
            val response = api.getAccruals(consumer, year)
            val result = response.body<GetAccruals>()
            if (response.status == HttpStatusCode.OK && result.code == 0 && result.list != null) {
                emit(ResultData.Success(result.list))
            } else if (!result.msg.isNullOrEmpty()) {
                emit(ResultData.Message(result.msg, response.status.value))
            } else {
                emit(ResultData.TimeOut())
            }
        }.catch {
            if (it.isConnection()) {
                emit(ResultData.TimeOut())
            } else {
                emit(ResultData.Message(it.message ?: "Произошла ошибка"))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun createAccruals(
        consumer: String?,
        acc: String?,
        type: String?,
        accSum: String?,
    ): Flow<ResultData<String>> {
        return flow {
            val response = api.createAccruals(CreateAccrualRequest(acc, accSum, type, consumer))
            val result = response.body<CreateAccrualResponse>()
            if (result.code == 0 && result.createAccrualBody?.balance != null) {
                emit(ResultData.Success(result.createAccrualBody.balance))
            } else if (!result.msg.isNullOrEmpty()) {
                emit(ResultData.Message(result.msg, response.status.value))
            } else {
                emit(ResultData.TimeOut())
            }
        }.catch {
            if (it.isConnection()) {
                emit(ResultData.TimeOut())
            } else {
                emit(ResultData.Message(it.message ?: "Произошла ошибка"))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun isExpired(): Boolean {
        return runBlocking {
            localStorage.getToken()?.let {
                val jsonString = it.decodeJwtPayload()
                val json = Json {
                    ignoreUnknownKeys = true
                    this.encodeDefaults = true
                    this.isLenient = true
                }
                val data = json.decodeFromString<TokenPayload?>(jsonString)
                data?.exp?.let { expTimestamp ->
                    val expirationTime = Instant.fromEpochSeconds(expTimestamp.toLong())
                    val currentTime = Clock.System.now()
                    currentTime > expirationTime
                }
            } ?: true
        }
    }

    override suspend fun tokenPayload(): TokenPayload? {
        return localStorage.getToken()?.let {
            val jsonString = it.decodeJwtPayload()
//            Log.d("DDDD", "tokenPayload: $jsonString")
            val json = Json {
                ignoreUnknownKeys = true
                this.encodeDefaults = true
                this.isLenient = true
            }
            val data = json.decodeFromString<TokenPayload?>(jsonString)
            data?.resourceAccess?.cabinetClient?.roles?.let { list ->
                role = list.map { s -> s.toRole() }
            }
            data
        }
    }

    override fun logOut() {
        runBlocking {
            localStorage.setToken(null)
        }
    }

//    override fun deleteConsumer(consumer: String): Flow<ResultData<String>> {
//        return flow {
//            val response = api.deleteConsumer(consumer)
//            val result = response.body<CommonResponse>()
//            if (result.code == 0) {
//                emit(ResultData.Success(result.msg ?: "Успех"))
//            } else if (!result.msg.isNullOrEmpty()) {
//                emit(ResultData.Message(result.msg))
//            } else {
//                emit(ResultData.TimeOut())
//            }
//        }.catch {
//            if (it.isConnection()) {
//                emit(ResultData.TimeOut())
//            } else {
//                emit(ResultData.Message(it.message ?: "Произошла ошибка"))
//            }
//        }.flowOn(Dispatchers.IO)
//    }
}

