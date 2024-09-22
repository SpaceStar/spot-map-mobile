package ru.spacestar.map.presentation.ui.views.map

import platform.UIKit.UIViewController
import ru.spacestar.core_ui.view.ComposeUpdateListener

interface MapViewController {
    operator fun invoke(
        initialEvent: MapUpdateEvent,
        updateListener: ComposeUpdateListener<MapUpdateEvent>
    ): UIViewController
}