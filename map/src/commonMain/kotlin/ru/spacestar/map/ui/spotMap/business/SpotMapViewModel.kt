package ru.spacestar.map.ui.spotMap.business

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.withContext
import ru.spacestar.core.utils.preferences.ApplicationPreferences
import ru.spacestar.core_ui.viewmodel.BaseSideEffect
import ru.spacestar.core_ui.viewmodel.BaseViewModel
import ru.spacestar.map.model.api.Spot
import ru.spacestar.map.model.internal.Bounds
import ru.spacestar.map.navigation.MapApiImpl
import ru.spacestar.map.repository.SpotRepository

internal class SpotMapViewModel(
    private val preferences: ApplicationPreferences,
    private val repository: SpotRepository,
) : BaseViewModel<SpotMapState, Any>() {

    private val mapApi by lazy { MapApiImpl() }
    private val cachedSpots = mutableSetOf<Spot>()
    private var cachedZoom: Double = NOT_CACHED
    private var refreshCache = false

    private val currentBounds = MutableStateFlow<Bounds?>(null)

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

    fun getSpots(
        bounds: Bounds,
        z: Double
    ) = intent {
        if (cachedZoom != z) {
            cachedZoom = z
            refreshCache = true
        }
        currentBounds.emit(bounds)
    }

    @OptIn(FlowPreview::class)
    suspend fun trackingMap(lifecycle: Lifecycle, spotType: Int?) {
        if (spotType == null) return
        withContext(Dispatchers.IO) {
            currentBounds
                .flowWithLifecycle(lifecycle)
                .filterNotNull()
                .sample(500)
                .collect {
                    val newSpots = repository.getSpotMap(
                        spotType = spotType,
                        lat1 = it.n.toPlainString(),
                        lon1 = it.w.toPlainString(),
                        lat2 = it.s.toPlainString(),
                        lon2 = it.e.toPlainString()
                    )
                    if (refreshCache) {
                        refreshCache = false
                        cachedSpots.clear()
                    }
                    cachedSpots.addAll(newSpots)
                    intent {
                        reduce {
                            state.copy(
                                spots = cachedSpots.toList()
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

    companion object {
        private const val NOT_CACHED = -1.0
    }
}