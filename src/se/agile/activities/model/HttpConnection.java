package se.agile.activities.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.AsyncTask;
import android.util.Log;

public class HttpConnection extends AsyncTask<String, Void, Void>{
	
	private static final String logTag = "gitIntegration";
	public static String OAUTH_URL = "https://github.com/login/oauth/authorize";
    public static String OAUTH_ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
    public static String CLIENT_ID = "387b05f90574b6fede43";
    public static String CLIENT_SECRET = "557392acf8c742ac6e6a3a4ff36b172f378c1633";
    public static String CALLBACK_URL = "princepolo://oauthresponse";//"http://localhost";

	@Override
	protected Void doInBackground(String... params) {
		Log.d(logTag, "Param to asynctask httpconnection: " + params[0]);
		HttpClient client = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(OAUTH_ACCESS_TOKEN_URL);
	    try {
	    	JSONObject holder = new JSONObject();
	    	try {
	    		holder.put("client_id", CLIENT_ID);
		    	holder.put("client_secret", CLIENT_SECRET);
				holder.put("code", params[0]);
			} catch (JSONException e1) {
				Log.d(logTag,"JSON ERROR");
				e1.printStackTrace();
			}
	    	Log.d(logTag, "Holder json: " + holder.toString());
            StringEntity se = new StringEntity( holder.toString());  
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httppost.setEntity(se);
	        Log.d(logTag, "client.execute");
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
		        	Log.d(logTag, "json result: ");
		        	JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
		        	String access_token = object.getString("access_token");
		        	Log.d(logTag, "json access_token: "+access_token);
		        	String token_type = object.getString("token_type");
		        	Log.d(logTag, "json token_type: " + token_type);
		        	String scope = object.getString("scope");
		        	Log.d(logTag, "json scope: "+ scope);
				} catch (JSONException e) {
					Log.d(logTag, "Error in JSON");
					e.printStackTrace();
				}
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
}
