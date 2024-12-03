package com.theomichail.com.theomichail.aoc24

import java.io.File
import java.util.PriorityQueue

fun main() {
    println("PART 1 =========================")
    part1()
    println("================================")
    println("PART 2 =========================")
    part2()
    println("================================")
}

private fun part1() {
    val input = File("inputs/3")

    val instructions = input.useLines { lines ->
        lines.flatMap { line ->
            instructionRegex.findAll(line).map { it.value }
        }.toList()
    }

    val total = calculateTotal(instructions)

    println("Total: $total")
}

private fun part2() {
    val input = File("inputs/3")

    var include = true
    val instructions = mutableListOf<String>()

    input.useLines { lines ->
        lines.forEach { line ->
            val matches = PriorityQueue<Pair<String, Int>> { a, b ->
                a.second.compareTo(b.second)
            }

            matches.addAll(instructionRegex.findAll(line).map { it.value to it.range.first })
            matches.addAll(controlRegex.findAll(line).map { it.value to it.range.first })

            while (matches.isNotEmpty()) {
                when (val match = matches.poll().first) {
                    "do()" -> include = true
                    "don't()" ->  include = false
                    else -> if (include) instructions.add(match)
                }
            }
        }
    }

    val total = calculateTotal(instructions)

    println("Total: $total")
}

private fun calculateTotal(instructions: List<String>): Int {
    var total = 0

    instructions.forEach { instruction ->
        total += numberRegex.findAll(instruction).toList().fold(1) { a, b ->
            a * b.value.toInt()
        }
    }

    return total
}

private val instructionRegex = """mul\(\d{1,3},\d{1,3}\)""".toRegex()
private val controlRegex = """do\(\)|don't\(\)""".toRegex()
private val numberRegex = """\d{1,3}""".toRegex()
