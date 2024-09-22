package ru.spacestar.map.data

import com.ionspin.kotlin.bignum.decimal.BigDecimal

class Spot(
    val id: Long,
    val spotType: Int,
    val lat: BigDecimal,
    val lon: BigDecimal,
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Spot) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}