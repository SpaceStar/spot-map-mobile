package ru.spacestar.core.utils.preferences

import ru.spacestar.core.utils.storage.KVStorage

class BaseField<T>(
    private val storage: KVStorage,
    private val fieldName: String,
    private val defaultValue: T?,
    private val deserialize: (String) -> T,
    private val serialize: (T) -> String = { it.toString() }
) {
    fun put(value: T) {
        storage.put(fieldName, serialize(value))
    }

    fun get(): T? {
        return storage.get(fieldName)?.let(deserialize) ?: defaultValue
    }

    fun remove() {
        storage.remove(fieldName)
    }
}