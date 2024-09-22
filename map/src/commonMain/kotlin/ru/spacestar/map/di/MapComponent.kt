package ru.spacestar.map.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import ru.spacestar.map.presentation.ui.spotMap.SpotMap as SpotMapInternal
import ru.spacestar.map.presentation.ui.spotType.SpotTypes as SpotTypesInternal

internal class MapComponent : KoinComponent {
    private val navController: NavController by inject()

    @Composable
    fun SpotMap() {
        SpotMapInternal(
            navController = navController,
            viewModel = viewModel { get() }
        )
    }

    @Composable
    fun SpotTypes() {
        SpotTypesInternal(
            navController = navController,
            viewModel = viewModel { get() }
        )
    }
}