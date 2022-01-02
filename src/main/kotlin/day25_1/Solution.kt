package day25_1

import day25_1.Cucumber.East
import day25_1.Cucumber.South
import solve
import utils.Point

enum class Cucumber {
    East, South
}

fun main() {
    solve { lines ->
        val map = mutableMapOf<Point, Cucumber>()
        var maxY = 0
        var maxX = 0
        lines.forEachIndexed { y, line ->
            if (y > maxY) maxY = y
            line.forEachIndexed { x, char ->
                if (x > maxX) maxX = x
                when (char) {
                    '>' -> map[Point(x, y)] = East
                    'v' -> map[Point(x, y)] = South
                }
            }
        }

        fun getNext(point: Point, cucumber: Cucumber): Point {
            return when (cucumber) {
                East -> point.copy(x = (point.x + 1) % (maxX + 1))
                South -> point.copy(y = (point.y + 1) % (maxY + 1))
            }
        }

        var steps = 0
        while (true) {
            var moved = false
            Cucumber.values().forEach { cucumber ->
                map.filter { it.value == cucumber }.map { it.key to getNext(it.key, cucumber) }.filterNot { map.contains(it.second) }.forEach { (from, to) ->
                    map.remove(from)
                    map[to] = cucumber
                    moved = true
                }
            }
            steps++
            if (!moved) break
        }

        steps
    }
}
