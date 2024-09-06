package ru.spacestar.map.ui.views.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.tappableElement
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.mapbox.common.MapboxOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.extension.compose.DisposableMapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotationGroup
import com.mapbox.maps.extension.compose.annotation.generated.withCircleColor
import com.mapbox.maps.extension.compose.rememberMapState
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.viewport.data.FollowPuckViewportStateBearing
import com.mapbox.maps.plugin.viewport.data.FollowPuckViewportStateOptions
import com.mapbox.maps.toCameraOptions
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.spacestar.core.utils.MAX_LON
import ru.spacestar.core.utils.MIN_LON
import ru.spacestar.core.utils.VALUE_360
import ru.spacestar.core.utils.toBigDecimal
import ru.spacestar.map.BuildConfig
import ru.spacestar.map.model.api.Spot
import ru.spacestar.map.model.internal.Bounds

// TODO: clean/refactor
@OptIn(ExperimentalPermissionsApi::class, FlowPreview::class)
actual val MapBox: Map
    get() = Map {
            modifier: Modifier,
            spots: List<Spot>,
            onScroll: (Bounds, Double) -> Unit ->
        var isMapsInitialized by remember { mutableStateOf(false) }
        val mapViewportState = rememberMapViewportState()

        val cameraPermissionState = rememberMultiplePermissionsState(
            listOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            )
        )

        val mapboxMap = remember {
            mutableStateOf<MapboxMap?>(null)
        }

        LaunchedEffect(true) {
            MapboxOptions.accessToken = BuildConfig.mapboxAccessToken
            isMapsInitialized = true
//            delay(1000)
//            mapViewportState.flyTo(
//                cameraOptions = cameraOptions {
//                    center(Point.fromLngLat(74.623155, 42.822047))
//                    zoom(11.0)
//                },
//                MapAnimationOptions.mapAnimationOptions { duration(1500) }
//            )
        }

        LaunchedEffect(cameraPermissionState, isMapsInitialized) {
            if (cameraPermissionState.permissions.any { it.status.isGranted } && isMapsInitialized) {
                mapViewportState.transitionToFollowPuckState(
                    followPuckViewportStateOptions = FollowPuckViewportStateOptions
                        .Builder()
                        .pitch(0.0)
                        .bearing(FollowPuckViewportStateBearing.Constant(0.0))
                        .build(),
                    completionListener = { mapViewportState.idle() }
                )
            } else {
                cameraPermissionState.launchMultiplePermissionRequest()
            }
        }

        if (isMapsInitialized) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                val mapState = rememberMapState {
                    gesturesSettings = gesturesSettings.toBuilder().setRotateEnabled(false).build()
                }

                val lifecycleOwner = LocalLifecycleOwner.current
                val coroutineScope = rememberCoroutineScope()

                // TODO: true? or args
                LaunchedEffect(lifecycleOwner, mapboxMap.value) {
                    if (mapboxMap.value == null) return@LaunchedEffect
                    mapState.cameraChangedEvents
                        .map { it.cameraState }
                        .stateIn(
                            scope = coroutineScope,
                            started = SharingStarted.WhileSubscribed(),
                            initialValue = mapboxMap.value!!.cameraState
                        )
                        .flowWithLifecycle(lifecycleOwner.lifecycle)
                        .collect { cameraState ->
                            val camera = cameraState.toCameraOptions()
                            val viewPortZoom = mapboxMap.value!!.coordinateBoundsZoomForCameraUnwrapped(camera)
                            val viewPort = viewPortZoom.bounds
                            val zoom = viewPortZoom.zoom
                            val n = viewPort.north().toBigDecimal()
                            val s = viewPort.south().toBigDecimal()
                            val rawW = viewPort.west().toBigDecimal()
                            val rawE = viewPort.east().toBigDecimal()
                            val w: BigDecimal
                            val e: BigDecimal
                            if (rawE - rawW >= BigDecimal.VALUE_360) {
                                w = BigDecimal.MIN_LON
                                e = BigDecimal.MAX_LON
                            } else {
                                w = if (rawW < BigDecimal.MIN_LON)
                                    rawW + BigDecimal.VALUE_360
                                else
                                    rawW
                                e = if (rawE > BigDecimal.MAX_LON)
                                    rawE - BigDecimal.VALUE_360
                                else
                                    rawE
                            }
                            val bounds = Bounds(
                                n = n,
                                e = e,
                                s = s,
                                w = w
                            )
                            onScroll(bounds, zoom)
                        }
                }

                MapboxMap(
                    modifier = Modifier.weight(1f),
                    compass = { Compass(
                        modifier = Modifier.windowInsetsPadding(WindowInsets.tappableElement)
                    ) },
                    scaleBar = { ScaleBar(
                        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
                    ) },
                    attribution = { Attribution(
                        modifier = Modifier.windowInsetsPadding(WindowInsets.tappableElement)
                    ) },
                    logo = { Logo(
                        modifier = Modifier.windowInsetsPadding(WindowInsets.tappableElement)
                    ) },
                    mapViewportState = mapViewportState,
                    mapState = mapState,
                    style = { MapStyle(style = BuildConfig.mapboxStyleUrl) }
                ) {

                    DisposableMapEffect(true) { mapView ->
                        mapView.location.locationPuck = createDefault2DPuck(true)
                        mapView.location.enabled = true
                        mapView.location.showAccuracyRing = true
                        mapView.location.puckBearingEnabled = true
                        mapView.location.puckBearing = PuckBearing.HEADING
                        onDispose { mapView.location.enabled = false }
                    }

                    DisposableMapEffect(true) {
                        mapboxMap.value = it.mapboxMap
                        onDispose { mapboxMap.value = null }
                    }

                    CircleAnnotationGroup(
                        annotations = spots.map {
                            CircleAnnotationOptions()
                                .withPoint(Point.fromLngLat(it.lon.toDouble(), it.lat.toDouble()))
                                .withCircleRadius(10.0)
                                .withCircleColor(Color.Red)
                        }
                    )
//                    CircleAnnotationGroup(
//                        annotations = POINTS_TO_ADD.map {
//                            CircleAnnotationOptions()
//                                .withPoint(it)
//                                .withCircleRadius(10.0)
//                                .withCircleColor(Color.Red)
//                        },
//                        annotationConfig = AnnotationConfig(
//                            annotationSourceOptions = AnnotationSourceOptions(
//                                clusterOptions = ClusterOptions(
//                                    textColorExpression = Expression.color(Color.Yellow.toArgb()),
//                                    textColor = Color.Black.toArgb(),
//                                    textSize = 20.0,
//                                    circleRadiusExpression = literal(25.0),
//                                    colorLevels = listOf(
//                                        Pair(100, Color.Red.toArgb()),
//                                        Pair(50, Color.Blue.toArgb()),
//                                        Pair(0, Color.Green.toArgb())
//                                    )
//                                )
//                            )
//                        ),
//                        onClick = {
//                            Toast.makeText(
//                                context,
//                                "Clicked on Circle Annotation Cluster item: $it",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                            true
//                        }
//                    )
//                    ViewAnnotation(
//                        options = viewAnnotationOptions {
//                            // set the view annotation associated geometry
//                            geometry(Point.fromLngLat(0.0, 0.0))
//                            annotationAnchor {
//                                anchor(ViewAnnotationAnchor.BOTTOM)
//                            }
//                            allowOverlap(false)
//                        }
//                    ) {
//                        // You can add the content to be drawn in the ViewAnnotation using Composable functions, e.g. to insert a button:
//                        Button(
//                            onClick = {
//                                Toast.makeText(context, "Click", LENGTH_SHORT).show()
//                            }
//                        ) {
//                            Text("Click me")
//                        }
//                    }
                }
            }
        }
    }

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun FeatureThatRequiresCameraPermission() {

    // Camera permission state
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    if (cameraPermissionState.status.isGranted) {
        Text("Camera permission Granted")
    } else {
        Column {
            val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
                // If the user has denied the permission but the rationale can be shown,
                // then gently explain why the app requires this permission
                "The camera is important for this app. Please grant the permission."
            } else {
                // If it's the first time the user lands on this feature, or the user
                // doesn't want to be asked again for this permission, explain that the
                // permission is required
                "Camera permission required for this feature to be available. " +
                        "Please grant the permission"
            }
            Text(textToShow)
            Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                Text("Request permission")
            }
        }
    }
}