package ru.spacestar.core_ui.viewmodel

sealed interface BaseSideEffect<in T : Any> {
    class Navigate(val route: String) : BaseSideEffect<Any>
    data object Back : BaseSideEffect<Any>
    class Other<T : Any>(val sf: T) : BaseSideEffect<T>
}