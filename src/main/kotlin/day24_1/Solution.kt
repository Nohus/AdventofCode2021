package day24_1

import solve

fun main() {
    solve { lines ->
        // Done by hand using equations below
        val list = (99893999291967).toString().toCharArray().map { it.digitToInt().toLong() }
        checkModel(list)
    }
}

private val constants = listOf<Triple<Long, Long, Long>>(
        Triple(12, 1, 6),   //  A == i1 + 6
        Triple(11, 1, 12),  //  B == i2 + 12
        Triple(10, 1, 5),   //  C == i3 + 5
        Triple(10, 1, 10),  //  D == i4 + 10
        Triple(-16, 26, 7), // i5 == D - 16
        Triple(14, 1, 0),   //  E == i6 + 0
        Triple(12, 1, 4),   //  F == i7 + 4
        Triple(-4, 26, 12), // i8 == F - 4
        Triple(15, 1, 14),  //  G == i9 + 14
        Triple(-7, 26, 13), //i10 == G - 7
        Triple(-8, 26, 10), //i11 == E - 8
        Triple(-4, 26, 11), //i12 == C - 4
        Triple(-15, 26, 9), //i13 == B - 15
        Triple(-8, 26, 9)   //i14 == A - 8
)

/*
    Simplifying:
    i5 == i4 - 6
    i8 == i7
    i10 == i9 + 7
    i11 == i6 - 8
    i12 == i3 + 1
    i13 == i2 - 3
    i14 == i1 - 2
 */

private fun checkDigit(w: Long, z: Long, a: Long, b: Long, c: Long): Long {
    val x = (z % 26) + a
    var newZ = z / b
    if (x != w) {
        newZ = (z * 26) + (w + c)
    }
    return newZ
}

private fun checkModel(number: List<Long>): Boolean {
    var z = 0L
    for (i in 0 until 14) {
        z = checkDigit(number[i], z, constants[i].first, constants[i].second, constants[i].third)
    }
    return z == 0L
}
