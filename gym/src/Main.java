import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import org.json.JSONException;
import wln.Bot;
import wln.Game;
import wln.Gym;
import wln.GymRunner;
import wln.MyProcessBuilder;

public class Main{
	public static void main(String[] args) throws IOException, JSONException{
		// BOTS
		Bot myBot = new Bot("mybot");
		Bot starterBot = new Bot("otherbots/StarterBot");
		Bot V6 = new Bot("otherbots/V6");
		// CODE
		/*ArrayList<String[]> params = new ArrayList<String[]>();
		for(double i = 0.05; i < 1.0; i += .05){
			String[] pms = { Double.toString(i) };
			params.add(pms);
		}
		GymRunner myGR = new GymRunner(params, myBot);
		myGR.runUntil(10, 00); */
		myBot.addArgument("0.6");
		Gym myGym = new Gym(myBot, V6);
		myGym.printIndividualGames = true;
		myGym.runGames(100);
	}
}
