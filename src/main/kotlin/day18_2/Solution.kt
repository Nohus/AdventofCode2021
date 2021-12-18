package day18_2

import day18_2.Snailfish.Number
import day18_2.Snailfish.Pair
import solve
import kotlin.math.roundToInt

sealed class Snailfish  {
    class Number(val value: Int): Snailfish()
    class Pair(val left: Snailfish, val right: Snailfish): Snailfish()
}

fun main() {
    solve { lines ->
        val numbers = lines.map { parseSnailfish(it) }
        numbers.flatMap { number -> (numbers - number).map { reduce(Pair(number, it)) } }.maxOf { magnitude(it) }
    }
}

fun parseSnailfish(input: String): Snailfish {
    if (!input.startsWith('[')) return Number(input.toInt())
    val inside = input.removeSurrounding("[", "]")
    var nestLevel = 0
    val index = inside.indexOfFirst {
        if (it == '[') nestLevel++
        else if (it == ']') nestLevel--
        else if (it == ',' && nestLevel == 0) return@indexOfFirst true
        false
    }
    return Pair(parseSnailfish(inside.substring(0, index)), parseSnailfish(inside.substring(index + 1)))
}

fun magnitude(snailfish: Snailfish): Int {
    return when (snailfish) {
        is Number -> snailfish.value
        is Pair -> 3 * magnitude(snailfish.left) + 2 * magnitude(snailfish.right)
    }
}

fun reduce(snailfish: Snailfish): Snailfish {
    var reduced = snailfish
    while (true) {
        val exploding = findExploding(reduced)
        if (exploding != null) {
            val flattened = flatten(reduced)
            val index = flattened.indexOf(exploding)
            val replacements: MutableList<kotlin.Pair<Snailfish, Snailfish>> = mutableListOf(exploding to Number(0))
            flattened.take(index).filterIsInstance(Number::class.java).lastOrNull()
                ?.let { replacements += it to Number(it.value + (exploding.left as Number).value) }
            flattened.drop(index + 1).filterIsInstance(Number::class.java).drop(2).firstOrNull()
                ?.let { replacements += it to Number(it.value + (exploding.right as Number).value) }
            reduced = replace(reduced, replacements)
            continue
        }
        val splitting = findSplitting(reduced)
        if (splitting != null) {
            val pair = Pair(Number(splitting.value / 2), Number(((splitting.value + 0.5) / 2.0).roundToInt()))
            reduced = replace(reduced, listOf(splitting to pair))
            continue
        }
        break
    }
    return reduced
}

fun findExploding(snailfish: Snailfish, nestLevel: Int = 0): Pair? {
    if (snailfish is Pair) {
        if (nestLevel == 4) return snailfish
        return findExploding(snailfish.left, nestLevel + 1) ?: findExploding(snailfish.right, nestLevel + 1)
    }
    return null
}

fun findSplitting(snailfish: Snailfish): Number? {
    return when (snailfish) {
        is Number -> if (snailfish.value >= 10) snailfish else null
        is Pair -> findSplitting(snailfish.left) ?: findSplitting(snailfish.right)
    }
}

fun replace(snailfish: Snailfish, replacements: List<kotlin.Pair<Snailfish, Snailfish>>): Snailfish {
    replacements.firstOrNull { it.first === snailfish }?.let { return it.second }
    return when (snailfish) {
        is Number -> snailfish
        is Pair -> Pair(replace(snailfish.left, replacements), replace(snailfish.right, replacements))
    }
}

fun flatten(snailfish: Snailfish): List<Snailfish> {
    return when (snailfish) {
        is Number -> emptyList()
        is Pair -> listOf(listOf(snailfish.left), flatten(snailfish.left), listOf(snailfish.right), flatten(snailfish.right)).flatten()
    }
}
