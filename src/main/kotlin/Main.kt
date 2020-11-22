fun main() {
    val robotKnowsPath = Robot()
    val robot = Robot()
    val twoSensors = Robot(sensors = listOf(Direction.NORTH, Direction.NORTH_EAST, Direction.NORTH_WEST))
    val moreSensors = Robot(sensors = listOf(Direction.NORTH, Direction.NORTH_EAST, Direction.NORTH_WEST, Direction.WEST, Direction.EAST))
    val range = (0 until 3)
    for (i in robotKnowsPath.knownWorld[2].indices) {
        robotKnowsPath.knownWorld[2][i] = i !in range
        robot.realWorld[2][i] = i !in range
        twoSensors.realWorld[2][i] = i !in range
        moreSensors.realWorld[2][i] = i !in range
    }

    robotKnowsPath.calculateStarPath()
    robot.calculateStarPath()
    twoSensors.calculateStarPath()
    moreSensors.calculateStarPath()
    println("${robotKnowsPath.position} : ${robotKnowsPath.path}")
    robotKnowsPath.travel()
    println("-----------------------------------")
    println("${robot.position} : ${robot.path}")
    robot.travel(false)
    println("-----------------------------------")
    twoSensors.travel(false)
    println("-----------------------------------")
    moreSensors.travel(false)
    robotKnowsPath.printStats("Knowing robot")
    robot.printStats("Simple sensor")
    twoSensors.printStats("Triple north sensor")
    moreSensors.printStats("Semi-cicle sensors")

}
//turn, detect, move, detect
