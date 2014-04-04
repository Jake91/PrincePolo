package se.agile.activities;	

import se.agile.activities.model.Preferences;
import se.agile.princepolo.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RepositoryOverviewFragment extends Fragment 
{
	private String logTag;
	private View rootView;
	public RepositoryOverviewFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		logTag = getResources().getString(R.string.logtag_main);
        View rootView = inflater.inflate(R.layout.fragment_repository_overview, container, false);
        this.rootView = rootView;
		updateUser();
		updateUserRepos();
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
}
