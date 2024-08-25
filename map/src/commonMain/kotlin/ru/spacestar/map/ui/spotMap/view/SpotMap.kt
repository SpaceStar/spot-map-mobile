package ru.spacestar.map.ui.spotMap.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.spacestar.core_ui.view.BaseAppBarScreen
import ru.spacestar.map.ui.spotMap.business.SpotMapViewModel
import ru.spacestar.map.ui.views.map.YandexMap

@Composable
fun SpotMap(
    navController: NavController,
    viewModel: SpotMapViewModel
) {
    val state by viewModel.collectAsState()

    BaseAppBarScreen(
        navController = navController
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            YandexMap(
                modifier = Modifier,
                spots = emptyList()
            )
        }
    }
}