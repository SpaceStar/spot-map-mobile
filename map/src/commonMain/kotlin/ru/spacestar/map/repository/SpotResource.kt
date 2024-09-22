package ru.spacestar.map.repository

import io.ktor.resources.Resource

@Resource("/spot")
class SpotResource {

    @Resource("types")
    class Types(val parent: SpotResource = SpotResource())

    @Resource("map")
    class Map(
        val parent: SpotResource = SpotResource(),
        val spotType: Int,
        val lat1: String,
        val lon1: String,
        val lat2: String,
        val lon2: String
    )
}