package ru.spacestar.core_ui.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

object UiExtensions {
    @Composable
    fun NavController.isDestination(route: String?) =
        currentBackStackEntryAsState().value?.destination?.route == route

    fun NavController.isBackStackEmpty() =
        previousBackStackEntry == null
}