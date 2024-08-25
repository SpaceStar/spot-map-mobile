package ru.spacestar.core.utils.preferences

import ru.spacestar.core.utils.storage.KVStorage

abstract class BasePreferences(
    private val storage: KVStorage
) {

    protected fun stringField(
        fieldName: String,
        defaultValue: String? = null
    ) = BaseField(
        storage,
        fieldName,
        defaultValue,
        serialize = { it },
        deserialize = { it }
    )

    protected fun booleanField(
        fieldName: String,
        defaultValue: Boolean = false
    ) = BaseField(
        storage,
        fieldName,
        defaultValue,
        deserialize = { it.toBoolean() }
    )

    protected fun floatField(
        fieldName: String,
        defaultValue: Float? = 0f
    ) = BaseField(
        storage,
        fieldName,
        defaultValue,
        deserialize = { it.toFloat() }
    )

    protected fun intField(
        fieldName: String,
        defaultValue: Int? = 0
    ) = BaseField(
        storage,
        fieldName,
        defaultValue,
        deserialize = { it.toInt() }
    )

    protected fun longField(
        fieldName: String,
        defaultValue: Long? = 0L
    ) = BaseField(
        storage,
        fieldName,
        defaultValue,
        deserialize = { it.toLong() }
    )
}