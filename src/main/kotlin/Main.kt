import Direction.*
import java.io.File

fun main() {
    val worlds = listOf(
        readMapFromFile(File("first.map")),
        readMapFromFile(File("second.map")),
        readMapFromFile(File("third.map")),
        readMapFromFile(File("fourth.map"))/*,
        readMapFromFile(File("fifth.map"))*/
    )
    worlds.forEachIndexed { index, map ->
        println("Map : ${index+1}")
        val listOfRobots = listOf(
            Robot(realWorld = map),
            Robot(
                realWorld = map,
                sensors = listOf(NORTH, SOUTH)
            ),
            Robot(
                realWorld = map,
                sensors = listOf(NORTH, SOUTH, EAST, WEST)
            ),
            Robot(
                realWorld = map,
                sensors = listOf(NORTH, NORTH_EAST, NORTH_WEST)
            ),
            Robot(
                realWorld = map,
                sensors = listOf(NORTH, NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST)
            ),
            Robot(
                realWorld = map,
                sensors = listOf(NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST)
            ),
            Robot(
                realWorld = map,
                sensors = listOf(NORTH, NORTH_EAST, EAST, NORTH_WEST, WEST)
            ),
            Robot(
                realWorld = map,
                sensors = listOf(NORTH, EAST, WEST)
            ),
            Robot(
                realWorld = map,
                sensors = listOf(NORTH, SOUTH_EAST, SOUTH_WEST)
            ),
            Robot(
                realWorld = map,
                sensors = listOf(NORTH, NORTH_EAST, SOUTH, NORTH_WEST)
            )
        )
        listOfRobots.forEach { rob ->
            rob.calculateStarPath()
            rob.travel(false)
//            println("-----------------------------------")
            rob.printStats()
//            rob.printFinalMap()
        }
        println()
        println()
    }
}

fun readMapFromFile(file: File): Array<BooleanArray> {
    val lines = file.readLines()
    val output = Array(lines.size) { BooleanArray(0) }
    lines.forEachIndexed { i, line ->
        val charLine = line.toCharArray()
        val outputLine = BooleanArray(line.length)
        charLine.forEachIndexed { j, character ->
            outputLine[j] = character == '1'
        }
        output[i] = outputLine
    }
    return output
}
//turn, detect, move, detect
