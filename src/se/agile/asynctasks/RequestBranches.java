package se.agile.asynctasks;

import java.util.ArrayList;

import android.util.Log;

import se.agile.activities.model.GitHubData.Branch;
import se.agile.model.JSONParser;
import se.agile.model.Preferences;

public class RequestBranches extends RequestTask<Void, Void, ArrayList<Branch>>{
	private final String logTag = "PrincePolo";
	
	private String url = "https://api.github.com/repos/";
	
	public RequestBranches(){
		super();
	}
	
	public RequestBranches(RequestListener listener){
		super(listener);
		url +=  super.getSelectedRepositoryName() + "/branches";
	}
	
	@Override
	protected ArrayList<Branch> doInBackground(Void... params) {
		return JSONParser.parseBranches(generalGETRequest(url));
	}
	@Override
	protected void onPostExecute(ArrayList<Branch> branchList){
		if(!isCancelled()){
			finishedWithRequest(branchList);
		}
	}
}