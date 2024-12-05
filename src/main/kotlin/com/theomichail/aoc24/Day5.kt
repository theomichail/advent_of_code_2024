package com.theomichail.com.theomichail.aoc24

import java.io.File

fun main() {
    println("PART 1 =========================")
    part1()
    println("================================")
    println("PART 2 =========================")
    part2()
    println("================================")
}

private fun part1() {
    val (rules, updates) = getRulesAndUpdates()

    var validMiddleSum = 0

    updates.forEach outer@{ update ->
        val applicableRules = getApplicableRules(rules, update)

        applicableRules.forEach inner@{ rule ->
            if (update violates rule) return@outer
        }

        validMiddleSum += update[update.size / 2]
    }

    println("Middle sum of valid updates: $validMiddleSum")
}

private fun part2() {
    val (rules, updates) = getRulesAndUpdates()

    var correctedMiddleSum = 0

    updates.forEach outer@{ update ->
        val applicableRules = getApplicableRules(rules, update)

        applicableRules.forEach inner@{ rule ->
            if (update violates rule) {
                correctedMiddleSum += correctAndGetMiddleSum(update, applicableRules)
                return@outer
            }
        }
    }

    println("Middle sum of corrected invalid updates: $correctedMiddleSum")
}

private fun getRulesAndUpdates() : Pair<Map<Int, Map<Int, Rule>>, List<List<Int>>> {
    val input = File("inputs/5")

    val rules = mutableMapOf<Int, MutableMap<Int, Rule>>()
    val updates = mutableListOf<List<Int>>()

    val newMapCreator = { mutableMapOf<Int, Rule>() }

    var parsingRules = true
    input.useLines outer@{ lines ->
        lines.forEach inner@{ line ->
            if (line.isBlank()) {
                parsingRules = false
                return@inner
            }

            if (parsingRules) {
                val numbers = line.split("|")
                val before = numbers[0].toInt()
                val after = numbers[1].toInt()
                when (before < after) {
                    true -> rules.getOrPut(before, newMapCreator)[after] = Rule(before, after, Position.AFTER)
                    false -> rules.getOrPut(after, newMapCreator)[before] = Rule(after, before, Position.BEFORE)
                }
            } else {
                updates.add(line.split(",").map { it.toInt() })
            }
        }
    }

    return rules to updates
}

private fun getApplicableRules(rules: Map<Int, Map<Int, Rule>>, update: List<Int>): List<Rule> = buildList {
    update.forEach { page ->
        rules[page]?.filter { it.key in update }?.values?.let(::addAll)
    }
}

private fun correctAndGetMiddleSum(update: List<Int>, applicableRules: List<Rule>): Int {
    val correctedList = update.sortedWith(RulesComparator(applicableRules))

    return correctedList[correctedList.size / 2]
}

private class RulesComparator(private val rules: List<Rule>): Comparator<Int> {
    override fun compare(o1: Int?, o2: Int?): Int {
        if (o1 == null || o2 == null) return 0

        val rule = rules.find { it.isFor(o1, o2) } ?: return 0

        return rule.compare(o1, o2)
    }
}

private infix fun List<Int>.violates(rule: Rule): Boolean {
    return when(rule.position) {
        Position.BEFORE -> indexOf(rule.larger) > indexOf(rule.smaller)
        Position.AFTER -> indexOf(rule.larger) < indexOf(rule.smaller)
    }
}

private fun Rule.isFor(o1: Int, o2: Int): Boolean =
    (o1 == smaller && o2 == larger) ||
    (o2 == smaller && o1 == larger)

private fun Rule.compare(o1: Int, o2: Int): Int {
    if (isFor(o1, o2).not()) return 0

    return when (o1 == smaller) {
        true -> if (position == Position.AFTER) -1 else 1
        false -> if (position == Position.BEFORE) -1 else 1
    }
}

// The position applies to the larger value i.e. BEFORE means larger before smaller
private data class Rule(
    val smaller: Int,
    val larger: Int,
    val position: Position,
)

private enum class Position {
    BEFORE,
    AFTER,
}
