package ru.spacestar.spotmap.di

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: @Composable () -> Module
    get() = {
        val context = LocalContext.current
        module {
            single<Context> { context.applicationContext }
        }
    }