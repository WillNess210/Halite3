import hlt.*;

// TODO use ship planner to start dropoffs, choose wall closest to most Halite
// TODO don't always move to the same spot in bottom right, choose random safe spot (use for gym)
// TODO test the following using gym, maybe force 48x48 map for simplicity : numShips
public class MyBot{
	public static void main(final String[] args){
		Game game = new Game();
		game.ready("WillBotV7");
		Log.logln("Successfully created bot! My Player ID is " + game.myId);
		Log.logVar("Map Dimensions", game.gameMap.width + " x " + game.gameMap.height);
		Log.logVarln("# Turns", game.getNumTurns() + "");
		for(int i = 0; i < args.length; i++){
			Log.logVarln("VAR " + i, args[i]);
		}
		// CONSTANTS TO PASS THROUGH
		double ratioToStopSpawningAt = 0.75;
		if(args.length == 1){
			ratioToStopSpawningAt = Double.parseDouble(args[0]);
		}
		while(true){
			game.updateFrame();
			CommandQueue.clear();
			final Player me = game.me;
			me.runTurn(game, ratioToStopSpawningAt);
			game.endTurn();
			Log.logln("");
		}
	}
}
