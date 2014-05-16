package se.agile.model;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import se.agile.activities.MainActivity;
import se.agile.activities.MainActivity.VIEW;
import se.agile.asynctasks.RequestBranches;
import se.agile.asynctasks.RequestFullCommit;
import se.agile.asynctasks.RequestListener;
import se.agile.asynctasks.RequestListenerAdapter;
import se.agile.githubdata.Branch;
import se.agile.githubdata.Commit;
import se.agile.githubdata.File;
import se.agile.model.Preferences.PREF_KEY;
import se.agile.princepolo.R;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotificationHandler extends RequestListenerAdapter<ArrayList<Branch>>{
	private ScheduledFuture<?> future;
	private RequestBranches requestBranches;
	private static NotificationManager notificationManager;
	private static Context context;
	private String logTag = "PrincePolo";
	private Runnable runnable;
	private ScheduledExecutorService scheduler;
	private PreferenceListener prefListener;
	
	
	private static ArrayList<NotificationListener> listenerList = new ArrayList<NotificationListener>();
	
	public NotificationHandler(Context contexts){
		context = contexts;
		final RequestListener<ArrayList<Branch>> listener = this;
		
		prefListener = new PreferenceListener() {
			
			@Override
			public void preferenceChanged(PREF_KEY key) {
				if(key.equals(PREF_KEY.TIME_DURATION)){
					stop();
					start();
				}
				
			}
		};
		
		Preferences.addListener(prefListener);
		
		runnable = new Runnable() {
			public void run() {
				requestBranches = new RequestBranches(listener);
				requestBranches.execute();
			}
		};
		
	}
	
	public void start(){
		scheduler = Executors.newSingleThreadScheduledExecutor();
		future = scheduler.scheduleAtFixedRate(runnable, 0, Preferences.getTimeDuration(), TimeUnit.SECONDS);
		// Time is set in the Settings fragment and the value is stored in the Preferences class
		// Integer.parseInt(Preferences.getTimeInterval())
	}
	
	public void stop(){
		if(future != null){
			requestBranches.cancel(false);
			future.cancel(false);
			scheduler.shutdown();
		}else{
			Log.e(logTag, "you haven't started before you cancel");
		}
	}

	@Override
	public void requestFinished(ArrayList<Branch> branches) {
		ArrayList<Branch> earlierBranches = TemporaryStorage.branchList;
		if(earlierBranches != null && branches != null){
			ArrayList<Branch> unselectedBrancherForNotification = Preferences.getUnselectedBranches();
			for(Branch branch : branches){
				int index = earlierBranches.indexOf(branch);
				if(index >= 0){
					//Branch exist
					
					//Does the user want the notification??
					boolean wantCommit = true;
					for(Branch unselectedBranch : unselectedBrancherForNotification){
						if(branch.equals(unselectedBranch)){
							wantCommit = false;
							break;
						}
					}
					if(wantCommit){
						Commit earlierCommit = earlierBranches.get(index).getLatestCommit();
						Commit latestCommit = branch.getLatestCommit();
						if(!earlierCommit.equals(latestCommit)){
//							Commits are different. A new commit have been received.
							getFullCommit(branch);
						}
					}
					
				}else{
					//New branch
					Log.d("PrincePolo", "New branch recieved");
					BranchNotification branchNotification = new BranchNotification(branch);
					TemporaryStorage.addNotification(branchNotification);
					fireNotification(branchNotification);
				}
			}
		}
		if(branches != null){
			TemporaryStorage.branchList = branches.size() > 0 ? branches : null;
		}
		
	}
	
	private void getFullCommit(Branch branch){
		final Commit simpleCommit = branch.getLatestCommit();
		RequestListener<Commit> listener = new RequestListenerAdapter<Commit>() {
			
			@Override
			public void requestFinished(Commit commit) {
				if(commit != null && commit.equals(simpleCommit)){
					CommitNotification commitNotificaiton = new CommitNotification(commit);
					ArrayList<File> workingFiles = TemporaryStorage.workingFiles;
					ArrayList<File> changedFiles = commit.getChangedFiles();
					TemporaryStorage.addNotification(commitNotificaiton );
					fireNotification(commitNotificaiton);
					for(File changedFile : changedFiles){
						for(File workingFile : workingFiles){
							if(changedFile.equals(workingFile)){
								ConflictNotification confNotif = new ConflictNotification(changedFile, commit);
								TemporaryStorage.addNotification(confNotif);
								fireNotification(confNotif);
							}
						}
					}
				}
			}
		};
		RequestFullCommit requestCommit = new RequestFullCommit(listener);
		requestCommit.execute(simpleCommit.getSha(),branch.getName());
	}

	private void fireNotification(Notification notification){
		Intent resultIntent = new Intent(context, MainActivity.class);
		resultIntent.setAction(VIEW.NOTIFICATIONS.getName());
		
		PendingIntent contentIntent = PendingIntent.getActivity(context, notification.getId(), resultIntent,PendingIntent.FLAG_CANCEL_CURRENT);
		
		Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    	long[] pattern = {500,500,500,500,500,500,500,500,500};
    	 
    	NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
    	builder.setContentIntent(contentIntent);
    	builder.setSmallIcon(R.drawable.ic_launcher);
    	builder.setContentTitle(notification.getContentTitle());
    	builder.setContentText(notification.getContentText());
    	if(Preferences.getSoundIsOn()){
    		builder.setSound(alarmSound);
    	}
    	builder.setVibrate(pattern);
    	builder.setAutoCancel(true);
    	if(notification instanceof CommitNotification){
    		CommitNotification commitNot = (CommitNotification) notification;
    		builder.setWhen(commitNot.getData().getDate().getTime());
    	}

    	notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    	notificationManager.notify(notification.getId(), builder.build());
    	for(NotificationListener listener : listenerList){
    		listener.notificationRecieved();
    	}
	}
	
	public static void viewedNotification(Notification notification){
		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Log.d("PrincePolo", "cancel id: " + notification.getId());
		notificationManager.cancel(notification.getId());
	}
	
	public static boolean addNotificationListener(NotificationListener listener){
		return listenerList.add(listener);
	}
	
	public static boolean removeNotificationListener(NotificationListener listener){
		return listenerList.remove(listener);
	}
	
	public void removeListenerFromPrefs(){
		Preferences.removeListener(prefListener);
	}
}
