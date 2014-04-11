package se.agile.activities;	

import java.text.BreakIterator;

import se.agile.model.Preferences;
import se.agile.princepolo.R;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SignOutFragment extends Fragment {
	private String logTag;
	public SignOutFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		logTag = getResources().getString(R.string.logtag_main);
        View rootView = inflater.inflate(R.layout.fragment_signout, container, false);
        
        OnClickListener buttonListener = new View.OnClickListener() 
        {
        	public void onClick(View v) 
        	{
        		Preferences.ClearPreferences();
        		Intent intent = new Intent(getActivity(), BrowserActivity.class);
        	    startActivity(intent);
        	}
        };
        
        ((Button) rootView.findViewById(R.id.signOutButton)).setOnClickListener(buttonListener);
        
        return rootView;
    }
}
