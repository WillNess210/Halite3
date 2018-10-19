// This Java API uses camelCase instead of the snake_case as documented in the API docs.
//     Otherwise the names of methods are consistent.
import hlt.*;
import wln.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class MyBot{
	public static void main(final String[] args){
		final long rngSeed;
		if(args.length > 1){
			rngSeed = Integer.parseInt(args[1]);
		}else{
			rngSeed = System.nanoTime();
		}
		final Random rng = new Random(rngSeed);
		Game game = new Game();
		game.ready("WillNessBotNew");
		CommandQueue.init();
		Log.log("Successfully created bot! My Player ID is " + game.myId + ". Bot rng seed is " + rngSeed + ".");
		for(;;){
			game.updateFrame();
			Player me = game.me;
			GameMap gameMap = game.gameMap;
			// GENERATE A GOOD TUNNEL MAP
			gameMap.minHaliteWall = 1000;
			me.tunnelMap = gameMap.getTunnelView(me);
			for(int i = 900; i >= 100 && gameMap.countTunnelMap(me.tunnelMap) < me.ships.size(); i -= 100) {
				gameMap.minHaliteWall = i;
				me.tunnelMap = gameMap.getTunnelView(me);
			}
			Log.log("minHaliteWall = " + gameMap.minHaliteWall);
			CommandQueue.clear();
			// If an enemy is camping my shipyard, I am okay with suiciding into them
			if(gameMap.at(me.shipyard).ship != null && gameMap.at(me.shipyard).ship.owner.id != me.id.id) {
				gameMap.at(me.shipyard).ship = null;
			}
			// ORGANIZE SHITS BY HALITE PRIORITY
			ArrayList<Ship> shipsSorted = new ArrayList<Ship>();
			for(Ship ship : me.ships.values()) {
				shipsSorted.add(ship);
			}
			// Database.arrayList.sort((o1, o2) -> o1.getStartDate().compareTo(o2.getStartDate()));
			shipsSorted.sort((o1, o2) -> o2.halite - o1.halite);
			for(Ship ship : shipsSorted){
				ship.updateStats(me);
				CommandQueue.add(ship.getCommand(me, gameMap, game, rng));
				ship.logLogString();
			}
			if(game.turnNumber <= 300 && me.halite >= Constants.SHIP_COST && !gameMap.at(me.shipyard).isOccupied() && me.ships.size() < 30){
				CommandQueue.addSpawn(me);
				Log.log("Spawning a ship.");
			}
			game.endTurn();
		}
	}
}
