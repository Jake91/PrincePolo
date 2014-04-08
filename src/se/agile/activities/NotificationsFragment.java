package se.agile.activities;	

import se.agile.model.Preferences;
import se.agile.princepolo.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NotificationsFragment extends Fragment {
	private String logTag;
	public NotificationsFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		logTag = getResources().getString(R.string.logtag_main);
        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        
//        How to access preferences
//        Preferences.getAccessToken(getActivity());
        return rootView;
    }
}
