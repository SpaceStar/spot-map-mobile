package ru.spacestar.spotmap

import org.koin.test.check.checkModules
import kotlin.test.Test

// TODO: think about testing the koin graph
class KoinTest {
    @Test
    fun test() {
        checkModules {
            modules()
        }
    }
}