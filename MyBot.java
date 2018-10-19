import java.util.ArrayList;
import java.util.Random;
import hlt.*;

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
		game.ready("WillBot");
		Log.logln("Successfully created bot! My Player ID is " + game.myId + ". Bot rng seed is " + rngSeed + ".");
		for(;;){
			game.updateFrame();
			final Player me = game.me;
			final GameMap gameMap = game.gameMap;
			final ArrayList<Command> commandQueue = new ArrayList<>();
			for(final Ship ship : me.ships.values()){
				if(gameMap.at(ship).halite < Constants.MAX_HALITE / 10 || ship.isFull()){
					final Direction randomDirection = Direction.ALL_CARDINALS.get(rng.nextInt(4));
					commandQueue.add(ship.move(randomDirection));
				}else{
					commandQueue.add(ship.stayStill());
				}
				ship.log();
			}
			if(game.turnNumber <= 200 && me.halite >= Constants.SHIP_COST && !gameMap.at(me.shipyard).isOccupied()){
				commandQueue.add(me.shipyard.spawn());
			}
			game.endTurn(commandQueue);
			Log.logln("");
		}
	}
}
