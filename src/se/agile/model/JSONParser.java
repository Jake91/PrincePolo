package se.agile.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.agile.activities.model.GitHubData.Branch;
import se.agile.activities.model.GitHubData.Commit;
import se.agile.activities.model.GitHubData.File;
import se.agile.activities.model.GitHubData.Repository;
import se.agile.activities.model.GitHubData.User;
import android.util.Log;

public class JSONParser {
	private final static String logTag = "PrincePolo";

	public static ArrayList<Branch> parseBranches(String json){
		ArrayList<Branch> list = new ArrayList<Branch>();
		if(json != null){
			try {
				JSONArray jsonArray = new JSONArray(json);
				for(int i = 0; i < jsonArray.length(); i++){
					Branch branch = new Branch(jsonArray.getJSONObject(i).getString("name"));
					Commit commit = new Commit(jsonArray.getJSONObject(i).getJSONObject("commit").getString("url"));
					commit.setSha(jsonArray.getJSONObject(i).getJSONObject("commit").getString("sha"));
					commit.setIsComplete(false);
					branch.setLatestCommit(commit);
					list.add(branch);
				}
			} catch (JSONException e) {
				Log.e(logTag, "Couldn't parse JSON String to JSONArray");
				e.printStackTrace();
			}
		}else{
			Log.e(logTag, "parseBranches: json string is null");
		}
		return list;
	}
	
	public static Branch parseBranch(String json){
		Branch branch = null;
		if(json != null){
			try {
				JSONObject object = new JSONObject(json);
				String branchName = object.getString("name");
				String url = "https://api.github.com/repos/" + Preferences.getSelectedRepository().getName() + "/branches/" + branchName;
				branch = new Branch(branchName);
				branch.setUrl(url);
				branch.setLatestCommit(parseCommitObject(object.getJSONObject("commit")));
			} catch (JSONException e) {
				Log.e(logTag, "Couldn't parse JSON String to JSONArray");
				e.printStackTrace();
			}
		}else{
			Log.e(logTag, "json string is null");
		}
		return branch;
	}
	
	private static Commit parseCommitObject(JSONObject object) throws JSONException{
		String sha = object.getString("sha");
		String url = "https://api.github.com/repos/" + Preferences.getSelectedRepository().getName() + "/commits/" + sha;
		Commit commit = new Commit(url);
		commit.setSha(sha);
		
		JSONObject object2 = object.getJSONObject("commit");
		commit.setCommitter(new User(object2.getJSONObject("committer").getString("name")));
		
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); 
	    Date startDate = null;
	    try {
	        startDate = df.parse(object2.getJSONObject("committer").getString("date"));
	    } catch (ParseException e) {
	    	Log.e(logTag, "couldn't parse date for commit");
	        e.printStackTrace();
	    }
		commit.setDate(startDate);
		commit.setMessage(object2.getString("message"));
		
		try{
			ArrayList<Commit> parentCommits = new ArrayList<Commit>();
			JSONArray parentArray = object.getJSONArray("parents");
			for(int i = 0; i < parentArray.length(); i++){
				JSONObject fileObject = parentArray.getJSONObject(i);
				Commit parent = new Commit(fileObject.getString("url"));
				parent.setSha(fileObject.getString("sha"));
				parentCommits.add(parent);
			}
			commit.setParentList(parentCommits);
		}catch(JSONException e){
			Log.d(logTag, "The commit has no Parent");
			e.printStackTrace();
			commit.setIsComplete(false);
		}
		
		
		ArrayList<File> changedFiles = new ArrayList<File>();
		try{
			JSONArray fileArray = object.getJSONArray("files");
			for(int i = 0; i < fileArray.length(); i++){
				JSONObject fileObject = fileArray.getJSONObject(i);
				File file = new File(fileObject.getString("filename"));
				file.setStatus(fileObject.getString("status"));
				try{
					int additions = Integer.parseInt(fileObject.getString("additions"));
					int deletions = Integer.parseInt(fileObject.getString("deletions"));
					int changes = Integer.parseInt(fileObject.getString("changes"));
					file.setAdditions(additions);
					file.setChanges(changes);
					file.setDeletions(deletions);
				}catch(NumberFormatException e){
					Log.e(logTag, "Error while parsing commit integers");
					e.printStackTrace();
				}
				changedFiles.add(file);
			}
			commit.setChangedFiles(changedFiles);
			commit.setIsComplete(true);
		}catch(JSONException e){
			//This data is only available when you get a 'whole'/'complete' commit
			commit.setIsComplete(false);
		}
		
		return commit;
		
	}
	
	public static Commit parseCommit(String json, String branchName){
		Commit commit = null;
		if(json != null){
			try {
				JSONObject object = new JSONObject(json);
				commit = parseCommitObject(object);
				commit.setBranchName(branchName);
			} catch (JSONException e) {
				Log.e(logTag, "Couldn't parse commit");
				e.printStackTrace();
			}
		}else{
			Log.e(logTag, "parseCommit: json string is null");
		}
		return commit;
	}
	
	public static ArrayList<Repository> parseRepositories(String json){
		ArrayList<Repository> list = new ArrayList<Repository>();
		if(json != null){
			try {
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
		}else{
			Log.e(logTag, "parseRepositories: json string is null");
		}
		return list;
	}
	
	public static User parseUser(String json){
		User user = null;
		if(json != null){
			try {
				JSONObject object = new JSONObject(json);
				user = new User(object.getString("login"));
			} catch (JSONException e) {
				Log.e(logTag, "Error in interpreting JSON");
				e.printStackTrace();
			}
		}else{
			Log.e(logTag, "parseUser: json string is null");
		}
		return user;
	}
	
}
