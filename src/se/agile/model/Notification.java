package se.agile.model;


public abstract class Notification<T> {
	private static int id = 0;
	private String contentText, contentTitle;
	private T data;
	
	public Notification(T data){
		this.data = data;
		id++;
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
	
	
}
