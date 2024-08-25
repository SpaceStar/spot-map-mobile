package ru.spacestar.core_ui.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import ru.spacestar.core_ui.utils.UiExtensions.isBackStackEmpty

@Composable
fun BaseAppBarScreen(
    navController: NavController,
    title: String? = null,
    onBackPressed: (() -> Unit) = { navController.popBackStack() },
    isBackEnabled: Boolean = !navController.isBackStackEmpty(),
    backIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    appBarMenu: @Composable RowScope.() -> Unit = {},
    fab: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = {}
) {
    Scaffold(
        topBar = {
            CustomAppBar(
                title = title,
                backIcon = backIcon,
                onBackPressed = if (isBackEnabled) onBackPressed else null,
                actions = appBarMenu
            )
        },
        floatingActionButton = fab,
        content = content
    )
}