package se.agile.asynctasks;

import se.agile.activities.model.GitHubData.Commit;
import se.agile.model.JSONParser;
import se.agile.model.Preferences;

public class RequestCommit extends RequestTask<String, Void, Commit>{
	private final String logTag = "PrincePolo";
	
	private String url = "https://api.github.com/repos/";
	
	public RequestCommit(){
		super();
	}
	
	public RequestCommit(RequestListener listener){
		super(listener);
		url +=  super.getSelectedRepositoryName() + "/commits/";
	}
	
	@Override
	protected Commit doInBackground(String... params) {
		String sha = params[0]; 
		return JSONParser.parseCommit(generalGETRequest(url + sha));
	}
	@Override
	protected void onPostExecute(Commit commit){
		if(!isCancelled()){
			finishedWithRequest(commit);
		}
	}
}