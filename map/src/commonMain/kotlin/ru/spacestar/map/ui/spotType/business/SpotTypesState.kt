package ru.spacestar.map.ui.spotType.business

import ru.spacestar.map.model.api.SpotType

data class SpotTypesState(
    val isLoading: Boolean,
    val spotTypes: List<SpotType>,
) {
    companion object {
        val DEFAULT = SpotTypesState(
            isLoading = true,
            spotTypes = emptyList(),
        )
    }
}
