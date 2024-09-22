package ru.spacestar.map.presentation.datasource

import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.get
import ru.spacestar.core.datasource.RemoteDataSource
import ru.spacestar.map.data.Bounds
import ru.spacestar.map.presentation.datasource.model.SpotModel
import ru.spacestar.map.repository.SpotResource
import ru.spacestar.map.repository.datasource.SpotDataSource

class SpotRemoteDataSource(
    private val client: HttpClient
) : RemoteDataSource(), SpotDataSource {
    override suspend fun getSpots(bounds: Bounds, spotType: Int): List<SpotModel> {
        val resource = SpotResource.Map(
            spotType = spotType,
            lat1 = bounds.n.toPlainString(),
            lon1 = bounds.w.toPlainString(),
            lat2 = bounds.s.toPlainString(),
            lon2 = bounds.e.toPlainString()
        )
        return handleResponse { client.get(resource) }
    }
}