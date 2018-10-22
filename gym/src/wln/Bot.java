package wln;

public class Bot{
	public String location;
	public int rank, score, id;
	public Bot(String location) {
		this.location = location;
		rank = -1;
		score = -1;
	}
	public String getFullPath() {
		return Constants.startDirectory + "/" + this.location;
	}
	public String getCommandLineRun() {
		return "java -classpath " + location + " MyBot";
	}
	public String getCommandLineRunQuotes() {
		return "\"" + this.getCommandLineRun() + "\"";
	}
}
