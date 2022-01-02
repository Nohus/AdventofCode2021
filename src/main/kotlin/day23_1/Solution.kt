package day23_1

import solve
import java.util.*
import kotlin.math.abs

data class State(
        val hallway: List<Char>, // Left to right
        val rooms: List<List<Char>>, // Rooms left to right, up to down inside
)

fun main() {
    solve { lines ->
        val state = State(listOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'), listOf(listOf('D', 'C'), listOf('D', 'A'), listOf('B', 'B'), listOf('A', 'C')))
        val target = State(listOf('.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'), listOf(listOf('A', 'A'), listOf('B', 'B'), listOf('C', 'C'), listOf('D', 'D')))

        val visited = mutableSetOf<State>()
        val unvisited = PriorityQueue<Pair<Int, State>> { o1, o2 ->
            (o1.first).compareTo(o2.first)
        }
        unvisited += 0 to state
        while (true) {
            val current = unvisited.remove()
            visited += current.second
            //printState(current.second)
            println("Unvisited: ${unvisited.size}, Cost: ${current.first}")
            if (current.second == target) {
                //println("Found target")
                return@solve current.first
            }
            val possibleMoves = getPossibleMoves(current.second)
            unvisited += possibleMoves.map { (current.first + it.first) to it.second }.filterNot { it.second in visited }
        }
    }
}

private fun printState(state: State) {
    println("#############")
    println("#${state.hallway.joinToString("")}#")
    println("###${state.rooms[0][0]}#${state.rooms[1][0]}#${state.rooms[2][0]}#${state.rooms[3][0]}###")
    println("  #${state.rooms[0][1]}#${state.rooms[1][1]}#${state.rooms[2][1]}#${state.rooms[3][1]}#")
    println("  #########")
}

private fun getPossibleMoves(state: State): MutableList<Pair<Int, State>> {
    val moves = mutableListOf<Pair<Int, State>>()
    val roomOffsets = (0..3).map { 2 + it * 2 }
    // Moves from rooms
    state.rooms.forEachIndexed { index, room ->
        if (room.any { it != '.' }) {
            // Check shrimp and extra steps
            var extraSteps = 0
            val shrimp = if (room[0] != '.') { // Top spot
                room[0]
            } else { // Bottom spot
                extraSteps = 1
                room[1]
            }

            // Check if in correct room already
            val destinationRoomIndex = getDestinationRoomIndex(shrimp)
            if (index != destinationRoomIndex || (room[0] == shrimp && room[1] != shrimp)) {
                // Create new room state
                val rooms = state.rooms.toMutableList()
                rooms[index] = if (room[0] != '.') { // Top spot
                    listOf('.', room[1])
                } else { // Bottom spot
                    listOf(room[0], '.')
                }

                // Check blocked hallway
                val roomOffset = roomOffsets[index]
                val freeFrom = (0 until roomOffset).lastOrNull { state.hallway[it] != '.' }?.let { it + 1 } ?: 0
                val freeTo = ((roomOffset + 1)..10).firstOrNull { state.hallway[it] != '.' }?.let { it - 1 } ?: 10
                val possibleSpots = (freeFrom..freeTo) - roomOffsets.toSet()

                // Create all moves
                possibleSpots.forEach { spot ->
                    // Create new hallway state
                    val hallway = state.hallway.toMutableList().also { it[spot] = shrimp }
                    val distance = abs(roomOffset - spot) + extraSteps + 1
                    moves += (getMoveCost(shrimp) * distance) to State(hallway, rooms)
                }
            }
        }
    }
    // Moves from hallway
    val shrimpsInHallway = state.hallway.withIndex().filter { it.value != '.' }
    shrimpsInHallway.forEach { (index, shrimp) ->
        val destinationRoomIndex = getDestinationRoomIndex(shrimp)
        // Check blocked hallway
        val roomOffset = roomOffsets[destinationRoomIndex]
        val freeFrom = (0 until roomOffset).lastOrNull { state.hallway[it] != '.' } ?: 0
        val freeTo = ((roomOffset + 1)..10).firstOrNull { state.hallway[it] != '.' } ?: 10
        if (index in (freeFrom..freeTo)) {
            val hallway = state.hallway.toMutableList().also { it[index] = '.' }
            val room = state.rooms[destinationRoomIndex]
            val rooms = state.rooms.toMutableList()
            val distance = abs(index - roomOffset)
            if (room.all { it == '.' }) { // Room is empty
                rooms[destinationRoomIndex] = listOf('.', shrimp)
                moves += (getMoveCost(shrimp) * (distance + 2)) to State(hallway, rooms)
            } else if (room[0] == '.' && room[1] == shrimp) { // Room has 1 matching shrimp
                rooms[destinationRoomIndex] = listOf(shrimp, shrimp)
                moves += (getMoveCost(shrimp) * (distance + 1)) to State(hallway, rooms)
            }
        }
    }
    return moves
}

private fun getMoveCost(shrimp: Char) = when (shrimp) {
    'A' -> 1
    'B' -> 10
    'C' -> 100
    'D' -> 1000
    else -> throw IllegalArgumentException()
}

private fun getDestinationRoomIndex(shrimp: Char) = when (shrimp) {
    'A' -> 0
    'B' -> 1
    'C' -> 2
    'D' -> 3
    else -> throw IllegalArgumentException()
}
