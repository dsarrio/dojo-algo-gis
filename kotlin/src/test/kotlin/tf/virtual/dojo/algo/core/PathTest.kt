package tf.virtual.dojo.algo.core

import org.junit.jupiter.api.Test
import java.util.Scanner
import kotlin.test.assertEquals

class PathTest {

    @Test
    fun testShortestPaths_1() {
        // [1] -4- [2]
        val input = "2 1 1 2 4 2 1 FACTORY 0 0 0 0 0 2 FACTORY 0 0 0 0 0"
        val game = Game(Scanner(input))
        val state = game.loadTurn()

        val f1 = state.factories.getValue(FactoryId(1))
        val f2 = state.factories.getValue(FactoryId(2))

        assertEquals(f2.id, f1.paths.getValue(f2.id).steps[0].destinationId)
        assertEquals(5, f1.paths.getValue(f2.id).nbTurnRequired)
    }

    @Test
    fun testShortestPaths_2() {
        // [1] -4- [2] -2- [3]
        //    \_____9_____/
        val input = "3 3 1 2 4 2 3 2 1 3 9 3 1 FACTORY 0 0 0 0 0 2 FACTORY 0 0 0 0 0 3 FACTORY 0 0 0 0 0"
        val game = Game(Scanner(input))
        val state = game.loadTurn()

        val f1 = state.factories.getValue(FactoryId(1))
        val f2 = state.factories.getValue(FactoryId(2))
        val f3 = state.factories.getValue(FactoryId(3))

        assertEquals(5, f1.paths.getValue(f2.id).nbTurnRequired)
        assertEquals(3, f2.paths.getValue(f3.id).nbTurnRequired)
        assertEquals(8, f1.paths.getValue(f3.id).nbTurnRequired)
    }

    @Test
    fun testShortestPaths_3() {
        //     _____3_______
        //    /             \
        // [1] -1- [2] --2-- [3]
        //   |       \       /
        //   |        1     3
        //   |         \   /
        //    \__4_____ [4]
        val input = "4 6 1 2 1 2 3 2 2 4 1 3 4 3 1 4 4 1 3 3 4 1 FACTORY 0 0 0 0 0 2 FACTORY 0 0 0 0 0 3 FACTORY 0 0 0 0 0 4 FACTORY 0 0 0 0 0"
        val game = Game(Scanner(input))
        val state = game.loadTurn()

        val f1 = state.factories.getValue(FactoryId(1))
        val f3 = state.factories.getValue(FactoryId(3))
        val f4 = state.factories.getValue(FactoryId(4))

        assertEquals(1, f1.paths.getValue(f3.id).steps.size)
        assertEquals(4, f1.paths.getValue(f3.id).nbTurnRequired)

        assertEquals(2, f1.paths.getValue(f4.id).steps.size)
        assertEquals(4, f1.paths.getValue(f4.id).nbTurnRequired)
    }
}
