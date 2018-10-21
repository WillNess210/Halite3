import hlt.CommandQueue;
import hlt.Game;
import hlt.Log;
import hlt.Player;

public class DumbBot{
	public static void main(final String[] args){
		final long rngSeed;
		if(args.length > 1){
			rngSeed = Integer.parseInt(args[1]);
		}else{
			rngSeed = System.nanoTime();
		}
		Game game = new Game();
		game.ready("DumbBot");
		Log.logln("Successfully created bot! My Player ID is " + game.myId + ". Bot rng seed is " + rngSeed + ".");
		Log.logVar("Map Dimensions", game.gameMap.width + " x " + game.gameMap.height);
		Log.logVarln("# Turns", game.getNumTurns() + "");
		while(true){
			game.updateFrame();
			game.endTurn();
		}
	}
}
