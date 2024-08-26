package ru.spacestar.spotmap.di

import androidx.compose.runtime.Composable
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: @Composable () -> Module
    get() = {
        module {}
    }