package se.agile.asynctasks;

import se.agile.githubdata.Commit;
import se.agile.model.JSONParser;

public class RequestFullCommit extends RequestTask<String, Void, Commit>{
	
	private String url = "https://api.github.com/repos/";
	
	public RequestFullCommit(){
		super();
	}
	
	public RequestFullCommit(RequestListener<Commit> listener){
		super(listener);
		url +=  super.getSelectedRepositoryName() + "/commits/";
	}
	
	@Override
	protected Commit doInBackground(String... params) {
		String sha = params[0]; 
		String branchName = params[1];
		return JSONParser.parseCommit(generalGETRequest(url + sha), branchName);
	}
	@Override
	protected void onPostExecute(Commit commit){
		if(!isCancelled()){
			finishedWithRequest(commit);
		}
	}
}