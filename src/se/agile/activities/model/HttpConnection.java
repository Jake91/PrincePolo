package se.agile.activities.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import se.agile.activities.model.GitHubData.Branch;
import se.agile.activities.model.GitHubData.Repository;
import se.agile.activities.model.GitHubData.User;

import android.os.AsyncTask;
import android.util.Log;

public class HttpConnection{

	private static final String logTag = "PrincePolo";
	private static String OAUTH_URL = "https://github.com/login/oauth/authorize";
	private static String OAUTH_ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
	private static String CALLBACK_URL = "princepolo://oauthresponse";//"http://localhost";

	/**
	 * Will send a post to GitHub requesting the access_token. If found it will save it to the preferences.
	 * 
	 * @param code
	 */
	public static void requestAccessToken(String code){
		if(code == null || !code.matches("[\\dA-z]+")){
			Log.e(logTag, "parameter to HttpConnection don't contain 'code'");
			return;
		}
		HttpClient client = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(OAUTH_ACCESS_TOKEN_URL);
		try {
			JSONObject holder = new JSONObject();
			try {
				holder.put("client_id", Preferences.getClientId());
				holder.put("client_secret", Preferences.getClientSecret());
				holder.put("code", code);
			} catch (JSONException e1) {
				Log.e(logTag,"Error creating JSON string");
				e1.printStackTrace();
				return;
			}
			StringEntity se = new StringEntity( holder.toString());  
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			httppost.setEntity(se);
			HttpResponse response = client.execute(httppost);
			HttpEntity respEntity = response.getEntity();
			//	        Log.d(logTag, "response Entity Utils: " + EntityUtils.toString(respEntity));
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(respEntity.getContent(), "UTF-8"));
				StringBuilder builder = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				String json = builder.toString();
				Log.d(logTag, "json= " + json);
				try {
					JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
					String access_token = object.getString("access_token");
					Preferences.setAccessToken(access_token);
					String token_type = object.getString("token_type");
					Preferences.setTokenType(token_type);
					String scope = object.getString("scope");
					Preferences.setScope(scope);
				} catch (JSONException e) {
					Log.e(logTag, "Error in interpreting JSON");
					e.printStackTrace();
					return;
				}
			}else{
				Log.e(logTag, "Didn't get statuscode 200");
			}
		} catch (ClientProtocolException e) {
			Log.d(logTag, "Error in testBrowser2");
		} catch (IOException e) {
			Log.d(logTag, "Error in testBrowser3");
		}
	}

	public static enum URL {
		GET_REPOSITORIES("https://api.github.com/user/repos"),
		GET_USER("https://api.github.com/user"),
		GET_BRANCHES("https://api.github.com/repos/Jake91/PrincePolo/branches");
		
		private final String url;

		private URL(String url) {
			this.url = url;
		}

		private String getUrl() {
			return url;
		}
	}
	
	public static User requestUser(){
		String json = getRequestGeneral(URL.GET_USER);
		User user = JSONParser.parseUser(json);
		Preferences.setUser(user);
		
		fireResponseRecieved(URL.GET_USER, "Updated User");
		return user;
	}
	
	public static ArrayList<Repository> requestRepositories(){
		String json = getRequestGeneral(URL.GET_REPOSITORIES);
		ArrayList<Repository> repos = JSONParser.parseRepositories(json);
		Preferences.setRepositories(repos);
		
		fireResponseRecieved(URL.GET_REPOSITORIES, "Updated Repositories");
		return repos;
	}
	
	public static ArrayList<Branch> requestBranches(){
		String json = getRequestGeneral(URL.GET_BRANCHES);
		ArrayList<Branch> branches = JSONParser.parseBranches(json);
		//Not saving.....
		
		fireResponseRecieved(URL.GET_BRANCHES, "Updated Branches");
		return branches;
	}
	
	private static String getRequestGeneral(URL url){
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url.getUrl());
		try {
			httpGet.addHeader("Authorization", "token " + Preferences.getAccessToken());
			HttpResponse response = client.execute(httpGet);
			HttpEntity respEntity = response.getEntity();
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(respEntity.getContent(), "UTF-8"));
				StringBuilder builder = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				return builder.toString();
			}else{
				Log.e(logTag, "Didn't get statuscode 200");
			}
		} catch (ClientProtocolException e) {
			Log.d(logTag, "Error in testBrowser2");
		} catch (IOException e) {
			Log.d(logTag, "Error in testBrowser3");
		}
		return null;
	}
	
	private static ArrayList<HttpConnectionResponseListener> listenerList = new ArrayList<HttpConnectionResponseListener>();
	public static void addListener(HttpConnectionResponseListener listener){
		listenerList.add(listener);
	}
	
	public static boolean removeListener(HttpConnectionResponseListener listener){
		return listenerList.remove(listener);
	}
	
	private static void fireResponseRecieved(URL url, String response){
		for(HttpConnectionResponseListener listener : listenerList){
			listener.receivedResponse(url, response);
		}
	}
}
