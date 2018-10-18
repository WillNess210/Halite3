// This Java API uses camelCase instead of the snake_case as documented in the API docs.
//     Otherwise the names of methods are consistent.
import hlt.*;
import wln.CommandQueue;
import java.util.ArrayList;
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
			me.tunnelMap = gameMap.getTunnelView(me);
			CommandQueue.clear();
			// Lower my priority once turn 300 hits
			if(game.turnNumber == 300) {
				gameMap.minHaliteWall = 25;
			}
			// If an enemy is camping my shipyard, I am okay with suiciding into them
			if(gameMap.at(me.shipyard).ship != null && gameMap.at(me.shipyard).ship.owner.id != me.id.id) {
				gameMap.at(me.shipyard).ship = null;
			}
			for(Ship ship : me.ships.values()){
				ship.updateStats(me);
				CommandQueue.add(ship.getCommand(me, gameMap, game, rng));
				ship.logLogString();
			}
			if(game.turnNumber <= 300 && me.halite >= Constants.SHIP_COST && !gameMap.at(me.shipyard).isOccupied() && me.ships.size() < 15){
				CommandQueue.addSpawn(me);
				Log.log("Spawning a ship.");
			}
			game.endTurn();
		}
	}
}
