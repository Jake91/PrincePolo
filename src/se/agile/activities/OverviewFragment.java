package se.agile.activities;	

import java.util.ArrayList;
import java.util.Date;

import se.agile.asynctasks.RequestFullCommit;
import se.agile.asynctasks.RequestLatestCommits;
import se.agile.asynctasks.RequestListener;
import se.agile.asynctasks.RequestListenerAdapter;
import se.agile.githubdata.Commit;
import se.agile.githubdata.Repository;
import se.agile.githubdata.User;
import se.agile.model.BranchNotification;
import se.agile.model.CommitNotification;
import se.agile.model.ConflictNotification;
import se.agile.model.Notification;
import se.agile.model.NotificationHandler;
import se.agile.model.PreferenceListener;
import se.agile.model.Preferences;
import se.agile.model.Preferences.PREF_KEY;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OverviewFragment extends Fragment 
{
	private String logTag;
	private View rootView;
	private User user;
	private Repository selectedRepository;
	private ArrayList<Commit> latestCommits; 
	private OnClickListener commitButtonListener;
	
	private LayoutInflater inflater;
	
	public OverviewFragment(){}
	
	private PreferenceListener prefListener;
	
	@Override
	public void onCreate (Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		prefListener = new PreferenceListener() {
			
			@Override
			public void preferenceChanged(PREF_KEY key) {
				switch(key){
				case USER_NAME:
					user = Preferences.getUser();
					break;
				case SELECTED_REPOSITORY:
					selectedRepository = Preferences.getSelectedRepository();
					break;
				default:
					break;
				
				}
				
			}
		};
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{	
		this.inflater = inflater;
		logTag = getResources().getString(R.string.logtag_main);
        View rootView = inflater.inflate(R.layout.fragment_overview, container, false);
        this.rootView = rootView;
        user = Preferences.getUser();
        selectedRepository = Preferences.getSelectedRepository();
        latestCommits = new ArrayList<Commit>();
        RequestListener<ArrayList<Commit>> requestListener = new RequestListenerAdapter<ArrayList<Commit>>() {
			
			@Override
			public void whenNoInternetConnection() {
				MainActivity.hasNoInternetConnection(getActivity());
				
			}
			
			@Override
			public void requestFinished(ArrayList<Commit> result) {
				latestCommits = result;
				updateElements();
				
			}
		};
		
		commitButtonListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Object ob = v.getTag();
				if(ob instanceof Commit){
					Commit commit = (Commit) ob;
					RequestListener<Commit> list = new RequestListenerAdapter<Commit>() {

						@Override
						public void requestFinished(Commit result) {
							CommitFragment commitFragment = new CommitFragment();
		         			commitFragment.setCommit(result);
		         			FragmentManager fm = getFragmentManager();
		         	        FragmentTransaction transaction = fm.beginTransaction();
		         	        transaction.addToBackStack(null);
		         	        transaction.replace(R.id.content_overview_holder, commitFragment);
		         	        transaction.commit();
						}
					};
					
					RequestFullCommit req = new RequestFullCommit(list, getActivity());
					req.execute(commit.getSha(), "master");
				}
			}
		};
		
		
		Preferences.addListener(prefListener);
        
		RequestLatestCommits request = new RequestLatestCommits(requestListener);
		request.execute();
        
        return rootView;
    }
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
	public void onResume(){
		super.onResume();
		updateElements();
	}
	
	private void updateElements(){
		TextView user = (TextView) rootView.findViewById(R.id.overview_user);
		TextView repository = (TextView) rootView.findViewById(R.id.overview_repository);
		user.setText(this.user.getName());
		repository.setText(this.selectedRepository.getName());
		LinearLayout list = (LinearLayout) rootView.findViewById(R.id.overview_commits_list);
		list.removeAllViews();
		for(Commit commit : latestCommits){
			addRowView(commit);
		}
		
	}
	
	private void addRowView(Commit commit){
		ViewGroup parent = (ViewGroup) rootView.findViewById(R.id.overview_commits_list);
		View rowView = inflater.inflate(R.layout.fragment_overview_row, parent, false);
		
		TextView title = (TextView) rowView.findViewById(R.id.overivew_row_title);
		TextView message = (TextView) rowView.findViewById(R.id.overivew_row_message);
		TextView date = (TextView) rowView.findViewById(R.id.overivew_row_date);
		
		title.setText("Commited by " + commit.getCommitter().getName());
		message.setText(commit.getMessage());
		Date dateObj = commit.getDate();
		Context context = getActivity();
		String formatedDate = DateFormat.getDateFormat(context).format(dateObj) + " " + DateFormat.getTimeFormat(context).format(dateObj);
		date.setText(formatedDate);
		
		rowView.setOnClickListener(commitButtonListener);
		rowView.setTag(commit);
		parent.addView(rowView);
	}
	
	public void OnStop(){
		Preferences.removeListener(prefListener);
		super.onStop();
	}
}
