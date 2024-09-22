package ru.spacestar.map.repository.datasource

import ru.spacestar.map.data.Bounds
import ru.spacestar.map.presentation.datasource.model.SpotModel

interface SpotDataSource {
    suspend fun getSpots(
        bounds: Bounds,
        spotType: Int
    ): List<SpotModel>
}