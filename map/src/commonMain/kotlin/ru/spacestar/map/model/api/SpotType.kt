package ru.spacestar.map.model.api

import kotlinx.serialization.Serializable

@Serializable
data class SpotType(
    val id: Int,
    val name: String,
    val selected: Boolean = false
)