import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class DirectionTest {
    @Test
    fun `Ordinals from int values are wrapping`() {
        assertEquals(Direction.NORTH, Direction.fromInt(0))
        assertEquals(Direction.NORTH, Direction.fromInt(8))
        assertEquals(Direction.NORTH_EAST, Direction.fromInt(9))

        assertEquals(Direction.NORTH_EAST, Direction.fromInt(1))
        assertEquals(Direction.EAST, Direction.fromInt(2))
        assertEquals(Direction.SOUTH_EAST, Direction.fromInt(3))
        assertEquals(Direction.SOUTH, Direction.fromInt(4))
        assertEquals(Direction.SOUTH_WEST, Direction.fromInt(5))
        assertEquals(Direction.WEST, Direction.fromInt(6))
        assertEquals(Direction.NORTH_WEST, Direction.fromInt(7))
    }

     @Test
     fun `Adding ordinals gets expected result`(){
         assertEquals(Direction.SOUTH, Direction.fromInt(Direction.NORTH.ordinal +4))
     }
}


