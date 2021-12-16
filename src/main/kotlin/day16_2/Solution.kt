package day16_2

import solve

fun main() {
    solve { lines ->
        val binary = lines.first().chunked(1).joinToString("") { it.toInt(16).toString(2).padStart(4, '0') }
        readPacket(InputReader(binary))()
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

fun readPacket(input: InputReader): () -> Long {
    input.nextInt(3)
    val type = input.nextInt(3)
    if (type == 4) return parseConstant(input).let { { it } }
    val packets = parseSubpackets(input)
    return when (type) {
        0 -> { { packets.sumOf { it() } } }
        1 -> { { packets.fold(1L) { acc, packet -> acc * packet() } } }
        2 -> { { packets.minOf { it() } } }
        3 -> { { packets.maxOf { it() } } }
        5 -> { { if (packets[0]() > packets[1]()) 1L else 0L } }
        6 -> { { if (packets[0]() < packets[1]()) 1L else 0L } }
        7 -> { { if (packets[0]() == packets[1]()) 1L else 0L } }
        else -> throw IllegalArgumentException()
    }
}

fun parseConstant(input: InputReader): Long {
    val until = (input.peek().chunked(5).indexOfFirst { it[0] == '0' } + 1) * 5
    return input.next(until).chunked(5).joinToString("") { it.drop(1) }.toLong(2)
}

fun parseSubpackets(input: InputReader): List<() -> Long> {
    return if (input.nextBoolean()) List(input.nextInt(11)) { readPacket(input) }
    else InputReader(input.next(input.nextInt(15))).let { data ->
        mutableListOf<() -> Long>().also { while (data.available()) it += readPacket(data) }
    }
}
