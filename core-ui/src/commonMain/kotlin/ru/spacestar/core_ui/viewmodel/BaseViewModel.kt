package ru.spacestar.core_ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.SettingsBuilder
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.Syntax
import ru.spacestar.core.model.BaseResponse
import ru.spacestar.core.model.NetworkError
import ru.spacestar.core.model.Response
import ru.spacestar.core.model.ServerError

abstract class BaseViewModel<S : Any, SF : Any> : ViewModel(), ContainerHost<S, BaseSideEffect<SF>> {

    private val retryFlow = MutableSharedFlow<Boolean>()
    private var retryEnabled = false

    private val errorFlow = MutableSharedFlow<ErrorSideEffect>()

    protected fun container(
        initialState: S,
        buildSettings: SettingsBuilder.() -> Unit = {},
        onCreate: (suspend Syntax<S, BaseSideEffect<SF>>.() -> Unit)? = null
    ) = viewModelScope.container(initialState, buildSettings, onCreate)

    protected suspend fun Syntax<S, BaseSideEffect<SF>>.postSideEffect(sideEffect: SF) {
        postSideEffect(BaseSideEffect.Other(sideEffect))
    }

    @Composable
    fun collectAsState(lifecycleState: Lifecycle.State = Lifecycle.State.STARTED) =
        container.stateFlow.collectAsStateWithLifecycle(minActiveState = lifecycleState)

    @Composable
    fun collectSideEffect(
        lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
        sideEffect: (suspend (sideEffect: BaseSideEffect<SF>) -> Unit)
    ) {
        val sideEffectFlow = container.sideEffectFlow
        val lifecycleOwner = LocalLifecycleOwner.current

        val callback by rememberUpdatedState(newValue = sideEffect)

        LaunchedEffect(sideEffectFlow, lifecycleOwner) {
            lifecycleOwner.lifecycle.repeatOnLifecycle(lifecycleState) {
                sideEffectFlow.collect { callback(it) }
            }
        }
    }

    @Composable
    fun collectErrors(
        lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
        errorCallback: (suspend (error: ErrorSideEffect) -> Unit)
    ) {
        retryEnabled = true
        val lifecycleOwner = LocalLifecycleOwner.current

        val callback by rememberUpdatedState(newValue = errorCallback)

        LaunchedEffect(lifecycleOwner) {
            lifecycleOwner.lifecycle.repeatOnLifecycle(lifecycleState) {
                errorFlow.collect { callback(it) }
            }
        }
    }

    suspend fun retry(value: Boolean) {
        retryFlow.emit(value)
    }

    protected suspend fun postError(e: ErrorSideEffect) {
        errorFlow.emit(e)
    }

    protected suspend fun waitForRetry(): Boolean = retryFlow.first()

    /**
     * returns Boolean indicating if caller should retry the request
     */
    protected open suspend fun handleNetworkError(response: NetworkError): Boolean {
        if (retryEnabled) {
            errorFlow.emit(ErrorSideEffect.NetworkErrorSF(response.e))
            return retryFlow.first()
        }
        return false
    }

    protected open suspend fun handleServerError(response: ServerError) {
        errorFlow.emit(ErrorSideEffect.ServerErrorSF(response))
    }

    /**
     * returns Boolean indicating if caller should retry the request
     */
    private suspend fun <T : Any> handleError(response: BaseResponse<T>): Boolean {
        return when (response) {
            is NetworkError -> handleNetworkError(response)
            is ServerError -> {
                handleServerError(response)
                false
            }
            is Response -> false
        }
    }

    protected suspend fun <T : Any> request(request: suspend () -> BaseResponse<T>): T? {
        var retry = true
        while (retry) {
            val response = request()
            retry = handleError(response)
            if (retry)
                continue
            return if (response is Response)
                response.data
            else
                null
        }
        throw IllegalStateException()
    }


}