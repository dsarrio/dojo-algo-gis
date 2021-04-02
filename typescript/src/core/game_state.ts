import { array_maxBy, array_minBy } from "./lib";
import { Factory, FactoryId, FactoryLink, GameState, OwnerType, Troop, TroopId, FactoryLinkList } from "./models";

export class FactoryLinkImpl implements FactoryLink {
    gameState: GameStateImpl;
    destinationId: FactoryId;
    distance: number;

    constructor(gameState: GameStateImpl, destinationId: FactoryId, distance: number) {
        this.gameState = gameState;
        this.destinationId = destinationId;
        this.distance = distance;
    }

    get destination(): FactoryImpl { 
        return this.gameState.getFactory(this.destinationId); 
    }

    clone(newState: GameStateImpl): FactoryLinkImpl {
        return new FactoryLinkImpl(newState, this.destinationId, this.distance);
    }
}

export class FactoryLinkListImpl extends Array<FactoryLinkImpl> implements FactoryLinkList {

    get enemyOrNeutralFactories(): FactoryLinkListImpl {
        const n = new FactoryLinkListImpl()
        n.push(...this.filter(it => it.destination.isEnemy || it.destination.isNeutral));
        return n;
    }

    get enemyFactories(): FactoryLinkListImpl {
        const n = new FactoryLinkListImpl()
        n.push(...this.filter(it => it.destination.isEnemy));
        return n;
    }

    get myFactories(): FactoryLinkListImpl {
        const n = new FactoryLinkListImpl()
        n.push(...this.filter(it => it.destination.isMine));
        return n;
    }

    get neutralFactories(): FactoryLinkListImpl {
        const n = new FactoryLinkListImpl()
        n.push(...this.filter(it => it.destination.isNeutral));
        return n;
    }

    get closestFactory(): FactoryLinkImpl {
        return array_minBy(this, (it => it.distance))
    }

    get furthestFactory(): FactoryLinkImpl {
        return array_maxBy(this, (it => it.distance))
    }

    get orderedByDistanceAsc(): FactoryLinkListImpl {
        const n = new FactoryLinkListImpl()
        n.push(...this.sort((l: FactoryLinkImpl, r: FactoryLinkImpl) => r.distance - l.distance));
        return n;
    }

    get orderedByDistanceDesc(): FactoryLinkListImpl {
        const n = new FactoryLinkListImpl()
        n.push(...this.sort((l: FactoryLinkImpl, r: FactoryLinkImpl) => l.distance - r.distance));
        return n;
    }
}

export class FactoryImpl implements Factory {
    gameState: GameState;
    id: FactoryId;
    ownerType: OwnerType;
    nbCyborgs: number;
    production: number;
    nbTurnUntilNextProduction: number;
    links: FactoryLinkListImpl;

    constructor(
        gameState: GameState,
        id: FactoryId,
        ownerType: OwnerType,
        nbCyborgs: number,
        production: number,
        nbTurnUntilNextProduction: number,
        links: FactoryLinkListImpl
    ) {
        this.gameState = gameState;
        this.id = id;
        this.ownerType = ownerType;
        this.nbCyborgs = nbCyborgs;
        this.production = production;
        this.nbTurnUntilNextProduction = nbTurnUntilNextProduction;
        this.links = links;
    }

    get isEnemy(): boolean { return this.ownerType == OwnerType.Enemy }

    get isNeutral(): boolean { return this.ownerType == OwnerType.Neutral }

    get isMine(): boolean { return this.ownerType == OwnerType.Me }

    atTurn(count: number): FactoryImpl {
        var cf: FactoryImpl = this

        for (var turnId = 0; turnId < count; ++turnId) {
            var defendingCyborgs = cf.nbCyborgs
            if (cf.nbTurnUntilNextProduction == 0 && !cf.isNeutral) {
                defendingCyborgs += cf.production
            }
            var nbTurnUntilNextProduction = Math.max(cf.nbTurnUntilNextProduction - 1, 0)

            var arrivedTroops = this.gameState.troops.filter(it => it.nbTurnsUntilDestination == (turnId + 1) && it.destination.sameAs(this))
            var myCyborgs = arrivedTroops.filter(it => it.ownerType == OwnerType.Me).reduce((prev, curr) => prev + curr.nbCyborgs, 0)
            var enemyCyborgs = arrivedTroops.filter(it => it.ownerType == OwnerType.Enemy).reduce((prev, curr) => prev + curr.nbCyborgs, 0)

            // first external troops are fighting
            var deaths = Math.min(myCyborgs, enemyCyborgs)
            myCyborgs -= deaths
            enemyCyborgs -= deaths

            // remaining fight or join defending
            var newOwner = cf.ownerType
            if (cf.ownerType == OwnerType.Me) {
                defendingCyborgs += myCyborgs - enemyCyborgs
                if (defendingCyborgs < 0) {
                    defendingCyborgs = Math.abs(defendingCyborgs)
                    newOwner = OwnerType.Enemy
                }
            } else if (cf.ownerType == OwnerType.Enemy) {
                defendingCyborgs += enemyCyborgs - myCyborgs
                if (defendingCyborgs < 0) {
                    defendingCyborgs = Math.abs(defendingCyborgs)
                    newOwner = OwnerType.Me
                }
            } else {
                defendingCyborgs -= enemyCyborgs + myCyborgs
                if (defendingCyborgs < 0) {
                    defendingCyborgs = Math.abs(defendingCyborgs)
                    if (enemyCyborgs > 0) {
                        newOwner = OwnerType.Enemy 
                    } else {
                        newOwner = OwnerType.Me
                    }
                }
            }

            cf = new FactoryImpl(
                this.gameState,
                this.id,
                newOwner,
                defendingCyborgs,
                this.production,
                nbTurnUntilNextProduction,
                this.links        
            );
        }
        
        return cf
    }

    toString(): string { return `Factory[id=${this.id}, prod=${this.production}, owner=${this.ownerType}, cyborgs=${this.nbCyborgs}]` }

    sameAs(other: Factory): boolean { return this.id.value === other.id.value }
}

export class TroopImpl implements Troop {
    gameState: GameState;
    id: TroopId;
    ownerType: OwnerType;
    sourceId: FactoryId;
    destinationId: FactoryId;
    nbCyborgs: number;
    nbTurnsUntilDestination: number;

    constructor(
        gameState: GameState,
        id: TroopId,
        ownerType: OwnerType,
        sourceId: FactoryId,
        destinationId: FactoryId,
        nbCyborgs: number,
        nbTurnsUntilDestination: number  
    ) {
        this.gameState = gameState;
        this.id = id;
        this.ownerType = ownerType;
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.nbCyborgs = nbCyborgs;
        this.nbTurnsUntilDestination = nbTurnsUntilDestination;
    }

    get source(): Factory { return this.gameState.getFactory(this.sourceId) }

    get destination(): Factory { return this.gameState.getFactory(this.destinationId) }

    toString():string { return `Troop[id=${this.id}, source=${this.sourceId}, destination=${this.destinationId}, owner=${this.ownerType}, cyborgs=${this.nbCyborgs}]` }

    clone(state: GameState, nbTurnsUntilDestination: number): TroopImpl {
        return new TroopImpl(
            state,
            this.id,
            this.ownerType,
            this.sourceId,
            this.destinationId,
            this.nbCyborgs,
            nbTurnsUntilDestination    
        );
    }

    sameAs(other: Troop): boolean { return this.id.value === other.id.value }
}

export class GameStateImpl implements GameState {
    _parentState: GameStateImpl | null;
    _factories: Map<number, FactoryImpl> = new Map()
    _troops: Map<number, TroopImpl> = new Map()

    turnId: number;

    constructor(parent: GameStateImpl | null, turnId: number) {
        this._parentState = parent;
        this.turnId = turnId;
    }

    get parentState(): GameState { return this.parentState!! }

    get factories(): Array<FactoryImpl> { return Array.from(this._factories.values()); }

    getFactory(id: FactoryId): FactoryImpl { return this._factories.get(id.value)}

    get troops(): Array<TroopImpl> { return Array.from(this._troops.values()) }

    getTroop(id: TroopId): TroopImpl { return this._troops.get(id.value)}

    get nextTurn(): GameState { return this.resolveNextTurn() }

    get enemyFactories(): Array<FactoryImpl> { return this.factories.filter(it => it.isEnemy) }

    get neutralFactories(): Array<FactoryImpl> { return this.factories.filter(it => it.isNeutral) }
    
    get myFactories(): Array<FactoryImpl> { return this.factories.filter(it => it.isMine) }
    
    resolveNextTurn(): GameStateImpl {
        var nextState = new GameStateImpl(this, this.turnId + 1)

        this.factories
            .map(f => f.atTurn(1))
            .forEach(n => nextState._factories.set(n.id.value, n));

        this.troops
            .filter(t => t.nbTurnsUntilDestination > 1)
            .map(t => t.clone(nextState, t.nbTurnsUntilDestination - 1))
            .forEach(n => nextState._troops.set(n.id.value, n));

        return nextState
    }
}
