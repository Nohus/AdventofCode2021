package day10_1

import solve

fun main() {
    solve { lines ->
        val pairs = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
        val scoring = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
        lines.map { line ->
            val stack = mutableListOf<Char>()
            line.toList().forEach {
                if (it in pairs.keys) stack += it else if (pairs[stack.lastOrNull()] == it) stack.removeLast() else return@map scoring.getValue(it)
            }
            0
        }.sum()
    }
}
