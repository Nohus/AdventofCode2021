package day2_2

import solve

fun main() {
    solve { lines ->
        var depth = 0
        var horizontal = 0
        var aim = 0
        lines.forEach {
            val value = it.substringAfter(" ").toInt()
            when (it.substringBefore(" ")) {
                "forward" -> {
                    horizontal += value
                    depth += aim * value
                }
                "down" -> aim += value
                "up" -> aim -= value
            }
        }
        depth * horizontal
    }
}
