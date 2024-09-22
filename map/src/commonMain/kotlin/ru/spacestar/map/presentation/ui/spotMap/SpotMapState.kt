package ru.spacestar.map.presentation.ui.spotMap

import ru.spacestar.map.data.Spot

data class SpotMapState(
    val spotType: Int?,
    val spots: List<Spot>
) {
    companion object {
        val DEFAULT = SpotMapState(
            spotType = null,
            spots = emptyList()
        )
    }
}