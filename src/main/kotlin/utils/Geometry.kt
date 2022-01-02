package utils

import utils.Direction.*
import utils.Turn.*
import kotlin.math.abs
import kotlin.math.atan2

data class Point(val x: Int, val y: Int) {

    companion object {
        val ORIGIN = Point(0, 0)
    }

    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }

    fun move(direction: Direction, distance: Int = 1) = when (direction) {
        EAST -> Point(x + distance, y)
        WEST -> Point(x - distance, y)
        NORTH -> Point(x, y - distance)
        SOUTH -> Point(x, y + distance)
    }

    fun manhattanDistanceTo(other: Point) = abs(x - other.x) + abs(y - other.y)

    fun getAdjacentSides(): List<Point> = listOf(
        Point(x, y - 1), Point(x - 1, y), Point(x + 1, y), Point(x, y + 1),
    )

    fun getAdjacent(): List<Point> = listOf(
        Point(x - 1, y - 1), Point(x, y - 1), Point(x + 1, y - 1),
        Point(x - 1, y), Point(x + 1, y),
        Point(x - 1, y + 1), Point(x, y + 1), Point(x + 1, y + 1),
    )

    fun isAdjacentSide(other: Point) = manhattanDistanceTo(other) == 1

    fun angleTo(other: Point): Double {
        val angle = Math.toDegrees(atan2((other.y - y).toDouble(), (other.x - x).toDouble())) + 90
        return if (angle >= 0) angle else angle + 360
    }
}

fun <T> Map<Point, T>.printArea(visualization: (T) -> Char = { it.toString()[0] }) {
    val xRange = keys.minOf { it.x }..keys.maxOf { it.x }
    val yRange = keys.minOf { it.y }..keys.maxOf { it.y }
    for (y in yRange) {
        for (x in xRange) {
            val value = get(Point(x, y))
            if (value != null) {
                print(visualization(value))
            } else {
                print(" ")
            }
        }
        println()
    }
}

@JvmName("printAreaBoolean")
fun Map<Point, Boolean>.printArea() {
    printArea { if (it) '█' else ' ' }
}

@JvmName("printAreaChar")
fun Map<Point, Char>.printArea() {
    printArea { it }
}

data class Vector3(val x: Int, val y: Int, val z: Int) {

    operator fun plus(other: Vector3): Vector3 {
        return Vector3(x + other.x, y + other.y, z + other.z)
    }

    operator fun minus(other: Vector3): Vector3 {
        return Vector3(x - other.x, y - other.y, z - other.z)
    }

    operator fun get(index: Int): Int = when (index) {
        0 -> x
        1 -> y
        2 -> z
        else -> throw IllegalArgumentException()
    }

    fun getAdjacent(): List<Vector3> {
        val adjacent = mutableListOf<Vector3>()
        for (x in -1..1) {
            for (y in -1..1) {
                for (z in -1..1) {
                    if (x == 0 && y == 0 && z == 0) continue
                    adjacent += (Vector3(x, y, z) + this)
                }
            }
        }
        return adjacent
    }
}

typealias VectorN<T> = List<T>

fun VectorN<Int>.getAdjacent(): List<VectorN<Int>> {
    val adjacent = mutableListOf(this)
    for (dimension in 0 until size) {
        adjacent += adjacent.flatMap {
            listOf(
                it.toMutableList().also { it[dimension] -= 1 },
                it.toMutableList().also { it[dimension] += 1 }
            )
        }
    }
    return adjacent.filterNot { it == this }
}

enum class Turn {
    RIGHT, LEFT;

    override fun toString() = when (this) {
        RIGHT -> "→"
        LEFT -> "←"
    }
}

enum class Direction {
    NORTH, EAST, SOUTH, WEST;

    fun rotate(turn: Turn, times: Int): Direction {
        return (0 until (times % 4)).toList().fold(this) { acc, _ -> acc.rotate(turn) }
    }

    fun rotate(turn: Turn): Direction {
        return if (turn == RIGHT) when (this) {
            NORTH -> EAST
            EAST -> SOUTH
            SOUTH -> WEST
            WEST -> NORTH
        } else when (this) {
            NORTH -> WEST
            EAST -> NORTH
            SOUTH -> EAST
            WEST -> SOUTH
        }
    }

    fun reverse() = when (this) {
        NORTH -> SOUTH
        SOUTH -> NORTH
        WEST -> EAST
        EAST -> WEST
    }

    override fun toString() = when (this) {
        NORTH -> "↑"
        EAST -> "→"
        SOUTH -> "↓"
        WEST -> "←"
    }
}
