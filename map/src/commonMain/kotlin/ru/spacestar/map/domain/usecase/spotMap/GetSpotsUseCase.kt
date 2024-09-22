package ru.spacestar.map.domain.usecase.spotMap

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.spacestar.map.data.BoundsZoom
import ru.spacestar.map.data.Spot
import ru.spacestar.map.domain.repository.SpotRepository

class GetSpotsUseCase(
    private val repository: SpotRepository
) {
    private val cachedSpots by lazy { hashSetOf<Spot>() }
    private var cachedZoom = 0f

    operator fun invoke(boundsZoom: BoundsZoom, spotType: Int) = flow {
        val cachedSpotType = cachedSpots.firstOrNull()?.spotType
        if (cachedSpotType != spotType) {
            cachedSpots.clear()
        }
        repository.getSpotsInBounds(boundsZoom.bounds, spotType)
            .collect { newSpots ->
                if (cachedZoom != boundsZoom.zoom) {
                    cachedZoom = boundsZoom.zoom
                    cachedSpots.clear()
                }
                cachedSpots.addAll(newSpots)
                emit(cachedSpots.toList())
            }
    }.flowOn(Dispatchers.IO)
}