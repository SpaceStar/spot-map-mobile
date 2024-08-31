package ru.spacestar.map.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.spacestar.core_feature_api.MapApi
import ru.spacestar.map.di.MapComponent

class MapApiImpl : MapApi {
    companion object {
        private const val BASE_ROUTE = "map"

        private const val SPOT_TYPES = "spot_types"
        private const val SPOT_TYPES_ROUTE = "$BASE_ROUTE/$SPOT_TYPES"
    }

    override fun route() = BASE_ROUTE
    fun spotTypes() = SPOT_TYPES_ROUTE

    override fun registerGraph(navGraphBuilder: NavGraphBuilder) {
        val mapComponent = MapComponent()
        navGraphBuilder.composable(BASE_ROUTE) {
            mapComponent.SpotMap()
        }
        navGraphBuilder.composable(SPOT_TYPES_ROUTE) {
            mapComponent.SpotTypes()
        }
    }
}