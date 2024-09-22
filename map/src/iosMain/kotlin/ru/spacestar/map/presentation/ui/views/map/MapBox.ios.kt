package ru.spacestar.map.presentation.ui.views.map

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitViewController
import kotlinx.cinterop.ExperimentalForeignApi
import ru.spacestar.core_ui.view.ComposeUpdateListener
import ru.spacestar.map.di.MapIosComponent
import ru.spacestar.map.model.internal.Bounds
import ru.spacestar.map.presentation.datasource.model.SpotModel

@OptIn(ExperimentalForeignApi::class)
actual val MapBox: Map
    get() = Map {
            modifier: Modifier,
            spots: List<SpotModel>,
            onScroll: (Bounds, Double) -> Unit ->
        val viewController = remember { MapIosComponent().mapViewController }
        val updateListener = remember { ComposeUpdateListener<MapUpdateEvent>() }

        LaunchedEffect(spots, onScroll) {
            updateListener.update(MapUpdateEvent(spots, onScroll))
        }

        UIKitViewController(
            factory = { viewController(
                MapUpdateEvent(spots, onScroll),
                updateListener
            ) },
            modifier = modifier,
        )
    }