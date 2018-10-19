import java.util.Random;
import hlt2.*;

public class StarterBot2{
	public static void main(final String[] args){
		final long rngSeed;
		if(args.length > 1){
			rngSeed = Integer.parseInt(args[1]);
		}else{
			rngSeed = System.nanoTime();
		}
		final Random rng = new Random(rngSeed);
		Game game = new Game();
		game.ready("Starter Bot");
		Log.logln("Successfully created bot! My Player ID is " + game.myId + ". Bot rng seed is " + rngSeed + ".");
		for(;;){
			game.updateFrame();
			CommandQueue.clear();
			final Player me = game.me;
			final GameMap gameMap = game.gameMap;
			for(final Ship ship : me.ships.values()){
				if(gameMap.at(ship).halite < Constants.MAX_HALITE / 10 || ship.isFull()){
					final Direction randomDirection = Direction.ALL_CARDINALS.get(rng.nextInt(4));
					CommandQueue.add(ship.move(randomDirection, gameMap));
				}else{
					CommandQueue.add(ship.stayStill());
				}
			}
			if(game.turnNumber <= 200 && me.halite >= Constants.SHIP_COST && !gameMap.at(me.shipyard).isOccupied()){
				CommandQueue.add(me.shipyard.spawn());
			}
			game.endTurn();
			Log.logln("");
		}
	}
}
