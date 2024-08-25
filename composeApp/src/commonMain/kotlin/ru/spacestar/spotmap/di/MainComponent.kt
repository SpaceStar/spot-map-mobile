package ru.spacestar.spotmap.di

import androidx.navigation.NavHostController
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.spacestar.core_feature_api.MapApi

class MainComponent : KoinComponent {
    val navController: NavHostController by inject()

    val mapApi: MapApi by inject()
}