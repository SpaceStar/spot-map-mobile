package ru.spacestar.map.model.api

import kotlinx.serialization.Serializable

@Serializable
class UserDetails(
    val id: Long,
    val name: String,
    val photo: String?,
)