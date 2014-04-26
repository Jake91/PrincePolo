package se.agile.model;

import java.util.ArrayList;

import se.agile.activities.model.GitHubData.Branch;

public class TemporaryStorage 
{
	//All static variables used throughout the app go here
//	public static Boolean haveNewCommitsArrived;
	
	public static ArrayList<Branch> branchList;
	
	private static ArrayList<Notification> notificationList = new ArrayList<Notification>();
	
	
	public static boolean addNotification(Notification notification){
		return notificationList.add(notification);
	}
	
	public static boolean removeNotification(Notification notification){
		return notificationList.remove(notification);
	}
}
