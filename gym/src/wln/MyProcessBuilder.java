package wln;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.json.JSONException;

public class MyProcessBuilder{
	public ArrayList<String> commands;
	public MyProcessBuilder(){
		commands = new ArrayList<String>();
	}
	public void add(String in){
		commands.add(in);
	}
	public void clearCommands(){
		commands.clear();
	}
	public void addls(){
		commands.add("ls");
	}
	public void addcd(String location){
		commands.add("cd " + location);
	}
	public void addcdMain(){
		this.addcd(Constants.startDirectory);
	}
	public void addBotCompile(Bot bot){
		this.addcd(bot.getFullPath());
		this.add("call compile.bat");
	}
	public void compileGameBots(Game game){
		for(Bot bot : game.players){
			this.addBotCompile(bot);
		}
	}
	public void addGameRun(Game game){
		this.addcdMain();
		this.add(game.getRunCommand());
	}
	public void addClearAll(){
		this.addcdMain();
		this.add("rm *.log");
		this.add("rm *.hlt");
		this.add("rm replays/*.log");
		this.add("rm replays/*.hlt");
		this.add("rm replays/*.txt");
	}
	public String generateString(){
		String result = "";
		for(int i = 0; i < commands.size(); i++){
			if(i == 0){
				result += commands.get(i);
			}else{
				result += "& " + commands.get(i);
			}
		}
		return result;
	}
	public void run() throws IOException{
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", this.generateString());
		builder.redirectErrorStream(true);
		builder.start();
	}
	public ArrayList<String> getOutput() throws IOException{
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", this.generateString());
		builder.redirectErrorStream(true);
		Process p = builder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		ArrayList<String> lines = new ArrayList<String>();
		String line;
		while(true){
			line = r.readLine();
			if(line == null){
				break;
			}
			lines.add(line);
		}
		return lines;
	}
	public String getOutputJson() throws IOException{
		ArrayList<String> strings = this.getOutput();
		boolean started = false;
		String json = "";
		for(int i = 0; i < strings.size(); i++){
			if(strings.get(i).equals("{")){
				started = true;
			}
			if(started){
				json += strings.get(i) + '\n';
			}
			if(strings.get(i).equals("}")){
				started = false;
			}
		}
		return json;
	}
	public void runAndLoadGame(Game game) throws IOException, JSONException{
		String json = this.getOutputJson();
		game.loadGameResults(json);
	}
}
