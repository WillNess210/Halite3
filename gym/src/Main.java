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
		ArrayList<String[]> params = new ArrayList<String[]>();
		for(int i = 10; i <= 30; i += 5){
			for(int j = 30; j <= 50; j += 5){
				String[] pms = { Integer.toString(i), Integer.toString(j) };
				params.add(pms);
			}
		}
		GymRunner myGR = new GymRunner(params, myBot);
		myGR.runUntil(8, 15);
	}
}
