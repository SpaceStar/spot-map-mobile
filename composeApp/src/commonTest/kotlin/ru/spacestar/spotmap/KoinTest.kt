package ru.spacestar.spotmap

import org.koin.test.check.checkModules
import ru.spacestar.spotmap.di.mainModule
import kotlin.test.Test

class KoinTest {
    @Test
    fun test() {
        checkModules {
            modules(mainModule)
        }
    }
}