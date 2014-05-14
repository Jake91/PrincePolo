package se.agile.model;

import java.util.ArrayList;
import java.util.LinkedList;

import se.agile.githubdata.Branch;
import se.agile.githubdata.File;

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
	public static ArrayList<File> workingFiles;
}
