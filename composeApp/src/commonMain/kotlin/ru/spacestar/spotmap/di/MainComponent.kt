package ru.spacestar.spotmap.di

import androidx.compose.runtime.Composable
import org.koin.core.component.KoinComponent
import ru.spacestar.core_ui.TestApp

class MainComponent : KoinComponent {
    @Composable
    fun MainScreen() = TestApp()
}