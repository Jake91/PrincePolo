package se.agile.activities;	

import se.agile.activities.model.Preferences;
import se.agile.activities.model.GitHubData.Repository;
import se.agile.princepolo.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView.FindListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SelectRepositoryFragment extends Fragment {
	private String logTag;
	private View rootView;
	public SelectRepositoryFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		logTag = getResources().getString(R.string.logtag_main);
        rootView = inflater.inflate(R.layout.fragment_select_repository, container, false);
        return rootView;
    }
	
	
	@Override
	public void onResume(){
		RadioGroup group = (RadioGroup) rootView.findViewById(R.id.radiogroup_repos);
		for(Repository repo : Preferences.getUserRepos()){
			RadioButton button = new RadioButton(rootView.getContext());
			button.setText(repo.getName());
			button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
			group.addView(button);
		}
		super.onResume();
	}
	/*
layout = (LinearLayout) findViewById(R.id.statsviewlayout);

	 */
}