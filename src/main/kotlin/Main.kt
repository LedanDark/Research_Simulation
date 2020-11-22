fun main() {
    val robotKnowsPath = Robot()
    val robot = Robot()
    val twoSensors = Robot(sensors = listOf(Direction.NORTH, Direction.NORTH_EAST, Direction.NORTH_WEST))
    val range = (0 until 3)
    for (i in robotKnowsPath.knownWorld[2].indices) {
        robotKnowsPath.knownWorld[2][i] = i !in range
        robot.realWorld[2][i] = i !in range
        twoSensors.realWorld[2][i] = i !in range
    }

    robotKnowsPath.calculateStarPath()
    robot.calculateStarPath()
    twoSensors.calculateStarPath()
    println("${robotKnowsPath.position} : ${robotKnowsPath.path}")
    robotKnowsPath.travel()
    println("-----------------------------------")
    println("${robot.position} : ${robot.path}")
    robot.travel(false)
    println("-----------------------------------")
    twoSensors.travel(false)
    println("Knowing robot : ${robotKnowsPath.distanceTravelled},${robotKnowsPath.turnsCounter},${robotKnowsPath.scansCounter}, ${robotKnowsPath.pathTravelled}")
    println("Exploring robot(NORTH x 1) : ${robot.distanceTravelled},${robot.turnsCounter},${robot.scansCounter}, ${robot.pathTravelled}")
    println("Exploring robot(NORTH x 3) : ${twoSensors.distanceTravelled},${twoSensors.turnsCounter},${twoSensors.scansCounter}, ${twoSensors.pathTravelled}")

}
//turn, detect, move, detect
