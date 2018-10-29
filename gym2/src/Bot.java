import java.util.ArrayList;

public class Bot{
	public int id;
	public String path;
	public ArrayList<String> args;
	public ArrayList<BotResult> results;
	public Bot(String path){
		this.path = path;
		this.args = new ArrayList<String>();
		this.results = new ArrayList<BotResult>();
	}
	public void clearData(){
		this.results.clear();
	}
	public void clearArgs(){
		args.clear();
	}
	public void setInt(int id){
		this.id = id;
	}
	public void addArgument(String argument){
		this.args.add(argument);
	}
	/*
	 * public String toString(){ return this.id + ": " + this.getRunString(); }
	 */
	public String getRunString(){
		String toReturn = "\"java -classpath ";
		toReturn += this.path + " MyBot";
		for(int i = 0; i < args.size(); i++){
			toReturn += " " + args.get(i);
		}
		toReturn += "\"";
		return toReturn;
	}
	public void addResult(BotResult a){
		this.results.add(a);
	}
	public double averageRank(){
		double sum = 0;
		for(BotResult result : this.results){
			sum += result.rank;
		}
		return sum / this.results.size();
	}
	public double averageScore(){
		double sum = 0;
		for(BotResult result : this.results){
			sum += result.score;
		}
		return sum / this.results.size();
	}
	public int numberWins(){
		int sum = 0;
		for(BotResult result : this.results){
			sum += result.win() ? 1 : 0;
		}
		return sum;
	}
	public double winPercentage(){
		return this.numberWins() / this.results.size();
	}
	public String getResultString(){
		return this.averageRank() + " " + this.averageScore();
	}
	public String toString(){
		return "[" + this.id + "] [" + this.getResultString() + "]";
	}
}
