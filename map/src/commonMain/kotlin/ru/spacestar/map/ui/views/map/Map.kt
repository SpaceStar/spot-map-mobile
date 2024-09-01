package ru.spacestar.map.ui.views.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.spacestar.map.model.api.Spot
import ru.spacestar.map.model.internal.Bounds

fun interface Map {
    @Composable
    operator fun invoke(
        modifier: Modifier,
        spots: List<Spot>,
        onScroll: (Bounds, Double) -> Unit
    )
}