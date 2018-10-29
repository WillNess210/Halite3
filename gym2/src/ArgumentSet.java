import java.util.ArrayList;

public class ArgumentSet{
	public ArrayList<Argument> args;
	public ArgumentSet(){
		this.args = new ArrayList<Argument>();
	}
	public static ArgumentSet createArgumentSet(double start, double end, double step){
		ArgumentSet newSet = new ArgumentSet();
		for(double i = start; i <= end; i += step){
			newSet.args.add(new Argument(i));
		}
		return newSet;
	}
}
