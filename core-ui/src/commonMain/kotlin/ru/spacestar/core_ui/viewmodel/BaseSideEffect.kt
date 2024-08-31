package ru.spacestar.core_ui.viewmodel

sealed interface BaseSideEffect<T> {
    class Navigate<T>(val route: String) : BaseSideEffect<T>
    class Back<T> : BaseSideEffect<T>
    class Other<T>(val sf: T) : BaseSideEffect<T>
}