package ru.spacestar.spotmap.di

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

val navigationModule: @Composable () -> Module
    get() = {
        val navController = rememberNavController()
        module {
            single { navController } bind NavController::class
        }
    }