package tf.virtual.dojo.algo.core

class TestData {
    companion object {
        private var _factoryCount = 0
        val nextFactoryId get() = ++_factoryCount

        fun randomFactory(
            state: GameState,
            id: Int? = null,
            ownerType: OwnerType? = null,
            nbCyborgs: Int? = null,
            production: Int? = null,
            nbTurnUntilNextProduction: Int? = null,
            links: List<FactoryLink>? = null
        ): FactoryImpl {
            return FactoryImpl(
                state,
                id?.let(::FactoryId) ?: FactoryId(nextFactoryId),
                ownerType ?: OwnerType.values().random(),
                nbCyborgs ?: (0..1000).random(),
                production ?: (0..3).random(),
                nbTurnUntilNextProduction ?: 0,
                links ?: emptyList(),
                emptyMap()
            )
        }

        private var _troopCount = 0
        val nextTroopId get() = ++_troopCount

        fun randomTroop(
            gameState: GameState,
            sourceId: Int,
            destinationId: Int,
            id: Int? = null,
            ownerType: OwnerType? = null,
            nbCyborgs: Int? = null,
            nbTurnsUntilDestination: Int? = null
        ): TroopImpl {
            return TroopImpl(
                gameState,
                id?.let(::TroopId) ?: TroopId(nextTroopId),
                ownerType ?: OwnerType.values().random(),
                FactoryId(sourceId),
                FactoryId(destinationId),
                nbCyborgs ?: (1..1000).random(),
                nbTurnsUntilDestination ?: (1..10).random()
            )
        }
    }
}
