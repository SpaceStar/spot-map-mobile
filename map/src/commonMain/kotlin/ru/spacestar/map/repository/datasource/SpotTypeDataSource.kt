package ru.spacestar.map.repository.datasource

import ru.spacestar.map.presentation.datasource.model.SpotTypeModel

interface SpotTypeDataSource {
    suspend fun getSpotTypes(): List<SpotTypeModel>
}