package com.theomichail.aoc24

private fun main() {
    Day09().main()
}

class Day09 : AdventOfCodeDay() {
    init {
        warmUpRuns = 0
    }

   private val diskMap = input.useLines { lines -> lines.flatMap { line -> line.mapNotNull { it.digitToIntOrNull() } }.toList() }
    // private val diskMap = "2333133121414131402".map { it.digitToInt() }

    override fun part1() {
        var leftIndex = 0
        var rightIndex = diskMap.size - 1

        var leftIsFile = true
        var rightIsFile = rightIndex % 2 == 0

        var blockIndex = 0
        var checksum = 0L

        var remainingBlocksOnRightFile = 0
        var remainingFileId = -1
        var remainingFreeSpace = 0

        while (leftIndex <= rightIndex) {
            if (leftIsFile) {
                val fileId = leftIndex / 2
                // val fileSize = diskMap[leftIndex]
                val fileSize = if (fileId == remainingFileId) remainingBlocksOnRightFile else diskMap[leftIndex]
                val fileChecksum = fileId * fileChecksum(blockIndex, fileSize)
                checksum += fileChecksum

                leftIsFile = false
                leftIndex++
                blockIndex += fileSize
            } else {
                if (!rightIsFile) {
                    rightIndex--
                    rightIsFile = true
                }

                val freeSpace = if (remainingFreeSpace > 0) remainingFreeSpace else diskMap[leftIndex]
                val fileSize = if (remainingBlocksOnRightFile > 0) remainingBlocksOnRightFile else diskMap[rightIndex]
                val fileId = rightIndex / 2

                val blocksToWrite = minOf(freeSpace, fileSize)

                val fileChecksum = fileId * fileChecksum(blockIndex, blocksToWrite)
                checksum += fileChecksum
                blockIndex += blocksToWrite

                if (blocksToWrite < fileSize) { // Blocks remaining on file
                    remainingBlocksOnRightFile = fileSize - blocksToWrite
                    remainingFreeSpace = 0
                    remainingFileId = fileId
                    leftIsFile = true
                    leftIndex++
                } else if (blocksToWrite < freeSpace) { // Free space still available
                    remainingBlocksOnRightFile = 0
                    remainingFreeSpace = freeSpace - blocksToWrite
                    rightIndex -= 2
                } else { // File exactly fit in space
                    remainingBlocksOnRightFile = 0
                    remainingFreeSpace = 0
                    leftIsFile = true
                    leftIndex++
                    rightIndex -= 2
                }
            }
        }

        println("Checksum: $checksum")
    }

    private fun fileChecksum(startIndex: Int, fileSize: Int): Int {
        return fileSize * (2 * startIndex + fileSize - 1) / 2
    }

    override fun part2() {
    }
}