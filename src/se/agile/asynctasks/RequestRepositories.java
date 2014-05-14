package se.agile.asynctasks;

import java.util.ArrayList;

import se.agile.githubdata.Repository;
import se.agile.model.JSONParser;
import se.agile.model.Preferences;

public class RequestRepositories extends RequestTask<Void, Void, ArrayList<Repository>>{
	
	private final String URL = "https://api.github.com/user/repos";
	
	public RequestRepositories(){
		super();
	}
	
	public RequestRepositories(RequestListener<ArrayList<Repository>> listener){
		super(listener);
	}
	
	@Override
	protected ArrayList<Repository> doInBackground(Void... params) {
		return JSONParser.parseRepositories(generalGETRequest(URL));
	}
	@Override
	protected void onPostExecute(ArrayList<Repository> repoList){
		if(!isCancelled()){
			Preferences.setRepositories(repoList);
			finishedWithRequest(repoList);
		}
	}
	
}
