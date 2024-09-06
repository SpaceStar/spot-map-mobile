package ru.spacestar.spotmap

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ComposeUIViewController
import org.koin.dsl.module
import ru.spacestar.map.ui.views.map.MapViewController
import ru.spacestar.spotmap.di.platformIosModule

fun MainViewController(
    mapViewController: MapViewController
) = ComposeUIViewController {
    var isInitialized by remember { mutableStateOf(false) }
    LaunchedEffect(true) {
        platformIosModule = module {
            single { mapViewController }
        }
        isInitialized = true
    }
    if (isInitialized)
        App()
}