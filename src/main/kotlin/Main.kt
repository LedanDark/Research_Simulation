import java.io.File

fun main() {

    val firstWorld = readMapFromFile(File("first.map"))
    val robotKnowsPath = Robot(firstWorld.size)
    val robot = Robot(firstWorld.size, realWorld = firstWorld)
    val northSensors = Robot(firstWorld.size, sensors = listOf(Direction.NORTH, Direction.NORTH_EAST, Direction.NORTH_WEST), realWorld = firstWorld)
    val moreSensors = Robot(firstWorld.size, sensors = listOf(Direction.NORTH, Direction.NORTH_EAST, Direction.NORTH_WEST, Direction.WEST, Direction.EAST), realWorld = firstWorld)
    val allSensors = Robot(firstWorld.size, sensors = listOf(Direction.NORTH,  Direction.SOUTH_EAST), realWorld = firstWorld)
    robotKnowsPath.knownWorld = firstWorld
//    val range = (0 until 3)
//    for (i in robotKnowsPath.knownWorld[2].indices) {
//        robotKnowsPath.knownWorld[2][i] = i !in range
//        robot.realWorld[2][i] = i !in range
//        twoSensors.realWorld[2][i] = i !in range
//        moreSensors.realWorld[2][i] = i !in range
//    }



    robotKnowsPath.calculateStarPath()
    robot.calculateStarPath()
    northSensors.calculateStarPath()
    moreSensors.calculateStarPath()
    allSensors.calculateStarPath()
    println("${robotKnowsPath.position} : ${robotKnowsPath.path}")
    robotKnowsPath.travel()
    println("-----------------------------------")
    println("${robot.position} : ${robot.path}")
    robot.travel(false)
    println("-----------------------------------")
    northSensors.travel(false)
    println("-----------------------------------")
    moreSensors.travel(false)
    println("-----------All------------------------")
    allSensors.travel(false)
    robotKnowsPath.printStats("Knowing robot")
    robot.printStats("Simple sensor")
    northSensors.printStats("Triple north sensor")
    moreSensors.printStats("Semi-cicle sensors")
    allSensors.printStats("All-sensors")

}

fun readMapFromFile(file : File) : Array<BooleanArray>{
    val lines = file.readLines()
    val output = Array(lines.size) { BooleanArray(0) }
    lines.forEachIndexed{ i, line ->
        val charLine = line.toCharArray()
        val outputLine = BooleanArray(line.length)
        charLine.forEachIndexed { j, character ->
            outputLine[j] = character == '1'
        }
        output[i] = outputLine
    }
    return  output
}
//turn, detect, move, detect
