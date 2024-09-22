package ru.spacestar.map.presentation.ui.spotType

import ru.spacestar.core.utils.preferences.ApplicationPreferences
import ru.spacestar.core_ui.viewmodel.BaseSideEffect
import ru.spacestar.core_ui.viewmodel.BaseViewModel
import ru.spacestar.map.data.SpotType
import ru.spacestar.map.domain.usecase.spotType.GetSpotTypesUseCase

internal class SpotTypesViewModel(
    private val preferences: ApplicationPreferences,
    private val getSpotTypesUseCase: GetSpotTypesUseCase
) : BaseViewModel<SpotTypesState, Any>() {

    override val container = container(SpotTypesState.DEFAULT) {
        val selected = preferences.selectedSpotType.get()
        getSpotTypesUseCase()
            .handleError()
            .collect {
                reduce {
                    state.copy(
                        spotTypes = refreshSelected(it, selected)
                    )
                }
            }
        reduce {
            state.copy(
                isLoading = false
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
        postSideEffect(BaseSideEffect.Back)
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