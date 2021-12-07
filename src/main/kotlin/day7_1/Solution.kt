package day7_1

import solve
import kotlin.math.abs

fun main() {
    solve { lines ->
        val crabs = lines.single().split(",").map { it.toInt() }
        (0..crabs.maxOf { it }).minOf { target -> crabs.sumOf { abs(target - it) } }
    }
}
