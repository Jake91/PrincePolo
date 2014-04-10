package se.agile.activities.model;

import java.util.ArrayList;

import se.agile.activities.model.GitHubData.Branch;

public class RequestBranches extends RequestTask<Void, Void, ArrayList<Branch>>{
	private final String logTag = "PrincePolo";
	
	private final String URL = "https://api.github.com/repos/Jake91/PrincePolo/branches";
	
	public RequestBranches(){
		super();
	}
	
	public RequestBranches(RequestListener listener){
		super(listener);
	}
	
	@Override
	protected ArrayList<Branch> doInBackground(Void... params) {
		return JSONParser.parseBranches(generalGETRequest(URL));
	}
	@Override
	protected void onPostExecute(ArrayList<Branch> branchList){
		finishedWithRequest(branchList);
	}
}