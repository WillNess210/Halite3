import java.io.IOException;
import org.json.JSONException;

public class GameRunner{
	public Game game;
	public BotTeam team;
	public GameRunner(BotTeam team) throws IOException{
		this.team = team;
		this.game = new Game(team);
		this.compileBots();
	}
	public void compileBots() throws IOException{
		CommandLineHandler clh = new CommandLineHandler();
		clh.add_team_compile(this.team);
	}
	public String runGetJSON() throws IOException{
		CommandLineHandler clh = new CommandLineHandler();
		clh.add_game_run(this.game);
		return clh.runAsJson();
	}
	public void runGame() throws IOException, JSONException{
		String json = this.runGetJSON();
		JSONParser.loadResults(this, json);
	}
	public void runGame(boolean print) throws IOException, JSONException{
		this.runGame();
		if(print){
			this.game.printResults();
		}
	}
	public void runGame(boolean print, int num) throws IOException, JSONException{
		this.runGame();
		if(print){
			this.game.printResults(num);
		}
	}
}
