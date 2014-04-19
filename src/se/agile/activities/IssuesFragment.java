package se.agile.activities;	

import se.agile.asynctasks.RequestBranch;
import se.agile.asynctasks.RequestCommit;
import se.agile.asynctasks.RequestListener;
import se.agile.princepolo.R;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class IssuesFragment extends Fragment implements RequestListener 
{
	private String logTag;
	
	private RequestBranch requestBranch;
	private RequestCommit requestCommit;
	public IssuesFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		logTag = getResources().getString(R.string.logtag_main);
        View rootView = inflater.inflate(R.layout.fragment_issues, container, false);
        
        //Just testing the requests
        requestBranch = new RequestBranch(this);
        requestBranch.execute("jake");
        
        return rootView;
    }

	@Override
	public void requestFinished() {
		Log.d(logTag, "n" + requestBranch.getResult().getName());
		if(requestCommit == null){
			requestCommit = new RequestCommit(this);
			requestCommit.execute(requestBranch.getResult().getLatestCommit().getSha());
		}else{
			Log.d(logTag, requestCommit.getResult().toString());
		}
	}

	@Override
	public void whenNoInternetConnection() {
		MainActivity.hasNoInternetConnection(getActivity());
		
	}

	@Override
	public void whenNoSelectedRepository() {
		MainActivity.hasNoSelectedRepository(getActivity());
		
	}
}
