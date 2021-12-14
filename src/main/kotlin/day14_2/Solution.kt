package day14_2

import solve
import kotlin.math.roundToLong

fun main() {
    solve { lines ->
        var polymer = lines.first().windowed(2).groupBy { it }.mapValues { it.value.size.toLong() }
        val rules = lines.drop(2).associate {
            it.split(" -> ").let { (from, to) -> from to listOf("${from[0]}$to", "$to${from[1]}") }
        }
        repeat(40) {
            polymer = polymer
                .flatMap { (pair, count) -> rules.getValue(pair).map { it to count } }
                .groupBy { it.first }
                .mapValues { it.value.sumOf { it.second } }
        }
        polymer
            .flatMap { listOf(it.key[0] to it.value, it.key[1] to it.value) }
            .groupBy { it.first }
            .map { (it.value.sumOf { it.second } / 2.0).roundToLong() }
            .sorted()
            .run { last() - first() }
    }
}
