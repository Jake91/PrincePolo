package se.agile.asynctasks;

import java.util.ArrayList;

import android.content.Context;

import se.agile.githubdata.Branch;
import se.agile.model.JSONParser;

public class RequestBranches extends RequestTask<Void, Void, ArrayList<Branch>>{
	
	private String url = "https://api.github.com/repos/";
	
	public RequestBranches(){
		super();
	}
	
	public RequestBranches(RequestListener<ArrayList<Branch>> listener){
		super(listener);
		url +=  super.getSelectedRepositoryName() + "/branches";
	}
	
	public RequestBranches(RequestListener<ArrayList<Branch>> listener, Context context){
		super(listener, context);
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