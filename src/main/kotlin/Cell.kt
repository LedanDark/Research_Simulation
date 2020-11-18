class Cell(val x: Int, val y: Int, var cost: Int = 0, var parent: Cell? = null) {

    override fun equals(other: Any?): Boolean {
        if (other !is Cell) {
            return false
        }
        return other.x == x && other.y == y // && other.cost == cost
    }

    override fun toString(): String {
        return "($x,$y,$cost)"
    }

    fun directionTo(next: Cell): Direction {
        return when {
            next.y == y -> when {
                //east or west
                next.x > x -> Direction.EAST
                else -> Direction.WEST
            }
            next.x == x -> when {
                //north or south
                next.y > y -> Direction.SOUTH
                else -> Direction.NORTH
            }
            next.y < y -> when {
                //north east or west
                next.x > x -> Direction.NORTH_EAST
                else -> Direction.NORTH_WEST
            }
            next.y > y -> when {
                //south east or west
                next.x > x -> Direction.SOUTH_EAST
                else -> Direction.SOUTH_WEST
            }
            else -> {
                println("Error, comparing with self")
                Direction.NORTH
            }
        }
    }
    //North is up, so lower y, south is larger y
    //North is up, so lower x is west and larger x is east
}
