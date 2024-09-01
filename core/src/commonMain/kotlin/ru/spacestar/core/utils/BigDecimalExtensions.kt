package ru.spacestar.core.utils

import com.ionspin.kotlin.bignum.decimal.BigDecimal

fun String.toBigDecimal() = BigDecimal.parseString(this)
fun Double.toBigDecimal() = BigDecimal.fromDouble(this)

val BigDecimal.Companion.MIN_LON: BigDecimal
    get() = parseString("-179.999999")

val BigDecimal.Companion.MAX_LON: BigDecimal
    get() = fromInt(180)

val BigDecimal.Companion.VALUE_360: BigDecimal
    get() = fromInt(360)