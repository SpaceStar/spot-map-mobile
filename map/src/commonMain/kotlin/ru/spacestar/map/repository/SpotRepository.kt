package ru.spacestar.map.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import ru.spacestar.core.model.BaseResponse
import ru.spacestar.core.repository.BaseRepository
import ru.spacestar.map.api.SpotResource
import ru.spacestar.map.model.api.Spot
import ru.spacestar.map.model.api.SpotType

internal class SpotRepository(
    private val client: HttpClient
) : BaseRepository() {
    suspend fun getSpotTypes(): BaseResponse<List<SpotType>> {
        return handleResponse { client.get(SpotResource.Types()).body() }
    }

    suspend fun getSpotMap(
        spotType: Int,
        lat1: String,
        lon1: String,
        lat2: String,
        lon2: String
    ): BaseResponse<List<Spot>> {
        val resource = SpotResource.Map(
            spotType = spotType,
            lat1 = lat1,
            lon1 = lon1,
            lat2 = lat2,
            lon2 = lon2
        )
        return handleResponse {
            client.get(resource)
        }
    }
}