package day14_1

import solve

fun main() {
    solve { lines ->
        var polymer = lines.first()
        val rules = lines.drop(2).associate { it.split(" -> ").let { it[0] to "${it[0][0]}${it[1]}" } }
        repeat(10) {
            polymer = polymer.windowed(2).joinToString("") { rules[it] ?: it } + polymer.last()
        }
        polymer.groupBy { it }.map { it.value.size }.sorted().run { last() - first() }
    }
}
