import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser{
	public static void loadResults(GameRunner gr, String json) throws JSONException{
		JSONObject entireTable = new JSONObject(json);
		gr.game.map.size = entireTable.getInt("map_width");
		gr.game.map.seed = entireTable.getInt("map_seed");
		JSONObject stats = entireTable.getJSONObject("stats");
		for(int i = 0; i < gr.game.team.numBots; i++){
			JSONObject pstats = stats.getJSONObject(Integer.toString(i));
			BotResult br = new BotResult(pstats.getInt("rank"), pstats.getInt("score"));
			gr.team.bots[i].results.add(br); // adding result to bot
			gr.game.results[i] = br; // adding result to game
		}
	}
}
