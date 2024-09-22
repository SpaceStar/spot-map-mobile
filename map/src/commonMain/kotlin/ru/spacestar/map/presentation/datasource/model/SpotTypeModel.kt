package ru.spacestar.map.presentation.datasource.model

import kotlinx.serialization.Serializable
import ru.spacestar.map.data.SpotType

@Serializable
class SpotTypeModel(
    val id: Int,
    val name: String,
) {
    fun toSpotType() = SpotType(
        id = id,
        name = name,
        selected = false
    )
}