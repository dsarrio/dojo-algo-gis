package tf.virtual.dojo.algo.core

import org.junit.jupiter.api.Test
import java.util.Scanner
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class GameTest {

    @Test
    fun testTurnLoading() {
        val inputInit = "9 36 0 1 2 0 2 2 0 3 5 0 4 5 0 5 5 0 6 5 0 7 5 0 8 5 1 2 6 1 3 2 1 4 9 1 5 1 1 6 9 1 7 3 1 8 8 2 3 9 2 4 2 2 5 9 2 6 1 2 7 8 2 8 3 3 4 12 3 5 1 3 6 12 3 7 2 3 8 12 4 5 12 4 6 1 4 7 12 4 8 2 5 6 12 5 7 4 5 8 11 6 7 11 6 8 4 7 8 12 9 0 FACTORY 0 0 0 0 0 1 FACTORY 1 25 0 0 0 2 FACTORY -1 25 0 0 0 3 FACTORY 0 6 3 0 0 4 FACTORY 0 6 3 0 0 5 FACTORY 0 10 3 0 0 6 FACTORY 0 10 3 0 0 7 FACTORY 0 0 0 0 0 8 FACTORY 0 0 0 0 0"
        val inputTurn = "23 0 FACTORY -1 0 0 0 0 1 FACTORY 1 0 0 0 0 2 FACTORY  1 0 0 0 0 3 FACTORY 1 6 3 0 0 4 FACTORY -1 3 3 0 0 5 FACTORY 1 0 3 0 0 6 FACTORY -1 3 3 0 0 7 FACTORY -1 3 0 0 0 8 FACTORY 0 0 0 0 0  98 TROOP -1 1 6 3 1 104 TROOP -1 3 2 6 3 118 TROOP -1 0 3 6 2 119 TROOP -1 4 3 3 9 120 TROOP -1 6 3 3 9 123 TROOP -1 6 3 3 10 124 TROOP -1 4 3 3 10 125 TROOP -1 7 2 3 7 127 TROOP -1 4 2 3 1 128 TROOP -1 6 2 3 1 129 TROOP -1 7 3 3 2 130 TROOP -1 4 2 3 2 1 BOMB 1 5 4 1 0 2 BOMB -1 4 -1 -1 0"
        val game = Game(Scanner("$inputInit $inputTurn"))
        val initState = game.loadTurn()
        val turnState = game.loadTurn()
        val nextTurnState = turnState.nextTurn

        // region init checks

        assertEquals(0, initState.turnId)
        assertEquals(9, initState.factories.size)
        assertEquals(0, initState.troops.size)
        assertEquals(0, initState.bombs.size)

        // 0 FACTORY 0 0 0 0 0
        with(initState.factories.getValue(FactoryId(0))) {
            assertEquals(8, links.size)
            assertEquals(OwnerType.Neutral, ownerType)
            assertEquals(0, nbCyborgs)
            assertEquals(0, production)
            assertEquals(0, nbTurnUntilNextProduction)
        }

        // 1 FACTORY 1 25 0 0 0
        with(initState.factories.getValue(FactoryId(1))) {
            assertEquals(8, links.size)
            assertEquals(OwnerType.Me, ownerType)
            assertEquals(25, nbCyborgs)
            assertEquals(0, production)
            assertEquals(0, nbTurnUntilNextProduction)
        }

        // 2 FACTORY -1 25 0 0 0
        with(initState.factories.getValue(FactoryId(2))) {
            assertEquals(8, links.size)
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(25, nbCyborgs)
            assertEquals(0, production)
            assertEquals(0, nbTurnUntilNextProduction)
        }

        // 3 FACTORY 0 6 3 0 0
        with(initState.factories.getValue(FactoryId(3))) {
            assertEquals(8, links.size)
            assertEquals(OwnerType.Neutral, ownerType)
            assertEquals(6, nbCyborgs)
            assertEquals(3, production)
            assertEquals(0, nbTurnUntilNextProduction)
        }

        // 4 FACTORY 0 6 3 0 0
        with(initState.factories.getValue(FactoryId(4))) {
            assertEquals(8, links.size)
            assertEquals(OwnerType.Neutral, ownerType)
            assertEquals(6, nbCyborgs)
            assertEquals(3, production)
            assertEquals(0, nbTurnUntilNextProduction)
        }

        // 5 FACTORY 0 10 3 0 0
        with(initState.factories.getValue(FactoryId(5))) {
            assertEquals(8, links.size)
            assertEquals(OwnerType.Neutral, ownerType)
            assertEquals(10, nbCyborgs)
            assertEquals(3, production)
            assertEquals(0, nbTurnUntilNextProduction)
        }

        // 6 FACTORY 0 10 3 0 0
        with(initState.factories.getValue(FactoryId(6))) {
            assertEquals(8, links.size)
            assertEquals(OwnerType.Neutral, ownerType)
            assertEquals(10, nbCyborgs)
            assertEquals(3, production)
            assertEquals(0, nbTurnUntilNextProduction)
        }

        // 7 FACTORY 0 0 0 0 0
        with(initState.factories.getValue(FactoryId(7))) {
            assertEquals(8, links.size)
            assertEquals(OwnerType.Neutral, ownerType)
            assertEquals(0, nbCyborgs)
            assertEquals(0, production)
            assertEquals(0, nbTurnUntilNextProduction)
        }

        // 8 FACTORY 0 0 0 0 0"
        with(initState.factories.getValue(FactoryId(8))) {
            assertEquals(8, links.size)
            assertEquals(OwnerType.Neutral, ownerType)
            assertEquals(0, nbCyborgs)
            assertEquals(0, production)
            assertEquals(0, nbTurnUntilNextProduction)
        }

        // endregion

        // region turn checks

        assertEquals(1, turnState.turnId)
        assertEquals(9, turnState.factories.size)
        assertEquals(12, turnState.troops.size)
        assertEquals(2, turnState.bombs.size)

        // 0 FACTORY -1 0 0 0 0
        with(turnState.factories.getValue(FactoryId(0))) {
            assertEquals(8, links.size)
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(0, nbCyborgs)
            assertEquals(0, production)
            assertEquals(0, nbTurnUntilNextProduction)
        }

        // 1 FACTORY 1 0 0 0 0
        with(turnState.factories.getValue(FactoryId(1))) {
            assertEquals(8, links.size)
            assertEquals(OwnerType.Me, ownerType)
            assertEquals(0, nbCyborgs)
            assertEquals(0, production)
            assertEquals(0, nbTurnUntilNextProduction)
        }

        // 2 FACTORY 1 0 0 0 0
        with(turnState.factories.getValue(FactoryId(2))) {
            assertEquals(8, links.size)
            assertEquals(OwnerType.Me, ownerType)
            assertEquals(0, nbCyborgs)
            assertEquals(0, production)
            assertEquals(0, nbTurnUntilNextProduction)
        }

        // 3 FACTORY 1 6 3 0 0
        with(turnState.factories.getValue(FactoryId(3))) {
            assertEquals(8, links.size)
            assertEquals(OwnerType.Me, ownerType)
            assertEquals(6, nbCyborgs)
            assertEquals(3, production)
            assertEquals(0, nbTurnUntilNextProduction)
        }

        // 4 FACTORY -1 3 3 0 0
        with(turnState.factories.getValue(FactoryId(4))) {
            assertEquals(8, links.size)
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(3, nbCyborgs)
            assertEquals(3, production)
            assertEquals(0, nbTurnUntilNextProduction)
        }

        // 5 FACTORY 1 0 3 0 0
        with(turnState.factories.getValue(FactoryId(5))) {
            assertEquals(8, links.size)
            assertEquals(OwnerType.Me, ownerType)
            assertEquals(0, nbCyborgs)
            assertEquals(3, production)
            assertEquals(0, nbTurnUntilNextProduction)
        }

        // 6 FACTORY -1 3 3 0 0
        with(turnState.factories.getValue(FactoryId(6))) {
            assertEquals(8, links.size)
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(3, nbCyborgs)
            assertEquals(3, production)
            assertEquals(0, nbTurnUntilNextProduction)
        }

        // 7 FACTORY -1 3 0 0 0
        with(turnState.factories.getValue(FactoryId(7))) {
            assertEquals(8, links.size)
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(3, nbCyborgs)
            assertEquals(0, production)
            assertEquals(0, nbTurnUntilNextProduction)
        }

        // 8 FACTORY 0 0 0 0 0
        with(turnState.factories.getValue(FactoryId(8))) {
            assertEquals(8, links.size)
            assertEquals(OwnerType.Neutral, ownerType)
            assertEquals(0, nbCyborgs)
            assertEquals(0, production)
            assertEquals(0, nbTurnUntilNextProduction)
        }

        // 98 TROOP -1 1 6 3 1
        with(turnState.troops.getValue(TroopId(98))) {
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(1, source.id.value)
            assertEquals(6, destination.id.value)
            assertEquals(3, nbCyborgs)
            assertEquals(1, nbTurnsUntilDestination)
        }

        // 104 TROOP -1 3 2 6 3
        with(turnState.troops.getValue(TroopId(104))) {
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(3, source.id.value)
            assertEquals(2, destination.id.value)
            assertEquals(6, nbCyborgs)
            assertEquals(3, nbTurnsUntilDestination)
        }

        // 118 TROOP -1 0 3 6 2
        with(turnState.troops.getValue(TroopId(118))) {
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(0, source.id.value)
            assertEquals(3, destination.id.value)
            assertEquals(6, nbCyborgs)
            assertEquals(2, nbTurnsUntilDestination)
        }

        // 119 TROOP -1 4 3 3 9
        with(turnState.troops.getValue(TroopId(119))) {
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(4, source.id.value)
            assertEquals(3, destination.id.value)
            assertEquals(3, nbCyborgs)
            assertEquals(9, nbTurnsUntilDestination)
        }

        // 120 TROOP -1 6 3 3 9
        with(turnState.troops.getValue(TroopId(120))) {
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(6, source.id.value)
            assertEquals(3, destination.id.value)
            assertEquals(3, nbCyborgs)
            assertEquals(9, nbTurnsUntilDestination)
        }

        // 123 TROOP -1 6 3 3 10
        with(turnState.troops.getValue(TroopId(123))) {
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(6, source.id.value)
            assertEquals(3, destination.id.value)
            assertEquals(3, nbCyborgs)
            assertEquals(10, nbTurnsUntilDestination)
        }

        // 124 TROOP -1 4 3 3 10
        with(turnState.troops.getValue(TroopId(124))) {
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(4, source.id.value)
            assertEquals(3, destination.id.value)
            assertEquals(3, nbCyborgs)
            assertEquals(10, nbTurnsUntilDestination)
        }

        // 125 TROOP -1 7 2 3 7
        with(turnState.troops.getValue(TroopId(125))) {
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(7, source.id.value)
            assertEquals(2, destination.id.value)
            assertEquals(3, nbCyborgs)
            assertEquals(7, nbTurnsUntilDestination)
        }

        // 127 TROOP -1 4 2 3 1
        with(turnState.troops.getValue(TroopId(127))) {
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(4, source.id.value)
            assertEquals(2, destination.id.value)
            assertEquals(3, nbCyborgs)
            assertEquals(1, nbTurnsUntilDestination)
        }

        // 128 TROOP -1 6 2 3 1
        with(turnState.troops.getValue(TroopId(128))) {
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(6, source.id.value)
            assertEquals(2, destination.id.value)
            assertEquals(3, nbCyborgs)
            assertEquals(1, nbTurnsUntilDestination)
        }

        // 129 TROOP -1 7 3 3 2
        with(turnState.troops.getValue(TroopId(129))) {
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(7, source.id.value)
            assertEquals(3, destination.id.value)
            assertEquals(3, nbCyborgs)
            assertEquals(2, nbTurnsUntilDestination)
        }

        // 130 TROOP -1 4 2 3 2
        with(turnState.troops.getValue(TroopId(130))) {
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(4, source.id.value)
            assertEquals(2, destination.id.value)
            assertEquals(3, nbCyborgs)
            assertEquals(2, nbTurnsUntilDestination)
        }

        with(turnState.troops.getValue(TroopId(130))) {
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(4, source.id.value)
            assertEquals(2, destination.id.value)
            assertEquals(3, nbCyborgs)
            assertEquals(2, nbTurnsUntilDestination)
        }

        with(turnState.bombs.getValue(BombId(1))) {
            assertEquals(OwnerType.Me, ownerType)
            assertEquals(5, source.id.value)
            assertEquals(4, destination?.id?.value)
            assertEquals(1, nbTurnsUntilDestination)
        }

        with(turnState.bombs.getValue(BombId(2))) {
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(4, source.id.value)
            assertNull(destination)
            assertNull(nbTurnsUntilDestination)
        }

        // endregion

        // region next turn checks

        assertEquals(2, nextTurnState.turnId)
        assertEquals(9, nextTurnState.factories.size)
        assertEquals(9, nextTurnState.troops.size)
        assertEquals(1, nextTurnState.bombs.size)

        // 0 FACTORY -1 0 0 0 0
        with(nextTurnState.factories.getValue(FactoryId(0))) {
            assertEquals(8, links.size)
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(0, nbCyborgs)
            assertEquals(0, production)
            assertEquals(0, nbTurnUntilNextProduction)
        }

        // 1 FACTORY 1 0 0 0 0
        with(nextTurnState.factories.getValue(FactoryId(1))) {
            assertEquals(8, links.size)
            assertEquals(OwnerType.Me, ownerType)
            assertEquals(0, nbCyborgs)
            assertEquals(0, production)
            assertEquals(0, nbTurnUntilNextProduction)
        }

        // 2 FACTORY -1 6 0 0 0
        with(nextTurnState.factories.getValue(FactoryId(2))) {
            assertEquals(8, links.size)
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(6, nbCyborgs)
            assertEquals(0, production)
            assertEquals(0, nbTurnUntilNextProduction)
        }

        // 3 FACTORY 1 9 3 0 0
        with(nextTurnState.factories.getValue(FactoryId(3))) {
            assertEquals(8, links.size)
            assertEquals(OwnerType.Me, ownerType)
            assertEquals(9, nbCyborgs)
            assertEquals(3, production)
            assertEquals(0, nbTurnUntilNextProduction)
        }

        // 4 FACTORY -1 3 3 0 0
        with(nextTurnState.factories.getValue(FactoryId(4))) {
            assertEquals(8, links.size)
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(3, nbCyborgs)
            assertEquals(3, production)
            assertEquals(0, nbTurnUntilNextProduction)
        }

        // 5 FACTORY 1 3 3 0 0
        with(nextTurnState.factories.getValue(FactoryId(5))) {
            assertEquals(8, links.size)
            assertEquals(OwnerType.Me, ownerType)
            assertEquals(3, nbCyborgs)
            assertEquals(3, production)
            assertEquals(0, nbTurnUntilNextProduction)
        }

        // 6 FACTORY -1 6 3 0 0
        with(nextTurnState.factories.getValue(FactoryId(6))) {
            assertEquals(8, links.size)
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(9, nbCyborgs)
            assertEquals(3, production)
            assertEquals(0, nbTurnUntilNextProduction)
        }

        // 7 FACTORY -1 0 0 0 0
        with(nextTurnState.factories.getValue(FactoryId(7))) {
            assertEquals(8, links.size)
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(3, nbCyborgs)
            assertEquals(0, production)
            assertEquals(0, nbTurnUntilNextProduction)
        }

        // 8 FACTORY 0 0 0 0 0
        with(nextTurnState.factories.getValue(FactoryId(8))) {
            assertEquals(8, links.size)
            assertEquals(OwnerType.Neutral, ownerType)
            assertEquals(0, nbCyborgs)
            assertEquals(0, production)
            assertEquals(0, nbTurnUntilNextProduction)
        }

        // 104 TROOP -1 3 2 6 2
        with(nextTurnState.troops.getValue(TroopId(104))) {
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(3, source.id.value)
            assertEquals(2, destination.id.value)
            assertEquals(6, nbCyborgs)
            assertEquals(2, nbTurnsUntilDestination)
        }

        // 118 TROOP -1 0 3 6 1
        with(nextTurnState.troops.getValue(TroopId(118))) {
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(0, source.id.value)
            assertEquals(3, destination.id.value)
            assertEquals(6, nbCyborgs)
            assertEquals(1, nbTurnsUntilDestination)
        }

        // 119 TROOP -1 4 3 3 8
        with(nextTurnState.troops.getValue(TroopId(119))) {
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(4, source.id.value)
            assertEquals(3, destination.id.value)
            assertEquals(3, nbCyborgs)
            assertEquals(8, nbTurnsUntilDestination)
        }

        // 120 TROOP -1 6 3 3 8
        with(nextTurnState.troops.getValue(TroopId(120))) {
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(6, source.id.value)
            assertEquals(3, destination.id.value)
            assertEquals(3, nbCyborgs)
            assertEquals(8, nbTurnsUntilDestination)
        }

        // 123 TROOP -1 6 3 3 9
        with(nextTurnState.troops.getValue(TroopId(123))) {
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(6, source.id.value)
            assertEquals(3, destination.id.value)
            assertEquals(3, nbCyborgs)
            assertEquals(9, nbTurnsUntilDestination)
        }

        // 124 TROOP -1 4 3 3 9
        with(nextTurnState.troops.getValue(TroopId(124))) {
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(4, source.id.value)
            assertEquals(3, destination.id.value)
            assertEquals(3, nbCyborgs)
            assertEquals(9, nbTurnsUntilDestination)
        }

        // 125 TROOP -1 7 2 3 6
        with(nextTurnState.troops.getValue(TroopId(125))) {
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(7, source.id.value)
            assertEquals(2, destination.id.value)
            assertEquals(3, nbCyborgs)
            assertEquals(6, nbTurnsUntilDestination)
        }

        // 129 TROOP -1 7 3 3 1
        with(nextTurnState.troops.getValue(TroopId(129))) {
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(7, source.id.value)
            assertEquals(3, destination.id.value)
            assertEquals(3, nbCyborgs)
            assertEquals(1, nbTurnsUntilDestination)
        }

        // 130 TROOP -1 4 2 3 1"
        with(nextTurnState.troops.getValue(TroopId(130))) {
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(4, source.id.value)
            assertEquals(2, destination.id.value)
            assertEquals(3, nbCyborgs)
            assertEquals(1, nbTurnsUntilDestination)
        }

        with(turnState.bombs.getValue(BombId(2))) {
            assertEquals(OwnerType.Enemy, ownerType)
            assertEquals(4, source.id.value)
            assertNull(destination)
            assertNull(nbTurnsUntilDestination)
        }
        // endregion
    }

    @Test
    fun testFactoryImpl() {
        val state = GameStateImpl(null , 0)
        val factory1 = TestData.randomFactory(state, id = 1)
        val factory2 = TestData.randomFactory(state, id = 2)
        val factory1bis = TestData.randomFactory(state, id = 1)
        assertEquals(factory1, factory1)
        assertEquals(factory1, factory1bis)
        assertNotEquals(factory1, factory2)
        assertFalse(factory1.equals("a string"))
        assertEquals(factory1.hashCode(), factory1bis.hashCode())
        assertTrue(factory1.toString().isNotBlank())
        assertSame(state, factory1.gameState)
    }

    @Test
    fun testFactoryLinkImpl() {
        val state = GameStateImpl(null, 0)
        val source = TestData.randomFactory(state, id = 1)
        val destination = TestData.randomFactory(state, id = 2)
        state.factories[source.id] = source
        state.factories[destination.id] = destination
        val link = FactoryLinkImpl(state, source.id, destination.id, 5)
        assertSame(state, link.gameState)
        assertEquals(destination.id, link.destinationId)
        assertSame(destination, link.destination)
        assertSame(source, link.source)
        assertEquals(link, link)
        assertFalse(link.equals("42"))
        val link2 = FactoryLinkImpl(state, FactoryId(10), destination.id, 5)
        assertNotEquals(link, link2)
        val link3 = FactoryLinkImpl(state, link.destinationId, link.sourceId, 5)
        assertEquals(link, link3)
        assertEquals(link.hashCode(), link3.hashCode())

        // equality
        assertEquals(link, FactoryLinkImpl(state, link.destinationId, link.sourceId, 5))
        assertNotEquals(link, FactoryLinkImpl(state, link.destinationId, FactoryId(10), 5))
        assertEquals(link, FactoryLinkImpl(state, link.sourceId, link.destinationId, 5))
        assertNotEquals(link, FactoryLinkImpl(state, link.sourceId, FactoryId(10), 5))
        assertNotEquals(link, FactoryLinkImpl(state, FactoryId(10), link.sourceId, 5))
        assertNotEquals(link, FactoryLinkImpl(state, FactoryId(10), link.destinationId, 5))
        assertNotEquals(link, FactoryLinkImpl(state, link.sourceId, FactoryId(10), 5))
        assertNotEquals(link, FactoryLinkImpl(state, link.destinationId, FactoryId(10), 5))
    }

    @Test
    fun testTroopImpl() {
        val state = GameStateImpl(null, 0)
        val troop1 = TestData.randomTroop(state, 1, 2, id = 1)
        val troop2 = TestData.randomTroop(state, 1, 2, id = 2)
        val troop1bis = TestData.randomTroop(state, 3, 4, id = 1)
        assertEquals(troop1, troop1)
        assertEquals(troop1, troop1bis)
        assertNotEquals(troop1, troop2)
        assertFalse(troop1.equals("a string"))
        assertEquals(troop1.hashCode(), troop1bis.hashCode())
        assertTrue(troop1.toString().isNotBlank())
        assertSame(state, troop1.gameState)
        assertEquals(FactoryId(1), troop1.sourceId)
        assertEquals(FactoryId(2), troop1.destinationId)
        assertSame(state, troop1.gameState)
        assertEquals(1, troop1.id.value)
    }

    @Test
    fun testBombImpl() {
        val state = GameStateImpl(null, 0)
        val source = TestData.randomFactory(state, id = 1).apply { state.factories[id] = this }
        val destination = TestData.randomFactory(state, id = 2).apply { state.factories[id] = this }
        val bomb1 = BombImpl(state, BombId(1), OwnerType.Me, source.id, destination.id, 5)
        val bomb1bis = BombImpl(state, BombId(1), OwnerType.Me, destination.id, source.id, 7)
        val bomb2 = BombImpl(state, BombId(2), OwnerType.Me, source.id, destination.id, 5)
        assertSame(state, bomb1.gameState)
        assertEquals(BombId(1), bomb1.id)
        assertEquals(source.id, bomb1.sourceId)
        assertEquals(destination.id, bomb1.destinationId)
        assertEquals(1, bomb1.id.value)
        assertEquals(bomb1, bomb1)
        assertEquals(bomb1, bomb1bis)
        assertFalse(bomb1.equals("42"))
        assertEquals(bomb1.hashCode(), bomb1bis.hashCode())
        assertNotEquals(bomb1, bomb2)
        assertNotEquals(bomb1.hashCode(), bomb2.hashCode())
        assertTrue(bomb1.toString().isNotBlank())
    }

    @Test
    fun testNextTurnPausedFactories() { // more local troops than attackers
        // given
        val state = GameStateImpl(null, 0)
        val factory = TestData.randomFactory(state, id = 1, ownerType = OwnerType.Enemy, nbCyborgs = 2, production = 3, nbTurnUntilNextProduction = 1)
        state.factories[factory.id] = factory

        // when
        val nextState = state.nextTurn

        // then
        with(nextState.factories.values.first()) {
            assertEquals(2, nbCyborgs)
            assertEquals(0, nbTurnUntilNextProduction)
        }
    }

    @Test
    fun testNextTurnCaptureFactory_1() { // more local troops than attackers
        // given
        val state = GameStateImpl(null, 0)
        val factories = listOf(
            TestData.randomFactory(state, id = 1, ownerType = OwnerType.Enemy),
            TestData.randomFactory(state, id = 2, ownerType = OwnerType.Me, nbCyborgs = 9, production = 2)
        )
        state.factories.putAll(factories.associateBy { it.id })
        val troops = listOf(
            TestData.randomTroop(state, sourceId = 1, destinationId = 2, nbCyborgs = 10, ownerType = OwnerType.Enemy, nbTurnsUntilDestination = 1),
            TestData.randomTroop(state, sourceId = 1, destinationId = 2, nbCyborgs = 10, ownerType = OwnerType.Me, nbTurnsUntilDestination = 2) // too far
        )
        state.troops.putAll(troops.associateBy { it.id })

        // when
        val nextState = state.nextTurn

        // then
        assertEquals(1, nextState.troops.size)
        with(nextState.factories.getValue(FactoryId(2))) {
            assertTrue(isMine)
            assertEquals(1, nbCyborgs)
        }
        assertEquals(1, nextState.myFactories.size)
        assertEquals(1, nextState.enemyFactories.size)
        assertSame(state, nextState.parentState)
    }

    @Test
    fun testNextTurnCaptureFactory_2() { // not enough local troops
        // given
        val state = GameStateImpl(null, 0)
        val factories = listOf(
            TestData.randomFactory(state, id = 1, ownerType = OwnerType.Enemy),
            TestData.randomFactory(state, id = 2, ownerType = OwnerType.Me, nbCyborgs = 5, production = 2)
        )
        state.factories.putAll(factories.associateBy { it.id })
        val troops = listOf(
            TestData.randomTroop(state, sourceId = 1, destinationId = 2, nbCyborgs = 10, ownerType = OwnerType.Enemy, nbTurnsUntilDestination = 1),
            TestData.randomTroop(state, sourceId = 1, destinationId = 2, nbCyborgs = 10, ownerType = OwnerType.Me, nbTurnsUntilDestination = 2) // too far
        )
        state.troops.putAll(troops.associateBy { it.id })

        // when
        val nextState = state.nextTurn

        // then
        assertEquals(1, nextState.troops.size)
        with(nextState.factories.getValue(FactoryId(2))) {
            assertTrue(isEnemy)
            assertEquals(3, nbCyborgs)
        }
        assertEquals(0, nextState.myFactories.size)
        assertEquals(2, nextState.enemyFactories.size)
    }

    @Test
    fun testNextTurnCaptureFactory_3() { // not enough with local troops + reinforcements
        // given
        val state = GameStateImpl(null, 0)
        val factories = listOf(
            TestData.randomFactory(state, id = 1, ownerType = OwnerType.Enemy),
            TestData.randomFactory(state, id = 2, ownerType = OwnerType.Me, nbCyborgs = 5, production = 2)
        )
        state.factories.putAll(factories.associateBy { it.id })
        val troops = listOf(
            TestData.randomTroop(state, sourceId = 1, destinationId = 2, nbCyborgs = 10, ownerType = OwnerType.Enemy, nbTurnsUntilDestination = 1),
            TestData.randomTroop(state, sourceId = 1, destinationId = 2, nbCyborgs = 2, ownerType = OwnerType.Me, nbTurnsUntilDestination = 1)
        )
        state.troops.putAll(troops.associateBy { it.id })

        // when
        val nextState = state.nextTurn

        // then
        assertTrue(nextState.troops.isEmpty())
        with(nextState.factories.getValue(FactoryId(2))) {
            assertTrue(isEnemy)
            assertEquals(1, nbCyborgs)
        }
        assertEquals(0, nextState.myFactories.size)
        assertEquals(2, nextState.enemyFactories.size)
    }

    @Test
    fun testNextTurnCaptureFactory_4() { // enough with local troops + reinforcements
        // given
        val state = GameStateImpl(null, 0)
        val factories = listOf(
            TestData.randomFactory(state, id = 1, ownerType = OwnerType.Enemy),
            TestData.randomFactory(state, id = 2, ownerType = OwnerType.Me, nbCyborgs = 5, production = 2)
        )
        state.factories.putAll(factories.associateBy { it.id })
        val troops = listOf(
            TestData.randomTroop(state, sourceId = 1, destinationId = 2, nbCyborgs = 10, ownerType = OwnerType.Enemy, nbTurnsUntilDestination = 1),
            TestData.randomTroop(state, sourceId = 1, destinationId = 2, nbCyborgs = 3, ownerType = OwnerType.Me, nbTurnsUntilDestination = 1)
        )
        state.troops.putAll(troops.associateBy { it.id })

        // when
        val nextState = state.nextTurn

        // then
        assertTrue(nextState.troops.isEmpty())
        with(nextState.factories.getValue(FactoryId(2))) {
            assertTrue(isMine)
            assertEquals(0, nbCyborgs)
        }
        assertEquals(1, nextState.myFactories.size)
        assertEquals(1, nextState.enemyFactories.size)
    }

    @Test
    fun testNextTurnCaptureEnemy_1() {
        // given
        val state = GameStateImpl(null, 0)
        val factories = listOf(
            TestData.randomFactory(state, id = 1, ownerType = OwnerType.Enemy, nbCyborgs = 5, production = 2),
            TestData.randomFactory(state, id = 2, ownerType = OwnerType.Me)
        )
        state.factories.putAll(factories.associateBy { it.id })
        val troops = listOf(
            TestData.randomTroop(state, sourceId = 2, destinationId = 1, nbCyborgs = 10, ownerType = OwnerType.Me, nbTurnsUntilDestination = 1)
        )
        state.troops.putAll(troops.associateBy { it.id })

        // when
        val nextState = state.nextTurn

        // then
        assertEquals(0, nextState.troops.size)
        with(nextState.factories.getValue(FactoryId(1))) {
            assertTrue(isMine)
            assertEquals(3, nbCyborgs)
        }
        assertEquals(2, nextState.myFactories.size)
        assertEquals(0, nextState.enemyFactories.size)
    }

    @Test
    fun testNextTurnCaptureNeutral_1() { // Me capturing
        // given
        val state = GameStateImpl(null, 0)
        val factories = listOf(
            TestData.randomFactory(state, id = 1, ownerType = OwnerType.Neutral, nbCyborgs = 5, production = 2),
            TestData.randomFactory(state, id = 2, ownerType = OwnerType.Me)
        )
        state.factories.putAll(factories.associateBy { it.id })
        val troops = listOf(
            TestData.randomTroop(state, sourceId = 2, destinationId = 1, nbCyborgs = 10, ownerType = OwnerType.Me, nbTurnsUntilDestination = 1)
        )
        state.troops.putAll(troops.associateBy { it.id })

        // when
        val nextState = state.nextTurn

        // then
        assertEquals(0, nextState.troops.size)
        with(nextState.factories.getValue(FactoryId(1))) {
            assertTrue(isMine)
            assertEquals(5, nbCyborgs)
        }
        assertEquals(2, nextState.myFactories.size)
        assertEquals(0, nextState.neutralFactories.size)
    }

    @Test
    fun testNextTurnCaptureNeutral_2() { // Enemy capturing
        // given
        val state = GameStateImpl(null, 0)
        val factories = listOf(
            TestData.randomFactory(state, id = 1, ownerType = OwnerType.Neutral, nbCyborgs = 5, production = 2),
            TestData.randomFactory(state, id = 2, ownerType = OwnerType.Enemy)
        )
        state.factories.putAll(factories.associateBy { it.id })
        val troops = listOf(
            TestData.randomTroop(state, sourceId = 2, destinationId = 1, nbCyborgs = 10, ownerType = OwnerType.Enemy, nbTurnsUntilDestination = 1)
        )
        state.troops.putAll(troops.associateBy { it.id })

        // when
        val nextState = state.nextTurn

        // then
        assertEquals(0, nextState.troops.size)
        with(nextState.factories.getValue(FactoryId(1))) {
            assertTrue(isEnemy)
            assertEquals(5, nbCyborgs)
        }
        assertEquals(2, nextState.enemyFactories.size)
        assertEquals(0, nextState.neutralFactories.size)
    }

    @Test
    fun testNextTurnBombLanding_1() {
        // given
        val state = GameStateImpl(null, 0)
        val factories = listOf(
            TestData.randomFactory(state, id = 1, ownerType = OwnerType.Me),
            TestData.randomFactory(state, id = 2, ownerType = OwnerType.Enemy, nbCyborgs = 10, production = 3),
            TestData.randomFactory(state, id = 3, ownerType = OwnerType.Enemy, nbCyborgs = 0, production = 0)
        )
        state.factories.putAll(factories.associateBy { it.id })

        val bombs = listOf(
            BombImpl(state, BombId(1), OwnerType.Me, sourceId = factories[0].id, destinationId = factories[1].id, nbTurnsUntilDestination = 1),
            BombImpl(state, BombId(2), OwnerType.Me, sourceId = factories[0].id, destinationId = factories[2].id, nbTurnsUntilDestination = 1),
            BombImpl(state, BombId(3), OwnerType.Enemy, sourceId = factories[1].id, destinationId = null, nbTurnsUntilDestination = null),
            BombImpl(state, BombId(4), OwnerType.Me, sourceId = factories[0].id, destinationId = factories[1].id, nbTurnsUntilDestination = 10)
        )
        state.bombs.putAll(bombs.associateBy { it.id })

        // when
        val nextState = state.nextTurn

        // then
        assertEquals(2, nextState.bombs.size)
        with(nextState.factories.getValue(FactoryId(2))) {
            assertEquals(6, nbCyborgs)
        }
        with(nextState.factories.getValue(FactoryId(3))) {
            assertEquals(0, nbCyborgs)
        }
        with(nextState.bombs.getValue(BombId(4))) {
            assertEquals(9, nbTurnsUntilDestination)
        }
    }
}
