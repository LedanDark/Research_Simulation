enum class Direction {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST;

    companion object {
        const val numDirections = 8
        fun fromInt(value: Int): Direction {
            return when (value) {
                0 -> NORTH
                1 -> NORTH_EAST
                2 -> EAST
                3 -> SOUTH_EAST
                4 -> SOUTH
                5 -> SOUTH_WEST
                6 -> WEST
                7 -> NORTH_WEST
                else -> fromInt(Math.floorMod(value, numDirections))
            }
        }
    }
}



