package ru.spacestar.map.ui.views.map

import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.mapbox.common.MapboxOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.ViewAnnotationAnchor
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotationGroup
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.withCircleColor
import com.mapbox.maps.extension.compose.rememberMapState
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.extension.style.expressions.dsl.generated.literal
import com.mapbox.maps.extension.style.expressions.generated.Expression
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.AnnotationSourceOptions
import com.mapbox.maps.plugin.annotation.ClusterOptions
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.viewannotation.annotationAnchor
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.imageResource
import ru.spacestar.map.BuildConfig
import ru.spacestar.map.R

actual val MapBox: Map
    get() = Map {
            modifier: Modifier,
            spots: List<Spot> ->
        var isMapsInitialized by remember { mutableStateOf(false) }
        val mapViewportState = rememberMapViewportState {
//            setCameraOptions {
//                center(Point.fromLngLat(24.9384, 60.1699))
//                zoom(9.0)
//                pitch(0.0)
//                bearing(0.0)
//            }
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

        if (isMapsInitialized) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                val mapState = rememberMapState().apply {
                    gesturesSettings = gesturesSettings.toBuilder().setRotateEnabled(false).build()
                }
                MapboxMap(
                    modifier = Modifier.weight(1f),
                    compass = { Compass() },
                    scaleBar = { ScaleBar() },
                    attribution = { Attribution() },
                    logo = { Logo() },
                    mapViewportState = mapViewportState,
                    mapState = mapState,
                    style = { MapStyle(style = BuildConfig.mapboxStyleUrl) }
                ) {
                    val context = LocalContext.current
                    val POINTS_TO_ADD = listOf(
                        Point.fromLngLat(0.0, 0.0),
                        Point.fromLngLat(10.0, 0.0),
                        Point.fromLngLat(0.1, 0.0),
                        Point.fromLngLat(0.12, 0.0),
                        Point.fromLngLat(0.123, 0.0),
                        Point.fromLngLat(0.1234, 0.0),
                        Point.fromLngLat(0.12345, 0.0),
                        Point.fromLngLat(0.123456, 0.0),
                    )
                    CircleAnnotationGroup(
                        annotations = POINTS_TO_ADD.map {
                            CircleAnnotationOptions()
                                .withPoint(it)
                                .withCircleRadius(10.0)
                                .withCircleColor(Color.Red)
                        },
                        annotationConfig = AnnotationConfig(
                            annotationSourceOptions = AnnotationSourceOptions(
                                clusterOptions = ClusterOptions(
                                    textColorExpression = Expression.color(Color.Yellow.toArgb()),
                                    textColor = Color.Black.toArgb(),
                                    textSize = 20.0,
                                    circleRadiusExpression = literal(25.0),
                                    colorLevels = listOf(
                                        Pair(100, Color.Red.toArgb()),
                                        Pair(50, Color.Blue.toArgb()),
                                        Pair(0, Color.Green.toArgb())
                                    )
                                )
                            )
                        ),
                        onClick = {
                            Toast.makeText(
                                context,
                                "Clicked on Circle Annotation Cluster item: $it",
                                Toast.LENGTH_SHORT
                            ).show()
                            true
                        }
                    )
                    ViewAnnotation(
                        options = viewAnnotationOptions {
                            // set the view annotation associated geometry
                            geometry(Point.fromLngLat(0.0, 0.0))
                            annotationAnchor {
                                anchor(ViewAnnotationAnchor.BOTTOM)
                            }
                            allowOverlap(false)
                        }
                    ) {
                        // You can add the content to be drawn in the ViewAnnotation using Composable functions, e.g. to insert a button:
                        Button(
                            onClick = {
                                Toast.makeText(context, "Click", LENGTH_SHORT).show()
                            }
                        ) {
                            Text("Click me")
                        }
                    }
                }
            }
        }
    }