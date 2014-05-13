package se.agile.activities;

import se.agile.activities.model.GitHubData.Branch;
import se.agile.activities.model.GitHubData.Commit;
import se.agile.activities.model.GitHubData.File;
import se.agile.model.Tuple;
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

public class ConflictFragment extends Fragment 
{
	private String logTag;
	private View rootView;
	private Tuple<File, Commit> tuple;
	public ConflictFragment(){

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		logTag = getResources().getString(R.string.logtag_main);
		rootView = inflater.inflate(R.layout.fragment_conflict, container, false);

		if(tuple != null){
			TextView textView = (TextView) rootView.findViewById(R.id.conflict_Message);
			textView.setText(tuple.first.toString());
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
		((RelativeLayout) rootView.findViewById(R.id.conflict_View)).setOnClickListener(viewListener);
		return rootView;
	}


	public Tuple<File, Commit> getTuple() {
		return tuple;
	}


	public void setTuple(Tuple<File, Commit> tuple) {
		this.tuple = tuple;
	}
	

}

