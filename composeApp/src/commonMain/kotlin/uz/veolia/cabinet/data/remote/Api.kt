package uz.veolia.cabinet.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.datetime.Clock
import uz.veolia.cabinet.data.remote.request.ConsumerCreateRequest
import uz.veolia.cabinet.data.remote.request.ConsumerDetailsRequest
import uz.veolia.cabinet.data.remote.request.ConsumerGetRequest
import uz.veolia.cabinet.data.remote.request.CreateAccrualRequest
import uz.veolia.cabinet.data.remote.request.DetailsUpdateRequest
import uz.veolia.cabinet.data.remote.request.LoginRequest
import uz.veolia.cabinet.data.remote.request.MonitoringRequest
import uz.veolia.cabinet.data.remote.request.SendCounterRequest

class Api(private val httpClient: HttpClient, private val clientFile: HttpClient) {
    suspend fun login(login: String, password: String): HttpResponse {
        return httpClient.post("auth/token") {
            setBody(LoginRequest(login, password))
        }
    }

    suspend fun getConsumer(request: ConsumerGetRequest): HttpResponse {
        return httpClient.post("consumer/v2/get") {
            setBody(request)
        }
    }

    suspend fun getMonitoring(request: MonitoringRequest): HttpResponse {
        return httpClient.post("/monitoring/get") {
            setBody(request)
        }
    }

    suspend fun getConsumerDetail(request: ConsumerDetailsRequest): HttpResponse {
        return httpClient.post("consumer/get/v2/details") {
            setBody(request)
        }
    }

    suspend fun createConsumer(request: ConsumerCreateRequest): HttpResponse {
        return httpClient.post("consumer/create") {
            setBody(request)
        }
    }

    suspend fun getCounterHistory(consumer: String, page: Int, size: Int): HttpResponse {
        return httpClient.get("consumer/meter/list") {
            parameter("consumer", consumer)
            parameter("page", page)
            parameter("size", size)
        }
    }

    suspend fun getPaymentList(consumer: String,page: Int,size: Int) : HttpResponse{
        return httpClient.get("consumer/payment/list"){
            parameter("consumer", consumer)
            parameter("page", page)
            parameter("size", size)
        }
    }

    suspend fun getLastCounter(consumer: String): HttpResponse {
        return httpClient.get("consumer/meter/last") {
            parameter("consumer", consumer)
        }
    }

    suspend fun sendCounters(request: SendCounterRequest): HttpResponse {
        return httpClient.post("consumer/meter/add") {
            setBody(request)
        }
    }

    suspend fun uploadImage(byteArray: ByteArray): HttpResponse {
        return clientFile.submitFormWithBinaryData(
            url = "file/upload",
            formData = formData {
                append("source", "consumer_cabinet_tablet")
                append("files", byteArray, Headers.build {
                    append(HttpHeaders.ContentType, "image/jpeg")
                    append(
                        HttpHeaders.ContentDisposition,
                        "filename=${Clock.System.now().epochSeconds}.png"
                    )
                })
            }
        )
    }

    suspend fun updateConsumerDetails(request: DetailsUpdateRequest): HttpResponse {
        return httpClient.put("consumer/put/v2/details") {
            setBody(request)
        }
    }

    suspend fun getAccruals(consumer: String?, year: Int?): HttpResponse {
        return httpClient.get("consumer/accruals/list") {
            parameter("consumer", consumer)
            parameter("year", year)
        }
    }

    suspend fun createAccruals(request: CreateAccrualRequest): HttpResponse {
        return httpClient.post("consumer/accruals/add") {
            setBody(request)
        }
    }

//    suspend fun deleteConsumer(consumerId: String) : HttpResponse{
//        return httpClient.delete(""){
//            setBody(ConsumerDeleteRequest(consumerId))
//        }
//    }
}