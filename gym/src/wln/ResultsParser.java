package wln;

public class ResultsParser{
	int totalGames, numBots;
	int[] scores;
	int[] wins;
	int[] numGames;
	int[][] scoresSize;
	int[][] winsSize;
	int[] possibleSizes = { 32, 40, 48, 56, 64 };
	public ResultsParser(int numBots){
		this.numBots = numBots;
		this.totalGames = 0;
		scores = new int[numBots];
		wins = new int[numBots];
		numGames = new int[possibleSizes.length];
		scoresSize = new int[possibleSizes.length][numBots];
		winsSize = new int[possibleSizes.length][numBots];
	}
	public int sizeToIndex(int size){
		return (size - 32) / 8;
	}
	public int indexToSize(int index){
		return (index * 8) + 32;
	}
	public void addGameResults(Game game){
		int gameSize = game.size;
		int gameIndex = this.sizeToIndex(gameSize);
		totalGames++;
		numGames[gameIndex]++;
		for(Bot bot : game.players){
			scores[bot.id] += bot.score;
			wins[bot.id] += bot.rank == 1 ? 1 : 0;
			scoresSize[gameIndex][bot.id] += bot.score;
			winsSize[gameIndex][bot.id] += bot.rank == 1 ? 1 : 0;
		}
	}
	public void printCurrentResults(){
		System.out.println("=== GAMES ===");
		System.out.println("Total: " + this.totalGames);
		for(int size : this.possibleSizes){
			int index = this.sizeToIndex(size);
			System.out.println(size + "x" + size + ": " + this.numGames[index]);
		}
		System.out.println("=== WINS ===");
		System.out.print("Total:");
		for(int winNum : this.wins){
			System.out.print(" " + winNum);
		}
		System.out.println();
		for(int size : this.possibleSizes){
			int index = this.sizeToIndex(size);
			System.out.print(size + "x" + size + ":");
			for(int winNum : this.winsSize[index]){
				System.out.print(" " + winNum);
			}
			System.out.println();
		}
		System.out.println("=== SCORES ===");
		int numTotalGames = Math.max(1, this.totalGames);
		int[] numSizeGames = new int[this.possibleSizes.length];
		for(int i = 0; i < numSizeGames.length; i++) {
			numSizeGames[i] = Math.max(1, this.numGames[i]);
		}
		System.out.print("Total:");
		for(int score : this.scores) {
			System.out.print(" " + (score/numTotalGames));
		}
		System.out.println();
		for(int size : this.possibleSizes) {
			int index = this.sizeToIndex(size);
			System.out.print(size + "x" + size + ":");
			for(int score : this.scoresSize[index]) {
				System.out.print(" " + (score/numSizeGames[index]));
			}
			System.out.println();
		}
	}
}
