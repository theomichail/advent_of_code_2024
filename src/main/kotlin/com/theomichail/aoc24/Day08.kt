package com.theomichail.aoc24

import kotlin.math.abs

private fun main() {
    Day08().main()
}

class Day08 : AdventOfCodeDay() {
    private val antennas: Map<Char, List<Coordinates>> = buildMap<Char, MutableList<Coordinates>> {
        input.useLines { lines ->
            var y = 0
            lines.forEach { line ->
                line.forEachIndexed { x, char ->
                    if (char == '.') return@forEachIndexed

                    getOrPut(char, { mutableListOf() }).add(x to y)
                }
                y++
            }
        }
    }
    private val minCoords: Coordinates = 0 to 0
    private val maxCoords: Coordinates = input.useLines { lines ->
        lines.toList().let { it[0].length - 1 to it.size - 1 }
    }

    override fun part1() {
        val antinodes = mutableSetOf<Coordinates>()

        antennas.forEach { (_, coords) ->
            if (coords.size < 2) return@forEach

            coords.combinations().forEach { (a, b) ->
                val diff = b - a

                val n1 = a - diff
                val n2 = b + diff

                if (n1.isInBounds()) antinodes.add(n1)
                if (n2.isInBounds()) antinodes.add(n2)
            }
        }

        println("Total antinodes: ${antinodes.size}")
    }

    override fun part2() {
        val antinodes = mutableSetOf<Coordinates>()

        antennas.forEach { (_, coords) ->
            if (coords.size < 2) return@forEach

            coords.combinations().forEach { (a, b) ->
                val diff = (b - a).reduce()

                var n1 = a
                while (n1.isInBounds()) {
                    antinodes.add(n1)
                    n1 -= diff
                }

                var n2 = a + diff
                while (n2.isInBounds()) {
                    antinodes.add(n2)
                    n2 += diff
                }
            }
        }

        println("Total antinodes: ${antinodes.size}")
    }

    private operator fun Coordinates.plus(other: Coordinates): Coordinates {
        return first + other.first to second + other.second
    }

    private operator fun Coordinates.minus(other: Coordinates): Coordinates {
        return first - other.first to second - other.second
    }

    private fun Coordinates.isInBounds(): Boolean =
        first >= minCoords.first && first <= maxCoords.first && second >= minCoords.second && second <= maxCoords.second

    private fun Coordinates.reduce(): Coordinates {
        fun gcd(a: Int, b: Int): Int {
            var num1 = a
            var num2 = b

            if (num1 < num2) {
                val temp = num1
                num1 = num2
                num2 = temp
            }

            while (num2 != 0) {
                val temp = num1 % num2
                num1 = num2
                num2 = temp
            }

            return num1
        }

        val divisor = gcd(abs(first), abs(second))

        if (divisor == 0 || divisor == 1) return this

        return first / divisor to second / divisor
    }

    private fun List<Coordinates>.combinations(): Sequence<Pair<Coordinates, Coordinates>> = sequence {
        for (i in indices) {
            for (j in (i + 1) until size) {
                yield(this@combinations[i] to this@combinations[j])
            }
        }
    }
}

private typealias Coordinates = Pair<Int, Int>
