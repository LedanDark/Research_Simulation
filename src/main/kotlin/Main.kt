import Direction.*
import java.io.File

fun main() {

    val firstWorld = readMapFromFile(File("first.map"))
    val robotKnowsPath = Robot(firstWorld.size)
    robotKnowsPath.knownWorld = firstWorld

    val listOfRobots = listOf(
        Robot(firstWorld.size),
        Robot(
            firstWorld.size, realWorld = firstWorld,
            sensors = listOf(NORTH, SOUTH)
        ),
        Robot(
            firstWorld.size, realWorld = firstWorld,
            sensors = listOf(NORTH, SOUTH, EAST, WEST)
        ),
        Robot(
            firstWorld.size, realWorld = firstWorld,
            sensors = listOf(NORTH, NORTH_EAST, NORTH_WEST)
        ),
        Robot(
            firstWorld.size, realWorld = firstWorld,
            sensors = listOf(NORTH, NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST)
        ),
        Robot(
            firstWorld.size, realWorld = firstWorld,
            sensors = listOf(NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST)
        ),
        Robot(
            firstWorld.size, realWorld = firstWorld,
            sensors = listOf(NORTH, NORTH_EAST, EAST, NORTH_WEST,  WEST)
        ),
        Robot(
            firstWorld.size, realWorld = firstWorld,
            sensors = listOf(NORTH, EAST, WEST)
        ),
        Robot(
            firstWorld.size, realWorld = firstWorld,
            sensors = listOf(NORTH, SOUTH_EAST, SOUTH_WEST)
        )
    )

    robotKnowsPath.calculateStarPath()
    robotKnowsPath.travel()
    listOfRobots.forEach { rob ->
        rob.calculateStarPath()
        rob.travel(false)
        println("-----------------------------------")
        rob.printStats()
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
