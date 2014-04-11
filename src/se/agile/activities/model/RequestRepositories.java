package se.agile.activities.model;

import java.util.ArrayList;

import se.agile.activities.model.GitHubData.Repository;

public class RequestRepositories extends RequestTask<Void, Void, ArrayList<Repository>>{
	private final String logTag = "PrincePolo";
	
	private final String URL = "https://api.github.com/user/repos";
	
	public RequestRepositories(){
		super();
	}
	
	public RequestRepositories(RequestListener listener){
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
