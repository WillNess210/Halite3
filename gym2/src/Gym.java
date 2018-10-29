import java.io.IOException;
import org.json.JSONException;

public class Gym{
	public GameRunner gr;
	public boolean printGames;
	public int numGames;
	public Gym(GameRunner gr){
		this.gr = gr;
		this.init();
	}
	public Gym(BotTeam team) throws IOException{
		this.gr = new GameRunner(team);
		this.init();
	}
	public void init(){
		this.printGames = true;
		this.numGames = 0;
	}
	public void runGames(int numGames) throws IOException, JSONException{
		for(int i = 1; i <= numGames; i++){
			gr.runGame(printGames, i);
		}
	}
	public void runGamesUntil(Time time) throws IOException, JSONException{
		AlarmRunner alarm = new AlarmRunner(time);
		int numGames = 0;
		while(!alarm.hasFinished()){
			numGames++;
			gr.runGame(printGames, numGames);
		}
	}
	public void printResults(){
		System.out.println();
		for(Bot bot : gr.team.bots){
			System.out.println(bot);
		}
	}
}
