import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONException;

public class Main{
	public static final boolean AM = false;
	public static final boolean PM = true;
	public static void main(String[] args) throws IOException, JSONException{
		// SETTINGS
		// BOTS
		Bot myBot = new Bot("mybot");
		Bot starterBot = new Bot("otherbots/StarterBot");
		Bot V61 = new Bot("otherbots/V6");
		Bot V62 = new Bot("otherbots/V6");
		Bot V63 = new Bot("otherbots/V6");
		// BOTS TO TEST
//		BotTeam team = new BotTeam(myBot, V61);
//		ArgumentSet sets = ArgumentSet.createArgumentSet(0, 1, 0.1);
//		System.out.println(sets.args.size());
//		ArgumentGym gym = new ArgumentGym(sets, team);
//		gym.runGamesUntil(new Time(11, 00, PM));
//		gym.printResults();
	}
}
