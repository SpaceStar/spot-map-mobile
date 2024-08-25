package ru.spacestar.core_ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.SettingsBuilder
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.Syntax

abstract class BaseViewModel<S: Any, SF> : ViewModel(), ContainerHost<S, BaseSideEffect<SF>> {

    protected fun container(
        initialState: S,
        buildSettings: SettingsBuilder.() -> Unit = {},
        onCreate: (suspend Syntax<S, BaseSideEffect<SF>>.() -> Unit)? = null
    ) = viewModelScope.container(initialState, buildSettings, onCreate)

    protected suspend fun Syntax<S, BaseSideEffect<SF>>.postSideEffect(sideEffect: SF) {
        postSideEffect(BaseSideEffect.Other(sideEffect))
    }

    @Composable
    fun collectAsState(lifecycleState: Lifecycle.State = Lifecycle.State.STARTED) =
        container.stateFlow.collectAsStateWithLifecycle(minActiveState = lifecycleState)

    @Composable
    fun collectSideEffect(
        lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
        sideEffect: (suspend (sideEffect: BaseSideEffect<SF>) -> Unit)
    ) {
        val sideEffectFlow = container.sideEffectFlow
        val lifecycleOwner = LocalLifecycleOwner.current

        val callback by rememberUpdatedState(newValue = sideEffect)

        LaunchedEffect(sideEffectFlow, lifecycleOwner) {
            lifecycleOwner.lifecycle.repeatOnLifecycle(lifecycleState) {
                sideEffectFlow.collect { callback(it) }
            }
        }
    }
}