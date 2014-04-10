package se.agile.activities;	

import java.util.ArrayList;

import se.agile.activities.model.PreferenceListener;
import se.agile.activities.model.Preferences;
import se.agile.activities.model.Preferences.PREF_KEY;
import se.agile.activities.model.RequestRepositories;
import se.agile.activities.model.GitHubData.Repository;
import se.agile.princepolo.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SelectRepositoryFragment extends Fragment{
	private String logTag;
	private View rootView;
	
	private PreferenceListener prefListener;
	
	private RequestRepositories thread;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		logTag = getResources().getString(R.string.logtag_main);
		rootView = inflater.inflate(R.layout.fragment_select_repository, container, false);
		thread = new RequestRepositories();
		
		prefListener = new PreferenceListener() {
			@Override
			public void preferenceChanged(PREF_KEY key) {
				switch (key) {
				case USER_REPOSITORIES:
					RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.radiogroup_repos);
					radioGroup.removeAllViews();
					ArrayList<Repository> repoList = Preferences.getRepositories();
					OnClickListener radioButtonListener = new OnClickListener() {
						@Override
						public void onClick(View v) {
							Preferences.setSelectedRepository(new Repository(((RadioButton) v).getText().toString()));
						}
					};
					String selectedRepo = Preferences.getSelectedRepository().getName();
					for(Repository repo : repoList){
						RadioButton button = new RadioButton(rootView.getContext());
						button.setText(repo.getName());
						button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
						button.setOnClickListener(radioButtonListener);
						radioGroup.addView(button);
						if(repo.getName().equals(selectedRepo)){
							button.setChecked(true);
						}
					}
					break;
				default:
					break;
				}
				
			}
		};
		Preferences.addListener(prefListener);
		
		thread.execute();
        return rootView;
    }
	
	@Override
	public void onDestroy(){
		Preferences.removeListener(prefListener);
		thread.cancel(true);
		super.onDestroy();
	}
}