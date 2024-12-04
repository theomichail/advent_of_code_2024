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
    val input = File("inputs/4")

    val grid = input.readLines().map { it.toCharArray() }.toTypedArray()

    val wordToFind = "XMAS"
    val firstChar = wordToFind[0]

    var wordCount = 0

    grid.forEachIndexed { y, row ->
        row.forEachIndexed { x, char ->
            if (char == firstChar) {
                wordCount += countWords(wordToFind, x, y, grid)
            }
        }
    }

    println("\"$wordToFind\" patterns found: $wordCount")
}

private fun part2() {
    val input = File("inputs/4")

    val grid = input.readLines().map { it.toCharArray() }.toTypedArray()

    val wordToFind = "MAS"
    if (wordToFind.length % 2 == 0) {
        println("The word must have an odd length.")
        return
    }

    val centerChar = wordToFind[wordToFind.length / 2]

    var patternCount = 0

    grid.forEachIndexed { y, row ->
        row.forEachIndexed { x, char ->
            if (char == centerChar) {
                val matches = countDiagonalMatchesAroundCenter(wordToFind, x, y, grid)
                if (matches >= 2) {
                    patternCount++
                }
            }
        }
    }

    println("\"X-$wordToFind\" patterns found: $patternCount")
}

private fun countWords(
    wordToFind: String,
    x: Int,
    y: Int,
    grid: Array<CharArray>,
): Int {
    var wordCount = 0
    (-1..1).forEach outer@{ xOff ->
        (-1..1).forEach inner@{ yOff ->
            if (xOff == 0 && yOff == 0) return@inner

            if (wordExistsInDirection(wordToFind, x, y, xOff, yOff, grid)) {
                wordCount++
            }
        }
    }

    return wordCount
}

private fun wordExistsInDirection(
    wordToFind: String,
    x: Int,
    y: Int,
    xOff: Int,
    yOff: Int,
    grid: Array<CharArray>
): Boolean {
    if (wordToFind.isEmpty()) return true

    if (wordToFind[0].isAtPlace(x, y, grid).not()) return false

    val newX = x + xOff
    val newY = y + yOff

    return wordExistsInDirection(wordToFind.drop(1), newX, newY, xOff, yOff, grid)
}

private fun countDiagonalMatchesAroundCenter(
    word: String,
    x: Int,
    y: Int,
    grid: Array<CharArray>
): Int {
    val directions = listOf(
        -1 to -1,
        -1 to  1,
        1  to -1,
        1  to  1,
    )

    var matches = 0

    directions.forEach { dir ->
        if (isDiagonalMatchAroundCenter(word, x, y, dir, grid)) {
            matches++
        }
    }

    return matches
}

private fun isDiagonalMatchAroundCenter(
    word: String,
    x: Int,
    y: Int,
    dir: Pair<Int, Int>,
    grid: Array<CharArray>
): Boolean {
    val middleIndex = word.length / 2

    word.indices.forEach { i ->
        val offset = i - middleIndex
        val currentX = x + dir.first * offset
        val currentY = y + dir.second * offset

        if (word[i].isAtPlace(currentX, currentY, grid).not()) return false
    }

    return true
}

private fun Char.isAtPlace(x: Int, y: Int, grid: Array<CharArray>): Boolean {
    return y in grid.indices && x in grid[y].indices && grid[y][x] == this
}
