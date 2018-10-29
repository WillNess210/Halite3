import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CommandLineHandler{
	ArrayList<String> commandQueue;
	public CommandLineHandler(){
		this.commandQueue = new ArrayList<String>();
	}
	public void add_ls(){
		commandQueue.add("ls");
	}
	public void add_cd(String dir){
		commandQueue.add("cd " + dir);
	}
	public void add_cd(Bot a){
		commandQueue.add("cd " + a.path);
	}
	public void add_cd_home(){
		commandQueue.add("cd C:/Users/WillN/Documents/College/CompetitiveProgramming/Halite3/Halite3Round2");
	}
	public void add_bot_compile(Bot a){
		this.add_cd(a);
		commandQueue.add("call compile.bat");
		this.add_cd_home();
	}
	public void add_team_compile(BotTeam a){
		for(Bot bot : a.bots){
			this.add_bot_compile(bot);
		}
	}
	public void add_game_run(Game game){
		commandQueue.add(game.getRunCommand());
	}
	public String generateString(){
		String result = "cd .. & ";
		for(int i = 0; i < this.commandQueue.size(); i++){
			if(i == 0){
				result += commandQueue.get(i);
			}else{
				result += " & " + commandQueue.get(i);
			}
		}
		return result;
	}
	public ArrayList<String> run() throws IOException{
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
		commandQueue.clear();
		return lines;
	}
	public String runAsJson() throws IOException{
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", this.generateString());
		builder.redirectErrorStream(true);
		Process p = builder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String lines = "";
		String line;
		while(true){
			line = r.readLine();
			if(line == null){
				break;
			}
			lines += line;
		}
		commandQueue.clear();
		return lines;
	}
}
