package se.agile.activities.model;

import se.agile.activities.model.GitHubData.User;

public class RequestUser extends RequestTask<String, Void, User>{
	private final String logTag = "PrincePolo";
	
	private final String URL = "https://api.github.com/user";
	
	public RequestUser(){
		super();
	}
	
	public RequestUser(RequestListener listener){
		super(listener);
	}
	
	@Override
	protected User doInBackground(String... params) {
		return JSONParser.parseUser(generalGETRequest(URL));
	}
	@Override
	protected void onPostExecute(User user){
		Preferences.setUser(user);
		finishedWithRequest(user);
	}
	
}
