package ru.spacestar.map.model.api

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
class Spot(
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
    override fun equals(other: Any?): Boolean {
        if (other !is Spot)
            return false
        return other.id == id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}