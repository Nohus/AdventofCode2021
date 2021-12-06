package day6_2

import solve

val cache = mutableMapOf<Int, Long>()

fun main() {
    solve { lines ->
        lines.single().split(",").map { it.toInt() }.sumOf { fish(256 - it + 6) }
    }
}

fun fish(days: Int): Long {
    return cache[days] ?: (1 + (1..(days / 7)).sumOf { fish(days - (7 * it) - 2) }).also { cache[days] = it }
}
