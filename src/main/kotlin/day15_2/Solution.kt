package day15_2

import solve
import utils.Point
import java.util.PriorityQueue

fun main() {
    solve { lines ->
        val risk = lines.flatMapIndexed { y: Int, row: String -> row.mapIndexed { x, c -> Point(x, y) to c.digitToInt() } }.toMap()
        val width = risk.keys.maxOf { it.x } + 1
        val height = risk.keys.maxOf { it.y } + 1
        val expandedRisk = mutableMapOf<Point, Int>()
        for (y in 0 until (height * 5)) {
            for (x in 0 until (width * 5)) {
                val value = risk.getValue(Point(x % width, y % height)) + x / width + y / height
                expandedRisk[Point(x, y)] = if (value > 9) value - 9 else value
            }
        }
        val target = Point(expandedRisk.maxOf { it.key.x }, expandedRisk.maxOf { it.key.y })
        val visited = mutableMapOf<Point, Int>()
        val queue = PriorityQueue<Pair<Point, Int>> { o1, o2 ->
            (o1.second + o1.first.manhattanDistanceTo(target)).compareTo(o2.second + o2.first.manhattanDistanceTo(target))
        }
        queue += Point(0, 0) to 0
        while(true) {
            val next = queue.remove()
            visited[next.first] = next.second
            if (next.first == target) return@solve visited[target]
            queue += next.first.getAdjacentSides().filterNot { it in visited }.map { it to ((expandedRisk[it] ?: 10000) + next.second) }
        }
    }
}
