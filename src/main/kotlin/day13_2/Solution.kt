package day13_2

import solve
import utils.Point

fun main() {
    solve { lines ->
        var (points, steps) = lines.indexOf("").let {
            lines.take(it).map { it.split(",").map { it.toInt() }.let { Point(it[0], it[1]) } } to lines.drop(it + 1)
        }
        steps.forEach { step ->
            val coordinate = step.substringAfter('=').toInt()
            val fold: (Char, Int) -> Int = { axis, it -> if (axis in step && it > coordinate) 2 * coordinate - it else it }
            points = points.map { Point(fold('x', it.x), fold('y', it.y)) }
        }
        for (y in 0..points.maxOf { it.y }) {
            for (x in 0..points.maxOf { it.x }) {
                print(if (Point(x, y) in points) "█" else " ")
            }
            println()
        }
    }
}

