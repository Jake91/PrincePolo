package se.agile.model;

import java.util.Date;


public abstract class Notification<T> implements Comparable<Notification<T>>{
	private static int counter = 0;
	private int id;
	private String contentText, contentTitle;
	private T data;
	private Date date;
	private boolean hasBeenViewed;
	
	public Notification(T data){
		this.data = data;
		this.date = new Date();
		id=counter;
		counter++;
	}
	
	public int getId(){
		return id;
	}
	

	public String getContentText() {
		return contentText;
	}

	public void setContentText(String contentText) {
		this.contentText = contentText;
	}

	public String getContentTitle() {
		return contentTitle;
	}

	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Date getDate() {
		return date;
	}

	public boolean hasBeenViewed() {
		return hasBeenViewed;
	}

	public void setHasBeenViewed(boolean hasBeenViewed) {
		this.hasBeenViewed = hasBeenViewed;
	}
	
	@Override
	public int compareTo(Notification<T> o){
		return this.date.compareTo(o.getDate());
		
	}
	
}
