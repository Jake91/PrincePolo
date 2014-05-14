package se.agile.activities;	

import java.util.Calendar;
import se.agile.princepolo.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SettingsFragment extends Fragment 
{
	private String logTag;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		logTag = getResources().getString(R.string.logtag_main);
		View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
		
	    
		return rootView;
	}
	
}
