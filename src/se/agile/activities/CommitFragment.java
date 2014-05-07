package se.agile.activities;

import se.agile.activities.model.GitHubData.Commit;
import se.agile.princepolo.R;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CommitFragment extends Fragment 
{
	private String logTag;
	private CharSequence text;
	private Commit commit;
	private View rootView;
	
	public CommitFragment(){
		
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		logTag = getResources().getString(R.string.logtag_main);
        rootView = inflater.inflate(R.layout.fragment_commit, container, false);
        
        if(commit != null){
        	TextView textView = (TextView) rootView.findViewById(R.id.Commit_Message);
            textView.setText(commit.toString());
        }
        android.view.View.OnClickListener viewListener = new View.OnClickListener() 
        {
        	@Override
        	public void onClick(View v) 
        	{
        		FragmentManager fm = getFragmentManager();
    	        FragmentTransaction transaction = fm.beginTransaction();
    	        transaction.replace(R.id.content_notification_holder, new NotificationsFragment());
    	        transaction.commit();
        	}
        };
        ((RelativeLayout) rootView.findViewById(R.id.Commit_View)).setOnClickListener(viewListener);
        return rootView;
    }


	public Commit getCommit() {
		return commit;
	}


	public void setCommit(Commit commit) {
		this.commit = commit;
	}
	
	
}

