package com.theomichail.aoc24

fun main() {
    Day07().main()
}

class Day07 : AdventOfCodeDay() {
    private val testCases = buildList {
        input.useLines { lines ->
            lines.forEach { line ->
                val parts = line.split(":")
                val target = parts[0].toLong()
                val numbers = parts[1].split(" ").mapNotNull { it.toLongOrNull() }

                add(numbers to target)
            }
        }
    }

    override fun part1() {
        calculateSum(setOf(Operator.MULTIPLY, Operator.ADD))
    }

    override fun part2() {
        calculateSum(setOf(Operator.MULTIPLY, Operator.ADD, Operator.CONCAT))
    }

    private fun calculateSum(operators: Set<Operator>) {
        val sum = testCases.sumOf { (numbers, target) ->
            if (canReachTarget(numbers, target, operators)) target else 0L
        }

        println("Sum: $sum")
    }

    private fun canReachTarget(numbers: List<Long>, target: Long, operators: Set<Operator>): Boolean {
        fun helper(index: Int, currentValue: Long): Boolean {
            if (index == numbers.size) {
                return currentValue == target
            }

            // Note: Tried with a cache here but the overhead was too much for this particular data set

            if (currentValue > target) return false

            val nextNumber = numbers[index]

            for (operator in operators) {
                val nextValue = when (operator) {
                    Operator.ADD -> currentValue + nextNumber
                    Operator.MULTIPLY -> currentValue * nextNumber
                    Operator.CONCAT -> currentValue.concat(nextNumber)
                }
                if (helper(index + 1, nextValue)) return true
            }

            return false
        }

        return helper(1, numbers[0])
    }

    private fun Long.concat(other: Long): Long {
        var multiplier = 1L
        var temp = other
        while (temp > 0) {
            multiplier *= 10
            temp /= 10
        }
        return this * multiplier + other
    }

    enum class Operator {
        ADD,
        MULTIPLY,
        CONCAT,
    }
}