public class Time{
	public int hour, minute;
	public Time(int hour, int minute){
		this.hour = hour;
		this.minute = minute;
	}
	public Time(int hour, int minute, boolean PM){
		this.hour = PM ? hour + 12 : hour;
		this.minute = minute;
	}
}
