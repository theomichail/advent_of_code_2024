package com.theomichail.aoc24

private fun main() {
    Day02().main()
}

class Day02 : AdventOfCodeDay() {
    private val reports = reports()

    override fun part1() {
        var safeReports = 0

        reports.forEach { report ->
            val increasing = report[0] < report[1]
            if (isReportSafe(report, increasing)) {
                safeReports++
            }
        }

        println("Safe reports: $safeReports")
    }

    override fun part2() {
        var safeReports = 0

        reports.forEach { report ->
            if (isReportSafeOrCanBeMadeSafe(report)) {
                safeReports++
            }
        }

        println("Safe reports: $safeReports")
    }

    private fun isReportSafe(report: List<Int>, increasing: Boolean): Boolean {
        var previousValue = report[0]
        report.drop(1).forEach { level ->
            val difference = if (increasing) level - previousValue else previousValue - level
            if (difference < 1 || difference > 3) {
                return false
            }
            previousValue = level
        }
        return true
    }

    private fun isReportSafeOrCanBeMadeSafe(report: List<Int>): Boolean {
        listOf(true, false).forEach { increasing ->
            if (isReportSafe(report, increasing)) {
                return true
            }

            val problemLevels = mutableSetOf<Int>()
            var previousValue = report[0]
            report.drop(1).forEachIndexed { index, level ->
                val difference = if (increasing) level - previousValue else previousValue - level
                if (difference < 1 || difference > 3) {
                    problemLevels.add(index)
                    problemLevels.add(index + 1)
                }
                previousValue = level
            }

            problemLevels.forEach { problemIndex ->
                val newReport = report.filterIndexed { index, _ -> index != problemIndex }
                if (isReportSafe(newReport, increasing)) {
                    return true
                }
            }
        }
        return false
    }

    private fun reports(): List<List<Int>> {
        return buildList {
            input.useLines { lines ->
                lines.forEach { line ->
                    add(line.split(" ").map { it.toInt() })
                }
            }
        }
    }
}
