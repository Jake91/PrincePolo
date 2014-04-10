package se.agile.activities;	

import se.agile.model.HttpConnection;
import se.agile.model.PreferenceListener;
import se.agile.model.Preferences;
import se.agile.model.HttpConnection.URL;
import se.agile.model.Preferences.PREF_KEY;
import se.agile.princepolo.R;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ConnectToGitHubFragment extends Fragment {
	private String logTag;
	private View rootView;
	public ConnectToGitHubFragment(){}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		logTag = getResources().getString(R.string.logtag_main);
        rootView = inflater.inflate(R.layout.fragment_connect_to_github, container, false);
		final View tempRootView = rootView;
        OnClickListener buttonListener = new View.OnClickListener() 
        {
        	public void onClick(View v) 
        	{
        		switch (v.getId()) 
        		{
    			case R.id.button_get2_github:
    				Intent intent = new Intent(getActivity(), StartActivity.class);
            	    startActivity(intent);
    				break;
    			case R.id.button_reset_connection:
    				Preferences.setAccessToken("");
    				Preferences.setScope("");
    				Preferences.setTokenType("");
    				Preferences.setUserName("");
    				Preferences.setUserRepos(new String[] {""});
    				break;
        		}	
        	}
        };
        ((Button) rootView.findViewById(R.id.button_get2_github)).setOnClickListener(buttonListener);
        ((Button) rootView.findViewById(R.id.button_reset_connection)).setOnClickListener(buttonListener);
        PreferenceListener listener = new PreferenceListener() 
        {
			@Override
			public void preferenceChanged(PREF_KEY key) 
			{
				switch(key)
				{
					case ACCESS_TOKEN:
						gotAccessToken(Preferences.isConnectedToGitHub());
						break;
					case USER_NAME:
						updateUser();
						break;
					case USER_REPOS:
						updateUserRepos();
						break;
						
				}
			}
		};
		
        Preferences.addListener(listener);
        
        return rootView;
    }
	
	private void updateUser()
	{
		((TextView) rootView.findViewById(R.id.textView_User)).setText("User: " + Preferences.getUserName());
	}
	
	private void updateUserRepos()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Repositories:\n");
		for(String s : Preferences.getUserRepos()){
			builder.append(s + "\n");
		}
		((TextView) rootView.findViewById(R.id.textView_Repositories)).setText(builder.toString());
	}
	
	private void gotAccessToken(boolean isConnectedToGitHub)
	{
		if(isConnectedToGitHub)
		{
			((Button) rootView.findViewById(R.id.button_get2_github)).setEnabled(false);
			((TextView) rootView.findViewById(R.id.txtLabel))
				.setText("You have connected to GitHub");
		}
		else
		{
			((Button) rootView.findViewById(R.id.button_get2_github)).setEnabled(true);
			((TextView) rootView.findViewById(R.id.txtLabel))
				.setText("You are not connected to GitHub. In order to get data from github you have to allow "+
							"the app to get data from you account.");
		}
	}

	
	@Override
	public void onResume()
	{
		gotAccessToken(Preferences.isConnectedToGitHub());
		updateUser();
		updateUserRepos();
		super.onResume();
	}
}
