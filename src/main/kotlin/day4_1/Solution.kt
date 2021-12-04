package day4_1

import solve

fun main() {
    solve { lines ->
        val numbers = lines.first().split(",").map { it.toInt() }
        val boards = lines.drop(1).filter { it.isNotEmpty() }.chunked(5).map {
            it.map { it.split(" ").filter { it.isNotEmpty() }.map { it.toInt() } }
        }
        numbers.indices.forEach {
            val drawn = numbers.take(it + 1)
            boards.indexOfFirst { isWin(it, drawn) }.takeIf { it > -1 }?.let {
                val sum = boards[it].flatten().filter { it !in drawn }.sum()
                return@solve sum * drawn.last()
            }
        }
    }
}

fun isWin(board: List<List<Int>>, drawn: List<Int>): Boolean {
    return board.any { it.all { it in drawn } } || (0..4).map { column -> board.map { it[column] } }.any { it.all { it in drawn } }
}
