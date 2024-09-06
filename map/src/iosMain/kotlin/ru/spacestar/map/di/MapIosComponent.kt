package ru.spacestar.map.di

import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.spacestar.map.ui.views.map.MapViewController

internal class MapIosComponent : KoinComponent {
    val mapViewController: MapViewController = get()
}