package day20_1

import solve
import utils.Point

fun main() {
    solve { lines ->
        val algorithm = lines.first().map { it == '#' }
        val input = lines.drop(2).flatMapIndexed { y, line -> line.mapIndexed { x, char -> Point(x, y) to (char == '#') } }.toMap().withDefault { false }
        (0 until 2).fold(input) { image, _ ->
            val new = mutableMapOf<Point, Boolean>()
            val range = -2..(image.keys.maxOf { it.x } + 2)
            for (y in range) for (x in range) {
                val kernel = mutableListOf<Point>().also { for (yDelta in -1..1) for (xDelta in -1..1) it += Point(x + xDelta, y + yDelta) }
                new[Point(x + 2, y + 2)] = algorithm[kernel.joinToString("") { if (image.getValue(it)) "1" else "0" }.toInt(2)]
            }
            new.withDefault { new.getValue(Point(0, 0)) }
        }.count { it.value }
    }
}
