package se.agile.activities;

import se.agile.activities.model.GitHubData.Branch;
import se.agile.princepolo.R;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BranchFragment extends Fragment 
{
	private String logTag;
	private CharSequence text;
	private Branch branch;
	private View rootView;

	public BranchFragment(){

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		logTag = getResources().getString(R.string.logtag_main);
		rootView = inflater.inflate(R.layout.fragment_branch, container, false);

		if(branch != null){
			TextView textView = (TextView) rootView.findViewById(R.id.Branch_Message);
			textView.setText(branch.toString());
		}
		android.view.View.OnClickListener viewListener = new View.OnClickListener() 
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
		((RelativeLayout) rootView.findViewById(R.id.Branch_View)).setOnClickListener(viewListener);
		return rootView;
	}


	public Branch getBranch() {
		return branch;
	}


	public void setBranch(Branch branch) {
		this.branch = branch;
	}


}

