package se.agile.activities;	

import se.agile.activities.MainActivity.VIEW;
import se.agile.model.Preferences;
import se.agile.princepolo.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SignOutFragment extends Fragment {
	private String logTag;
	public SignOutFragment(){}
	private Activity activity;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		logTag = getResources().getString(R.string.logtag_main);
        View rootView = inflater.inflate(R.layout.fragment_signout, container, false);
        activity = getActivity();
        new AlertDialog.Builder(activity)
		.setTitle("Sign out")
		.setMessage("Are you sure you want to disconnect from GitHub?")
		.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int whichButton) {
						Preferences.ClearPreferences();
		        		Intent intent = new Intent(getActivity(), LoginActivity.class);
		        	    startActivity(intent);
					}
				})
		.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						((MainActivity) activity).displayView(VIEW.OVERVIEW);
					}
				}).show();
        return rootView;
    }
}
