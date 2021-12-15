package day15_1

import solve
import utils.Point
import java.util.PriorityQueue

fun main() {
    solve { lines ->
        val risk = lines.flatMapIndexed { y: Int, row: String -> row.mapIndexed { x, c -> Point(x, y) to c.digitToInt() } }.toMap()
        val target = Point(risk.maxOf { it.key.x }, risk.maxOf { it.key.y })
        val visited = mutableMapOf<Point, Int>()
        val queue = PriorityQueue<Pair<Point, Int>> { o1, o2 ->
            (o1.second + o1.first.manhattanDistanceTo(target)).compareTo(o2.second + o2.first.manhattanDistanceTo(target))
        }
        queue += Point(0, 0) to 0
        while(true) {
            val next = queue.remove()
            visited[next.first] = next.second
            if (next.first == target) return@solve visited[target]
            queue += next.first.getAdjacentSides().filterNot { it in visited }.map { it to ((risk[it] ?: 10000) + next.second) }
        }
    }
}
