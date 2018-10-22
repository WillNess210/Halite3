import hlt.*;

// TODO use ship planner to start dropoffs, choose wall closest to most Halite
// TODO don't always move to the same spot in bottom right, choose random safe spot (use for gym)
public class MyBot{
	public static void main(final String[] args){
		final long rngSeed;
		if(args.length > 1){
			rngSeed = Integer.parseInt(args[1]);
		}else{
			rngSeed = System.nanoTime();
		}
		Game game = new Game();
		game.ready("WillBotV7");
		Log.logln("Successfully created bot! My Player ID is " + game.myId + ". Bot rng seed is " + rngSeed + ".");
		Log.logVar("Map Dimensions", game.gameMap.width + " x " + game.gameMap.height);
		Log.logVarln("# Turns", game.getNumTurns() + "");
		for(int i = 0; i < args.length; i++) {
			Log.logVarln("VAR " + i, args[i]);
		}
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
