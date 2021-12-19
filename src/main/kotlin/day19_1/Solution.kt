package day19_1

import solve
import utils.Vector3

fun main() {
    solve { lines ->
        var scanner = -1
        val scanners = mutableMapOf<Int, List<Vector3>>()
        lines.forEach { line ->
            if (line.startsWith("---")) {
                scanner++
                scanners[scanner] = emptyList()
            }
            else if (line.isNotBlank()) line.split(",").map { it.toInt() }.let { (x, y, z) -> scanners[scanner] = scanners.getValue(scanner) + Vector3(x, y, z) }
        }
        val scannerOrientations = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>() // Scanner pair -> Orientations pair
        val scannerPositions = mutableMapOf(0 to Vector3(0, 0, 0)) // Scanner 0 is the origin

        fun getOrientationsPath(scanner: Int): List<Int> {
            return mutableListOf<Int>().apply {
                var scannerInPath = scanner
                while (true) {
                    val (scannersPair, orientationsPair) = scannerOrientations.entries.firstOrNull { it.key.second == scannerInPath } ?: break
                    this += orientationsPair.toList().reversed()
                    scannerInPath = scannersPair.first
                }
            }
        }

        while (!scannerPositions.keys.containsAll(scanners.keys)) {
            scanners.entries.forEach { (scanner, beacons) ->
                scanners.entries.filter { it.key != scanner }.forEach otherScannerSearch@{ (otherScanner, otherBeacons) ->
                    if (otherScanner !in scannerPositions.keys) {
                        beacons.getAllOrientations().forEachIndexed { scannerOrientation, beaconsInOrientation ->
                            otherBeacons.getAllOrientations().forEachIndexed { otherScannerOrientation, otherBeaconsInOrientation ->
                                if (otherScannerOrientation == inverseOrientation(otherScannerOrientation)) {
                                    val sortedA = beaconsInOrientation.sortedWith(compareBy({ it.x }, { it.y }, { it.z }))
                                    val sortedB = otherBeaconsInOrientation.sortedWith(compareBy({ it.x }, { it.y }, { it.z }))
                                    List(sortedB.size) { sortedB.drop(it) + sortedB.take(it) }.forEach { sortedBOrder ->
                                        sortedA.zip(sortedBOrder).map { (a, b) -> a - b }.groupBy { it }.mapValues { it.value.size }.entries.firstOrNull { it.value >= 12 }?.let {
                                            if ((scanner in scannerOrientations.keys.map { it.second } || scanner == 0) && otherScanner !in scannerOrientations.keys.map { it.second } && otherScanner !in scannerOrientations.keys.map { it.first }) {
                                                scannerOrientations[scanner to otherScanner] = scannerOrientation to otherScannerOrientation
                                                val orientationsPath = listOf(scannerOrientation) + getOrientationsPath(scanner)
                                                scannerPositions[otherScanner] = scannerPositions.getValue(scanner) + orientationsPath.fold(it.key) { acc, i -> acc.orient(inverseOrientation(i)) }
                                                return@otherScannerSearch
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        scanners.keys.flatMap { currentScanner ->
            val orientationsPath = getOrientationsPath(currentScanner)
            val orientedBeacons = scanners.getValue(currentScanner).map { beacon -> orientationsPath.fold(beacon) { acc, i -> acc.orient(inverseOrientation(i)) } }
            orientedBeacons.map { it + scannerPositions.getValue(currentScanner) }
        }.sortedBy { it.x }.toSet().size
    }
}

fun List<Vector3>.getAllOrientations(): List<List<Vector3>> {
    return List(24) { orientation -> map { it.orient(orientation) } }
}

fun Vector3.orient(orientation: Int): Vector3 {
    return when (orientation % 24) {
        // Up
        0 -> Vector3(z, x, y) // Vector3(y, z, x) +15
        1 -> Vector3(z, -y, x) // Vector3(z, -y, x) 0
        2 -> Vector3(z, -x, -y) // Vector3(-y, -z, x) +7
        3 -> Vector3(z, y, -x) // Vector3(-z, y, x) +17
        // Forward
        4 -> Vector3(x, y, z) // Vector3(x, y, z) 0
        5 -> Vector3(x, -z, y) // Vector3(x, z, -y) +2
        6 -> Vector3(x, -y, -z) // Vector3(x, -y, -z) 0
        7 -> Vector3(x, z, -y) // Vector3(x, -z, y) -2
        // Left
        8 -> Vector3(-y, x, z) // Vector3(y, -x, z) +4
        9 -> Vector3(-y, -z, x) // Vector3(z, -x, -y) -7
        10 -> Vector3(-y, -x, -z) // Vector3(-y, -x, -z) 0
        11 -> Vector3(-y, z, -x) // Vector3(-z, -x, y) +10
        // Right
        12 -> Vector3(y, -x, z) // Vector3(-y, x, z) -4
        13 -> Vector3(y, -z, -x) // Vector3(-z, x, -y) +10
        14 -> Vector3(y, x, -z) // Vector3(y, x, -z) 0
        15 -> Vector3(y, z, x) // Vector3(z, x, y) -15
        // Back
        16 -> Vector3(-x, -y, z) // Vector3(-x, -y, z) 0
        17 -> Vector3(-x, -z, -y) // Vector3(-x, -z, -y) 0
        18 -> Vector3(-x, y, -z) // Vector3(-x, y, -z) 0
        19 -> Vector3(-x, z, y) // Vector3(-x, z, y) 0
        // Down
        20 -> Vector3(-z, y, x) // Vector3(z, y, -x) -17
        21 -> Vector3(-z, -x, y) // Vector3(-y, z, -x) -10
        22 -> Vector3(-z, -y, -x) // Vector3(-z, -y, -x) 0
        23 -> Vector3(-z, x, -y) // Vector3(y, -z, -x) -10
        else -> throw IllegalStateException()
    }
}

fun inverseOrientation(orientation: Int): Int {
    if (orientation in listOf(1, 4, 6, 10, 14, 16, 17, 18, 19, 22)) return orientation // Mirror orientations
    val inverseOrientations = mapOf(0 to 15, 2 to 9, 3 to 20, 5 to 7, 8 to 12, 11 to 21, 13 to 23)
    val reverseInverseOrientations = inverseOrientations.entries.associate { it.value to it.key }
    return (inverseOrientations + reverseInverseOrientations).getValue(orientation)
}
