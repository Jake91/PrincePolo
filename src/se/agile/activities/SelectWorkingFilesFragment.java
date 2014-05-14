package se.agile.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import se.agile.asynctasks.RequestBranches;
import se.agile.asynctasks.RequestListener;
import se.agile.asynctasks.RequestListenerAdapter;
import se.agile.githubdata.Branch;
import se.agile.githubdata.Directory;
import se.agile.githubdata.File;
import se.agile.githubdata.Folder;
import se.agile.model.DirectoryListArrayAdapter;
import se.agile.model.Preferences;
import se.agile.model.TemporaryStorage;
import se.agile.princepolo.R;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class SelectWorkingFilesFragment extends Fragment{
	private String logTag;
	private View rootView;
	private static String selectedBranch, browsePath, currentRepo;
	private static ArrayList<String> branchNameList;
	private String currentPath, earlierBranch;
	
	private ArrayAdapter<String> branchAdapter;
	private RequestBranches reqBranches;
	private OnItemSelectedListener branchSelectedListener;
	
	private ArrayList<Directory> directoryList;
	private DirectoryListArrayAdapter directoryAdapter;
	private OnItemClickListener folderClickListener;
	
	private OnClickListener checkListener;
	private OnClickListener doneButtonListener;
	
	private boolean isSpinnerListenerLocked;
	
	private Context context;
	
	public static void resetStaticVariables(){
		selectedBranch = null;
		browsePath = null;
		currentRepo = null;
		branchNameList = null;
	}
	
	/**
	 * The system calls this when creating the fragment. 
	 * Within your implementation, you should initialize 
	 * essential components of the fragment that you want 
	 * to retain when the fragment is paused or stopped, then resumed.
	 */
	@Override
	public void onCreate (Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//----------- Branches -------------------
		context = getActivity();
		RequestListener<ArrayList<Branch>> reqBranchesListener = new RequestListenerAdapter<ArrayList<Branch>>() {

			@Override
			public void requestFinished(ArrayList<Branch> result) {
				for(Branch branch : result){
					branchNameList.add(branch.getName());
				}
				Collections.sort(branchNameList, new Comparator<String>() {

					@Override
					public int compare(String lhs, String rhs) {
						return lhs.toLowerCase().compareTo(rhs.toLowerCase());
					}
				});
				branchAdapter.notifyDataSetChanged();
				setSpinnerSelection();
			}

			@Override
			public void requestUpdate() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void whenNoInternetConnection() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void whenNoSelectedRepository() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void requestFailed() {
				// TODO Auto-generated method stub
				
			}
			
		};
		reqBranches = new RequestBranches(reqBranchesListener);
		//Initialize static variables
		if(branchNameList == null){
			branchNameList = new ArrayList<String>();
			selectedBranch = "master";
			browsePath = "";
			currentRepo = "";
		}
		branchAdapter = new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_spinner_dropdown_item, branchNameList);
		branchSelectedListener = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
				if(isSpinnerListenerLocked){
					isSpinnerListenerLocked = false;
				}else{
					earlierBranch = selectedBranch;
					selectedBranch = branchAdapter.getItem(pos);
					directoryAdapter.changeBranchAndUpdateData(selectedBranch);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		};
		
		//---------- Directories --------------
		
		RequestListener<ArrayList<Directory>> reqDirectoriesListener = new RequestListenerAdapter<ArrayList<Directory>>() {

			@Override
			public void requestFinished(ArrayList<Directory> result) {
				directoryList.clear();
				for(Directory dir : result){
					directoryList.add(dir);
				}
				Collections.sort(directoryList);
				directoryAdapter.notifyDataSetChanged();
			}

			@Override
			public void requestUpdate() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void whenNoInternetConnection() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void whenNoSelectedRepository() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void requestFailed() {
				new AlertDialog.Builder(context).setTitle("Path doesn't exist for branch")
						.setMessage("The selected path doesn't exist for the current path you are in. Changing back to earlier branch.")
						.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								selectedBranch = earlierBranch;
								directoryAdapter.changeBranchAndUpdateData(selectedBranch);
								setSpinnerSelection();
							}
						}).show();
			}
		};
		currentPath = browsePath;
		earlierBranch = selectedBranch;
		
		checkListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Object ob = v.getTag();
				if(ob instanceof File){
					File file = (File) ob;
					if(file.isWorkingFile()){
						file.switchIsWorkingFile();
						TemporaryStorage.workingFiles.remove(file);
					}else{
						file.switchIsWorkingFile();
						TemporaryStorage.workingFiles.add(file);
					}
					
				}
				
			}
		};
		
		directoryList = new ArrayList<Directory>();
		directoryAdapter = new DirectoryListArrayAdapter(getActivity(), directoryList, selectedBranch, currentPath, reqDirectoriesListener, checkListener);
		folderClickListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				Directory dir = directoryAdapter.getDirectory(position);
				if(dir instanceof Folder){
					browsePath = dir.getPath();
					Fragment fragment = new SelectWorkingFilesFragment();
					FragmentManager fm = getFragmentManager();
					FragmentTransaction transaction = fm.beginTransaction();
					transaction.addToBackStack(null);
					transaction.replace(R.id.content_directory_holder, fragment);
					transaction.commit();
				}
			}
        };
        doneButtonListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().finish();
				getActivity().overridePendingTransition(0, 0);
				
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
		rootView = inflater.inflate(R.layout.fragment_directory_list_view, container, false);
		if(currentRepo.equals("")){
			currentRepo = Preferences.getSelectedRepository().getName().split("/")[1];
		}else{
			String repo = Preferences.getSelectedRepository().getName().split("/")[1];
			//if another repo has been selected is changed
			if(!currentRepo.equals(repo)){
				branchNameList.clear();
				reqBranches.execute();
				currentRepo = repo;
			}
		}
		
		//----- Branches -----
		
		if(branchNameList.isEmpty()){
			reqBranches.execute();
		}
		Spinner spinner = (Spinner) rootView.findViewById(R.id.directory_spinner);
		spinner.setAdapter(branchAdapter);
		spinner.setOnItemSelectedListener(branchSelectedListener);
		
		//----- Directories -----
		ListView listview = (ListView) rootView.findViewById(R.id.directory_list_view);
		directoryAdapter.changeBranch(selectedBranch);
		listview.setAdapter(directoryAdapter);
		listview.setOnItemClickListener(folderClickListener);
		Button doneButton = (Button) rootView.findViewById(R.id.directory_done_button);
		doneButton.setOnClickListener(doneButtonListener);
		setPath();
        
		return rootView;
	}
	
	private void setSpinnerSelection(){
		isSpinnerListenerLocked = true;
		Spinner spinner = (Spinner) rootView.findViewById(R.id.directory_spinner);
		spinner.setSelection(branchAdapter.getPosition(selectedBranch));
	}
	private void setPath(){
		TextView path = (TextView) rootView.findViewById(R.id.directory_path);
		path.setText("Path: " + currentRepo + "/" + (currentPath.equals("") ? "" : currentPath + "/"));
	}
	
	public void onResume(){
		super.onResume();
		setSpinnerSelection();
	}
}
