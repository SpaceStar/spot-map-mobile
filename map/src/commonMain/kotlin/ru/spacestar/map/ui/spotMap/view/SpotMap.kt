package ru.spacestar.map.ui.spotMap.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import org.jetbrains.compose.resources.stringResource
import ru.spacestar.core_ui.view.BaseAppBarScreen
import ru.spacestar.core_ui.viewmodel.BaseSideEffect
import ru.spacestar.map.ui.spotMap.business.SpotMapViewModel
import ru.spacestar.map.ui.views.map.MapBox
import spotmap.map.generated.resources.Res
import spotmap.map.generated.resources.select_category_button

@Composable
internal fun SpotMap(
    navController: NavController,
    viewModel: SpotMapViewModel
) {
    val state by viewModel.collectAsState()

    LaunchedEffect(true) {
        viewModel.refreshSpotType()
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner, state) {
        viewModel.trackingMap(lifecycleOwner.lifecycle, state.spotType)
    }

    viewModel.collectSideEffect {
        when (it) {
            is BaseSideEffect.Back -> navController.popBackStack()
            is BaseSideEffect.Navigate -> navController.navigate(it.route)
            is BaseSideEffect.Other -> {}
        }
    }

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
                spots = state.spots,
                onScroll = { bounds, z ->
                    viewModel.getSpots(bounds, z)
                }
            )
            Button(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = { viewModel.showCategories() }
            ) {
                Text(stringResource(Res.string.select_category_button))
            }
        }
    }
}