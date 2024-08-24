package ru.spacestar.spotmap

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform