package uz.veolia.cabinet.di

import io.github.aakira.napier.Napier
import org.koin.dsl.module
import io.ktor.client.*
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import uz.veolia.cabinet.data.local.LocalStorage
import uz.veolia.cabinet.data.remote.Api

private const val TIME_OUT = 10000L
val networkModule = module {
    single(named("base")) { httpClient(get()) }
    single(named("file")) { httpClientFile(get()) }
    single { Api(get(named("base")), get(named("file"))) }
}

private fun httpClient(localStorage: LocalStorage): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                    encodeDefaults = true
                }
            )
        }

        install(HttpTimeout) {
            requestTimeoutMillis = TIME_OUT
            connectTimeoutMillis = TIME_OUT
            socketTimeoutMillis = TIME_OUT
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.d(tag = "Logger Ktor =>",message = message)
                }
            }
            level = LogLevel.ALL
        }

        install(ResponseObserver) {
            onResponse { response ->
                Napier.d(tag = "HTTP status:", message = "${response.status.value}")
            }
        }

        install(DefaultRequest) {
//            url("http://10.1.1.163:1003/")
            url("https://api.veoliaenergy.uz:10003/")
            val token = runBlocking { localStorage.getToken() }
            if (token != null) {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
//            header("Authorization", "Bearer ${localStorage.token ?: ""}")
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }
}

private fun httpClientFile(localStorage: LocalStorage): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                    encodeDefaults = true
                }
            )
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.d(tag = "Logger Ktor =>",message = message)
                }
            }
            level = LogLevel.ALL
        }

        install(ResponseObserver) {
            onResponse { response ->
                Napier.d(tag = "HTTP status:", message = "${response.status.value}")
            }
        }

        install(DefaultRequest) {
//            url("http://10.1.1.163:9002/")
            url("https://api.veoliaenergy.uz:19002/")
            val token = runBlocking { localStorage.getToken() }
            if (token != null) {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
//            header("Authorization", "Bearer ${localStorage.token ?: ""}")
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }
}