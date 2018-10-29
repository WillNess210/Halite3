public class Argument{
	public double value;
	public Argument(double value){
		this.value = value;
	}
	public String toString(){
		int toReturn = (Double.toString(value)).indexOf(".");
		return Double.toString(value).substring(0, Math.min(toReturn + 3, Double.toString(value).length()));
	}
}
