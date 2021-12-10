package day10_2

import solve

fun main() {
    solve { lines ->
        val pairs = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
        lines.mapNotNull { line ->
            val stack = mutableListOf<Char>()
            line.toList().forEach {
                if (it in pairs.keys) stack += it else if (pairs[stack.lastOrNull()] == it) stack.removeLast() else return@mapNotNull null
            }
            stack.reversed().map { pairs.keys.indexOf(it) + 1 }.fold(0L) { acc, it -> acc * 5 + it }
        }.sorted().let { it[it.size / 2] }
    }
}
