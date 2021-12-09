package day9_1

import solve
import utils.Point

fun main() {
    solve { lines ->
        val heights = mutableMapOf<Point, Int>()
        lines.mapIndexed { y, row -> row.toList().forEachIndexed { x, height -> heights[Point(x, y)] = height.digitToInt() } }
        heights.filter { entry -> entry.key.getAdjacentSides().all { heights.getOrDefault(it, 9) > entry.value } }.map { 1 + it.value }.sum()
    }
}
