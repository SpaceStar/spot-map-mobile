package ru.spacestar.map.ui.spotType.business

import ru.spacestar.core.utils.preferences.ApplicationPreferences
import ru.spacestar.core_ui.viewmodel.BaseSideEffect
import ru.spacestar.core_ui.viewmodel.BaseViewModel
import ru.spacestar.map.model.api.SpotType
import ru.spacestar.map.repository.SpotRepository

internal class SpotTypesViewModel(
    private val repository: SpotRepository,
    private val preferences: ApplicationPreferences
) : BaseViewModel<SpotTypesState, Any>() {

    override val container = container(SpotTypesState.DEFAULT) {
        val selected = preferences.selectedSpotType.get()
        val spotTypes = repository.getSpotTypes()
        reduce {
            state.copy(
                isLoading = false,
                spotTypes = refreshSelected(spotTypes, selected)
            )
        }
    }

    fun selectSpotType(id: Int) = intent {
        preferences.selectedSpotType.put(id)
        reduce {
            state.copy(
                spotTypes = refreshSelected(state.spotTypes, id)
            )
        }
        postSideEffect(BaseSideEffect.Back())
    }

    fun isSpotTypeSelected(): Boolean {
        val spotType = preferences.selectedSpotType.get()
        return spotType != null
    }

    private fun refreshSelected(spotTypes: List<SpotType>, selected: Int?): List<SpotType> {
        return spotTypes.map {
            it.copy(selected = it.id == selected)
        }
    }
}