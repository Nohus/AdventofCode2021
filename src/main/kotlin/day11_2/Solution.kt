package day11_2

import solve
import utils.Point

fun main() {
    solve { lines ->
        var octopi = lines.flatMapIndexed { y, line -> line.mapIndexed { x, it -> Point(x, y) to it.digitToInt() } }.toMap()
        var step = 0
        while (true) {
            step++
            octopi = octopi.mapValues { it.value + 1 }
            val flashed = mutableListOf<Point>()
            while (true) {
                val flashing = octopi.filter { it.value > 9 && it.key !in flashed }.keys
                if (flashing.isEmpty()) break
                val charged = flashing.flatMap { it.getAdjacent() }.groupBy { it }.mapValues { it.value.size }
                octopi = octopi.mapValues { it.value + (charged[it.key] ?: 0) }
                flashed.addAll(flashing)
            }
            octopi = octopi.mapValues { if (it.key in flashed) 0 else it.value }
            if (octopi.values.all { it == 0 }) return@solve step
        }
    }
}
