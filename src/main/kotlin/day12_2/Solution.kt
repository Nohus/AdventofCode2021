package day12_2

import solve

fun main() {
    solve { lines ->
        val connections = lines.flatMap { it.split('-').let { (a, b) -> listOf(a to b, b to a) } }
        fun List<String>.getNotAllowed() = filter { it[0].isLowerCase() }.let { if (it.groupBy { it }.any { it.value.size > 1 }) it else emptyList() }
        var paths = listOf(listOf("start"))
        while (paths.any { it.last() != "end" }) {
            paths = paths.flatMap { path ->
                if (path.last() == "end") return@flatMap listOf(path)
                (connections.filter { it.first == path.last() }.map { it.second } - path.getNotAllowed() - listOf("start")).map { path + it }
            }
        }
        paths.size
    }
}
