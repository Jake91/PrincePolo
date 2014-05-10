package se.agile.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import se.agile.princepolo.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.GradientDrawable;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListViewArrayAdapter extends ArrayAdapter<Notification> implements NotificationListener{
	private final Context context;
	private LinkedList<Notification>  notificationList;

	public ListViewArrayAdapter(Context context, LinkedList<Notification> notificationList) {
		super(context, R.layout.list_view_notification,R.id.List_View_Message, notificationList);
		this.context = context;
		this.notificationList = TemporaryStorage.getNotifications();
		NotificationHandler.addNotificationListener(this);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.list_view_notification, parent, false);
		
		TextView titleView = (TextView) rowView.findViewById(R.id.List_View_Title);
		TextView dateView = (TextView) rowView.findViewById(R.id.List_View_Date);
		TextView messageView = (TextView) rowView.findViewById(R.id.List_View_Message);
		
		Notification notification = notificationList.get(position);
		
		if(notification.hasBeenViewed()){
			View circleView = (View) rowView.findViewById(R.id.CircleView);
			GradientDrawable circleDrawable = (GradientDrawable)circleView.getBackground();
			circleView.setVisibility(View.INVISIBLE);
		}
		titleView.setText(notification.getContentTitle());
		messageView.setText(notification.getContentText());
		Date date = notification.getDate();
		String formatedDate = DateFormat.getDateFormat(context).format(date) + " " + DateFormat.getTimeFormat(context).format(date);
		dateView.setText(formatedDate);
		return rowView;
	}
	
	public void setNotification(LinkedList<Notification> list){
		this.notificationList = list;
		this.notifyDataSetChanged();
	}
	
	
	public Notification getNotification(int position){
		return notificationList.get(position);
	}

	@Override
	public void notificationRecieved() {
		this.notificationList = TemporaryStorage.getNotifications();
		this.notifyDataSetChanged();
		
	}
}
