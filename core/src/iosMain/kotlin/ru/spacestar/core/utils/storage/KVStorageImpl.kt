package ru.spacestar.core.utils.storage

import platform.Foundation.NSUserDefaults

class KVStorageImpl : KVStorage {
    private val userDefaults = NSUserDefaults.standardUserDefaults()

    override fun put(key: String, value: String) {
        userDefaults.setObject(value, forKey = key)
        userDefaults.synchronize()
    }

    override fun get(key: String) = userDefaults.stringForKey(key)

    override fun remove(key: String) {
        userDefaults.removeObjectForKey(key)
    }
}