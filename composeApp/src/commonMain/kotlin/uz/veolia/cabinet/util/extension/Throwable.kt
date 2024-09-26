package uz.veolia.cabinet.util.extension

import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException


fun Throwable.isConnection(): Boolean {
    return when (this) {
        is HttpRequestTimeoutException -> false
        is ConnectTimeoutException -> false
        is SocketTimeoutException -> false
        else -> true
    }
}