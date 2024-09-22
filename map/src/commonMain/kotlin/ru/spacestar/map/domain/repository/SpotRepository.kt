package ru.spacestar.map.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.spacestar.map.data.Bounds
import ru.spacestar.map.data.Spot

interface SpotRepository {
    fun getSpotsInBounds(bounds: Bounds, spotType: Int): Flow<List<Spot>>
}