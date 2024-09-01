package ru.spacestar.map.ui.spotMap.business

import ru.spacestar.map.model.api.Spot

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