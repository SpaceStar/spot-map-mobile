package ru.spacestar.core.datasource

import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.util.network.UnresolvedAddressException
import ru.spacestar.core.exception.NetworkException
import ru.spacestar.core.exception.ServerException
import ru.spacestar.core.model.ServerError

abstract class RemoteDataSource {

    protected suspend inline fun <reified T : Any> handleResponse(
        request: () -> HttpResponse
    ): T {
        val response = try {
            request()
        } catch (e: ConnectTimeoutException) {
            throw NetworkException(e)
        } catch (e: HttpRequestTimeoutException) {
            throw NetworkException(e)
        } catch (e: UnresolvedAddressException) {
            throw NetworkException(e)
        } catch (e: Throwable) {
            e.printStackTrace()
            throw NetworkException(e)
        }
        when {
            response.status.isSuccess() -> {
                return response.body()
            }
            response.status.value == 400 -> {
                val error = try {
                    response.body<ServerError>()
                } catch (e: Throwable) {
                    getErrorFromResponse(response)
                }
                throw ServerException(error)
            }
            else -> {
                val error = getErrorFromResponse(response)
                throw ServerException(error)
            }
        }
    }

    protected suspend inline fun getErrorFromResponse(response: HttpResponse) = ServerError(
        code = -1,
        message = response.bodyAsText().ifEmpty { response.status.description }
    )
}