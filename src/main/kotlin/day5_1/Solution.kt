package day5_1

import solve
import utils.Point
import kotlin.math.abs
import kotlin.math.max

fun main() {
    solve { lines ->
        lines
            .map {
                it.split(" -> ").map {
                    it.split(",").let { (x, y) -> Point(x.toInt(), y.toInt()) }
                }
            }
            .filter { (a, b) -> a.x == b.x || a.y == b.y }
            .flatMap { (a, b) -> getPointsOnLine(a, b) }
            .groupBy { it }
            .count { it.value.size > 1 }
    }
}

fun getPointsOnLine(a: Point, b: Point): List<Point> {
    return (0..max(abs(a.x - b.x), abs(a.y - b.y))).map { step ->
        val x = if (b.x > a.x) a.x + step else if (b.x < a.x) a.x - step else a.x
        val y = if (b.y > a.y) a.y + step else if (b.y < a.y) a.y - step else a.y
        Point(x, y)
    }
}
