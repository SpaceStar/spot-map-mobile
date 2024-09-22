package ru.spacestar.map.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.spacestar.map.data.SpotType

interface SpotTypeRepository {
    fun getSpotTypes(): Flow<List<SpotType>>
}