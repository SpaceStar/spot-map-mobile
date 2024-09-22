package ru.spacestar.map.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.spacestar.map.data.SpotType
import ru.spacestar.map.domain.repository.SpotTypeRepository
import ru.spacestar.map.repository.datasource.SpotTypeDataSource

class SpotTypeRepositoryImpl(
    private val remoteDS: SpotTypeDataSource
) : SpotTypeRepository {
    override fun getSpotTypes(): Flow<List<SpotType>> = flow {
        val result = remoteDS.getSpotTypes()
            .map { it.toSpotType() }
        emit(result)
    }
}