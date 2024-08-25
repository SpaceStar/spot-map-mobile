package ru.spacestar.map.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.spacestar.core_feature_api.MapApi
import ru.spacestar.map.di.MapComponent

class MapApiImpl : MapApi {
    companion object {
        private const val BASE_ROUTE = "map"
    }

    override fun route() = BASE_ROUTE

    override fun registerGraph(navGraphBuilder: NavGraphBuilder) {
        val mapComponent = MapComponent()
        navGraphBuilder.composable(BASE_ROUTE) {
            mapComponent.SpotMap()
        }
    }
}