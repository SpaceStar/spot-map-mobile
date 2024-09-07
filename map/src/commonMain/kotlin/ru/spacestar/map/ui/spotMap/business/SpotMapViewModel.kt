package ru.spacestar.map.ui.spotMap.business

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.withContext
import ru.spacestar.core.model.NetworkError
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

    private val currentBounds = MutableSharedFlow<Bounds>(
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
        if (cachedSpots.firstOrNull()?.spotTypeId != spotType) {
            cachedSpots.clear()
            intent { reduce { state.copy(spots = emptyList()) } }
        }
        withContext(Dispatchers.IO) {
            currentBounds
                .flowWithLifecycle(lifecycle)
                .sample(500)
                .collect {
                    val newSpots = request {
                        repository.getSpotMap(
                            spotType = spotType,
                            lat1 = it.n,
                            lon1 = it.w,
                            lat2 = it.s,
                            lon2 = it.e
                        )
                    } ?: return@collect
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

    override suspend fun handleNetworkError(response: NetworkError): Boolean {
        return false
    }

    companion object {
        private const val NOT_CACHED = -1.0
    }
}