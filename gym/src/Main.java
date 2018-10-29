import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import org.json.JSONException;
import wln.BooleanTimeAlarm;
import wln.Bot;
import wln.Game;
import wln.Gym;
import wln.GymRunner;
import wln.MyProcessBuilder;
import wln.ResultsRecorder;

public class Main{
	public static final boolean AM = false;
	public static final boolean PM = true;
	public static void main(String[] args) throws IOException, JSONException{
		// TODO CREATE UML AND REDESIGN
		// BOTS
		Bot myBot = new Bot("mybot");
		Bot starterBot = new Bot("otherbots/StarterBot");
		Bot V6 = new Bot("otherbots/V6");
		// CODE
		ArrayList<String[]> params = new ArrayList<String[]>();
		for(double i = 0.50; i <= 0.95; i += .025){
			String[] pms = { Double.toString(i) };
			params.add(pms);
		}
		//		GymRunner myGR = new GymRunner(params, myBot, V6);
		//		System.out.println(BooleanTimeAlarm.getHour() + ":" + BooleanTimeAlarm.getMinute());
		//		myGR.runUntil(8, 15, AM);
		//		myBot.addArgument("0.6");
		Gym myGym = new Gym(myBot, V6);
		myGym.printIndividualGames = true;
		myGym.runGames(100);
	}
}
