package tf.virtual.dojo.algo

import tf.virtual.dojo.algo.core.Actions
import tf.virtual.dojo.algo.core.Game
import tf.virtual.dojo.algo.core.closestFactory
import tf.virtual.dojo.algo.core.enemyOrNeutralFactories
import java.util.Scanner

val actions = Actions()
val game = Game(Scanner(System.`in`))

fun main() {
    while (true) {
        val current = game.loadTurn()

        for (source in current.myFactories) {
            if (source.nbCyborgs > 0) {
                val candidates = source.links.enemyOrNeutralFactories
                if (candidates.isNotEmpty()) {
                    actions.move(source, candidates.closestFactory.destination, source.nbCyborgs)
                    break;
                }
            }
        }

        actions.commit()
    }
}
