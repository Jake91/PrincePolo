package se.agile.activities;	


import se.agile.githubdata.Repository;
import se.agile.model.PreferenceListener;
import se.agile.model.Preferences;
import se.agile.model.Preferences.PREF_KEY;
import se.agile.princepolo.R;
import android.app.Fragment;
import android.content.Intent;
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
	
	private PreferenceListener prefListener;
	
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
    				Intent intent = new Intent(getActivity(), LoginActivity.class);
            	    startActivity(intent);
    				break;
    			case R.id.button_reset_connection:
    				Preferences.ClearPreferences();
    				break;
        		}	
        	}
        };
        ((Button) rootView.findViewById(R.id.button_get2_github)).setOnClickListener(buttonListener);
        ((Button) rootView.findViewById(R.id.button_reset_connection)).setOnClickListener(buttonListener);

        prefListener = new PreferenceListener() {
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
					case USER_REPOSITORIES:
						updateUserRepos();
						break;
						
				}
			}
		};
		
        Preferences.addListener(prefListener);
        
        return rootView;
    }
	
	private void updateUser(){
		((TextView) rootView.findViewById(R.id.textView_User)).setText("User: " + Preferences.getUser().getName());
	}
	
	private void updateUserRepos()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Repositories:\n");
		for(Repository repo : Preferences.getRepositories()){
			builder.append(repo.getName() + "\n");
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
	@Override
	public void onDestroy(){
		Preferences.removeListener(prefListener);
		super.onDestroy();
	}
}
