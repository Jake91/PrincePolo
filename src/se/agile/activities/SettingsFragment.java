package se.agile.activities;	

import java.util.Calendar;

import se.agile.princepolo.R;
import android.app.Dialog;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.view.View.OnClickListener;


public class SettingsFragment extends Fragment 
{
	private String logTag;
	public SettingsFragment(){}
	public int LED_NOTIFICATION_ID = 1;
	
	private void RedFlash(){
		Log.d(logTag, "Red Flash");
	    NotificationManager nm = ( NotificationManager ) getActivity().getSystemService(Context.NOTIFICATION_SERVICE );
	    Notification notif = new Notification();
	    notif.ledARGB = 0xFFff0000;
	    notif.flags = Notification.FLAG_SHOW_LIGHTS;
	    notif.ledOnMS = 100; 
	    notif.ledOffMS = 100; 
	    nm.notify(LED_NOTIFICATION_ID, notif);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		logTag = getResources().getString(R.string.logtag_main);
		View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
		
	    Button button = (Button) rootView.findViewById(R.id.flashlights);
	    button.setOnClickListener(buttonOnClick);
	    
		return rootView;
	}
	
	public OnClickListener buttonOnClick = new OnClickListener() {

	    @Override
	    public void onClick(View v) 
	    {
	    	Log.d(logTag, "Clicked!");
	    	Notification.Builder noteBuilder = new Notification.Builder(getActivity())
            .setAutoCancel(true)
            .setPriority(Notification.PRIORITY_DEFAULT)
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle("The title")
            .setContentText("Content text")
            .setLights(Color.YELLOW, 500, 500)
            ;

Intent noteIntent = new Intent(getActivity(),SettingsFragment.class);
PendingIntent notePendingIntent = PendingIntent.getActivity(getActivity(), 0, noteIntent, PendingIntent.FLAG_CANCEL_CURRENT);
noteBuilder.setContentIntent(notePendingIntent);

Notification notification = noteBuilder.build();
notification.defaults = 0;
notification.ledARGB = 0xff00ff00;         
notification.ledOnMS = 300;         
notification.ledOffMS = 1000;         
notification.flags |= Notification.FLAG_SHOW_LIGHTS;
//notification.ledARGB = Color.YELLOW;
NotificationManager mgr = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
mgr.notify(0,notification);
	    }
	  };


}
