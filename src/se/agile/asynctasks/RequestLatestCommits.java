package se.agile.asynctasks;

import java.util.ArrayList;

import android.content.Context;
import se.agile.githubdata.Commit;
import se.agile.model.JSONParser;

public class RequestLatestCommits extends RequestTask<Void, Void, ArrayList<Commit>>{
	
	private String url = "https://api.github.com/repos/";
	
	public RequestLatestCommits(){
		super();
	}
	
	public RequestLatestCommits(RequestListener<ArrayList<Commit>> listener){
		super(listener);
		url +=  super.getSelectedRepositoryName() + "/commits";
	}
	
	public RequestLatestCommits(RequestListener<ArrayList<Commit>> listener, Context context){
		super(listener, context);
		url +=  super.getSelectedRepositoryName() + "/commits";
	}
	
	@Override
	protected ArrayList<Commit> doInBackground(Void... params) {
		return JSONParser.parseLatestCommits(generalGETRequest(url));
	}
	@Override
	protected void onPostExecute(ArrayList<Commit> commits){
		finishedWithRequest(commits);
	}
}