package ru.spacestar.core.utils.storage

import android.content.Context

class KVStorageImpl(context: Context) : KVStorage {
    private val prefs = context.getSharedPreferences(null, Context.MODE_PRIVATE)

    override fun put(key: String, value: String) {
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }

    override fun get(key: String) = prefs.getString(key, null)

    override fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }
}