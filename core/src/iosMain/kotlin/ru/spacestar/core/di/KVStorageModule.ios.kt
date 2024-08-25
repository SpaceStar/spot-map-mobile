package ru.spacestar.core.di

import org.koin.core.module.Module
import org.koin.dsl.module
import ru.spacestar.core.utils.storage.KVStorage
import ru.spacestar.core.utils.storage.KVStorageImpl

internal actual val kvStorageModule = module {
    factory<KVStorage> { KVStorageImpl() }
}