package wln;
import java.util.ArrayList;

public class Bot{
	public String location;
	public ArrayList<String> args;
	public int rank, score, id;
	public Bot(String location){
		this.location = location;
		rank = -1;
		score = -1;
		args = new ArrayList<String>();
	}
	public String getFullPath(){
		return Constants.startDirectory + "/" + this.location;
	}
	public String getCommandLineRun(){
		String toReturn = "java -classpath " + location + " MyBot ";
		for(String arg : args){
			toReturn += arg;
		}
		return toReturn;
	}
	public String getCommandLineRunQuotes(){
		return "\"" + this.getCommandLineRun() + "\"";
	}
	public void clearArguments(){
		args.clear();
	}
	public void addArgument(String str){
		args.add(str);
	}
}
