package ru.spacestar.map.ui.views.map

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitViewController
import kotlinx.cinterop.ExperimentalForeignApi
import ru.spacestar.core_ui.view.ComposeUpdateListener
import ru.spacestar.map.di.MapIosComponent
import ru.spacestar.map.model.api.Spot
import ru.spacestar.map.model.internal.Bounds

@OptIn(ExperimentalForeignApi::class)
actual val MapBox: Map
    get() = Map {
            modifier: Modifier,
            spots: List<Spot>,
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