package se.agile.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import se.agile.activities.model.GitHubData.Branch;

public class TemporaryStorage 
{
	//All static variables used throughout the app go here
//	public static Boolean haveNewCommitsArrived;
	
	public static ArrayList<Branch> branchList;
	
	private static LinkedList<Notification> notificationList = new LinkedList<Notification>();
	
	
	public static void addNotification(Notification notification){
		notificationList.addFirst(notification);
	}
	
	public static boolean removeNotification(Notification notification){
		return notificationList.remove(notification);
	}
	
	public static LinkedList<Notification> getNotifications(){
		return notificationList;
	}
}
