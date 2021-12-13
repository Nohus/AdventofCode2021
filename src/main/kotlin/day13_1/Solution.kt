package day13_1

import solve
import utils.Point

fun main() {
    solve { lines ->
        val (points, steps) = lines.indexOf("").let {
            lines.take(it).map { it.split(",").map { it.toInt() }.let { Point(it[0], it[1]) } } to lines.drop(it + 1)
        }
        val coordinate = steps[0].substringAfter('=').toInt()
        val fold: (Char, Int) -> Int = { axis, it -> if (axis in steps[0] && it > coordinate) 2 * coordinate - it else it }
        points.map { Point(fold('x', it.x), fold('y', it.y)) }.toSet().size
    }
}
