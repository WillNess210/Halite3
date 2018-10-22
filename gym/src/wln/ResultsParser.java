package wln;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResultsParser{
	int totalGames;
	Map<Integer, Integer> wins;
	Map<Integer, Integer> score;
	Map<Integer, Integer> gamesSize;
	Map<Integer, Map<Integer, Integer>> winsSize;
	Map<Integer, Map<Integer, Integer>> scoreSize;
	public ResultsParser(){
		gamesSize = new LinkedHashMap<>();
		winsSize = new LinkedHashMap<>();
		scoreSize = new LinkedHashMap<>();
		wins = new LinkedHashMap<>();
		score = new LinkedHashMap<>();
		totalGames = 0;
	}
	public void setup(Game game){
		int gameSize = game.size;
		if(!gamesSize.containsKey(gameSize)){
			gamesSize.put(gameSize, 0);
			for(Bot bot : game.players){
				winsSize.put(gameSize, new LinkedHashMap<>());
				winsSize.get(gameSize).put(bot.id, 0);
				scoreSize.put(gameSize, new LinkedHashMap<>());
				scoreSize.get(gameSize).put(bot.id, 0);
			}
		}
		for(Bot bot : game.players){
			if(!wins.containsKey(bot.id)){
				wins.put(bot.id, 0);
				score.put(bot.id, 0);
			}
		}
	}
	public void addGameResults(Game game){
		this.totalGames++;
		int gameSize = game.size;
		gamesSize.put(gameSize, gamesSize.get(gameSize) + 1);
		for(Bot bot : game.players){
			if(bot.rank == 1){
				winsSize.get(gameSize).put(bot.id, winsSize.get(gameSize).get(bot.id) + 1);
				wins.put(bot.id, wins.get(bot.id) + 1);
			}
			scoreSize.get(gameSize).put(bot.id, scoreSize.get(gameSize).get(bot.id) + bot.score);
			score.put(bot.id, score.get(bot.id) + bot.score);
		}
	}
	public void printCurrentResults(){
		System.out.println("=== GAMES ===");
		System.out.println("Total Games Played: " + this.totalGames);
		for(int mapSize : gamesSize.keySet()){
			System.out.println(mapSize + "x" + mapSize + ": " + gamesSize.get(mapSize));
		}
		System.out.println("=== TOTAL WINS ===");
		for(int botid : wins.keySet()){
			System.out.println(botid + ": " + wins.get(botid));
		}
		System.out.println("=== AVERAGE SCORE ===");
		for(int botid : score.keySet()){
			System.out.println(botid + ": " + (score.get(botid) / this.totalGames));
		}
		System.out.println("=== WINS ON SIZE ===");
		for(int mapSize : gamesSize.keySet()){
			System.out.print(mapSize + "x" + mapSize + ":");
			Map<Integer, Integer> winsLocal = winsSize.get(mapSize);
			for(int botid : winsLocal.keySet()){
				System.out.print(" " + winsLocal.get(botid));
			}
			System.out.println();
		}
		System.out.println("=== SCORE ON SIZE ===");
		for(int mapSize : scoreSize.keySet()){
			System.out.print(mapSize + "x" + mapSize + ":");
			Map<Integer, Integer> scoresLocal = scoreSize.get(mapSize);
			for(int botid : scoresLocal.keySet()){
				System.out.print(" " + (scoresLocal.get(botid) / gamesSize.get(mapSize)));
			}
			System.out.println();
		}
	}
}
