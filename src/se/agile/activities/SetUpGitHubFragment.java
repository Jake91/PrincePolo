package se.agile.activities;	

import se.agile.activities.model.PreferenceListener;
import se.agile.activities.model.Preferences;
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

public class SetUpGitHubFragment extends Fragment {
	private String logTag;
	private View rootView;
	public SetUpGitHubFragment(){}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		logTag = getResources().getString(R.string.logtag_main);
        rootView = inflater.inflate(R.layout.setup_github_fragment, container, false);
		final View tempRootView = rootView;
        OnClickListener buttonListener = new View.OnClickListener() {
        	public void onClick(View v) {
        		switch (v.getId()) {
    			case R.id.button_get2_github:
    				Intent intent = new Intent(getActivity(), BrowserActivity.class);
            	    startActivity(intent);
    				break;
    			case R.id.button_reset_connection:
    				Preferences.setAccessToken("");
    				Preferences.setScope("");
    				Preferences.setTokenType("");
    				break;
        		}
        		
        	}
        };
        ((Button) rootView.findViewById(R.id.button_get2_github)).setOnClickListener(buttonListener);
        ((Button) rootView.findViewById(R.id.button_reset_connection)).setOnClickListener(buttonListener);
        PreferenceListener listener = new PreferenceListener() {
			@Override
			public void preferenceChanged(String key) {
				if(key.equals("access_token")){
					gotAccessToken(Preferences.getAccessToken().equals("Empty"));
				}
			}
		};
		
        Preferences.addListener(listener);
        
        return rootView;
    }
	
	private void gotAccessToken(boolean hasAccessToken){
		if(hasAccessToken){
			((Button) rootView.findViewById(R.id.button_get2_github)).setEnabled(true);
			((TextView) rootView.findViewById(R.id.txtLabel))
				.setText("You are not connected to GitHub. In order to get data from github you have to allow "+
							"the app to get data from you account.");
		}else{
			((Button) rootView.findViewById(R.id.button_get2_github)).setEnabled(false);
			((TextView) rootView.findViewById(R.id.txtLabel))
				.setText("You have connected to GitHub");
		}
	}

	
	@Override
	public void onResume(){
		gotAccessToken(Preferences.getAccessToken().equals("Empty"));
		super.onResume();
	}
}
