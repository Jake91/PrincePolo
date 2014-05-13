package se.agile.activities;

import java.util.ArrayList;
import java.util.Collections;

import se.agile.activities.model.GitHubData.File;
import se.agile.model.Preferences;
import se.agile.model.TemporaryStorage;
import se.agile.model.WorkingFileListArrayAdapter;
import se.agile.princepolo.R;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class SelectWorkingFilesOverviewFragment extends Fragment{
	private String logTag;
	private View rootView;
	private WorkingFileListArrayAdapter fileAdapter;
	private OnClickListener buttonAddListener;
	
	
	/**
	 * The system calls this when creating the fragment. 
	 * Within your implementation, you should initialize 
	 * essential components of the fragment that you want 
	 * to retain when the fragment is paused or stopped, then resumed.
	 */
	@Override
	public void onCreate (Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		ArrayList<File> fileList = TemporaryStorage.workingFiles;
		Collections.sort(fileList);
		fileAdapter = new WorkingFileListArrayAdapter(getActivity(), fileList);
		buttonAddListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),SelectWorkingFilesActivity.class);
				getActivity().startActivity(intent);
				
			}
		};
	}

	/**
	 * The system calls this when it's time for the fragment to draw its user
	 * interface for the first time. To draw a UI for your fragment, you must
	 * return a View from this method that is the root of your fragment's
	 * layout. You can return null if the fragment does not provide a UI.
	 * 
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		logTag = getResources().getString(R.string.logtag_main);
		rootView = inflater.inflate(R.layout.fragment_select_working_files_overview, container, false);
		Button addButton = (Button) rootView.findViewById(R.id.working_add_button);
		addButton.setOnClickListener(buttonAddListener);
		ListView listview = (ListView) rootView.findViewById(R.id.working_files_list_view);
		listview.setAdapter(fileAdapter);
		return rootView;
	}
	
	public void onResume(){
		super.onResume();
		fileAdapter.notifyDataSetChanged();
	}
	
	
	/**
	 * The system calls this method as the first indication that the user is
	 * leaving the fragment (though it does not always mean the fragment is
	 * being destroyed). This is usually where you should commit any changes
	 * that should be persisted beyond the current user session (because the
	 * user might not come back).
	 */
	@Override
	public void onPause(){
		super.onPause();
	}
	
	
	/**
	 * The fragment is not visible. Either the host activity has been stopped or
	 * the fragment has been removed from the activity but added to the back
	 * stack. A stopped fragment is still alive (all state and member
	 * information is retained by the system). However, it is no longer visible
	 * to the user and will be killed if the activity is killed.
	 */
	@Override
	public void onStop(){
		super.onPause();
	}
	
	
	/**
	 * Called when the view hierarchy associated with the fragment is being removed.
	 */
	@Override
	public void onDestroyView(){
		super.onDestroyView();
	}
}
