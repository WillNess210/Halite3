// This Java API uses camelCase instead of the snake_case as documented in the API docs.
//     Otherwise the names of methods are consistent.
import hlt.*;
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
		// At this point "game" variable is populated with initial map data.
		// This is a good place to do computationally expensive start-up pre-processing.
		// As soon as you call "ready" function below, the 2 second per turn timer will
		// start.
		game.ready("MyJavaBot");
		Log.log("Successfully created bot! My Player ID is " + game.myId + ". Bot rng seed is " + rngSeed + ".");
		for(;;){
			game.updateFrame();
			Player me = game.me;
			GameMap gameMap = game.gameMap;
			ArrayList<Command> commandQueue = new ArrayList<>();
			for(Ship ship : me.ships.values()){
				ship.turnsSinceDropoff++;
				Log.log(ship.id + ": " + ship.turnsSinceDropoff);
				if(gameMap.at(ship).halite < Constants.MAX_HALITE / 10 || ship.isFull()){
					Position rP = new Position(rng.nextInt(gameMap.width), rng.nextInt(gameMap.height));
					commandQueue.add(ship.move(gameMap.naiveNavigate(ship, rP)));
				}else if(ship.halite > 500) {
					Position rP = me.shipyard.position;
					commandQueue.add(ship.move(gameMap.naiveNavigate(ship, rP)));
				}else{
					commandQueue.add(ship.stayStill());
				}
			}
			if(game.turnNumber <= 200 && me.halite >= Constants.SHIP_COST && !gameMap.at(me.shipyard).isOccupied()){
				commandQueue.add(me.shipyard.spawn());
			}
			game.endTurn(commandQueue);
		}
	}
}
