package com.theomichail.aoc24

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
    val (board, originalGuardState) = createBoardAndState()

    var currentGuardState = originalGuardState

    while (true) {
        currentGuardState = getNextState(currentGuardState, board) ?: break
    }

    val total = board.flatten().count { it == Tile.VISITED }
    println("Total visited tiles: $total")
}

private fun part2() {
    val (board, originalGuardState) = createBoardAndState()

    var newObstacleCount = 0

    board.forEachIndexed outer@{ y, row ->
        row.forEachIndexed inner@{ x, tile ->
            var currentGuardState = originalGuardState

            if (tile == Tile.OBSTACLE) return@inner
            if (originalGuardState.isAt(x, y)) return@inner

            val modifiedBoard = board.clone()
            modifiedBoard[x,y] = Tile.OBSTACLE

            val states = mutableSetOf<State>()

            while (true) {
                currentGuardState = getNextState(currentGuardState, modifiedBoard) ?: break

                if (states.contains(currentGuardState)) {
                    newObstacleCount ++
                    break
                } else {
                    states.add(currentGuardState)
                }
            }
        }
    }

    println("Total possible new obstacles: $newObstacleCount")
}

private fun createBoardAndState(): Pair<Board, State> {
    val input = File("inputs/6")
    var guardPosition = -1 to -1
    var guardDirection = Direction.NORTH

    val board = buildList {
        input.useLines { lines ->
            lines.forEachIndexed { y, line ->
                add(
                    line.mapIndexed { x, ch ->
                        when (ch) {
                            '.' -> Tile.UNVISITED
                            '#' -> Tile.OBSTACLE
                            '^', 'v', '<', '>' -> {
                                guardPosition = x to y
                                guardDirection = Direction.fromChar(ch)
                                Tile.VISITED
                            }
                            else -> throw IllegalArgumentException("Unrecognized character: $ch")
                        }
                    }.toMutableList()
                )
            }
        }
    }

    if (guardPosition == -1 to -1) throw IllegalArgumentException("Guard not found!")
    return board to State(guardPosition.first, guardPosition.second, guardDirection)
}

private fun getNextState(currentState: State, board: Board): State? {
    val movedState = currentState.move()
    if (movedState.isOnBoard(board).not()) return null

    return with(movedState) {
        when (board[x,y]) {
            Tile.UNVISITED, Tile.VISITED -> {
                board[x,y] = Tile.VISITED
                movedState
            }
            Tile.OBSTACLE -> currentState.turn()
        }
    }
}

private typealias Board = List<MutableList<Tile>>

private fun Board.clone() = map { it.toList().toMutableList() }

private operator fun Board.get(x: Int, y: Int) = this[y][x]
private operator fun Board.set(x: Int, y: Int, value: Tile) {
    this[y][x] = value
}

private val Board.width get() = this[0].size
private val Board.height get() = this.size

private enum class Tile {
    UNVISITED,
    VISITED,
    OBSTACLE,
}

private data class State(
    val x: Int,
    val y: Int,
    val direction: Direction,
)

private fun State.isOnBoard(board: Board) = x in 0..<board.width && y in 0..<board.height

private fun State.isAt(x: Int, y: Int) = this.x == x && this.y == y

private fun State.move() = copy(
    x = x + direction.dx,
    y = y + direction.dy,
)

private fun State.turn() = copy(
    direction = when(direction) {
        Direction.NORTH -> Direction.EAST
        Direction.EAST -> Direction.SOUTH
        Direction.SOUTH -> Direction.WEST
        Direction.WEST -> Direction.NORTH
    }
)

private enum class Direction(val dx: Int, val dy: Int) {
    NORTH(0, -1),
    EAST(1, 0),
    SOUTH(0, 1),
    WEST(-1, 0);

    companion object {
        fun fromChar(ch: Char) = when(ch) {
            '^' -> NORTH
            'v' -> SOUTH
            '<' -> WEST
            '>' -> EAST
            else -> throw IllegalArgumentException("Unrecognized character: $ch")
        }
    }
}
