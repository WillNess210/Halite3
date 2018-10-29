public class Game{
	public BotTeam team;
	public Map map;
	// RULES
	public boolean logs, replays, fixedSize, fixedSeed, resultsAsJson;
	public String replayDirectory;
	public BotResult[] results;
	public Game(BotTeam team){
		this.team = team;
		this.map = new Map();
		this.loadDefault();
	}
	public void loadDefault(){
		this.fixedSize = false;
		this.replays = false;
		this.logs = false;
		this.replayDirectory = "replays/";
		this.resultsAsJson = true;
		this.results = new BotResult[team.numBots];
	}
	public String getRunCommand(){
		String toReturn = "halite.exe ";
		toReturn += this.replays ? "--replay-directory " + this.replayDirectory + " " : "--no-replay ";
		toReturn += !this.logs ? "--no-logs " : "";
		toReturn += this.fixedSize ? "--width " + map.size + " --height " + map.size + " " : "";
		toReturn += this.fixedSeed ? "--seed " + map.seed + " " : "";
		toReturn += this.resultsAsJson ? "--results-as-json " : "";
		toReturn += team.getRunString();
		return toReturn;
	}
	public void printResults(int gameNum){
		System.out.print("GAME " + gameNum + ": ");
		this.printResults();
	}
	public void printResults(){
		for(int i = 0; i < results.length; i++){
			if(i > 0){
				System.out.print(" | ");
			}
			System.out.print("[" + this.team.bots[i].id + "] [" + this.results[i] + "]");
		}
		System.out.print(" | " + this.map.size + " " + this.map.seed);
		System.out.println();
	}
}
