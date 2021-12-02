package day2_1

import solve

fun main() {
    solve { lines ->
        var depth = 0
        var horizontal = 0
        lines.forEach {
            val value = it.substringAfter(" ").toInt()
            when (it.substringBefore(" ")) {
                "forward" -> horizontal += value
                "down" -> depth += value
                "up" -> depth -= value
            }
        }
        depth * horizontal
    }
}
