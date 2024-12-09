package com.theomichail.aoc24

import java.io.File
import java.io.OutputStream
import java.io.PrintStream
import kotlin.time.measureTime

abstract class AdventOfCodeDay {
    private val dayNumber = this::class.simpleName?.replace("Day", "") ?: "Unknown"
    protected val input = File("inputs/$dayNumber")

    private var warmUpRuns = 10

    fun main() {
        // Allow runtime optimisations to kick in before timing
        warmUp()

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

    private fun warmUp() {
        repeat(warmUpRuns) {
            suppressOutput {
                part1()
                part2()
            }
        }
    }

    private fun suppressOutput(block: () -> Unit) {
        val originalOut = System.out
        val dummyStream = OutputStream.nullOutputStream()
        val dummyPrintStream = PrintStream(dummyStream)
        System.setOut(dummyPrintStream)
        return try {
            block()
        } finally {
            System.setOut(originalOut)
        }
    }
}