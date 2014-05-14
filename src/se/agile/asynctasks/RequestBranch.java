package se.agile.asynctasks;

import se.agile.githubdata.Branch;
import se.agile.model.JSONParser;
import android.util.Log;

public class RequestBranch extends RequestTask<String, Void, Branch>{
	private final String logTag = "PrincePolo";
	
	private String url = "https://api.github.com/repos/";
	
	public RequestBranch(){
		super();
	}
	
	public RequestBranch(RequestListener<Branch> listener){
		super(listener);
		url +=  super.getSelectedRepositoryName() + "/branches/";
	}
	
	@Override
	protected Branch doInBackground(String... params) {
		String branch = params[0]; 
		return JSONParser.parseBranch(generalGETRequest(url + branch));
	}
	@Override
	protected void onPostExecute(Branch branch){
		if(!isCancelled()){
			if(branch == null){
				Log.d(logTag, "null!!!!!!");
			}
			finishedWithRequest(branch);
		}
	}
}