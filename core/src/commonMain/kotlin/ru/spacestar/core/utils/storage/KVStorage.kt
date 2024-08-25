package ru.spacestar.core.utils.storage

interface KVStorage {
    fun put(key: String, value: String)
    fun get(key: String): String?
    fun remove(key: String)
}