package se.agile.model;

import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import se.agile.activities.MainActivity;
import se.agile.activities.MainActivity.VIEW;
import se.agile.activities.model.GitHubData.Branch;
import se.agile.activities.model.GitHubData.Commit;
import se.agile.asynctasks.RequestBranches;
import se.agile.asynctasks.RequestCommit;
import se.agile.asynctasks.RequestListener;
import se.agile.princepolo.R;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotificationHandler implements RequestListener{
	private ScheduledFuture<?> future;
	private RequestBranches requestBranches;
	private NotificationManager notificationManager;
	private Context context;
	private String logTag = "PrincePolo";
	private Runnable runnable;
	private ScheduledExecutorService scheduler;
	
	public NotificationHandler(Context context){
		this.context = context;
		final RequestListener listener = this;

		runnable = new Runnable() {
			public void run() {
				requestBranches = new RequestBranches(listener);
				requestBranches.execute();
			}
		};
		
	}
	
	public void start(){
		scheduler = Executors.newSingleThreadScheduledExecutor();
		future = scheduler.scheduleAtFixedRate(runnable, 0, 10, TimeUnit.SECONDS);
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
	public void requestFinished() {
		ArrayList<Branch> branches = requestBranches.getResult();
		ArrayList<Branch> earlierBranches = TemporaryStorage.branchList;
		if(earlierBranches != null){
			for(Branch branch : branches){
				int index = earlierBranches.indexOf(branch);
				if(index >= 0){
					//Branch exist
					Commit earlierCommit = earlierBranches.get(index).getLatestCommit();
					Commit latestCommit = branch.getLatestCommit();
					if(!earlierCommit.equals(latestCommit)){
//						Commits are different. A new commit have been received.
						getFullCommit(latestCommit);
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
		TemporaryStorage.branchList = branches.size() > 0 ? branches : null;
	}
	
	private ArrayList<RequestCommit> requestCommitList = new ArrayList<RequestCommit>();
	
	private void getFullCommit(final Commit simpleCommit){
		RequestListener listener = new RequestListener() {
			
			@Override
			public void whenNoSelectedRepository() {
				// Since this is for notifications we dont show any messages
				
			}
			
			@Override
			public void whenNoInternetConnection() {
				// Since this is for notifications we dont show any messages
				
			}
			
			@Override
			public void requestFinished() {
				Log.d("PrincePolo", "New commit recieved");
				for(RequestCommit req : requestCommitList){
					Commit commit = req.getResult();
					if(commit != null && commit.equals(simpleCommit)){
						CommitNotification commitNotificaiton = new CommitNotification(commit);
						TemporaryStorage.addNotification(commitNotificaiton );
						fireNotification(commitNotificaiton);
						requestCommitList.remove(req);
						break;
					}
				}
			}
		};
		RequestCommit requestCommit = new RequestCommit(listener);
		requestCommitList.add(requestCommit);
		requestCommit.execute(simpleCommit.getSha());
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
    	builder.setSound(alarmSound);
    	builder.setVibrate(pattern);
    	builder.setAutoCancel(true);
    	if(notification instanceof CommitNotification){
    		CommitNotification commitNot = (CommitNotification) notification;
    		builder.setWhen(commitNot.getData().getDate().getTime());
    	}

    	notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    	notificationManager.notify(notification.getId(), builder.build());
	}

	@Override
	public void whenNoInternetConnection() {
		// Since this is for notifications we dont show any messages
		
	}

	@Override
	public void whenNoSelectedRepository() {
		// Since this is for notifications we dont show any messages	
	}
}
