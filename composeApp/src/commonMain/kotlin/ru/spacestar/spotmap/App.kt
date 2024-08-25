package ru.spacestar.spotmap

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.context.startKoin
import ru.spacestar.spotmap.di.MainComponent
import ru.spacestar.spotmap.di.mainModule

@Composable
@Preview
fun App() {
    var koinStarted by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        startKoin {
            modules(mainModule)
        }
        koinStarted = true
    }

    if (koinStarted) {
        val component = MainComponent()
        MaterialTheme {
            component.MainScreen()
        }
    }
}