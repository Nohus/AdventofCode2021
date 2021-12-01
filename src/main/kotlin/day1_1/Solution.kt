package day1_1

import solve

fun main() {
    solve { lines ->
        lines.map { it.toInt() }.windowed(2).count { it.last() > it.first() }
    }
}
