package wln;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Game{
	Bot[] players;
	boolean logs, replay, fixedSize, resultsAsJson;
	int size, seed;
	String replayDirectory;
	public Game(Bot bot){
		players = new Bot[1];
		players[0] = bot;
		this.loadDefault();
	}
	public Game(Bot bot1, Bot bot2){
		players = new Bot[2];
		players[0] = bot1;
		players[1] = bot2;
		this.loadDefault();
	}
	public Game(Bot bot1, Bot bot2, Bot bot3, Bot bot4){
		players = new Bot[4];
		players[0] = bot1;
		players[1] = bot2;
		players[2] = bot3;
		players[3] = bot4;
		this.loadDefault();
	}
	public void loadDefault(){
		this.resultsAsJson = true;
		this.logs = false;
		this.replay = false;
		this.fixedSize = false;
		this.replayDirectory = "replays/";
		this.size = 48;
		for(int i = 0; i < players.length; i++){
			players[i].id = i;
		}
	}
	public int getNumPlayers(){
		return players.length;
	}
	public void loadGameResults(String json) throws JSONException{
		JSONObject obj = new JSONObject(json);
		this.size = obj.getInt("map_width");
		this.seed = obj.getInt("map_seed");
		JSONObject stats = obj.getJSONObject("stats");
		for(int i = 0; i < this.getNumPlayers(); i++){
			JSONObject pstats = stats.getJSONObject(Integer.toString(i));
			this.players[i].rank = pstats.getInt("rank");
			this.players[i].score = pstats.getInt("score");
		}
	}
	public void printGameResults(){
		String result = "";
		for(int i = 0; i < players.length; i++){
			result += "| " + i + players[i].rank + players[i].score + " |";
		}
		result += " - " + this.size + "x" + this.size;
		System.out.println(result);
	}
	public String getRunCommand(){
		String toReturn = "halite.exe ";
		// REPLAY FLAG
		if(this.replay){
			toReturn += "--replay-directory " + this.replayDirectory + " ";
		}else{
			toReturn += "--no-replay ";
		}
		if(!this.logs){
			toReturn += "--no-logs ";
		}
		if(this.fixedSize){
			toReturn += "--width " + this.size + " --height " + this.size + " ";
		}
		if(this.resultsAsJson){
			toReturn += "--results-as-json ";
		}
		for(Bot bot : this.players){
			toReturn += bot.getCommandLineRunQuotes() + " ";
		}
		return toReturn;
	}
	public void compileBots(MyProcessBuilder builder) throws IOException{
		builder.clearCommands();
		for(Bot bot : this.players){
			builder.addBotCompile(bot);
		}
		builder.run();
	}
}
