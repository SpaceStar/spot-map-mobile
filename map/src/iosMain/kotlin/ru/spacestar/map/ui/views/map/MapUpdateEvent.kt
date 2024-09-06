package ru.spacestar.map.ui.views.map

import ru.spacestar.map.model.api.Spot
import ru.spacestar.map.model.internal.Bounds

class MapUpdateEvent(
    val spots: List<Spot>,
    val onScroll: (Bounds, Double) -> Unit
)