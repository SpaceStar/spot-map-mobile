package ru.spacestar.map.ui.spotMap.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import ru.spacestar.core_ui.view.BaseAppBarScreen
import ru.spacestar.map.ui.spotMap.business.SpotMapViewModel
import ru.spacestar.map.ui.views.map.MapBox

@Composable
fun SpotMap(
    navController: NavController,
    viewModel: SpotMapViewModel
) {
    val state by viewModel.collectAsState()

    BaseAppBarScreen(
        navController = navController
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            MapBox(
                modifier = Modifier,
                spots = emptyList()
            )
        }
    }
}