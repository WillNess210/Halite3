import java.io.IOException;
import org.json.JSONException;
import wln.Bot;
import wln.Game;
import wln.Gym;
import wln.MyProcessBuilder;

public class Main{
	public static void main(String[] args) throws IOException, JSONException{
		// BOTS
		Bot myBot = new Bot("mybot");
		Bot starterBot = new Bot("otherbots/StarterBot");
		Bot V6 = new Bot("otherbots/V6");
		// GAME SETUP
		Game game = new Game(myBot, V6);
		// CODE
		Gym myGym = new Gym(myBot, V6);
		myGym.runGames(5);
	}
}
