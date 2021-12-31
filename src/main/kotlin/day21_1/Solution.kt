package day21_1

import solve

fun main() {
    solve { lines ->
        var player1Position = lines[0].substringAfterLast(" ").toInt()
        var player2Position = lines[1].substringAfterLast(" ").toInt()
        var player1Score = 0
        var player2Score = 0
        var diceRolls = 0
        var nextDiceNumber = 1

        fun rollDie(): Int {
            return nextDiceNumber.also {
                diceRolls++
                nextDiceNumber = (nextDiceNumber % 100) + 1
            }
        }

        fun getNextMove(): Int {
            return rollDie() + rollDie() + rollDie()
        }

        while (true) {
            val player1Move = getNextMove()
            player1Position = (((player1Position - 1) + player1Move) % 10) + 1
            player1Score += player1Position
            if (player1Score >= 1000) return@solve player2Score * diceRolls

            val player2Move = getNextMove()
            player2Position = (((player2Position - 1) + player2Move) % 10) + 1
            player2Score += player2Position
            if (player2Score >= 1000) return@solve player1Score * diceRolls
        }
    }
}
