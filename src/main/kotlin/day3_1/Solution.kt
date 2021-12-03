package day3_1

import solve

fun main() {
    solve { lines ->
        (0 until lines.first().length).joinToString("") { position ->
            lines.count { it[position] == '1' }.let { if (it >= lines.size - it) "1" else "0" }
        }.toInt(2).let { it * (it xor 0xFFF) }
    }
}
