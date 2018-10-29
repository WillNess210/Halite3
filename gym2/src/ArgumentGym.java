import java.io.IOException;
import org.json.JSONException;

public class ArgumentGym{
	// Assumes team[0] is the tested bot
	public ArgumentSet set;
	public GameRunner[] runners;
	public boolean printGames, printBotDefs;
	public int numGameSets;
	public ArgumentGym(ArgumentSet set, BotTeam team) throws IOException{
		this.set = set;
		runners = new GameRunner[set.args.size()];
		for(int i = 0; i < runners.length; i++){
			Bot botOne = new Bot(team.bots[0].path);
			botOne.addArgument(set.args.get(i).toString());
			if(team.numBots == 1){
				runners[i] = new GameRunner(new BotTeam(botOne));
			}else if(team.numBots == 2){
				Bot botTwo = new Bot(team.bots[1].path);
				runners[i] = new GameRunner(new BotTeam(botOne, botTwo));
			}else{ // 4 bots
				Bot botTwo = new Bot(team.bots[1].path);
				Bot botThree = new Bot(team.bots[2].path);
				Bot botFour = new Bot(team.bots[3].path);
				runners[i] = new GameRunner(new BotTeam(botOne, botTwo, botThree, botFour));
			}
		}
		this.printGames = true;
		this.printBotDefs = false;
		this.numGameSets = 0;
	}
	public void runMatchSet() throws IOException, JSONException{
		this.numGameSets++;
		for(int i = 0; i < runners.length; i++){
			if(this.printGames){
				System.out.print("SET " + this.numGameSets + " ");
			}
			runners[i].runGame(this.printGames, i);
			if(this.printBotDefs){
				System.out.println(runners[i].team.bots[0].getRunString());
			}
		}
	}
	public void runSets(int numGames) throws IOException, JSONException{
		for(int i = 1; i <= numGames; i++){
			this.runMatchSet();
		}
	}
	public void runGamesUntil(Time time) throws IOException, JSONException{
		AlarmRunner alarm = new AlarmRunner(time);
		int numGames = 0;
		while(!alarm.hasFinished()){
			this.runMatchSet();
		}
	}
	public void printResults(){
		System.out.println();
		for(int i = 0; i < this.runners.length; i++){
			System.out.println(set.args.get(i).toString() + " : " + runners[i].team.bots[0]);
		}
	}
}
