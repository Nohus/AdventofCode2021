package day4_2

import solve

fun main() {
    solve { lines ->
        val numbers = lines.first().split(",").map { it.toInt() }
        val boards = lines.drop(1).filter { it.isNotEmpty() }.chunked(5).map {
            it.map { it.split(" ").filter { it.isNotEmpty() }.map { it.toInt() } }
        }
        val wins = mutableListOf<Int>()
        numbers.indices.forEach { upTo ->
            val drawn = numbers.take(upTo + 1)
            val newWins = boards.withIndex().filter { it.index !in wins && isWin(it.value, drawn) }.map { it.index }
            wins += newWins
            if (wins.size == boards.size) {
                val sum = boards[newWins.single()].flatten().filter { it !in drawn }.sum()
                return@solve sum * drawn.last()
            }
        }
    }
}

fun isWin(board: List<List<Int>>, drawn: List<Int>): Boolean {
    return board.any { it.all { it in drawn } } || (0..4).map { column -> board.map { it[column] } }.any { it.all { it in drawn } }
}
