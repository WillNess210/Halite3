import hlt2.*;

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
		for(;;){
			game.updateFrame();
			CommandQueue.clear();
			final Player me = game.me;
			me.runTurn(game, game.gameMap);
			game.endTurn();
			Log.logln("");
		}
	}
}
