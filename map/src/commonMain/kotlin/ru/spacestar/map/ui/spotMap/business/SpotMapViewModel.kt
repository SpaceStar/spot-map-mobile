package ru.spacestar.map.ui.spotMap.business

import ru.spacestar.core_ui.viewmodel.BaseViewModel

class SpotMapViewModel : BaseViewModel<SpotMapState, Any>() {

    override val container = container(SpotMapState())
}