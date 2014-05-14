package se.agile.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import se.agile.activities.model.GitHubData.Commit;
import se.agile.activities.model.GitHubData.File;
import se.agile.princepolo.R;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CommitFragment extends Fragment 
{
	private String logTag;
	private CharSequence text;
	private Commit commit;
	private View rootView;
	private LayoutInflater inflater;
	
	public CommitFragment(){
		
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		logTag = getResources().getString(R.string.logtag_main);
		this.inflater = inflater;
        rootView = inflater.inflate(R.layout.fragment_commit, container, false);
        Context context = (Context) getActivity();
        if(commit != null){
        	TextView committer = (TextView) rootView.findViewById(R.id.commit_committer);
        	TextView date = (TextView) rootView.findViewById(R.id.commit_date);
        	TextView message = (TextView) rootView.findViewById(R.id.Commit_Message);
        	TextView sha = (TextView) rootView.findViewById(R.id.Commit_SHA);
        	TextView totalAdditions = (TextView) rootView.findViewById(R.id.commit_total_additions);
        	TextView totalDeletions = (TextView) rootView.findViewById(R.id.commit_total_deletions);
        	
        	committer.setText("Committer: " + commit.getCommitter().getName());
        	Date dateObj = commit.getDate();
    		String formatedDate = DateFormat.getDateFormat(context).format(dateObj) + " " + DateFormat.getTimeFormat(context).format(dateObj);
        	date.setText(formatedDate);
        	message.setText(commit.getMessage());
        	sha.setText(commit.getSha());
        	ArrayList<File> files = commit.getChangedFiles();
        	Collections.sort(files);
        	int additions = 0;
        	int deletions = 0;
        	for(File file : files){
        		addRowView(file);
        		additions += file.getAdditions();
        		deletions += file.getDeletions();
        	}
        	totalAdditions.setText("Total Additions: " + additions);
        	totalDeletions.setText("Total Deletions: " + deletions);
        }
        android.view.View.OnClickListener backListener = new View.OnClickListener() 
        {
        	@Override
        	public void onClick(View v) 
        	{
        		FragmentManager fm = getFragmentManager();
    	        FragmentTransaction transaction = fm.beginTransaction();
    	        fm.popBackStack();
    	        transaction.commit();
        	}
        };
        ((Button) rootView.findViewById(R.id.commit_back_button)).setOnClickListener(backListener);
        
        
        
        
        return rootView;
    }
	
	private void addRowView(File file){
		ViewGroup parent = (ViewGroup) rootView.findViewById(R.id.commit_changed_files_list);
		View rowView = inflater.inflate(R.layout.fragment_commit_row, parent, false);
		
		if(file.getStatus().equals("added")){
			ImageView icon = (ImageView) rowView.findViewById(R.id.directory_icon);
			icon.setImageResource(R.drawable.file_added);
		}else if(file.getStatus().equals("removed")){
			ImageView icon = (ImageView) rowView.findViewById(R.id.directory_icon);
			icon.setImageResource(R.drawable.file_deleted);
		}
		TextView name = (TextView) rowView.findViewById(R.id.directory_name);
		TextView path = (TextView) rowView.findViewById(R.id.directory_path);
		TextView additions = (TextView) rowView.findViewById(R.id.commit_list_additions);
		TextView deletions = (TextView) rowView.findViewById(R.id.commit_list_deletions);
		name.setText(file.getName());
		path.setText(file.getPath());
		additions.setText(file.getAdditions() + "");
		deletions.setText(file.getDeletions() + "");
		parent.addView(rowView);
	}


	public Commit getCommit() {
		return commit;
	}


	public void setCommit(Commit commit) {
		this.commit = commit;
	}
	
	
}

