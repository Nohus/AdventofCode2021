package day11_1

import solve
import utils.Point

fun main() {
    solve { lines ->
        var octopi = lines.flatMapIndexed { y, line -> line.mapIndexed { x, it -> Point(x, y) to it.digitToInt() } }.toMap()
        var totalFlashes = 0
        repeat(100) {
            octopi = octopi.mapValues { it.value + 1 }
            val flashed = mutableListOf<Point>()
            while (true) {
                val flashing = octopi.filter { it.value > 9 && it.key !in flashed }.keys
                if (flashing.isEmpty()) break
                val charged = flashing.flatMap { it.getAdjacent() }.groupBy { it }.mapValues { it.value.size }
                octopi = octopi.mapValues { it.value + (charged[it.key] ?: 0) }
                flashed.addAll(flashing)
                totalFlashes += flashing.size
            }
            octopi = octopi.mapValues { if (it.key in flashed) 0 else it.value }
        }
        totalFlashes
    }
}
