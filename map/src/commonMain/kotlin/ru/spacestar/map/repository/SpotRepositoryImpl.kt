package ru.spacestar.map.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.spacestar.map.data.Bounds
import ru.spacestar.map.data.Spot
import ru.spacestar.map.domain.repository.SpotRepository
import ru.spacestar.map.repository.datasource.SpotDataSource

internal class SpotRepositoryImpl(
    private val remoteDS: SpotDataSource
) : SpotRepository {
    override fun getSpotsInBounds(
        bounds: Bounds,
        spotType: Int
    ): Flow<List<Spot>> = flow {
        val result = remoteDS.getSpots(bounds, spotType)
            .map { it.toSpot() }
        emit(result)
    }
}