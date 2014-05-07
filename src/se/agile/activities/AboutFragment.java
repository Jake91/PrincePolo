package se.agile.activities;	

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import se.agile.model.CreateNotificationActivity;
import se.agile.model.HttpConnection;
import se.agile.model.NotificationDialog;
import se.agile.model.Preferences;
import se.agile.model.HttpConnection.URL;
import se.agile.princepolo.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AboutFragment extends Fragment 
{
	
	private String logTag;
	private View rootView;
	ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
	public AboutFragment(){}
	private int notificationID = 100;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		logTag = getResources().getString(R.string.logtag_main);
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        this.rootView = rootView;
		CountBranches task = new CountBranches();
		task.execute(new String[] { "https://api.github.com/repos/Jake91/PrincePolo/branches?access_token=aa534e873012c9a6881ee6826f31e494ad6ca6db" });
        updateUser();
		updateUserRepos();
		String token = Preferences.getAccessToken();
		//Log.d(logTag, ""+token);
		
		scheduler.scheduleAtFixedRate (new Runnable() 
		{
			public void run() 
			{
				GetTheCommitHistory task = new GetTheCommitHistory();
				//task.execute(new String[] { "https://api.github.com/repos/Jake91/PrincePolo/commits?access_token=aa534e873012c9a6881ee6826f31e494ad6ca6db" });
				task.execute(new String[] { "https://api.github.com/repos/gautsson/testing/commits?access_token=aa534e873012c9a6881ee6826f31e494ad6ca6db" });
			}
		}, 0, 10, TimeUnit.SECONDS);
		
        return rootView;
    }
	
	
	



	

	private class GetTheCommitHistory extends AsyncTask<String, Void, String> 
	{
		@Override
		protected String doInBackground(String... urls) 
	    {
			String response = "";
			for (String url : urls) 
			{
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);
				try 
				{
					HttpResponse execute = client.execute(httpGet);
					InputStream content = execute.getEntity().getContent();
				
					BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
					String s = "";
					while ((s = buffer.readLine()) != null) 
					{
				        response += s;
					}

				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
			return response;
	    }

	    @Override
	    protected void onPostExecute(String result) 
	    {
	    	String newValue = getMD5EncryptedString(result);
    	    //Log.d(logTag, "Old value is: "+Preferences.getMD5Value());
    	    //Log.d(logTag, "NEW value is: "+newValue);
    	    
    	    if (Preferences.getMD5Value().equals(""))
    	    {
    	    	Preferences.setMD5Value(newValue);
    	    }
    	    
    	    if (!newValue.equals(Preferences.getMD5Value()))
    	    {
    	    	Preferences.setMD5Value(newValue);
    	    	Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    	    	long[] pattern = {500,500,500,500,500,500,500,500,500};
    	    	
    	    	NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity());
    	    	mBuilder.setSmallIcon(R.drawable.ic_launcher);
    	    	mBuilder.setContentTitle("Incoming commit");
    	    	mBuilder.setContentText("Click to view it");
    	    	mBuilder.setSound(alarmSound);
    	    	mBuilder.setVibrate(pattern);
    	    	
    	    	NotificationManager mNotificationManager =
    	    		    (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
    	    		    
    	    		// notificationID allows you to update the notification later on.
    	    		mNotificationManager.notify(notificationID, mBuilder.build());
    	    }
    	    else return;
	    }
	}
	
	
	
	
	
	
	
	private void updateUser()
	{
		((TextView) rootView.findViewById(R.id.textView_User)).setText("User: " + Preferences.getUserName());
	}
	
	
	private void updateUserRepos()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Currently selected repository:\n");
		for(String s : Preferences.getUserRepos())
		{
			if (Preferences.getSelectedRepository().equals(s))
			{
				builder.append(s + "\n");
			}
		}
		((TextView) rootView.findViewById(R.id.textView_Repositories)).setText(builder.toString());
	}
	
	// Calculates MD5 for a file
	public static String getMD5EncryptedString(String encTarget)
	{
        MessageDigest mdEnc = null;
        try 
        {
        	mdEnc = MessageDigest.getInstance("MD5");
        } 
        catch (NoSuchAlgorithmException e) 
        {
            //System.out.println("Exception while encrypting to md5");
            e.printStackTrace();
        } 
        // Encryption algorithm
        mdEnc.update(encTarget.getBytes(), 0, encTarget.length());
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
        while ( md5.length() < 32 ) 
        {
            md5 = "0"+md5;
        }
        return md5;
    }
	
	
	
	
	private class CountBranches extends AsyncTask<String, Void, String> 
	{
		@Override
		protected String doInBackground(String... urls) 
	    {
			String response = "";
			for (String url : urls) 
			{
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);
				try 
				{
					HttpResponse execute = client.execute(httpGet);
					InputStream content = execute.getEntity().getContent();
				
					BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
					String s = "";
					while ((s = buffer.readLine()) != null) 
					{
				        response += s;
					}

				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
			return response;
	    }

	    @Override
	    protected void onPostExecute(String result) 
	    {
			String findStr = "name";
			int lastIndex = 0;
			int count =0;

			while(lastIndex != -1)
			{
				lastIndex = result.indexOf(findStr,lastIndex);

				 if( lastIndex != -1)
				 {
					 count ++;
					 lastIndex+=findStr.length();
				 }
			}
			//Log.d(logTag, "numberofBranches:"+count);
	    }
	}
}
