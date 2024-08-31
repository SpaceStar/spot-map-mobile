package ru.spacestar.map.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import ru.spacestar.map.api.SpotResource
import ru.spacestar.map.model.api.SpotType

internal class SpotRepository(
    private val client: HttpClient
) {
    suspend fun getSpotTypes(): List<SpotType> {
        return client.get(SpotResource.Types()).body()
    }
}