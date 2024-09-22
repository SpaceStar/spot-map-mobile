package ru.spacestar.map.domain.usecase.spotType

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.flowOn
import ru.spacestar.map.domain.repository.SpotTypeRepository

class GetSpotTypesUseCase(
    private val repository: SpotTypeRepository
) {
    operator fun invoke() = repository.getSpotTypes().flowOn(Dispatchers.IO)
}