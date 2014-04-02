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

public class HttpConnection{
	
	private static final String logTag = "PrincePolo";
	public static String OAUTH_URL = "https://github.com/login/oauth/authorize";
    public static String OAUTH_ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
    public static String CALLBACK_URL = "princepolo://oauthresponse";//"http://localhost";
    
    /**
     * Will send a post to GitHub requesting the access_token. If found it will save it to the preferences.
     * 
     * @param code
     */
    public static synchronized void requestAccessToken(String code){
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
}
