package day21_2

import solve
import java.math.BigInteger

fun main() {
    solve { lines ->
        data class Game(val p1Pos: Int, val p2Pos: Int, val p1: Int = 0, val p2: Int = 0, val p2Turn: Boolean = false)
        fun Game.isFinished() = p1 >= 21 || p2 >= 21
        var games = listOf<Pair<Game, BigInteger>>(Game(lines[0].substringAfterLast(" ").toInt(), lines[1].substringAfterLast(" ").toInt()) to BigInteger.ONE)
        while (games.any { !it.first.isFinished() }) {
            games = games.filter { it.first.isFinished() } + games.filterNot { it.first.isFinished() }.let { ongoingGames ->
                listOf(1, 2, 3).flatMap { die1 -> listOf(1, 2, 3).flatMap { die2 -> listOf(1, 2, 3).flatMap { die3 ->
                    ongoingGames.map { (game, count) ->
                        val position = ((((if (game.p2Turn) game.p2Pos else game.p1Pos) - 1) + die1 + die2 + die3) % 10) + 1
                        val score = (if (game.p2Turn) game.p2 else game.p1) + position
                        (if (game.p2Turn) game.copy(p2Turn = false, p2Pos = position, p2 = score) else game.copy(p2Turn = true, p1Pos = position, p1 = score)) to count
                    }
                } } }.groupBy { it.first }.map { it.key to it.value.sumOf { it.second } }
            }
        }
        games.groupBy { it.first.p1 >= 21 }.map { it.value.map { it.second }.reduce { acc, count -> acc + count } }.maxOrNull()
    }
}
