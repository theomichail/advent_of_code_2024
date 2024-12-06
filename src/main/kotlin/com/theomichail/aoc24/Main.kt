package com.theomichail.aoc24

import org.reflections.Reflections
import org.reflections.scanners.Scanners

fun main() {
    val subclasses = Reflections("com.theomichail.aoc24", Scanners.SubTypes)
        .getSubTypesOf(AdventOfCodeDay::class.java)
        .sortedBy { it.simpleName }
        .mapNotNull {
            runCatching {
                it.getDeclaredConstructor().newInstance()
            }.getOrNull()
        }

    subclasses.forEach {
        it.main()
    }
}
