package day8_1

import solve

fun main() {
    solve { lines ->
        lines.sumOf { it.substringAfter("|").split(" ").count { it.length in listOf(2, 3, 4, 7) } }
    }
}
