import java.util.Calendar;

public class AlarmRunner{
	public boolean hitHour;
	Time time;
	public AlarmRunner(Time time){
		this.time = time;
		this.init();
	}
	public AlarmRunner(int hour, int minute){
		this.time = new Time(hour, minute);
		this.init();
	}
	public AlarmRunner(int hour, int minute, boolean PM){
		this.time = new Time(hour, minute, PM);
		this.init();
	}
	public void init(){
		this.hitHour = false;
	}
	public boolean hasFinished(){
		Calendar rightNow = Calendar.getInstance();
		int thisMinute = rightNow.get(Calendar.MINUTE);
		int thisHour = rightNow.get(Calendar.HOUR_OF_DAY);
		if(!this.hitHour && thisHour == this.time.hour){
			this.hitHour = true;
		}
		if(this.hitHour && (thisMinute >= this.time.minute || thisHour > this.time.hour)){
			return true;
		}
		return false;
	}
}
