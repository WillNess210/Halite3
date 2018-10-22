import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main{
	public static void main(String[] args) throws IOException{
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd .. & ls");
		builder.redirectErrorStream(true);
		Process p = builder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while(true){
			line = r.readLine();
			if(line == null){
				break;
			}
			System.out.println(line);
		}
	}
	private static void watch(final Process process){
		new Thread(){
			public void run(){
				BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = null;
				try{
					while((line = input.readLine()) != null){
						System.out.println(line);
					}
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}.start();
	}
}
