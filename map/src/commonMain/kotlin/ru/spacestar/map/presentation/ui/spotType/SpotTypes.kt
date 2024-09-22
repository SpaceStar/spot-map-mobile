package ru.spacestar.map.presentation.ui.spotType

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.jetbrains.compose.resources.stringResource
import ru.spacestar.core_ui.view.BaseAppBarScreen
import ru.spacestar.core_ui.viewmodel.BaseSideEffect
import ru.spacestar.map.presentation.ui.views.item.SpotTypeItem
import spotmap.map.generated.resources.Res
import spotmap.map.generated.resources.select_category_button

@Composable
internal fun SpotTypes(
    navController: NavController,
    viewModel: SpotTypesViewModel
) {
    val state by viewModel.collectAsState()
    val isBackEnabled = remember { viewModel.isSpotTypeSelected() }

    viewModel.collectSideEffect {
        when (it) {
            is BaseSideEffect.Back -> navController.popBackStack()
            is BaseSideEffect.Navigate -> navController.navigate(it.route)
            is BaseSideEffect.Other -> {}
        }
    }

    BaseAppBarScreen(
        navController = navController,
        bindViewModel = viewModel,
        title = stringResource(Res.string.select_category_button),
        isBackEnabled = isBackEnabled
    ) { paddingValues ->
        // TODO: read about LazyColumn and it's optimizations
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (state.isLoading) {
                item {
                    Text("Loading...")
                }
                return@LazyColumn
            }
            items(state.spotTypes) { item ->
                SpotTypeItem(
                    modifier = Modifier.fillMaxWidth(),
                    spotType = item,
                    onClick = { viewModel.selectSpotType(it) }
                )
            }
        }
    }
}