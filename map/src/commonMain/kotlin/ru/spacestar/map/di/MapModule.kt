package ru.spacestar.map.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.spacestar.core_feature_api.MapApi
import ru.spacestar.map.navigation.MapApiImpl
import ru.spacestar.map.repository.SpotRepository
import ru.spacestar.map.ui.spotMap.business.SpotMapViewModel
import ru.spacestar.map.ui.spotType.business.SpotTypesViewModel

val mapModule = module {
    factory<MapApi> { MapApiImpl() }
    factoryOf(::SpotMapViewModel)
    factoryOf(::SpotRepository)
    factoryOf(::SpotTypesViewModel)
}