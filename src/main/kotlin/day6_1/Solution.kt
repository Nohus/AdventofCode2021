package day6_1

import solve

fun main() {
    solve { lines ->
        lines.single().split(",").map { it.toInt() }.sumOf { fish(80 - it + 6) }
    }
}

fun fish(days: Int): Int = (1 + (1..(days / 7)).sumOf { fish(days - (7 * it) - 2) })
