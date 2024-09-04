package ru.spacestar.core_ui.viewmodel

import ru.spacestar.core.model.ServerError

sealed interface ErrorSideEffect {
    class NetworkErrorSF(val error: Throwable?) : ErrorSideEffect
    class ServerErrorSF(val error: ServerError) : ErrorSideEffect
}