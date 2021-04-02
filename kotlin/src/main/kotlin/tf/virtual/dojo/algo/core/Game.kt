package tf.virtual.dojo.algo.core

import java.util.Scanner

class Game(private val input: Scanner) {

    private val turns: MutableList<GameStateImpl> = mutableListOf()
    private val links: MutableMap<FactoryId, MutableList<FactoryLinkImpl>> = mutableMapOf()
    private val paths: MutableMap<FactoryId, MutableMap<FactoryId, PathImpl>> = mutableMapOf()

    private fun Int.asOwnerType() = when (this) {
        1 -> OwnerType.Me
        -1 -> OwnerType.Enemy
        else -> OwnerType.Neutral
    }

    fun loadTurn(): GameState {
        val parentState = if (turns.isEmpty()) null else turns.last()
        val gameState = GameStateImpl(parentState, turns.size)

        if (turns.isEmpty()) {
            input.nextInt() // skip factoryCount
            val linkCount = input.nextInt()
            for (i in 0 until linkCount) {
                val factory1Id = FactoryId(input.nextInt())
                val factory2Id = FactoryId(input.nextInt())
                val distance = input.nextInt()
                links.getOrPut(factory1Id, { mutableListOf() }).add(FactoryLinkImpl(gameState, factory1Id, factory2Id, distance))
                links.getOrPut(factory2Id, { mutableListOf() }).add(FactoryLinkImpl(gameState, factory2Id, factory1Id, distance))
            }

            // compute paths
            links.keys.forEach { sourceId ->
                paths[sourceId] = mutableMapOf()
                links.keys.forEach { destinationId ->
                    if (sourceId != destinationId) {
                        val m = paths.getValue(sourceId)
                        m[destinationId] = computeBestPath(sourceId, destinationId)
                    }
                }
            }
        }

        val entityCount = input.nextInt()
        for (i in 0 until entityCount) {
            val entityId = input.nextInt()
            val entityType = input.next()
            val arg1: Int = input.nextInt()
            val arg2: Int = input.nextInt()
            val arg3: Int = input.nextInt()
            val arg4: Int = input.nextInt()
            val arg5: Int = input.nextInt()

            if (entityType == "FACTORY") {
                val factoryId = FactoryId(entityId)
                val factory = FactoryImpl(
                    gameState,
                    factoryId,
                    arg1.asOwnerType(),
                    nbCyborgs = arg2,
                    production = arg3,
                    nbTurnUntilNextProduction = arg4,
                    links = links.getValue(factoryId).map { it.copy(gameState = gameState) },
                    paths = paths.getValue(factoryId)
                )
                gameState.factories[factory.id] = factory
            }

            if (entityType == "TROOP") {
                val troop = TroopImpl(
                    gameState,
                    TroopId(entityId),
                    arg1.asOwnerType(),
                    sourceId = FactoryId(arg2),
                    destinationId = FactoryId(arg3),
                    nbCyborgs = arg4,
                    nbTurnsUntilDestination = arg5
                )
                gameState.troops[troop.id] = troop
            }

            if (entityType == "BOMB") {
                val bomb = BombImpl(
                    gameState,
                    BombId(entityId),
                    arg1.asOwnerType(),
                    sourceId = FactoryId(arg2),
                    destinationId = if (arg3 >= 0) FactoryId(arg3) else null,
                    nbTurnsUntilDestination = if (arg4 >= 0) arg4 else null
                )
                gameState.bombs[bomb.id] = bomb
            }
        }

        turns.add(gameState)

        return gameState
    }

    private fun computeBestPath(sourceId: FactoryId, destinationId: FactoryId): PathImpl {
        val directLink = links.getValue(sourceId).first { it.destinationId == destinationId } // we want to find a path cheaper than direct one
        val ps = PathStepImpl(destinationId, directLink.distance + 1)
        var result = PathImpl(listOf(ps), ps.nbTurnRequired)

        fun dig(sourceId: FactoryId, steps: List<PathStep>, nbTurnsRequired: Int, visitedLinks: Set<FactoryLinkImpl>) {
            if (sourceId == destinationId) {
                if (nbTurnsRequired < result.nbTurnRequired) {
                    result = PathImpl(steps, nbTurnsRequired)
                } else if (nbTurnsRequired == result.nbTurnRequired && steps.size < result.steps.size) { // paths with more steps are better for an equal cost
                    result = PathImpl(steps, nbTurnsRequired)
                }
            } else if (nbTurnsRequired < result.nbTurnRequired) {
                for (link in links.getValue(sourceId)) {
                    if (link !in visitedLinks) {
                        val step = PathStepImpl(link.destinationId, link.distance + 1 /* turn needed to create the troop */)
                        dig(link.destinationId, steps + step, nbTurnsRequired + step.nbTurnRequired, visitedLinks + link)
                    }
                }
            }
        }

        dig(sourceId, emptyList(), 0, emptySet())
        return result
    }
}
