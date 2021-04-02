
export class FactoryId {
    value: number;
    constructor(value: number) { this.value = value }
    toString = (): string => `${this.value}`;
    sameAs = (other: FactoryId): boolean => this.value === other.value ;
}

export class TroopId {
    value: number;
    constructor(value: number) { this.value = value; }
    toString = (): string => `${this.value}`;
    sameAs = (other: TroopId): boolean => this.value === other.value ;
}

export enum OwnerType {
    Me,
    Enemy,
    Neutral,
}

export interface GameState {
    turnId: number
    parentState: GameState
    factories: Array<Factory>
    troops: Array<Troop>
    nextTurn: GameState

    enemyFactories: Array<Factory>
    neutralFactories: Array<Factory>
    myFactories: Array<Factory>

    getFactory(id: FactoryId): Factory
    getTroop(id: TroopId): Troop
}

export interface FactoryLink {
    destination: Factory
    distance: number
}

export interface FactoryLinkList extends Array<FactoryLink> {
    enemyOrNeutralFactories: FactoryLinkList
    enemyFactories: FactoryLinkList
    myFactories: FactoryLinkList
    neutralFactories: FactoryLinkList
    closestFactory: FactoryLink
    furthestFactory: FactoryLink
    orderedByDistanceAsc: FactoryLinkList
    orderedByDistanceDesc: FactoryLinkList
}

export interface Factory extends FactoryProjection {
    id: FactoryId
    ownerType: OwnerType
    nbCyborgs: number
    production: number
    nbTurnUntilNextProduction: number
    links: FactoryLinkList

    atTurn(count: number): FactoryProjection
}

export interface FactoryProjection {
    id: FactoryId
    ownerType: OwnerType
    nbCyborgs: number
    production: number
    nbTurnUntilNextProduction: number
    isEnemy: boolean
    isNeutral: boolean
    isMine: boolean

    sameAs(other: FactoryProjection): boolean
}

export interface Troop {
    id: TroopId
    ownerType: OwnerType
    source: Factory
    destination: Factory
    nbCyborgs: number
    nbTurnsUntilDestination: number

    sameAs(other: Troop): boolean
}
