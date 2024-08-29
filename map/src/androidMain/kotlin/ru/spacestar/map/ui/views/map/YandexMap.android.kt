package ru.spacestar.map.ui.views.map

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mapbox.common.MapboxOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.style.MapStyle
import ru.spacestar.map.BuildConfig

actual val YandexMap: Map
    get() = Map {
            modifier: Modifier,
            spots: List<Spot> ->
        var isMapsInitialized by remember { mutableStateOf(false) }

        LaunchedEffect(true) {
            MapboxOptions.accessToken = BuildConfig.MapboxAccessToken
            isMapsInitialized = true
        }

        if (isMapsInitialized) {
            MapboxMap(
                modifier = modifier,
                mapViewportState = rememberMapViewportState {
                    setCameraOptions {
                        center(Point.fromLngLat(24.9384, 60.1699))
                        zoom(9.0)
                        pitch(0.0)
                        bearing(0.0)
                    }
                },
                style = { MapStyle(style = "mapbox://styles/yutkachev/cm0fetjsp000201qy8u933dj0") }
            )
        }
    }