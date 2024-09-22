package ru.spacestar.map.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.spacestar.core_feature_api.MapApi
import ru.spacestar.map.domain.repository.SpotRepository
import ru.spacestar.map.domain.repository.SpotTypeRepository
import ru.spacestar.map.domain.usecase.spotMap.GetSpotsUseCase
import ru.spacestar.map.domain.usecase.spotType.GetSpotTypesUseCase
import ru.spacestar.map.presentation.datasource.SpotRemoteDataSource
import ru.spacestar.map.presentation.datasource.SpotTypeRemoteDataSource
import ru.spacestar.map.presentation.navigation.MapApiImpl
import ru.spacestar.map.presentation.ui.spotMap.SpotMapViewModel
import ru.spacestar.map.presentation.ui.spotType.SpotTypesViewModel
import ru.spacestar.map.repository.SpotRepositoryImpl
import ru.spacestar.map.repository.SpotTypeRepositoryImpl
import ru.spacestar.map.repository.datasource.SpotDataSource
import ru.spacestar.map.repository.datasource.SpotTypeDataSource

val mapModule = module {
    factory<MapApi> { MapApiImpl() }
    factoryOf(::SpotMapViewModel)
    factoryOf(::GetSpotsUseCase)
    factory<SpotRepository> { SpotRepositoryImpl(get()) }
    factory<SpotDataSource> { SpotRemoteDataSource(get()) }
    factoryOf(::SpotTypesViewModel)
    factoryOf(::GetSpotTypesUseCase)
    factory<SpotTypeRepository> { SpotTypeRepositoryImpl(get()) }
    factory<SpotTypeDataSource> { SpotTypeRemoteDataSource(get()) }
}