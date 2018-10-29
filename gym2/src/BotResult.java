public class BotResult{
	public int rank, score;
	public BotResult(int rank, int score){
		this.rank = rank;
		this.score = score;
	}
	public boolean win(){
		return this.rank == 1;
	}
	public String toString() {
		return rank + " " + score;
	}
}
