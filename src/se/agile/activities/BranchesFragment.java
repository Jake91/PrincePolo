package se.agile.activities;	

import java.util.ArrayList;

import se.agile.activities.model.GitHubData.Branch;
import se.agile.asynctasks.RequestBranches;
import se.agile.model.NotificationHandler;
import se.agile.model.Preferences;
import se.agile.model.NotificationHandler;
import se.agile.model.TemporaryStorage;
import se.agile.model.Preferences.PREF_KEY;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class BranchesFragment extends ListFragment {
	
	private RequestBranches requestBranches;
	private final static String logTag = "PrincePolo";
	private NotificationHandler notificationHandler;

	  @Override
	  public void onActivityCreated(Bundle savedInstanceState) 
	  {
	    super.onActivityCreated(savedInstanceState);
	    
	    RequestListener listener = new RequestListener
	    
	    
	    
	    //ArrayList<Branch> list = TemporaryStorage.branchList;
		String listString = "";
	
		for (Branch s : list)
		{
			listString += s.getName() + "\t";
		}
	    String[] values = new String[] { "Master", "thor", "thor2", "test" };
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), 
	    		android.R.layout.simple_list_item_1, values);
	    setListAdapter(adapter);
	  }

	  @Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		  String item = (String) getListAdapter().getItem(position);
		  Toast.makeText(getActivity(), item + " selected", Toast.LENGTH_LONG).show();
			
		  ArrayList<Branch> list = TemporaryStorage.branchList;
		  
		  String listString = "";

		  for (Branch s : list)
		  {
		      listString += s.getName() + "\t";
		  }

		  Log.d(logTag, ""+listString);
	    	//Log.d(logTag, ""+ allBranches);
	  }
	} 