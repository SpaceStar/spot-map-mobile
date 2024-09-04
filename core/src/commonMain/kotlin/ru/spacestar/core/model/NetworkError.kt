package ru.spacestar.core.model

class NetworkError (
    val e: Throwable
) : BaseResponse<Any>