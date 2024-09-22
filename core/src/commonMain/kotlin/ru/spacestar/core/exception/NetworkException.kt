package ru.spacestar.core.exception

class NetworkException(override val cause: Throwable) : Exception(cause)