import { actions } from './core/actions';
import { Game } from './core/game';
import { Scanner } from './core/lib';

const scanner = new Scanner();
const game = new Game(scanner);

while (true) {
    const current = game.loadTurn();

    for (var source of current.myFactories) {
        if (source.nbCyborgs > 0) {
            const candidates = source.links.enemyOrNeutralFactories;
            if (candidates.length > 0) {
                actions.move(source, candidates.closestFactory.destination, source.nbCyborgs);
                break;
            }
        }
    }

    actions.commit();
}
