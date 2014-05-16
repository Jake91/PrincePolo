package se.agile.asynctasks;

import android.content.Context;
import se.agile.githubdata.User;
import se.agile.model.JSONParser;
import se.agile.model.Preferences;

public class RequestUser extends RequestTask<String, Void, User>{
	
	private final String URL = "https://api.github.com/user";
	
	public RequestUser(){
		super();
	}
	
	public RequestUser(RequestListener<User> listener){
		super(listener);
	}
	
	public RequestUser(RequestListener<User> listener, Context context){
		super(listener, context);
	}
	
	@Override
	protected User doInBackground(String... params) {
		return JSONParser.parseUser(generalGETRequest(URL));
	}
	@Override
	protected void onPostExecute(User user){
		if(!isCancelled()){
			Preferences.setUser(user);
		}
		finishedWithRequest(user);
	}
	
}
