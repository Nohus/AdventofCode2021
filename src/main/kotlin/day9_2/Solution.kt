package day9_2

import solve
import utils.Point

fun main() {
    solve { lines ->
        val heights = mutableMapOf<Point, Int>()
        lines.mapIndexed { y, row -> row.toList().forEachIndexed { x, height -> heights[Point(x, y)] = height.digitToInt() } }
        heights.filter { entry -> entry.key.getAdjacentSides().all { heights.getOrDefault(it, 9) > entry.value } }.map { (point, _) ->
            val visited = mutableListOf<Point>()
            val candidates = mutableListOf(point)
            while (candidates.isNotEmpty()) {
                val next = candidates.removeFirst()
                candidates += next.getAdjacentSides().filter { it in heights.keys && it !in visited && it !in candidates &&
                        heights.getOrDefault(it, 9) > heights.getValue(next) && heights[it] != 9 }
                visited += next
            }
            visited.size
        }.sortedDescending().take(3).reduce { acc, i -> acc * i }
    }
}
