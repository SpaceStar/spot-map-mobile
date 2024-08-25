package ru.spacestar.map.di

import org.koin.dsl.module
import ru.spacestar.core_feature_api.MapApi
import ru.spacestar.map.navigation.MapApiImpl
import ru.spacestar.map.ui.spotMap.business.SpotMapViewModel

val mapModule = module {
    factory<MapApi> { MapApiImpl() }
    factory { SpotMapViewModel() }
}