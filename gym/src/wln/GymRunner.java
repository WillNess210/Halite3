package wln;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import org.json.JSONException;

public class GymRunner{
	public Bot[] bots;
	public ArrayList<String[]> args;
	public int[] gamesPlayed;
	public int[] scores;
	public GymRunner(ArrayList<String[]> args, Bot a){
		this.init(args);
		bots = new Bot[1];
		bots[0] = a;
	}
	public GymRunner(ArrayList<String[]> args, Bot a, Bot b){
		this.init(args);
		bots = new Bot[2];
		bots[0] = a;
		bots[1] = b;
	}
	public GymRunner(ArrayList<String[]> args, Bot a, Bot b, Bot c, Bot d){
		this.init(args);
		bots = new Bot[4];
		bots[0] = a;
		bots[1] = b;
		bots[2] = c;
		bots[3] = d;
	}
	public void init(ArrayList<String[]> args){
		this.args = args;
		gamesPlayed = new int[args.size()];
		scores = new int[args.size()];
	}
	public void runUntil(int hour, int minute) throws IOException, JSONException{
		BooleanTimeAlarm alarm = new BooleanTimeAlarm(hour, minute);
		for(int i = 0; i < args.size(); i++){
			scores[i] = 0;
			gamesPlayed[i] = 0;
		}
		while(!alarm.hasReachedTime()){
			int mapSeed = -1;
			for(int i = 0; i < args.size(); i++){
				bots[0].clearArguments();
				String[] thisargs = args.get(i);
				String argsString = "";
				for(String arg : thisargs){
					bots[0].addArgument(arg);
					argsString += arg + " ";
				}
				Gym myGym = new Gym(bots);
				myGym.game.fixedSize = true;
				myGym.game.size = 48;
				myGym.game.resultsAsJson = true;
				if(i > 0){
					myGym.game.fixedSeed = true;
					myGym.game.seed = mapSeed;
				}
				myGym.runGames(1);
				if(i == 0){
					mapSeed = myGym.game.seed;
				}
				int myScore = myGym.rp.getAverageScores()[0];
				gamesPlayed[i]++;
				scores[i] += myScore;
				System.out.println(argsString + " - " + myScore);
			}
		}
		// 30 35 wins first
		for(int i = 0; i < args.size(); i++){
			for(String arg : args.get(i)){
				System.out.print(arg + " ");
			}
			System.out.print("- " + gamesPlayed[i] + " games - ");
			System.out.println("" + (scores[i] / gamesPlayed[i]));
		}
	}
}
