import { GameStateImpl, FactoryLinkImpl, FactoryImpl, TroopImpl, FactoryLinkListImpl } from "./game_state";
import { Scanner } from "./lib";
import { FactoryId, GameState, OwnerType, TroopId } from "./models";

export class Game { 
    private scanner: Scanner;
    private turns: Array<GameStateImpl> = []
    private links: Map<number, Array<FactoryLinkImpl>> = new Map()

    constructor(scanner: Scanner) {
        this.scanner = scanner;
    }

    private asOwnerType(value: number): OwnerType {
        if (value == 1) return OwnerType.Me
        if (value == -1) return OwnerType.Enemy
        return OwnerType.Neutral
    }

    loadTurn(): GameState {
        var parentState = (this.turns.length == 0) ? null : this.turns[this.turns.length - 1];
        var gameState = new GameStateImpl(parentState, this.turns.length);

        if (this.turns.length == 0) {
            this.scanner.readLine(); // skip factoryCount
            this.scanner.readLine();
            var linkCount = this.scanner.nextInt();

            for (var i = 0; i < linkCount; ++i) {
                this.scanner.readLine();
                var factory1Id = this.scanner.nextInt();
                var factory2Id = this.scanner.nextInt();
                var distance = this.scanner.nextInt();

                const l1 = this.links.get(factory1Id) ?? [];
                l1.push(new FactoryLinkImpl(gameState, new FactoryId(factory2Id), distance));
                this.links.set(factory1Id, l1);

                const l2 = this.links.get(factory2Id) ?? [];
                l2.push(new FactoryLinkImpl(gameState, new FactoryId(factory1Id), distance));
                this.links.set(factory2Id, l2);
            }
        }

        this.scanner.readLine();
        var entityCount = this.scanner.nextInt();
        for (var i = 0; i < entityCount; ++i) {
            this.scanner.readLine();
            var entityId = this.scanner.nextInt();
            var entityType = this.scanner.nextToken();
            var arg1 = this.scanner.nextInt();
            var arg2 = this.scanner.nextInt();
            var arg3 = this.scanner.nextInt();
            var arg4 = this.scanner.nextInt();
            var arg5 = this.scanner.nextInt();

            if (entityType == "FACTORY") {
                var factoryId = new FactoryId(entityId);
                var links = this.links.get(factoryId.value).map((it: FactoryLinkImpl) => it.clone(gameState = gameState));
                var linkList = new FactoryLinkListImpl()
                linkList.push(...links);
                var factory = new FactoryImpl(
                    gameState,
                    factoryId,
                    this.asOwnerType(arg1),
                    arg2,
                    arg3,
                    arg4,
                    linkList
                );
                gameState._factories.set(factory.id.value, factory);
            }

            if (entityType == "TROOP") {
                var troop = new TroopImpl(
                    gameState,
                    new TroopId(entityId),
                    this.asOwnerType(arg1),
                    new FactoryId(arg2),
                    new FactoryId(arg3),
                    arg4,
                    arg5
                );
                gameState._troops.set(troop.id.value, troop);
            }
        }

        this.turns.push(gameState);
        return gameState;
    }
}
