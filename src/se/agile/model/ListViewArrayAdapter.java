package se.agile.model;


import java.util.Date;

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

public class ListViewArrayAdapter extends ArrayAdapter<Notification>{
	private final Context context;
	private final Notification[] values;

	public ListViewArrayAdapter(Context context, Notification[] values) {
		super(context, R.layout.list_view_notification, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.list_view_notification, parent, false);
		
		TextView titleView = (TextView) rowView.findViewById(R.id.List_View_Title);
		TextView dateView = (TextView) rowView.findViewById(R.id.List_View_Date);
		TextView messageView = (TextView) rowView.findViewById(R.id.List_View_Message);
		
		Notification notification = values[position];
		
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
	
	
	
	public Notification getNotification(int position){
		if(position >= 0 && position < values.length){
			return values[position];
		}else{
			return null;
		}
	}
}
