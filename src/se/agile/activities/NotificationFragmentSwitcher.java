package se.agile.activities;

import se.agile.princepolo.R;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NotificationFragmentSwitcher extends Fragment{
	private String logTag;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		logTag = getResources().getString(R.string.logtag_main);
        View rootView = inflater.inflate(R.layout.fragment_notification_switcher, container, false);
        
        Fragment fragment = new NotificationsFragment();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content_notification_holder, fragment);
        transaction.commit();
        return rootView;
	}
}
