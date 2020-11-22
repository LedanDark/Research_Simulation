import java.util.*
import kotlin.math.abs
import kotlin.math.max

class Robot(
    val sizeOfWorld: Int = 10,
    val goal: Cell = Cell((sizeOfWorld - 1), 0, 0),
    var orientation: Direction = Direction.NORTH,
    val sensorRange: Int = 4,
    val sensors: List<Direction> = listOf(Direction.NORTH)
) {
    var path = mutableListOf<Cell>()
    val pathTravelled = mutableListOf<Cell>()
    val realWorld =
        Array(sizeOfWorld) { y -> BooleanArray(sizeOfWorld) { x -> false } }

    //World of obstacles, true if is blocked
    val knownWorld = Array(sizeOfWorld) { BooleanArray(sizeOfWorld) { false } }

    //Map costs to get to goal
    var costMap =
        Array(sizeOfWorld) { y -> IntArray(sizeOfWorld) { x -> if (x == goal.x && y == goal.y) 0 else Int.MAX_VALUE } }

    // Position 0,0 has a wall on it.
    var position = Cell(0, (sizeOfWorld - 1), 0)
    var distanceTravelled = 0
    var turnsCounter = 0
    var scansCounter = 0
    fun heuristic(cell: Cell): Int = max(
        abs(cell.x - goal.x),
        abs(cell.y - goal.y)
    )

    fun heuristic(x: Int, y: Int): Int = max(
        abs(x - goal.x),
        abs(y - goal.y)
    )

    fun resetCostmap() {
        costMap =
            Array(sizeOfWorld) { y -> IntArray(sizeOfWorld) { x -> if (x == goal.x && y == goal.y) 0 else Int.MAX_VALUE } }
    }

    //A star stuff
    fun calculateStarPath(): List<Cell> {
        costMap[position.y][position.x] = 0
        resetCostmap()
        val openQueue = PriorityQueue<Cell> { a, b -> a.cost - b.cost }
        openQueue.addNeighbours(position)
        while (openQueue.isNotEmpty()) {
            val currentCell = openQueue.poll()
            if (openQueue.addNeighbours(currentCell)) {
                break
            }
        }
        path = backTrackPath()
        println("Calculating shortest path... ->$path")
        return path
    }

    /**
     * Returns true iff one a neighbour is the goal.
     */
    fun PriorityQueue<Cell>.addNeighbours(currentCell: Cell): Boolean {
        for (y in currentCell.y - 1..currentCell.y + 1) {
            for (x in currentCell.x - 1..currentCell.x + 1) {
                if (x == goal.x && y == goal.y) {
                    goal.parent = currentCell
                    return true
                }
                if (!(x == currentCell.x && y == currentCell.y) && knownWorld.isClear(x, y)) {
                    val newCost = currentCell.cost + heuristic(x, y)
                    if (costMap[y][x] > newCost) {
                        costMap[y][x] = newCost
                        this.add(Cell(x, y, newCost, currentCell))
                    }
                }
            }
        }
        return false
    }

    fun Array<BooleanArray>.isClear(x: Int, y: Int): Boolean {
        return this.inBounds(x, y) && !this[y][x]
    }

    fun Array<BooleanArray>.isBlocked(x: Int, y: Int): Boolean {
        return this.inBounds(x, y) && this[y][x]
    }

    fun Array<BooleanArray>.isBlocked(cell: Cell): Boolean {
        return this.inBounds(cell.x, cell.y) && this[cell.y][cell.x]
    }

    fun Array<BooleanArray>.inBounds(x: Int, y: Int): Boolean =
        y < this.size && x >= 0 && x < this[0].size && y >= 0


    fun backTrackPath(): MutableList<Cell> {
        //costmap, go from goal to robot position
        var currentPos: Cell = goal
        val path = mutableListOf<Cell>()
        while (currentPos.parent != null) {
            path.add(0, currentPos)
            currentPos = currentPos.parent!!
        }
        return path
    }

    fun Cell.getNeighbours(): List<Cell> {
        val neighbours = mutableListOf<Cell>()
        (y - 1..y + 1)
            .asSequence()
            .forEach { neighbourY ->
                (x - 1..x + 1)
                    .asSequence()
                    .filter { knownWorld.inBounds(it, neighbourY) && !(it == this.x && neighbourY == this.y) }
                    .filterNot { neighbours.contains(Cell(it, neighbourY, costMap[neighbourY][it])) }
                    .forEach { neighbours.add(Cell(it, neighbourY, costMap[neighbourY][it])) }
            }
        return neighbours
    }

    /** Move and */
    fun update() {
        moveToNextPosition()
    }

    fun travel(printing: Boolean = false) {
        while (path.isNotEmpty()) {
            update()
            if (printing) println("$position : $path")
        }
    }

    /** True on success, false if failed to move or detected new obstacle */
    fun moveToNextPosition() {
        //turn, detect, moveToCell, detect.
        if (path.isEmpty()) {
            return
        }
        var next = path.removeAt(0)
        while (turnAndDetect(next)) {
            resetCostmap()
            calculateStarPath()
            next = path.removeAt(0)
        }
        move(next)// TODO : Need to turn to this direction first....
        if (sensorsDetectsNewBlockage()) {
            resetCostmap()
            calculateStarPath()
        }
    }

    fun turnAndDetect(next: Cell): Boolean {
        val newDirection = position.directionTo(next)
        var newBlockage = false
        if (orientation != newDirection) {
            //Math.floorMod(diff,
            //TODO, IMPLEMENT stepwise turning.
            val clockwiseDistance = Math.floorMod(newDirection.ordinal - orientation.ordinal, Direction.numDirections)
            val turningStep = if (clockwiseDistance <= 4) {
                //println("Turning clockwise, $orientation -> $newDirection")
                1
            } else {
                //println("Turning widdershins, $orientation -> $newDirection")
                -1
            }
            while (orientation != newDirection) {
                orientation = Direction.fromInt(orientation.ordinal + turningStep)
                turnsCounter++
                val stepBlockage = sensorsDetectsNewBlockage()
                //println("New direction : $orientation : $stepBlockage")
                newBlockage = newBlockage || stepBlockage
            }
        }

        return newBlockage
    }

    /** Returns true if position has changed */
    fun move(next: Cell): Boolean {
        if (!knownWorld.isClear(next.x, next.y)) {
            return false
        }

        //Direction, can move only in direction we are facing.
        next.cost = 0
        next.parent = null
        pathTravelled.add(position)
        position = next
        distanceTravelled++
        return true
    }

    //only has sensor on front, and assume distance of 4
    //check each cell in that direction, until we hit a true, then update knownWorld and return true.
    //Otherwise, return false
    fun sensorsDetectsNewBlockage(): Boolean {
        var detectedBlockages = false
        for (sensor in sensors) {
            val sensorOrientation = Direction.fromInt(orientation.ordinal + sensor.ordinal)
            detectedBlockages = detectedBlockages || sensorDetection(sensorOrientation, sensor)
            scansCounter++
        }
        return detectedBlockages
    }

    fun sensorDetection(dir: Direction, sensor: Direction): Boolean {
        for (i in 1..sensorRange) {
            val nextCell = cellAtDistance(i, dir) //Front facing sensor, cell directly in orientation.
            if (realWorld.isBlocked(nextCell.x, nextCell.y)) {
                return if (knownWorld.isClear(nextCell.x, nextCell.y)) {
                    knownWorld[nextCell.y][nextCell.x] = true
                    println("Detected blockage at (${nextCell.x},${nextCell.y}), from ${position.x}, ${position.y}, ${orientation} , sensor = ${sensor}")
                    true
                } else {
                    //no update, and cant see in this direction anmyore.
                    false
                }
            }
        }
        return false
    }

    fun cellAtDistance(i: Int, dir: Direction): Cell = when (dir) {
        //South is down, so positive y
        Direction.NORTH -> Cell(position.x, position.y - i)
        Direction.SOUTH -> Cell(position.x, position.y + i)
        Direction.WEST -> Cell(position.x - i, position.y)
        Direction.EAST -> Cell(position.x + i, position.y)
        Direction.NORTH_WEST -> Cell(position.x - i, position.y - i)
        Direction.NORTH_EAST -> Cell(position.x + i, position.y - i)
        Direction.SOUTH_WEST -> Cell(position.x - i, position.y + i)
        Direction.SOUTH_EAST -> Cell(position.x + i, position.y + i)
    }
}
