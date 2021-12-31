package day22_2

import solve
import utils.Vector3
import kotlin.math.max
import kotlin.math.min

fun main() {
    solve { lines ->
        data class Cuboid(val from: Vector3, val to: Vector3) {
            fun overlap(other: Cuboid) = Cuboid(
                    Vector3(max(from.x, other.from.x), max(from.y, other.from.y), max(from.z, other.from.z)),
                    Vector3(min(to.x, other.to.x), min(to.y, other.to.y), min(to.z, other.to.z))
            )
            val size = (to.x - from.x + 1L).coerceAtLeast(0L) * (to.y - from.y + 1L).coerceAtLeast(0L) * (to.z - from.z + 1L).coerceAtLeast(0L)
        }
        val steps = lines.map {
            val (state, x1, x2, y1, y2, z1, z2) = Regex("""(.+) x=(.+)\.\.(.+),y=(.+)\.\.(.+),z=(.+)\.\.(.+)""").find(it)!!.destructured
            (state == "on") to Cuboid(Vector3(x1.toInt(), y1.toInt(), z1.toInt()), Vector3(x2.toInt(), y2.toInt(), z2.toInt()))
        }
        val plus = mutableListOf<Cuboid>()
        val minus = mutableListOf<Cuboid>()
        steps.forEach{ (state, cuboid) ->
            val plusIntersections = plus.map { it.overlap(cuboid) }.filter { it.size > 0 }
            val minusIntersections = minus.map { it.overlap(cuboid) }.filter { it.size > 0 }
            minus += plusIntersections
            plus += minusIntersections
            if (state) plus += cuboid
        }
        plus.sumOf { it.size } - minus.sumOf { it.size }
    }
}
