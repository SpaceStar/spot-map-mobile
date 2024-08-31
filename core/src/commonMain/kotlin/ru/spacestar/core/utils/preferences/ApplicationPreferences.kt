package ru.spacestar.core.utils.preferences

import ru.spacestar.core.utils.storage.KVStorage

class ApplicationPreferences(
    storage: KVStorage
) : BasePreferences(storage) {
    val token = stringField("token")
}