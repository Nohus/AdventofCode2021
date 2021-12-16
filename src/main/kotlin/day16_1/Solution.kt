package day16_1

import solve

fun main() {
    solve { lines ->
        val binary = lines.first().chunked(1).joinToString("") { it.toInt(16).toString(2).padStart(4, '0') }
        parsePacket(InputReader(binary))
    }
}

class InputReader(var input: String) {
    private var index = 0
    fun next(length: Int) = input.drop(index).take(length).also { index += length }
    fun nextInt(length: Int) = next(length).toInt(2)
    fun nextBoolean() = next(1).single() == '1'
    fun peek() = input.drop(index)
    fun available() = index < input.length
}

fun parsePacket(input: InputReader): Int {
    val version = input.nextInt(3)
    val type = input.nextInt(3)
    if (type == 4) return input.next((input.peek().chunked(5).indexOfFirst { it[0] == '0' } + 1) * 5).let { version }
    return version + parseSubpackets(input)
}

fun parseSubpackets(input: InputReader): Int {
    return if (input.nextBoolean()) List(input.nextInt(11)) { parsePacket(input) }.sum()
    else InputReader(input.next(input.nextInt(15))).let { data ->
        mutableListOf<Int>().also { while (data.available()) it += parsePacket(data) }.sum()
    }
}
