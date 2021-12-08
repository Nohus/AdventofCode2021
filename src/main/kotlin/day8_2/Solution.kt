package day8_2

import solve

fun main() {
    solve { lines ->
        lines.sumOf { line ->
            val (digits, number) = line.split(" | ").map { it.split(" ").map { it.toList() } }
            val wiring = mutableMapOf<Int, List<Char>>()
            wiring[1] = digits.find(2)
            wiring[4] = digits.find(4)
            wiring[7] = digits.find(3)
            wiring[8] = digits.find(7)
            wiring[2] = digits.find(5) { it.count { it in wiring.getValue(4) } == 2 }
            wiring[3] = digits.find(5) { it.containsAll(wiring.getValue(1)) }
            wiring[5] = digits.find(5) { it !in listOf(wiring.getValue(2), wiring.getValue(3)) }
            wiring[6] = digits.find(6) { !it.containsAll(wiring.getValue(1)) }
            wiring[9] = digits.find(6) { it.containsAll(wiring.getValue(4)) }
            wiring[0] = digits.find(6) { it !in listOf(wiring.getValue(6), wiring.getValue(9)) }
            val map = wiring.entries.associate { (k, v) -> v.sorted() to k }
            number.map { map.getValue(it.sorted()) }.joinToString("").toInt()
        }
    }
}

fun List<List<Char>>.find(size: Int, predicate: (List<Char>) -> Boolean = { true }): List<Char> {
    return filter { it.size == size }.single { predicate(it) }
}
