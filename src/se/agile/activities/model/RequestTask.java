package se.agile.activities.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public abstract class RequestTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>{
	private final String logTag = "PrincePolo";
	
	private RequestListener listener;
	
	private Result result;
	
	public RequestTask(){
		this(null);
	}
	
	public RequestTask(RequestListener listener){
		this.listener = listener;
	}
	
	
	protected String generalGETRequest(String url) {
		
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
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
	
	public void finishedWithRequest(Result result){
		this.result = result;
		if(listener != null){
			listener.requestFinished();
		}
	}
	
	public Result getResult(){
		return this.result;
	}
}
