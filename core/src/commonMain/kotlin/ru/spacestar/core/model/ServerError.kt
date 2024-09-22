package ru.spacestar.core.model

class ServerError (
    val code: Int,
    val message: String?
) {
    val userMessage: String?
        get() = message
}