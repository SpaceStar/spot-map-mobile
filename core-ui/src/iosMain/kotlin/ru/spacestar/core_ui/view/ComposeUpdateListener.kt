package ru.spacestar.core_ui.view

class ComposeUpdateListener <T> {
    private var listener: ((event: T) -> Unit)? = null

    fun listen(listener: (event: T) -> Unit) {
        this.listener = listener
    }

    fun update(event: T) {
        listener?.invoke(event)
    }
}