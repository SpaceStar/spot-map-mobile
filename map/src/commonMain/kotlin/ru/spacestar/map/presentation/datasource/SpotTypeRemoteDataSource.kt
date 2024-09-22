package ru.spacestar.map.presentation.datasource

import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.get
import ru.spacestar.core.datasource.RemoteDataSource
import ru.spacestar.map.presentation.datasource.model.SpotTypeModel
import ru.spacestar.map.repository.SpotResource
import ru.spacestar.map.repository.datasource.SpotTypeDataSource

class SpotTypeRemoteDataSource(
    private val client: HttpClient
) : RemoteDataSource(), SpotTypeDataSource {
    override suspend fun getSpotTypes(): List<SpotTypeModel> {
        return handleResponse { client.get(SpotResource.Types()) }
    }
}