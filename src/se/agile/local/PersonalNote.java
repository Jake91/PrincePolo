package se.agile.local;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PersonalNote {
	
	private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM d HH:mm");
	private String message;
	private String date;
	
	public PersonalNote(String message){
		update(message);
		System.out.println("This is your personal message: " + message + " And it was made at: " + date);
	}
	
	public void update(String message){
		Calendar c = Calendar.getInstance();
		this.date = DATE_FORMAT.format(c.getTime());
		this.message = message;
	}
	public String message(){
		return message;
	}
	public String date(){
		return date;
	}
	public String toString(){
		return message;
		
	}
}
