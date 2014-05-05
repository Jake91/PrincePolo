package se.agile.asynctasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import se.agile.model.Preferences;
import android.util.Log;

public class RequestAccessToken extends RequestTask<String, Void, String>{
	private static String OAUTH_ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
	private final String logTag = "PrincePolo";
	private static boolean isRequestingAccessToken;
	public RequestAccessToken(RequestListener listener){
		super(listener);
	}

	@Override
	protected String doInBackground(String... params) {
		if(!isCancelled()){
			isRequestingAccessToken = true;
			String code = params[0];	
			if(code == null || !code.matches("[\\dA-z]+")){
				Log.e(logTag, "parameter to HttpConnection don't contain 'code'");
				return null;
			}
			HttpClient client = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(OAUTH_ACCESS_TOKEN_URL);
			try {
				JSONObject holder = new JSONObject();
				try {
					holder.put("Accept", "application/json");
					holder.put("client_id", Preferences.getClientId());
					holder.put("client_secret", Preferences.getClientSecret());
					holder.put("code", code);
				} catch (JSONException e1) {
					Log.e(logTag,"Error creating JSON string");
					e1.printStackTrace();
					return null;
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
					return builder.toString();
				}else{
					Log.e(logTag, "Didn't get statuscode 200");
				}
			} catch (ClientProtocolException e) {
				Log.d(logTag, "Error in testBrowser2");
			} catch (IOException e) {
				Log.d(logTag, "Error in testBrowser3");
			}
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(String json){
		isRequestingAccessToken = false;
		if(!isCancelled()){
			String access_token = "";
			if(json != null){
				Log.d(logTag, "json= " + json);
				try {
					JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
					access_token = object.getString("access_token");
					Preferences.setAccessToken(access_token);
					Preferences.setTokenType(object.getString("token_type"));
					Preferences.setScope(object.getString("scope"));
					
				} catch (JSONException e) {
					Log.e(logTag, "Error in interpreting JSON");
					e.printStackTrace();
				}
			}
			finishedWithRequest(access_token);
		}
	}
	
	
	public static boolean isRequestingAccessToken(){
		return isRequestingAccessToken;
	}
}
