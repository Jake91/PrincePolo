package se.agile.activities;	

import java.util.ArrayList;

import se.agile.activities.model.HttpConnection;
import se.agile.activities.model.HttpConnectionResponseListener;
import se.agile.activities.model.Preferences;
import se.agile.activities.model.GitHubData.Repository;
import se.agile.activities.model.HttpConnection.URL;
import se.agile.princepolo.R;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SelectRepositoryFragment extends Fragment {
	private String logTag;
	private View rootView;
	public SelectRepositoryFragment(){}
	private ArrayList<Repository> reposList;
	private ArrayList<Button> radioButtonList;
	private RadioGroup radioGroup;
	private OnClickListener radioButtonListener; 
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		logTag = getResources().getString(R.string.logtag_main);
		rootView = inflater.inflate(R.layout.fragment_select_repository, container, false);
		reposList = new ArrayList<Repository>();
		radioButtonList = new ArrayList<Button>();
		radioGroup = (RadioGroup) rootView.findViewById(R.id.radiogroup_repos);
		HttpConnection.addListener(new HttpConnectionResponseListener() {
			@Override
			public void receivedResponse(URL url, String response) {
				switch (url) {
				case GET_REPOSITORIES:
					reposList = Preferences.getRepositories();
					break;
				default:
					break;
				}
			}
		});
		radioButtonListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				for(Button radioButton: radioButtonList){
					if(radioButton.getId() == v.getId()){
						for(Repository repo : reposList){
							if(repo.getName().equals(radioButton.getText())){
								Preferences.setSelectedRepository(repo);
							}
						}
					}
				}
				
			}
		};
		((Button) rootView.findViewById(R.id.button_select_repo_refresh)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AsyncTask<Void, Void, Void>(){
        			@Override
        			protected Void doInBackground(Void... params) {
						HttpConnection.requestRepositories();
        				return null;
        			}
        		}.execute();
				};
			}
		);
        return rootView;
    }
	
	private void updateGUI(){
		for(Repository repo : reposList){
			boolean buttonExist = false;
			for(Button button : radioButtonList){
				if(repo.getName().equals(button.getText())){
					buttonExist = true;
					break;
				}
			}
			if(!buttonExist){
				RadioButton button = new RadioButton(rootView.getContext());
				button.setText(repo.getName());
				button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
				button.setOnClickListener(radioButtonListener);
				radioGroup.addView(button);
				radioButtonList.add(button);
			}
		}
	}
	
	
	@Override
	public void onResume(){
		updateGUI();
		super.onResume();
	}
	/*
layout = (LinearLayout) findViewById(R.id.statsviewlayout);

	 */
}