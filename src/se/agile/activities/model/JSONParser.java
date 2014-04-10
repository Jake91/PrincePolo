package se.agile.activities.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import se.agile.activities.model.GitHubData.Branch;
import se.agile.activities.model.GitHubData.Repository;
import se.agile.activities.model.GitHubData.User;

import android.util.Log;

public class JSONParser {
	private final static String logTag = "PrincePolo";

	public static ArrayList<Branch> parseBranches(String json){
		ArrayList<Branch> list = new ArrayList<Branch>();
		Log.d(logTag, "Whole string: " + json);
		StringBuilder builder = new StringBuilder();
		try {
			JSONArray jsonArray = new JSONArray(json);
			for(int i = 0; i < jsonArray.length(); i++){
				Branch branch = new Branch(jsonArray.getJSONObject(i).getString("name"));
				branch.setUrl(jsonArray.getJSONObject(i).getJSONObject("commit").getString("url"));
				branch.setSha(jsonArray.getJSONObject(i).getJSONObject("commit").getString("sha"));
				list.add(branch);
			}
		} catch (JSONException e) {
			Log.e(logTag, "Couldn't parse JSON String to JSONArray");
			e.printStackTrace();
		}
		return list;
	}
	
	public static ArrayList<Repository> parseRepositories(String json){
		ArrayList<Repository> list = new ArrayList<Repository>();
		try {
			Log.d(logTag, "json: " + json);
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				try{
					list.add(new Repository(jsonObject.getString("full_name")));
				}catch(JSONException e){
					e.printStackTrace();
				}
			}
		} catch (JSONException e) {
			Log.e(logTag, "Couldn't parse JSON String to JSONArray");
			e.printStackTrace();
		}
		return list;
	}
	
	public static User parseUser(String json){
		if(json == null){
			return null;
		}else{
			User user = null;
			try {
				JSONObject object = new JSONObject(json);
				user = new User(object.getString("login"));
			} catch (JSONException e) {
				Log.e(logTag, "Error in interpreting JSON");
				e.printStackTrace();
			}
			return user;
		}
		
	}
	
}
