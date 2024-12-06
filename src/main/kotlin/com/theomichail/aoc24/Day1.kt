package com.theomichail.aoc24

import java.io.File
import java.util.PriorityQueue
import kotlin.math.abs

fun main() {
    println("PART 1 =========================")
    part1()
    println("================================")
    println("PART 2 =========================")
    part2()
    println("================================")
}

private fun part1() {
    val input = File("inputs/1")

    val list1 = PriorityQueue<Int>()
    val list2 = PriorityQueue<Int>()

    input.useLines { lines ->
        lines.forEach {
            val split = it.split("   ")
            list1.add(split[0].toInt())
            list2.add(split[1].toInt())
        }
    }

    val sequence1 = generateSequence { if (list1.isNotEmpty()) list1.poll() else null }
    val sequence2 = generateSequence { if (list2.isNotEmpty()) list2.poll() else null }

    var totalDistance = 0

    sequence1.zip(sequence2).forEach { (a, b) ->
        totalDistance += abs(a - b)
    }

    println("Total distance: $totalDistance")
}

private fun part2() {
    val input = File("inputs/1")

    val list1 = mutableListOf<Int>()
    val list2Map = mutableMapOf<Int, Int>()

    input.useLines { lines ->
        lines.forEach {
            val split = it.split("   ")
            list1.add(split[0].toInt())

            val id = split[1].toInt()
            list2Map[id] = list2Map.getOrDefault(id, 0) + 1
        }
    }

    var similarityScore = 0

    list1.forEach {
        similarityScore += list2Map.getOrDefault(it, 0) * it
    }

    println("Similarity score: $similarityScore")
}