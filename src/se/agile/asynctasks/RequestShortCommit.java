package se.agile.asynctasks;

import se.agile.githubdata.Commit;
import se.agile.model.JSONParser;
import android.content.Context;

public class RequestShortCommit extends RequestTask<String, Void, Commit>{
	
	private String url = "https://api.github.com/repos/";
	
	public RequestShortCommit(){
		super();
	}
	
	public RequestShortCommit(RequestListener<Commit> listener){
		super(listener);
		url +=  super.getSelectedRepositoryName() + "/git/commits/";
	}
	
	public RequestShortCommit(RequestListener<Commit> listener, Context context){
		super(listener, context);
		url +=  super.getSelectedRepositoryName() + "/git/commits/";
	}
	
	@Override
	protected Commit doInBackground(String... params) {
		String sha = params[0]; 
		String branchName = params[1];
		return JSONParser.parseCommit(generalGETRequest(url + sha), branchName);
	}
	@Override
	protected void onPostExecute(Commit commit){
		finishedWithRequest(commit);
	}
}