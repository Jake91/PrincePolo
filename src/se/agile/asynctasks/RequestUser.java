package se.agile.asynctasks;

import se.agile.activities.model.GitHubData.User;
import se.agile.model.JSONParser;
import se.agile.model.Preferences;

public class RequestUser extends RequestTask<String, Void, User>{
	private final String logTag = "PrincePolo";
	
	private final String URL = "https://api.github.com/user";
	
	public RequestUser(){
		super();
	}
	
	public RequestUser(RequestListener<User> listener){
		super(listener);
	}
	
	@Override
	protected User doInBackground(String... params) {
		return JSONParser.parseUser(generalGETRequest(URL));
	}
	@Override
	protected void onPostExecute(User user){
		if(!isCancelled()){
			Preferences.setUser(user);
			finishedWithRequest(user);
		}
		
	}
	
}
