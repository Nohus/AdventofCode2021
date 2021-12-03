package day3_2

import solve

fun main() {
    solve { lines ->
        (0 until lines.first().length).fold(lines.toList() to lines.toList()) { acc, position ->
            acc.first.filterByCommonDigit(position, false) to acc.second.filterByCommonDigit(position, true)
        }.let { (a, b) -> a.single().toInt(2) * b.single().toInt(2) }
    }
}

fun List<String>.filterByCommonDigit(position: Int, invert: Boolean): List<String> {
    if (size == 1) return this
    val commonDigit = count { it[position] == '1' }.let { if (it >= size - it) '1' else '0' }
    return filter { (it[position] == commonDigit) xor invert }
}
