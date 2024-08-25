package ru.spacestar.map.ui.views.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

fun interface Map {
    @Composable
    operator fun invoke(
        modifier: Modifier,
        spots: List<Spot>
    )
}