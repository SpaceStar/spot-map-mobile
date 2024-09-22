package ru.spacestar.map.presentation.datasource.model

import kotlinx.serialization.Serializable

@Serializable
class UserDetails(
    val id: Long,
    val name: String,
    val photo: String?,
)