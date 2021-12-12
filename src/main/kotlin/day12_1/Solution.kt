package day12_1

import solve

fun main() {
    solve { lines ->
        val connections = lines.flatMap { it.split('-').let { (a, b) -> listOf(a to b, b to a) } }
        var paths = listOf(listOf("start"))
        while (paths.any { it.last() != "end" }) {
            paths = paths.flatMap { path ->
                if (path.last() == "end") return@flatMap listOf(path)
                (connections.filter { it.first == path.last() }.map { it.second } - path.filter { it[0].isLowerCase() }).map { path + it }
            }
        }
        paths.size
    }
}
