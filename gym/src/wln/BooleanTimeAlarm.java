package wln;
import java.util.Calendar;

public class BooleanTimeAlarm{
	boolean hitHour, hitMinute;
	int hour, minute;
	public BooleanTimeAlarm(int hour, int minute){
		this.hour = hour;
		this.minute = minute;
		this.hitHour = false;
		this.hitMinute = false;
	}
	public boolean hasReachedTime(){
		Calendar rightNow = Calendar.getInstance();
		int thisMinute = rightNow.get(Calendar.MINUTE);
		int thisHour = rightNow.get(Calendar.HOUR_OF_DAY);
		if(!this.hitHour && thisHour == this.hour){
			this.hitHour = true;
		}
		if(this.hitHour && thisMinute >= this.minute){
			this.hitMinute = true;
		}else if(this.hitHour && thisHour != this.hour){ // Will work if the code isn't called in the rest of the hour after the minute (ex 2:00 will set off an alarm for 1:59)
			this.hitMinute = true;
		}
		return this.hitHour && this.hitMinute;
	}
}
