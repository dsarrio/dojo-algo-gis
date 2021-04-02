import { Game } from "./game";
import { Scanner } from "./lib";
import { FactoryId, OwnerType, TroopId } from "./models";

test('turns processing', () => {
    var scanner = new Scanner();
    var game = new Game(scanner);

    var inputInit = "9\n36\n0 1 2\n0 2 2\n0 3 5\n0 4 5\n0 5 5\n0 6 5\n0 7 5\n0 8 5\n1 2 6\n1 3 2\n1 4 9\n1 5 1\n1 6 9\n1 7 3\n1 8 8\n2 3 9\n2 4 2\n2 5 9\n2 6 1\n2 7 8\n2 8 3\n3 4 12\n3 5 1\n3 6 12\n3 7 2\n3 8 12\n4 5 12\n4 6 1\n4 7 12\n4 8 2\n5 6 12\n5 7 4\n5 8 11\n6 7 11\n6 8 4\n7 8 12\n9\n0 FACTORY 0 0 0 0 0\n1 FACTORY 1 25 0 0 0\n2 FACTORY -1 25 0 0 0\n3 FACTORY 0 6 3 0 0\n4 FACTORY 0 6 3 0 0\n5 FACTORY 0 10 3 0 0\n6 FACTORY 0 10 3 0 0\n7 FACTORY 0 0 0 0 0\n8 FACTORY 0 0 0 0 0";
    scanner.setSource(inputInit)
    var initState = game.loadTurn();

    var inputTurn = "21\n0 FACTORY -1 0 0 0 0\n1 FACTORY 1 0 0 0 0\n2 FACTORY 1 0 0 0 0\n3 FACTORY 1 6 3 0 0\n4 FACTORY -1 3 3 0 0\n5 FACTORY 1 0 3 0 0\n6 FACTORY -1 3 3 0 0\n7 FACTORY -1 3 0 0 0\n8 FACTORY 0 0 0 0 0\n98 TROOP -1 1 6 3 1\n104 TROOP -1 3 2 6 3\n118 TROOP -1 0 3 6 2\n119 TROOP -1 4 3 3 9\n120 TROOP -1 6 3 3 9\n123 TROOP -1 6 3 3 10\n124 TROOP -1 4 3 3 10\n125 TROOP -1 7 2 3 7\n127 TROOP -1 4 2 3 1\n128 TROOP -1 6 2 3 1\n129 TROOP -1 7 3 3 2\n130 TROOP -1 4 2 3 2";
    scanner.setSource(inputTurn)
    var turnState = game.loadTurn();
    var nextTurnState = turnState.nextTurn;

    //#region region init checks

    expect(initState.turnId).toBe(0);
    expect(initState.factories.length).toBe(9);
    expect(initState.troops.length).toBe(0);

    // 0 FACTORY 0 0 0 0 0
    var f = initState.getFactory(new FactoryId(0));
    expect(f.links.length).toBe(8);
    expect(f.ownerType).toBe(OwnerType.Neutral);
    expect(f.nbCyborgs).toBe(0);
    expect(f.production).toBe(0);
    expect(f.nbTurnUntilNextProduction).toBe(0);

    // 1 FACTORY 1 25 0 0 0
    f = initState.getFactory(new FactoryId(1));
    expect(f.links.length).toBe(8);
    expect(f.ownerType).toBe(OwnerType.Me);
    expect(f.nbCyborgs).toBe(25);
    expect(f.production).toBe(0);
    expect(f.nbTurnUntilNextProduction).toBe(0);

    // 2 FACTORY -1 25 0 0 0
    f = initState.getFactory(new FactoryId(2));
    expect(f.links.length).toBe(8);
    expect(f.ownerType).toBe(OwnerType.Enemy);
    expect(f.nbCyborgs).toBe(25);
    expect(f.production).toBe(0);
    expect(f.nbTurnUntilNextProduction).toBe(0);

    // 3 FACTORY 0 6 3 0 0
    f = initState.getFactory(new FactoryId(3));
    expect(f.links.length).toBe(8);
    expect(f.ownerType).toBe(OwnerType.Neutral);
    expect(f.nbCyborgs).toBe(6);
    expect(f.production).toBe(3);
    expect(f.nbTurnUntilNextProduction).toBe(0);

    // 4 FACTORY 0 6 3 0 0
    f = initState.getFactory(new FactoryId(4));
    expect(f.links.length).toBe(8);
    expect(f.ownerType).toBe(OwnerType.Neutral);
    expect(f.nbCyborgs).toBe(6);
    expect(f.production).toBe(3);
    expect(f.nbTurnUntilNextProduction).toBe(0);

    // 5 FACTORY 0 10 3 0 0
    f = initState.getFactory(new FactoryId(5));
    expect(f.links.length).toBe(8);
    expect(f.ownerType).toBe(OwnerType.Neutral);
    expect(f.nbCyborgs).toBe(10);
    expect(f.production).toBe(3);
    expect(f.nbTurnUntilNextProduction).toBe(0);

    // 6 FACTORY 0 10 3 0 0
    f = initState.getFactory(new FactoryId(6));
    expect(f.links.length).toBe(8);
    expect(f.ownerType).toBe(OwnerType.Neutral);
    expect(f.nbCyborgs).toBe(10);
    expect(f.production).toBe(3);
    expect(f.nbTurnUntilNextProduction).toBe(0);

    // 7 FACTORY 0 0 0 0 0
    f = initState.getFactory(new FactoryId(7));
    expect(f.links.length).toBe(8);
    expect(f.ownerType).toBe(OwnerType.Neutral);
    expect(f.nbCyborgs).toBe(0);
    expect(f.production).toBe(0);
    expect(f.nbTurnUntilNextProduction).toBe(0);

    // 8 FACTORY 0 0 0 0 0"
    f = initState.getFactory(new FactoryId(8));
    expect(f.links.length).toBe(8);
    expect(f.ownerType).toBe(OwnerType.Neutral);
    expect(f.nbCyborgs).toBe(0);
    expect(f.production).toBe(0);
    expect(f.nbTurnUntilNextProduction).toBe(0);

    //#endregion

    //#region turn checks

    expect(turnState.turnId).toBe(1);
    expect(turnState.factories.length).toBe(9);
    expect(turnState.troops.length).toBe(12);

    // 0 FACTORY -1 0 0 0 0
    f = turnState.getFactory(new FactoryId(0));
    expect(f.links.length).toBe(8);
    expect(f.ownerType).toBe(OwnerType.Enemy);
    expect(f.nbCyborgs).toBe(0);
    expect(f.production).toBe(0);
    expect(f.nbTurnUntilNextProduction).toBe(0);

    // 1 FACTORY 1 0 0 0 0
    f = turnState.getFactory(new FactoryId(1));
    expect(f.links.length).toBe(8);
    expect(f.ownerType).toBe(OwnerType.Me);
    expect(f.nbCyborgs).toBe(0);
    expect(f.production).toBe(0);
    expect(f.nbTurnUntilNextProduction).toBe(0);

    // 2 FACTORY 1 0 0 0 0
    f = turnState.getFactory(new FactoryId(2));
    expect(f.links.length).toBe(8);
    expect(f.ownerType).toBe(OwnerType.Me);
    expect(f.nbCyborgs).toBe(0);
    expect(f.production).toBe(0);
    expect(f.nbTurnUntilNextProduction).toBe(0);

    // 3 FACTORY 1 6 3 0 0
    f = turnState.getFactory(new FactoryId(3));
    expect(f.links.length).toBe(8);
    expect(f.ownerType).toBe(OwnerType.Me);
    expect(f.nbCyborgs).toBe(6);
    expect(f.production).toBe(3);
    expect(f.nbTurnUntilNextProduction).toBe(0);

    // 4 FACTORY -1 3 3 0 0
    f = turnState.getFactory(new FactoryId(4));
    expect(f.links.length).toBe(8);
    expect(f.ownerType).toBe(OwnerType.Enemy);
    expect(f.nbCyborgs).toBe(3);
    expect(f.production).toBe(3);
    expect(f.nbTurnUntilNextProduction).toBe(0);

    // 5 FACTORY 1 0 3 0 0
    f = turnState.getFactory(new FactoryId(5));
    expect(f.links.length).toBe(8);
    expect(f.ownerType).toBe(OwnerType.Me);
    expect(f.nbCyborgs).toBe(0);
    expect(f.production).toBe(3);
    expect(f.nbTurnUntilNextProduction).toBe(0);

    // 6 FACTORY -1 3 3 0 0
    f = turnState.getFactory(new FactoryId(6));
    expect(f.links.length).toBe(8);
    expect(f.ownerType).toBe(OwnerType.Enemy);
    expect(f.nbCyborgs).toBe(3);
    expect(f.production).toBe(3);
    expect(f.nbTurnUntilNextProduction).toBe(0);

    // 7 FACTORY -1 3 0 0 0
    f = turnState.getFactory(new FactoryId(7));
    expect(f.links.length).toBe(8);
    expect(f.ownerType).toBe(OwnerType.Enemy);
    expect(f.nbCyborgs).toBe(3);
    expect(f.production).toBe(0);
    expect(f.nbTurnUntilNextProduction).toBe(0);

    // 8 FACTORY 0 0 0 0 0
    f = turnState.getFactory(new FactoryId(8));
    expect(f.links.length).toBe(8);
    expect(f.ownerType).toBe(OwnerType.Neutral);
    expect(f.nbCyborgs).toBe(0);
    expect(f.production).toBe(0);
    expect(f.nbTurnUntilNextProduction).toBe(0);

    // 98 TROOP -1 1 6 3 1
    var t = turnState.getTroop(new TroopId(98));
    expect(t.ownerType).toBe(OwnerType.Enemy);
    expect(t.source.id.value).toBe(1);
    expect(t.destination.id.value).toBe(6);
    expect(t.nbCyborgs).toBe(3);
    expect(t.nbTurnsUntilDestination).toBe(1);

    // 104 TROOP -1 3 2 6 3
    t = turnState.getTroop(new TroopId(104));
    expect(t.ownerType).toBe(OwnerType.Enemy);
    expect(t.source.id.value).toBe(3);
    expect(t.destination.id.value).toBe(2);
    expect(t.nbCyborgs).toBe(6);
    expect(t.nbTurnsUntilDestination).toBe(3);

    // 118 TROOP -1 0 3 6 2
    t = turnState.getTroop(new TroopId(118));
    expect(t.ownerType).toBe(OwnerType.Enemy);
    expect(t.source.id.value).toBe(0);
    expect(t.destination.id.value).toBe(3);
    expect(t.nbCyborgs).toBe(6);
    expect(t.nbTurnsUntilDestination).toBe(2);

    // 119 TROOP -1 4 3 3 9
    t = turnState.getTroop(new TroopId(119));
    expect(t.ownerType).toBe(OwnerType.Enemy);
    expect(t.source.id.value).toBe(4);
    expect(t.destination.id.value).toBe(3);
    expect(t.nbCyborgs).toBe(3);
    expect(t.nbTurnsUntilDestination).toBe(9);

    // 120 TROOP -1 6 3 3 9
    t = turnState.getTroop(new TroopId(120));
    expect(t.ownerType).toBe(OwnerType.Enemy);
    expect(t.source.id.value).toBe(6);
    expect(t.destination.id.value).toBe(3);
    expect(t.nbCyborgs).toBe(3);
    expect(t.nbTurnsUntilDestination).toBe(9);

    // 123 TROOP -1 6 3 3 10
    t = turnState.getTroop(new TroopId(123));
    expect(t.ownerType).toBe(OwnerType.Enemy);
    expect(t.source.id.value).toBe(6);
    expect(t.destination.id.value).toBe(3);
    expect(t.nbCyborgs).toBe(3);
    expect(t.nbTurnsUntilDestination).toBe(10);

    // 124 TROOP -1 4 3 3 10
    t = turnState.getTroop(new TroopId(124));
    expect(t.ownerType).toBe(OwnerType.Enemy);
    expect(t.source.id.value).toBe(4);
    expect(t.destination.id.value).toBe(3);
    expect(t.nbCyborgs).toBe(3);
    expect(t.nbTurnsUntilDestination).toBe(10);

    // 125 TROOP -1 7 2 3 7
    t = turnState.getTroop(new TroopId(125));
    expect(t.ownerType).toBe(OwnerType.Enemy);
    expect(t.source.id.value).toBe(7);
    expect(t.destination.id.value).toBe(2);
    expect(t.nbCyborgs).toBe(3);
    expect(t.nbTurnsUntilDestination).toBe(7);

    // 127 TROOP -1 4 2 3 1
    t = turnState.getTroop(new TroopId(127));
    expect(t.ownerType).toBe(OwnerType.Enemy);
    expect(t.source.id.value).toBe(4);
    expect(t.destination.id.value).toBe(2);
    expect(t.nbCyborgs).toBe(3);
    expect(t.nbTurnsUntilDestination).toBe(1);

    // 128 TROOP -1 6 2 3 1
    t = turnState.getTroop(new TroopId(128));
    expect(t.ownerType).toBe(OwnerType.Enemy);
    expect(t.source.id.value).toBe(6);
    expect(t.destination.id.value).toBe(2);
    expect(t.nbCyborgs).toBe(3);
    expect(t.nbTurnsUntilDestination).toBe(1);

    // 129 TROOP -1 7 3 3 2
    t = turnState.getTroop(new TroopId(129));
    expect(t.ownerType).toBe(OwnerType.Enemy);
    expect(t.source.id.value).toBe(7);
    expect(t.destination.id.value).toBe(3);
    expect(t.nbCyborgs).toBe(3);
    expect(t.nbTurnsUntilDestination).toBe(2);

    // 130 TROOP -1 4 2 3 2
    t = turnState.getTroop(new TroopId(130));
    expect(t.ownerType).toBe(OwnerType.Enemy);
    expect(t.source.id.value).toBe(4);
    expect(t.destination.id.value).toBe(2);
    expect(t.nbCyborgs).toBe(3);
    expect(t.nbTurnsUntilDestination).toBe(2);

    //#endregion

    //#region next turn checks

    expect(nextTurnState.turnId).toBe(2);
    expect(nextTurnState.factories.length).toBe(9);
    expect(nextTurnState.troops.length).toBe(9);

    // 0 FACTORY -1 0 0 0 0
    f = nextTurnState.getFactory(new FactoryId(0));
    expect(f.links.length).toBe(8);
    expect(f.ownerType).toBe(OwnerType.Enemy);
    expect(f.nbCyborgs).toBe(0);
    expect(f.production).toBe(0);
    expect(f.nbTurnUntilNextProduction).toBe(0);

    // 1 FACTORY 1 0 0 0 0
    f = nextTurnState.getFactory(new FactoryId(1));
    expect(f.links.length).toBe(8);
    expect(f.ownerType).toBe(OwnerType.Me);
    expect(f.nbCyborgs).toBe(0);
    expect(f.production).toBe(0);
    expect(f.nbTurnUntilNextProduction).toBe(0);

    // 2 FACTORY -1 6 0 0 0
    f = nextTurnState.getFactory(new FactoryId(2));
    expect(f.links.length).toBe(8);
    expect(f.ownerType).toBe(OwnerType.Enemy);
    expect(f.nbCyborgs).toBe(6);
    expect(f.production).toBe(0);
    expect(f.nbTurnUntilNextProduction).toBe(0);

    // 3 FACTORY 1 9 3 0 0
    f = nextTurnState.getFactory(new FactoryId(3));
    expect(f.links.length).toBe(8);
    expect(f.ownerType).toBe(OwnerType.Me);
    expect(f.nbCyborgs).toBe(9);
    expect(f.production).toBe(3);
    expect(f.nbTurnUntilNextProduction).toBe(0);

    // 4 FACTORY -1 3 3 0 0
    f = nextTurnState.getFactory(new FactoryId(4));
    expect(f.links.length).toBe(8);
    expect(f.ownerType).toBe(OwnerType.Enemy);
    expect(f.nbCyborgs).toBe(6);
    expect(f.production).toBe(3);
    expect(f.nbTurnUntilNextProduction).toBe(0);

    // 5 FACTORY 1 3 3 0 0
    f = nextTurnState.getFactory(new FactoryId(5));
    expect(f.links.length).toBe(8);
    expect(f.ownerType).toBe(OwnerType.Me);
    expect(f.nbCyborgs).toBe(3);
    expect(f.production).toBe(3);
    expect(f.nbTurnUntilNextProduction).toBe(0);

    // 6 FACTORY -1 6 3 0 0
    f = nextTurnState.getFactory(new FactoryId(6));
    expect(f.links.length).toBe(8);
    expect(f.ownerType).toBe(OwnerType.Enemy);
    expect(f.nbCyborgs).toBe(9);
    expect(f.production).toBe(3);
    expect(f.nbTurnUntilNextProduction).toBe(0);

    // 7 FACTORY -1 0 0 0 0
    f = nextTurnState.getFactory(new FactoryId(7));
    expect(f.links.length).toBe(8);
    expect(f.ownerType).toBe(OwnerType.Enemy);
    expect(f.nbCyborgs).toBe(3);
    expect(f.production).toBe(0);
    expect(f.nbTurnUntilNextProduction).toBe(0);

    // 8 FACTORY 0 0 0 0 0
    f = nextTurnState.getFactory(new FactoryId(8));
    expect(f.links.length).toBe(8);
    expect(f.ownerType).toBe(OwnerType.Neutral);
    expect(f.nbCyborgs).toBe(0);
    expect(f.production).toBe(0);
    expect(f.nbTurnUntilNextProduction).toBe(0);

    // 104 TROOP -1 3 2 6 2
    t = nextTurnState.getTroop(new TroopId(104));
    expect(t.ownerType).toBe(OwnerType.Enemy);
    expect(t.source.id.value).toBe(3);
    expect(t.destination.id.value).toBe(2);
    expect(t.nbCyborgs).toBe(6);
    expect(t.nbTurnsUntilDestination).toBe(2);

    // 118 TROOP -1 0 3 6 1
    t = nextTurnState.getTroop(new TroopId(118));
    expect(t.ownerType).toBe(OwnerType.Enemy);
    expect(t.source.id.value).toBe(0);
    expect(t.destination.id.value).toBe(3);
    expect(t.nbCyborgs).toBe(6);
    expect(t.nbTurnsUntilDestination).toBe(1);

    // 119 TROOP -1 4 3 3 8
    t = nextTurnState.getTroop(new TroopId(119));
    expect(t.ownerType).toBe(OwnerType.Enemy);
    expect(t.source.id.value).toBe(4);
    expect(t.destination.id.value).toBe(3);
    expect(t.nbCyborgs).toBe(3);
    expect(t.nbTurnsUntilDestination).toBe(8);

    // 120 TROOP -1 6 3 3 8
    t = nextTurnState.getTroop(new TroopId(120));
    expect(t.ownerType).toBe(OwnerType.Enemy);
    expect(t.source.id.value).toBe(6);
    expect(t.destination.id.value).toBe(3);
    expect(t.nbCyborgs).toBe(3);
    expect(t.nbTurnsUntilDestination).toBe(8);

    // 123 TROOP -1 6 3 3 9
    t = nextTurnState.getTroop(new TroopId(123));
    expect(t.ownerType).toBe(OwnerType.Enemy);
    expect(t.source.id.value).toBe(6);
    expect(t.destination.id.value).toBe(3);
    expect(t.nbCyborgs).toBe(3);
    expect(t.nbTurnsUntilDestination).toBe(9);

    // 124 TROOP -1 4 3 3 9
    t = nextTurnState.getTroop(new TroopId(124));
    expect(t.ownerType).toBe(OwnerType.Enemy);
    expect(t.source.id.value).toBe(4);
    expect(t.destination.id.value).toBe(3);
    expect(t.nbCyborgs).toBe(3);
    expect(t.nbTurnsUntilDestination).toBe(9);

    // 125 TROOP -1 7 2 3 6
    t = nextTurnState.getTroop(new TroopId(125));
    expect(t.ownerType).toBe(OwnerType.Enemy);
    expect(t.source.id.value).toBe(7);
    expect(t.destination.id.value).toBe(2);
    expect(t.nbCyborgs).toBe(3);
    expect(t.nbTurnsUntilDestination).toBe(6);

    // 129 TROOP -1 7 3 3 1
    t = nextTurnState.getTroop(new TroopId(129));
    expect(t.ownerType).toBe(OwnerType.Enemy);
    expect(t.source.id.value).toBe(7);
    expect(t.destination.id.value).toBe(3);
    expect(t.nbCyborgs).toBe(3);
    expect(t.nbTurnsUntilDestination).toBe(1);

    // 130 TROOP -1 4 2 3 1"
    t = nextTurnState.getTroop(new TroopId(130));
    expect(t.ownerType).toBe(OwnerType.Enemy);
    expect(t.source.id.value).toBe(4);
    expect(t.destination.id.value).toBe(2);
    expect(t.nbCyborgs).toBe(3);
    expect(t.nbTurnsUntilDestination).toBe(1);

    //#endregion
});
