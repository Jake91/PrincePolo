package se.agile.activities;	

import se.agile.activities.model.GitHubData.Repository;
import se.agile.model.PreferenceListener;
import se.agile.model.Preferences;
import se.agile.model.Preferences.PREF_KEY;
import se.agile.princepolo.R;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RepositoryOverviewFragment extends Fragment 
{
	private String logTag;
	private View rootView;
	public RepositoryOverviewFragment(){}
	
	private PreferenceListener prefListener;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		logTag = getResources().getString(R.string.logtag_main);
        View rootView = inflater.inflate(R.layout.fragment_repository_overview, container, false);
        this.rootView = rootView;
        
        
        
        prefListener = new PreferenceListener() {
			@Override
			public void preferenceChanged(PREF_KEY key) 
			{
				switch(key)
				{
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

        updateUser();
        updateUserRepos();
		
		// latest commits
		// issues
		// branches
		// collaborators
        return rootView;
    }
	
	private void updateUser()
	{
		((TextView) rootView.findViewById(R.id.textView_User)).setText("User: " + Preferences.getUser().getName());
	}
	
	
	private void updateUserRepos()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Currently selected repository:\n");
		String selectedRepo = Preferences.getSelectedRepository().getName();
		for(Repository repo : Preferences.getRepositories())
		{
			if (selectedRepo.equals(repo.getName()))
			{
				builder.append(repo.getName() + "\n");
			}
		}
		((TextView) rootView.findViewById(R.id.textView_Repositories)).setText(builder.toString());
	}
	
	@Override
	public void onDestroy(){
		Preferences.removeListener(prefListener);
		super.onDestroy();
	}
}
