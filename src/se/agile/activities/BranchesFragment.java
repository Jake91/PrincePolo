package se.agile.activities;  

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import se.agile.activities.MainActivity.VIEW;
import se.agile.activities.model.GitHubData.Branch;
import se.agile.asynctasks.RequestBranches;
import se.agile.asynctasks.RequestListenerAdapter;
import se.agile.model.BranchSelectionModel;
import se.agile.model.Preferences;
import se.agile.model.InteractiveArrayAdapter;
import se.agile.princepolo.R;
import android.app.ListFragment;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ProgressBar;
import android.widget.TextView;
public class BranchesFragment extends ListFragment 

{
	ArrayList<Branch> allBranches;
	private final static String logTag = "PrincePolo";
	public static boolean unselectButton;
	Button selectionButton;
	private ProgressBar spinner;
	private TextView tv;
	private CheckBox checkbox;
	
	RequestListenerAdapter<ArrayList<Branch>> listener = new RequestListenerAdapter<ArrayList<Branch>>() 
	{
		@Override
		public void requestFinished(ArrayList<Branch> result) 
		{
			allBranches = result;
			// Create an array of strings, that will be put to our ListActivity
			ArrayAdapter<BranchSelectionModel> adapter = new InteractiveArrayAdapter(getActivity(), getModel());
			setListAdapter(adapter);
			
			spinner.setVisibility(View.GONE);
			selectionButton.setVisibility(View.VISIBLE);
			tv.setVisibility(View.VISIBLE);
		}
	};
	
	@Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) 
	{
	    View rootView = inflater.inflate(R.layout.fragment_branches, container, false);
        spinner = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);
        tv = (TextView) rootView.findViewById(R.id.selecttext);
        tv.setVisibility(View.INVISIBLE);
        selectionButton = (Button) rootView.findViewById(R.id.unselectButton);
        selectionButton.setVisibility(View.INVISIBLE);
        checkbox = (CheckBox) rootView.findViewById(R.id.chkAll);

        Button nextButton = (Button) rootView.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), SelectWorkingFilesActivity.class);
				startActivity(intent);
				((MainActivity) getActivity()).displayView(VIEW.REPOSITORY_OVERVIEW);
				
			}
		});
        
		RequestBranches reqbranches = new RequestBranches(listener);
		reqbranches.execute();
		
		unselectButton = true;
		
		
		if (checkbox.isChecked())
//		      checkbox.toggle(); Just removed this and it seemed to work fine /JL

		    checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() 
		    {

		      public void onCheckedChanged(CompoundButton arg0, boolean isChecked) 
		      {
		      		RequestBranches reqbranches = new RequestBranches(listener);
		      		reqbranches.execute();
		      		
		    	  if (checkbox.isChecked())
		    	  {
	      	          unselectButton = true;
	      	          Preferences.removeAllBranches();	  
	      	          
		    	  }
		    	  else
		    	  {

	      	          unselectButton = false;
	      	          
	        			// All branches in an arraylist string
	        			List<String> namesOfAllBranches = new ArrayList<String>();
	      			for (int i = 0; i < allBranches.size(); i++)
	      			{
	      				namesOfAllBranches.add(allBranches.get(i).getName());
	      			}
	        			Preferences.setUnselectedBranchesArray(namesOfAllBranches);
		    	  }
		      }
		    });

		

        // create our incredible click listener
        OnClickListener selectionClickListener = new OnClickListener() 
        {
          @Override
          public void onClick(View v) 
          {
      		//ArrayList<Branch> allBranches = new ArrayList<Branch>();
      		RequestBranches reqbranches = new RequestBranches(listener);
      		reqbranches.execute();
      		
      		if (selectionButton.getText().equals("Unselect all")) 
      		{
      	          selectionButton.setText("Select all");
      	          unselectButton = false;
      	          
      			// All branches in an arraylist string
      			List<String> namesOfAllBranches = new ArrayList<String>();
    			for (int i = 0; i < allBranches.size(); i++)
    			{
    				namesOfAllBranches.add(allBranches.get(i).getName());
    			}
      			Preferences.setUnselectedBranchesArray(namesOfAllBranches);
      			//Log.d(logTag,Arrays.toString(namesOfAllBranches.toArray()));
  	        } 
      		else 
      		{
      	          selectionButton.setText("Unselect all");
      	          unselectButton = true;
      	          Preferences.removeAllBranches();	          
  	        }
          }
        };
    
        // assign click listener to the OK button (btnOK)
        selectionButton.setOnClickListener(selectionClickListener);
	        
        return rootView;
	}
	
	// The branch selection model
	private List<BranchSelectionModel> getModel() 
	{
		List<BranchSelectionModel> list = new ArrayList<BranchSelectionModel>();
		ArrayList<Branch> allUnselectedBranches = Preferences.getUnselectedBranches();
		
		// Adds checkboxes for all branches
		for (Branch s : allBranches)
		{
			list.add(get(s.getName()));
		}
		
		// Not finished
		if (unselectButton == false)
		{
			return list;
		}
		else
		{
		// If no branches have been saved to preferences, then select all the checkboxes
		if (allUnselectedBranches.isEmpty())
		{
			for (int i = 0; i< list.size(); i++)
			{
				list.get(i).setSelected(true);
			}
			selectionButton.setText("Unselect all");
		}
		
		// If branches have been saved to preferences, then select just them
		else
		{
			// Unselected branches in an arraylist string
			ArrayList<String> namesOfUnselectedBranches = new ArrayList<String>();
			for (int i = 0; i < allUnselectedBranches.size(); i++)
			{
				namesOfUnselectedBranches.add(allUnselectedBranches.get(i).getName());
			}
			
			// All branches in an arraylist string
			ArrayList<String> namesOfAllBranches = new ArrayList<String>();
			for (int i = 0; i < allBranches.size(); i++)
			{
				namesOfAllBranches.add(allBranches.get(i).getName());
			}
			
			// Pick those branches which belong to both arraylists ..		
			for (int i = 0; i < namesOfAllBranches.size(); i++)
			{
				if (!namesOfUnselectedBranches.contains(namesOfAllBranches.get(i)))
				{
					list.get(i).setSelected(true);
				}
			}
			
		}
		
		return list;
		}
	}
	
	private BranchSelectionModel get(String s) 
	{
		return new BranchSelectionModel(s);
	}
} 