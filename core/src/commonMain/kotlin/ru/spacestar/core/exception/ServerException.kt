package ru.spacestar.core.exception

import ru.spacestar.core.model.ServerError

class ServerException(
    val serverError: ServerError
) : Exception(serverError.message)