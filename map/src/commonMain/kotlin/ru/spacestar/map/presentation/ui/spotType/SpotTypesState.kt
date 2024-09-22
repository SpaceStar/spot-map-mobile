package ru.spacestar.map.presentation.ui.spotType

import ru.spacestar.map.data.SpotType

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
