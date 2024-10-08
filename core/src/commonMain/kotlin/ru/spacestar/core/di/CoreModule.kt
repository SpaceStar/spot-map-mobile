package ru.spacestar.core.di

import SpotMap.core.BuildConfig
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.request.host
import io.ktor.http.Url
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.spacestar.core.utils.preferences.ApplicationPreferences

val coreModule = module {
    includes(kvStorageModule)
    factoryOf(::ApplicationPreferences)
    factory {
        @OptIn(ExperimentalSerializationApi::class)
        Json {
            encodeDefaults = true
            explicitNulls = false
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }
    // TODO: remove logging from release build
    // TODO: crashing by timeout
    // TODO: crashing btw - FIX!!! NO CRASHES PLS
    single {
        HttpClient {
            install(HttpCache)
            install(HttpTimeout) {
                connectTimeoutMillis = 10000
                requestTimeoutMillis = HttpTimeout.INFINITE_TIMEOUT_MS
            }
            // TODO: think about
            install(HttpRequestRetry) {
                maxRetries = 5
                retryOnExceptionIf { _, e ->
                    e is ConnectTimeoutException
                            || e is HttpRequestTimeoutException
                }
                exponentialDelay()
            }
            defaultRequest {
                url(BuildConfig.BASE_URL)
            }
            install(ContentNegotiation) {
                json(get())
            }
            install(Resources)
            install(Auth) {
                bearer {
                    val preferences: ApplicationPreferences = get()
                    val host = Url(BuildConfig.BASE_URL).host
                    loadTokens {
                        preferences.token.get()?.let { BearerTokens(it, "") }
                    }
                    refreshTokens {
                        preferences.token.get()?.let { BearerTokens(it, "") }
                    }
                    sendWithoutRequest {
                        it.host == host
                    }
                }
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.v(message, null, "HTTP Client")
                    }
                }
                level = LogLevel.ALL
            }
        }.also { Napier.base(DebugAntilog()) }
    }
}