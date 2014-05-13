package se.agile.activities;	

import java.util.LinkedList;

import se.agile.model.BranchNotification;
import se.agile.model.CommitNotification;
import se.agile.model.ConflictNotification;
import se.agile.model.NotificationListArrayAdapter;
import se.agile.model.Notification;
import se.agile.model.NotificationHandler;
import se.agile.model.TemporaryStorage;
import se.agile.princepolo.R;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class NotificationsFragment extends Fragment {
	private String logTag;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		logTag = getResources().getString(R.string.logtag_main);
        View rootView = inflater.inflate(R.layout.list_view, container, false);
        
        final ListView listview = (ListView) rootView.findViewById(R.id.listview);
        LinkedList<Notification> list = TemporaryStorage.getNotifications();
        if(list != null){
        	final NotificationListArrayAdapter adapter = new NotificationListArrayAdapter(getActivity(), list);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            	@Override
            	public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
            		Log.d(logTag, "pressed pos: " + position );
            		
            		Notification not = adapter.getNotification(position);   	
            		if(not instanceof CommitNotification){
            			CommitNotification commitNot = (CommitNotification) not;
            			CommitFragment commitFragment = new CommitFragment();
            			commitFragment.setCommit(commitNot.getData());
            			if(!not.hasBeenViewed()){
            				not.setHasBeenViewed(true);
                			NotificationHandler.viewedNotification(commitNot);
            			}
            			FragmentManager fm = getFragmentManager();
            	        FragmentTransaction transaction = fm.beginTransaction();
            	        transaction.addToBackStack("Notes");
            	        transaction.replace(R.id.content_notification_holder, commitFragment);
            	        transaction.commit();
            		}else if(not instanceof BranchNotification){
            			BranchNotification branchNot = (BranchNotification) not;
            			BranchFragment branchFragment = new BranchFragment();
            			branchFragment.setBranch(branchNot.getData());
            			
            			if(!not.hasBeenViewed()){
            				not.setHasBeenViewed(true);
                			NotificationHandler.viewedNotification(branchNot);
            			}
            			FragmentManager fm = getFragmentManager();
            	        FragmentTransaction transaction = fm.beginTransaction();
            	        transaction.addToBackStack("Notes");
            	        transaction.replace(R.id.content_notification_holder, branchFragment);
            	        transaction.commit();
            		}else if(not instanceof ConflictNotification){
            			ConflictNotification conflictNot = (ConflictNotification) not;
            			ConflictFragment conflictFragment = new ConflictFragment();
            			conflictFragment.setTuple(conflictNot.getData());
            			
            			if(!not.hasBeenViewed()){
            				not.setHasBeenViewed(true);
                			NotificationHandler.viewedNotification(conflictNot);
            			}
            			FragmentManager fm = getFragmentManager();
            	        FragmentTransaction transaction = fm.beginTransaction();
            	        transaction.addToBackStack("Notes");
            	        transaction.replace(R.id.content_notification_holder, conflictFragment);
            	        transaction.commit();
            		}
            	}
            });
        }
        return rootView;
	}
}