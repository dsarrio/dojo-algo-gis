package tf.virtual.dojo.algo.core

// Links
val List<FactoryLink>.enemyOrNeutralFactories get() = this.filter { it.destination.isEnemy || it.destination.isNeutral }

val List<FactoryLink>.enemyFactories get() = this.filter { it.destination.isEnemy }

val List<FactoryLink>.myFactories get() = this.filter { it.destination.isMine }

val List<FactoryLink>.neutralFactories get() = this.filter { it.destination.isNeutral }

val List<FactoryLink>.closestFactory get() = this.minBy { it.distance }!!

val List<FactoryLink>.furthestFactory get() = this.maxBy { it.distance }!!

val List<FactoryLink>.orderedByDistanceAsc get() = this.sortedBy { it.distance }

val List<FactoryLink>.orderedByDistanceDesc get() = this.sortedByDescending { it.distance }
