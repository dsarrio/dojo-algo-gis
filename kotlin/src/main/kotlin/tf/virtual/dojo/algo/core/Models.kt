package tf.virtual.dojo.algo.core

data class FactoryId(val value: Int) {
    override fun toString() = "$value"
}

data class TroopId(val value: Int) {
    override fun toString() = "$value"
}

data class BombId(val value: Int) {
    override fun toString() = "$value"
}

enum class OwnerType {
    Me,
    Enemy,
    Neutral,
}

interface GameState {
    val turnId: Int
    val parentState: GameState
    val factories: Map<FactoryId, Factory>
    val troops: Map<TroopId, Troop>
    val bombs: Map<BombId, Bomb>
    val nextTurn: GameState
    val enemyFactories: Collection<Factory>
    val neutralFactories: Collection<Factory>
    val myFactories: Collection<Factory>
    val myBombs: Collection<Bomb>
    val enemyBombs: Collection<Bomb>
}

interface FactoryLink {
    val source: Factory
    val destination: Factory
    val distance: Int
}

interface Factory: FactoryProjection {
    override val id: FactoryId
    override val ownerType: OwnerType
    override val nbCyborgs: Int
    override val production: Int
    override val nbTurnUntilNextProduction: Int
    fun atTurn(count: Int): FactoryProjection // compute factory state after <count> turns (including incoming troops, battles, production, etc.)
}

interface FactoryProjection {
    val id: FactoryId
    val ownerType: OwnerType
    val nbCyborgs: Int
    val production: Int
    val nbTurnUntilNextProduction: Int
    val isEnemy: Boolean
    val isNeutral: Boolean
    val isMine: Boolean
    val links: List<FactoryLink> // direct hops to other factories
    val paths: Map<FactoryId, Path> // shortest path with as many hops as possible
}

interface Troop {
    val id: TroopId
    val ownerType: OwnerType
    val source: Factory
    val destination: Factory
    val nbCyborgs: Int
    val nbTurnsUntilDestination: Int
}

interface Bomb {
    val id: BombId
    val ownerType: OwnerType
    val source: Factory
    val destination: Factory? // only available if ownerType is Me
    val nbTurnsUntilDestination: Int? // only available if ownerType is Me
    val isEnemy: Boolean
    val isMine: Boolean
}

interface Path {
    val steps: List<PathStep>
    val nbTurnRequired: Int // TOTAL turns needed to reach destination
}

interface PathStep {
    val destinationId: FactoryId
    val nbTurnRequired: Int // turns needed to reach step destination
}
