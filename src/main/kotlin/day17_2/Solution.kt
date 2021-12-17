package day17_2

import solve

fun main() {
    solve { lines ->
        val targetX = lines.first().substringAfter("x=").substringBefore(",").split("..").let { it[0].toInt()..it[1].toInt() }
        val targetY = lines.first().substringAfter("y=").split("..").let { it[0].toInt()..it[1].toInt() }
        val yVelocities = getValidVelocities(targetY, targetY.first..(-targetY.first), { it - 1 }, { v, _ -> v >= targetY.first })
        val xVelocities = getValidVelocities(targetX, 1..targetX.last, { if (it > 0) it - 1 else it }, { _, s -> s <= yVelocities.maxOf { it.first } })
        xVelocities.flatMap { (step, xVelocity) ->
            yVelocities.filter { it.first == step }.map { it.second }.map { yVelocity -> xVelocity to yVelocity }
        }.toSet().size
    }
}

fun getValidVelocities(target: IntRange, range: IntRange, physics: (Int) -> Int, predicate: (Int, Int) -> Boolean): List<Pair<Int, Int>> {
    val velocities = mutableListOf<Pair<Int, Int>>()
    for (velocity in range) {
        var position = 0
        var currentVelocity = velocity
        var step = 1
        while (predicate(currentVelocity, step)) {
            position += currentVelocity
            if (position in target) velocities += step to velocity
            currentVelocity = physics(currentVelocity)
            step++
        }
    }
    return velocities
}
