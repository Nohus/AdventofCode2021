package day1_2

import solve

fun main() {
    solve { lines ->
        lines.map { it.toInt() }.windowed(4).count { it.last() > it.first() }
    }
}
