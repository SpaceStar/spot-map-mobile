package ru.spacestar.spotmap.di

import androidx.compose.runtime.Composable
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.spacestar.core.di.coreModule
import ru.spacestar.map.di.mapModule

val mainModule: @Composable () -> Module
    get() = {
        val platformModule = platformModule()
        val navigationModule = navigationModule()
        module {
            includes(coreModule, mapModule, platformModule, navigationModule)
        }
    }