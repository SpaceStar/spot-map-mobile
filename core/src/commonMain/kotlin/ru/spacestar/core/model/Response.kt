package ru.spacestar.core.model

class Response<T : Any>(
    val data: T,
) : BaseResponse<T>