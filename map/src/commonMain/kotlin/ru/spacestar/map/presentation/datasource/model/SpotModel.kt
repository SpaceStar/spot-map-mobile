package ru.spacestar.map.presentation.datasource.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import ru.spacestar.core.utils.toBigDecimal
import ru.spacestar.map.data.Spot

@Serializable
class SpotModel(
    val id: Long,
    val groupCount: Int?,
    val spotTypeId: Int,
    val userId: Long?,
    val name: String,
    val lat: String,
    val lon: String,
    val description: String?,
    val created: LocalDateTime,
    val accessLevel: Short,
    val approved: Boolean,
    val actual: Boolean,
    val photos: List<String>?,
    val userDetails: UserDetails?,
) {
    fun toSpot() = Spot(
        id = id,
        spotType = spotTypeId,
        lat = lat.toBigDecimal(),
        lon = lon.toBigDecimal(),
    )
}