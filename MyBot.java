// This Java API uses camelCase instead of the snake_case as documented in the API docs.
//     Otherwise the names of methods are consistent.
import hlt.*;
import wln.CollisionAvoidance;
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
		game.ready("MyJavaBot");
		CommandQueue.init();
		Log.log("Successfully created bot! My Player ID is " + game.myId + ". Bot rng seed is " + rngSeed + ".");
		for(;;){
			game.updateFrame();
			Player me = game.me;
			GameMap gameMap = game.gameMap;
			CommandQueue.clear();
			CollisionAvoidance.clear();
			for(Ship ship : me.ships.values()){
				ship.updateStats(me);
				ship.log();
				CommandQueue.add(ship.getCommand(me, gameMap, rng));
			}
			gameMap.logTunnelView(me);
			if(game.turnNumber <= 300 && me.halite >= Constants.SHIP_COST && !gameMap.at(me.shipyard).isOccupied()){
				CommandQueue.addSpawn(me);
				Log.log("Spawning a ship.");
			}
			game.endTurn();
		}
	}
}
