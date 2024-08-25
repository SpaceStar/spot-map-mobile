package ru.spacestar.spotmap

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.AnimationConstants
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.context.startKoin
import ru.spacestar.core_ui.theme.AppTheme
import ru.spacestar.spotmap.di.MainComponent
import ru.spacestar.spotmap.di.mainModule

private fun NavGraphBuilder.registerNavGraphs(
    mainComponent: MainComponent
) {
    mainComponent.mapApi.registerGraph(this)
}

@Composable
@Preview
fun App() {
    var koinStarted by remember { mutableStateOf(false) }
    val mainModule = mainModule()

    LaunchedEffect(true) {
        startKoin {
            modules(mainModule)
        }
        koinStarted = true
    }

    if (koinStarted) {
        val mainComponent = MainComponent()

        val startDestination: String = remember { mainComponent.mapApi.route() }

        AppTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    NavHost(
                        navController = mainComponent.navController,
                        startDestination = startDestination,
                        enterTransition = {
                            slideIntoContainer(SlideDirection.Start) },
                        exitTransition = {
                            fadeOut(animationSpec = tween(
                                durationMillis = 1,
                                delayMillis = AnimationConstants.DefaultDurationMillis
                            )) },
                        popEnterTransition = { EnterTransition.None },
                        popExitTransition = {
                            slideOutOfContainer(SlideDirection.End) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        registerNavGraphs(mainComponent)
                    }
                    // You can place Ad block here
                }
            }
        }
    }
}