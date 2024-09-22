package ru.spacestar.map.presentation.ui.views.map

import ru.spacestar.map.model.internal.Bounds
import ru.spacestar.map.presentation.datasource.model.SpotModel

class MapUpdateEvent(
    val spots: List<SpotModel>,
    val onScroll: (Bounds, Double) -> Unit
)