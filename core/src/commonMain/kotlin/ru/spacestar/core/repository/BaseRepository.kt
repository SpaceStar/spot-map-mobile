package ru.spacestar.core.repository

import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.util.network.UnresolvedAddressException
import ru.spacestar.core.model.BaseResponse
import ru.spacestar.core.model.NetworkError
import ru.spacestar.core.model.Response
import ru.spacestar.core.model.ServerError

abstract class BaseRepository {

    protected suspend inline fun <reified T : Any> handleResponse(
        request: () -> HttpResponse
    ): BaseResponse<T> {
        val response = try {
            request()
        } catch (e: ConnectTimeoutException) {
            return NetworkError(e)
        } catch (e: HttpRequestTimeoutException) {
            return NetworkError(e)
        } catch (e: UnresolvedAddressException) {
            return NetworkError(e)
        } catch (e: Throwable) {
            e.printStackTrace()
            return NetworkError(e)
        }
        return when {
            response.status.isSuccess() -> {
                Response(
                    data = response.body()
                )
            }
            response.status.value == 400 -> {
                try {
                    response.body<ServerError>()
                } catch (e: Throwable) {
                    ServerError(
                        code = -1,
                        message = response.bodyAsText().ifEmpty { response.status.description }
                    )
                }
            }
            else -> {
                ServerError(
                    code = -1,
                    message = response.bodyAsText().ifEmpty { response.status.description }
                )
            }
        }
    }
}