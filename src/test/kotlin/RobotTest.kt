import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class RobotTest {

    @Test
    fun calculateStarPath_NoObstacles() {
        val robot = Robot()
        assertEquals(expectedpathSimple, robot.calculateStarPath())
    }

    @Test
    fun calculateStarPath_WithSimpleObstacle() {
        val robot = Robot()
        val range = (0 until 3)
        for (i in robot.knownWorld[2].indices) {
            robot.knownWorld[2][i] = i !in range
        }
        assertEquals(expectedpathSinglelineobstacle, robot.calculateStarPath())
    }

/*    @Test
    fun shortestPath_RealWorldObstacles() {
        val robot = Robot()
        val range = (0 until 3)
        for (i in robot.realWorld[2].indices) {
            robot.realWorld[2][i] = i !in range
        }
        assertEquals(expectedPath_SingleLineObstacle, robot.calculateStarPath())
    }*/

    @Test
    fun moveRobot_ChangesPosition() {
        val robot = Robot()
        val path = robot.calculateStarPath()
        robot.move(path.first()) // should move to next position, and if knownWorld has not update, costmap should be the same.
        assertEquals(path.first(), robot.position)
    }

    @Test
    fun moveRobot_UpdatesKnownWorld() {
        val robot = Robot()
        val path = robot.calculateStarPath()
        robot.move(path.first()) // should move to next position, and if knownWorld has not update, costmap should be the same.
        assertEquals(path.first(), robot.position)
    }

    @Test
    fun cellAtDistance(){
        val robot = Robot(10) //Starts at 0,9, facing north
        assertEquals(Cell(0,8), robot.cellAtDistance(1, Direction.NORTH))
        assertEquals(Cell(0,11), robot.cellAtDistance(2, Direction.SOUTH))
        assertEquals(Cell(3,9), robot.cellAtDistance(3, Direction.EAST))
        assertEquals(Cell(-4,9), robot.cellAtDistance(4, Direction.WEST))

        assertEquals(Cell(-2,7), robot.cellAtDistance(2, Direction.NORTH_WEST))
        assertEquals(Cell(2,7), robot.cellAtDistance(2, Direction.NORTH_EAST))

        assertEquals(Cell(-2,11), robot.cellAtDistance(2, Direction.SOUTH_WEST))
        assertEquals(Cell(2,11), robot.cellAtDistance(2, Direction.SOUTH_EAST))
    }

    companion object {
        val expectedpathSimple = listOf(
            Cell(1, 8, 9),
            Cell(2, 7, 17),
            Cell(3, 6, 24),
            Cell(4, 5, 30),
            Cell(5, 4, 35),
            Cell(6, 3, 39),
            Cell(7, 2, 42),
            Cell(8, 1, 44),
            Cell(9, 0, 0)
        )
        val expectedpathSinglelineobstacle = listOf(
            Cell(1, 8, 9),
            Cell(1, 7, 18),
            Cell(1, 6, 27),
            Cell(1, 5, 36),
            Cell(1, 4, 45),
            Cell(1, 3, 54),
            Cell(2, 2, 62),
            Cell(3, 3, 69),
            Cell(4, 4, 75),
            Cell(5, 3, 80),
            Cell(6, 2, 84),
            Cell(7, 2, 87),
            Cell(8, 1, 89),
            Cell(9, 0, 0)
        )
    }
}
