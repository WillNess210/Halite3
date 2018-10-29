package wln;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ResultsRecorder{
	BufferedWriter out;
	String path;
	public ResultsRecorder(String path) throws IOException{
		this.path = path;
		this.init();
	}
	public ResultsRecorder() throws IOException{
		this.path = "results" + BooleanTimeAlarm.getHour() + "_" + BooleanTimeAlarm.getMinute() + ".txt";
		this.init();
	}
	public void init() throws IOException{
		out = new BufferedWriter(new FileWriter(this.path, true));
		out.write("Starting log at " + BooleanTimeAlarm.getHour() + ":" + BooleanTimeAlarm.getMinute());
	}
	public void addLine(String line) throws IOException{
		out.write(line);
	}
	public void close() throws IOException{
		out.close();
	}
}
