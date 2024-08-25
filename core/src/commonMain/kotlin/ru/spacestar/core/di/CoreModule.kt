package ru.spacestar.core.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.spacestar.core.utils.preferences.ApplicationPreferences

val coreModule = module {
    includes(kvStorageModule)
    factoryOf(::ApplicationPreferences)
}