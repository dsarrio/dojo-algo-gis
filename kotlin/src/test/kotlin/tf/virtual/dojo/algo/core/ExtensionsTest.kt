package tf.virtual.dojo.algo.core

import org.junit.jupiter.api.Test
import kotlin.reflect.KFunction
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ExtensionsTest {

    @Test
    fun testGameStateOwnerFilters() {
        // given
        val state = GameStateImpl(null, 0)
        val factories = listOf(
            TestData.randomFactory(state, ownerType = OwnerType.Enemy),
            TestData.randomFactory(state, ownerType = OwnerType.Me),
            TestData.randomFactory(state, ownerType = OwnerType.Enemy),
            TestData.randomFactory(state, ownerType = OwnerType.Neutral),
            TestData.randomFactory(state, ownerType = OwnerType.Enemy),
            TestData.randomFactory(state, ownerType = OwnerType.Neutral)
        )
        state.factories.putAll(factories.associateBy { it.id })

        // then
        assertEquals(1, state.myFactories.size)
        assertEquals(2, state.neutralFactories.size)
        assertEquals(3, state.enemyFactories.size)
    }

    @Test
    fun testLinkListOwnerFilters() {
        // given
        val state = GameStateImpl(null, 0)
        val origin = TestData.randomFactory(state, ownerType = OwnerType.Me)
        state.factories[origin.id] = origin
        val factories = listOf(
            TestData.randomFactory(state, ownerType = OwnerType.Enemy),
            TestData.randomFactory(state, ownerType = OwnerType.Me),
            TestData.randomFactory(state, ownerType = OwnerType.Enemy),
            TestData.randomFactory(state, ownerType = OwnerType.Neutral),
            TestData.randomFactory(state, ownerType = OwnerType.Enemy),
            TestData.randomFactory(state, ownerType = OwnerType.Neutral)
        )
        state.factories.putAll(factories.associateBy { it.id })
        val links = factories.map { FactoryLinkImpl(state, origin.id, it.id, 0) }

        // then
        assertEquals(1, links.myFactories.size)
        assertEquals(2, links.neutralFactories.size)
        assertEquals(3, links.enemyFactories.size)
        assertEquals(5, links.enemyOrNeutralFactories.size)
    }

    @Test
    fun testLinkListDistanceFilters() {
        // given
        val state = GameStateImpl(null, 0)
        val origin = TestData.randomFactory(state, ownerType = OwnerType.Me)
        state.factories[origin.id] = origin
        val factories = listOf(
            TestData.randomFactory(state, id = 1, ownerType = OwnerType.Enemy),
            TestData.randomFactory(state, id = 2, ownerType = OwnerType.Me),
            TestData.randomFactory(state, id = 3, ownerType = OwnerType.Enemy),
            TestData.randomFactory(state, id = 4, ownerType = OwnerType.Neutral),
            TestData.randomFactory(state, id = 5, ownerType = OwnerType.Enemy),
            TestData.randomFactory(state, id = 6, ownerType = OwnerType.Neutral)
        )
        state.factories.putAll(factories.associateBy { it.id })
        val links = factories.map { FactoryLinkImpl(state, origin.id, it.id, it.id.value) }

        // then
        assertEquals(1, links.closestFactory.distance)
        assertEquals(1, links.closestFactory.destination.id.value)

        assertEquals(6, links.furthestFactory.distance)
        assertEquals(6, links.furthestFactory.destination.id.value)

        assertEquals(listOf(1, 2, 3, 4, 5, 6), links.orderedByDistanceAsc.map { it.destination.id.value })
        assertEquals(listOf(6, 5, 4, 3, 2, 1), links.orderedByDistanceDesc.map { it.destination.id.value })
    }

    @Test
    fun testFactoryOwnershipHelpers() {
        val state = GameStateImpl(null, 0)
        val mine = TestData.randomFactory(state, ownerType = OwnerType.Me)
        val enemy = TestData.randomFactory(state, ownerType = OwnerType.Enemy)
        val neutral = TestData.randomFactory(state, ownerType = OwnerType.Neutral)

        assertTrue(mine.isMine)
        assertFalse(mine.isEnemy)
        assertFalse(mine.isNeutral)

        assertFalse(enemy.isMine)
        assertTrue(enemy.isEnemy)
        assertFalse(enemy.isNeutral)

        assertFalse(neutral.isMine)
        assertFalse(neutral.isEnemy)
        assertTrue(neutral.isNeutral)
    }

    @Test
    fun testBombOwnershipHelpers() {
        val state = GameStateImpl(null, 0)
        val mine = BombImpl(state, BombId(1), OwnerType.Me, FactoryId(1), FactoryId(2), 1)
        val mine2 = BombImpl(state, BombId(2), OwnerType.Me, FactoryId(1), FactoryId(2), 3)
        val enemy = BombImpl(state, BombId(3), OwnerType.Enemy, FactoryId(1), null, null)
        state.bombs.putAll(listOf(mine, mine2, enemy).associateBy { it.id })

        assertTrue(mine.isMine)
        assertFalse(mine.isEnemy)

        assertFalse(enemy.isMine)
        assertTrue(enemy.isEnemy)

        assertEquals(2, state.myBombs.size)
        assertEquals(1, state.enemyBombs.size)
    }

    @Test
    fun testFactoryAtTurn() {
        val state = GameStateImpl(null, 0)
        val factory = TestData.randomFactory(state, id = 1, ownerType = OwnerType.Neutral, nbCyborgs = 3, production = 2)
        state.factories[factory.id] = factory
        var nextTurn = 1
        fun append(vararg troops: TroopImpl) { state.troops.putAll(troops.associateBy { it.id }) }
        fun atNextTurn(block: FactoryProjection.() -> Unit) = block(factory.atTurn(nextTurn++))

        // --------------------------------------------------------------------
        atNextTurn {
            assertEquals(3, nbCyborgs)
            assertTrue(isNeutral)
        }

        // --------------------------------------------------------------------
        append(
            TestData.randomTroop(state, 2, 1, ownerType = OwnerType.Me, nbCyborgs = 8, nbTurnsUntilDestination = nextTurn)
        )
        atNextTurn {
            assertEquals(5, nbCyborgs)
            assertTrue(isMine)
        }

        // --------------------------------------------------------------------
        append(
            TestData.randomTroop(state, 2, 1, ownerType = OwnerType.Me, nbCyborgs = 1, nbTurnsUntilDestination = nextTurn)
        )
        atNextTurn {
            assertEquals(8, nbCyborgs)
            assertTrue(isMine)
        }

        // --------------------------------------------------------------------
        append(
            TestData.randomTroop(state, 2, 1, ownerType = OwnerType.Enemy, nbCyborgs = 10, nbTurnsUntilDestination = nextTurn)
        )
        atNextTurn {
            assertEquals(0, nbCyborgs)
            assertTrue(isMine)
        }

        // --------------------------------------------------------------------
        append(
            TestData.randomTroop(state, 2, 1, ownerType = OwnerType.Me, nbCyborgs = 100, nbTurnsUntilDestination = nextTurn),
            TestData.randomTroop(state, 2, 1, ownerType = OwnerType.Enemy, nbCyborgs = 99, nbTurnsUntilDestination = nextTurn)
        )
        atNextTurn {
            assertEquals(3, nbCyborgs)
            assertTrue(isMine)
        }

        // --------------------------------------------------------------------
        append(
            TestData.randomTroop(state, 2, 1, ownerType = OwnerType.Me, nbCyborgs = 100, nbTurnsUntilDestination = nextTurn),
            TestData.randomTroop(state, 2, 1, ownerType = OwnerType.Enemy, nbCyborgs = 105, nbTurnsUntilDestination = nextTurn)
        )
        atNextTurn {
            assertEquals(0, nbCyborgs)
            assertTrue(isMine)
        }

        // --------------------------------------------------------------------
        append(
            TestData.randomTroop(state, 2, 1, ownerType = OwnerType.Me, nbCyborgs = 10, nbTurnsUntilDestination = nextTurn),
            TestData.randomTroop(state, 2, 1, ownerType = OwnerType.Enemy, nbCyborgs = 13, nbTurnsUntilDestination = nextTurn)
        )
        atNextTurn {
            assertEquals(1, nbCyborgs)
            assertTrue(isEnemy)
        }

        // --------------------------------------------------------------------
        append(
            TestData.randomTroop(state, 2, 1, ownerType = OwnerType.Enemy, nbCyborgs = 1000, nbTurnsUntilDestination = nextTurn)
        )
        atNextTurn {
            assertEquals(1003, nbCyborgs)
            assertTrue(isEnemy)
        }

        // --------------------------------------------------------------------
        append(
            TestData.randomTroop(state, 2, 1, ownerType = OwnerType.Me, nbCyborgs = 2000, nbTurnsUntilDestination = nextTurn)
        )
        atNextTurn {
            assertEquals(995, nbCyborgs)
            assertTrue(isMine)
        }
    }
}
