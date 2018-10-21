import hlt.*;

// TODO use ship planner to start dropoffs, choose wall closest to most Halite
// TODO early game send bots back once their total halite >= 1000

public class MyBot{
	public static void main(final String[] args){
		final long rngSeed;
		if(args.length > 1){
			rngSeed = Integer.parseInt(args[1]);
		}else{
			rngSeed = System.nanoTime();
		}
		Game game = new Game();
		game.ready("WillBot");
		Log.logln("Successfully created bot! My Player ID is " + game.myId + ". Bot rng seed is " + rngSeed + ".");
		Log.logVar("Map Dimensions", game.gameMap.width + " x " + game.gameMap.height);
		Log.logVarln("# Turns", game.getNumTurns() + "");
		while(true){
			game.updateFrame();
			CommandQueue.clear();
			final Player me = game.me;
			me.runTurn(game);
			game.endTurn();
			Log.logln("");
		}
	}
}
