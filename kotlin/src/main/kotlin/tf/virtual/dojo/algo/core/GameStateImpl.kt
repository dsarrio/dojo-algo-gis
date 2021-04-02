package tf.virtual.dojo.algo.core

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class FactoryLinkImpl(
    val gameState: GameState,
    val sourceId: FactoryId,
    val destinationId: FactoryId,
    override val distance: Int
): FactoryLink {
    override val source: Factory get() = gameState.factories.getValue(sourceId)
    override val destination: Factory get() = gameState.factories.getValue(destinationId)
    override fun hashCode(): Int {
        val a = min(sourceId.value, destinationId.value)
        val b = max(sourceId.value, destinationId.value)
        return 31 * a.hashCode() + b.hashCode()
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FactoryLinkImpl) return false
        return (sourceId == other.sourceId && destinationId == other.destinationId)
            || (sourceId == other.destinationId && destinationId == other.sourceId)
    }
}

data class FactoryImpl(
    val gameState: GameState,
    override val id: FactoryId,
    override val ownerType: OwnerType,
    override val nbCyborgs: Int,
    override val production: Int,
    override val nbTurnUntilNextProduction: Int,
    override val links: List<FactoryLink>,
    override val paths: Map<FactoryId, PathImpl>
): Factory {
    override val isEnemy get() = ownerType == OwnerType.Enemy
    override val isNeutral get() = ownerType == OwnerType.Neutral
    override val isMine get() = ownerType == OwnerType.Me

    override fun atTurn(count: Int): FactoryImpl {
        var cf = this
        repeat(count) { turnId ->
            var defendingCyborgs = cf.nbCyborgs
            if (cf.nbTurnUntilNextProduction == 0 && !cf.isNeutral) {
                defendingCyborgs += cf.production
            }
            val nbTurnUntilNextProduction = max(cf.nbTurnUntilNextProduction - 1, 0)

            val arrivedTroops = gameState.troops.values.filter { it.nbTurnsUntilDestination == (turnId + 1) && it.destination == this }
            var myCyborgs = arrivedTroops.filter { it.ownerType == OwnerType.Me }.sumBy { it.nbCyborgs }
            var enemyCyborgs = arrivedTroops.filter { it.ownerType == OwnerType.Enemy }.sumBy { it.nbCyborgs }

            // first external troops are fighting
            val deaths = min(myCyborgs, enemyCyborgs)
            myCyborgs -= deaths
            enemyCyborgs -= deaths

            // remaining fight or join defending
            var newOwner = cf.ownerType
            if (cf.ownerType == OwnerType.Me) {
                defendingCyborgs += myCyborgs - enemyCyborgs
                if (defendingCyborgs < 0) {
                    defendingCyborgs = abs(defendingCyborgs)
                    newOwner = OwnerType.Enemy
                }
            } else if (cf.ownerType == OwnerType.Enemy) {
                defendingCyborgs += enemyCyborgs - myCyborgs
                if (defendingCyborgs < 0) {
                    defendingCyborgs = abs(defendingCyborgs)
                    newOwner = OwnerType.Me
                }
            } else {
                defendingCyborgs -= enemyCyborgs + myCyborgs
                if (defendingCyborgs < 0) {
                    defendingCyborgs = abs(defendingCyborgs)
                    newOwner = if (enemyCyborgs > 0) OwnerType.Enemy else OwnerType.Me
                }
            }

            // resolve bombs
            val myBombs = gameState.bombs.values.filter { it.ownerType == OwnerType.Me && it.nbTurnsUntilDestination == (turnId + 1) && it.destination == this }
            for (myBomb in myBombs) {
                defendingCyborgs /= 2
            }

            cf = cf.copy(
                nbCyborgs = defendingCyborgs,
                ownerType = newOwner,
                nbTurnUntilNextProduction = nbTurnUntilNextProduction
            )
        }
        return cf
    }

    override fun toString() = "Factory[id=$id, prod=$production, owner=$ownerType, cyborgs=$nbCyborgs]"
    override fun hashCode(): Int = id.hashCode()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Factory) return false
        return id == other.id
    }
}

data class TroopImpl(
    val gameState: GameState,
    override val id: TroopId,
    override val ownerType: OwnerType,
    val sourceId: FactoryId,
    val destinationId: FactoryId,
    override val nbCyborgs: Int,
    override val nbTurnsUntilDestination: Int
): Troop {
    override val source: Factory get() = gameState.factories.getValue(sourceId)
    override val destination: Factory get() = gameState.factories.getValue(destinationId)
    override fun toString() = "Troop[id=$id, source=$sourceId, destination=$destinationId owner=$ownerType, cyborgs=$nbCyborgs]"
    override fun hashCode(): Int = id.hashCode()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Troop) return false
        return id == other.id
    }
}

data class BombImpl(
    val gameState: GameState,
    override val id: BombId,
    override val ownerType: OwnerType,
    val sourceId: FactoryId,
    val destinationId: FactoryId?,
    override val nbTurnsUntilDestination: Int?
): Bomb {
    override val source: Factory get() = gameState.factories.getValue(sourceId)
    override val destination: Factory? get() = destinationId?.let { gameState.factories.getValue(it) }
    override val isEnemy get() = ownerType == OwnerType.Enemy
    override val isMine get() = ownerType == OwnerType.Me
    override fun hashCode(): Int = id.hashCode()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Bomb) return false
        return id == other.id
    }
}

class GameStateImpl(
    private val _parentState: GameStateImpl?,
    override val turnId: Int
): GameState {
    override val parentState: GameState get() = _parentState!!
    override val factories: MutableMap<FactoryId, FactoryImpl> = mutableMapOf()
    override val troops: MutableMap<TroopId, TroopImpl> = mutableMapOf()
    override val bombs: MutableMap<BombId, BombImpl> = mutableMapOf()
    override val nextTurn: GameState get() = resolveNextTurn()
    override val enemyFactories get() = this.factories.filter { (_, factory) -> factory.isEnemy }.values
    override val neutralFactories get() = this.factories.filter { (_, factory) -> factory.isNeutral }.values
    override val myFactories get() = this.factories.filter { (_, factory) -> factory.isMine }.values
    override val myBombs get() = this.bombs.filter { (_, bomb) -> bomb.isMine }.values
    override val enemyBombs get() = this.bombs.filter { (_, bomb) -> bomb.isEnemy }.values

    private fun resolveNextTurn(): GameStateImpl {
        val nextState = GameStateImpl(this, turnId + 1)
        nextState.factories.putAll(factories.values.map { it.atTurn(1) }.associateBy { it.id })

        nextState.troops.putAll(
            troops.values
                .filter { it.nbTurnsUntilDestination > 1 }
                .map {
                    it.copy(
                        gameState = nextState,
                        nbTurnsUntilDestination = it.nbTurnsUntilDestination - 1
                    )
                }
                .associateBy { it.id }
        )

        nextState.bombs.putAll(
            bombs.values
                .filter { it.nbTurnsUntilDestination != 1 }
                .map {
                    it.copy(
                        gameState = nextState,
                        nbTurnsUntilDestination = it.nbTurnsUntilDestination?.dec()
                    )
                }
                .associateBy { it.id }
        )

        return nextState
    }
}

class PathImpl(
    override val steps: List<PathStep>,
    override val nbTurnRequired: Int
): Path

class PathStepImpl(
    override val destinationId: FactoryId,
    override val nbTurnRequired: Int
): PathStep
