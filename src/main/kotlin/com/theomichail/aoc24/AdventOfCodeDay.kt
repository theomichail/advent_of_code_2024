package com.theomichail.aoc24

import java.io.File
import kotlin.time.measureTime

abstract class AdventOfCodeDay {
    private val dayNumber = this::class.simpleName?.replace("Day", "") ?: "Unknown"
    protected val input = File("inputs/$dayNumber")

    fun main() {
        println("============ DAY $dayNumber ============")
        println("============ PART 1 ============")
        val part1Time = measureTime { part1() }
        println("================================")
        println("Time taken: $part1Time")
        println("================================")

        println("============ PART 2 ============")
        val part2Time = measureTime { part2() }
        println("================================")
        println("Time taken: $part2Time")
        println("================================")

        println()
    }

    abstract fun part1()

    abstract fun part2()
}