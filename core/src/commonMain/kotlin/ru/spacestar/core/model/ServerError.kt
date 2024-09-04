package ru.spacestar.core.model

class ServerError (
    val code: Int,
    val message: String?
) : BaseResponse<Any> {
    val userMessage: String?
        get() = message
}