package ru.spacestar.map.presentation.ui.views.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.spacestar.map.data.BoundsZoom
import ru.spacestar.map.data.Spot

fun interface Map {
    @Composable
    operator fun invoke(
        modifier: Modifier,
        spots: List<Spot>,
        onScroll: (BoundsZoom) -> Unit
    )
}