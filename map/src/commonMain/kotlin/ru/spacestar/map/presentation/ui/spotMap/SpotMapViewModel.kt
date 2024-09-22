package ru.spacestar.map.presentation.ui.spotMap

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.withContext
import ru.spacestar.core.utils.preferences.ApplicationPreferences
import ru.spacestar.core_ui.viewmodel.BaseSideEffect
import ru.spacestar.core_ui.viewmodel.BaseViewModel
import ru.spacestar.map.data.BoundsZoom
import ru.spacestar.map.domain.usecase.spotMap.GetSpotsUseCase
import ru.spacestar.map.presentation.navigation.MapApiImpl

internal class SpotMapViewModel(
    private val preferences: ApplicationPreferences,
    private val getSpotsUseCase: GetSpotsUseCase,
) : BaseViewModel<SpotMapState, Any>() {

    private val mapApi by lazy { MapApiImpl() }

    private val currentBounds = MutableSharedFlow<BoundsZoom>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val container = container(SpotMapState.DEFAULT)

    fun refreshSpotType() = intent {
        val spotType = preferences.selectedSpotType.get() ?: run {
            postSideEffect(BaseSideEffect.Navigate(mapApi.spotTypes()))
            return@intent
        }
        reduce {
            state.copy(spotType = spotType)
        }
    }

    fun getSpots(boundsZoom: BoundsZoom) = intent {
        currentBounds.emit(boundsZoom)
    }

    @OptIn(FlowPreview::class)
    suspend fun trackingMap(lifecycle: Lifecycle, spotType: Int?) {
        if (spotType == null) return
        withContext(Dispatchers.IO) {
            currentBounds
                .flowWithLifecycle(lifecycle)
                .sample(500)
                .requestFlow { getSpotsUseCase(it, spotType) }
                .collect {
                    intent {
                        reduce {
                            state.copy(
                                spots = it
                            )
                        }
                    }
                }
        }
    }

    fun showCategories() = intent {
        val route = mapApi.spotTypes()
        postSideEffect(BaseSideEffect.Navigate(route))
    }

    override suspend fun handleNetworkError(e: Throwable): Boolean {
        return false
    }
}