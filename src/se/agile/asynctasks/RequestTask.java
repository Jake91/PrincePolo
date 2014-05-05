package se.agile.asynctasks;

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
import org.apache.http.util.EntityUtils;

import se.agile.activities.MainActivity;
import se.agile.activities.model.GitHubData.Commit;
import se.agile.model.JSONParser;
import se.agile.model.Preferences;
import android.os.AsyncTask;
import android.util.Log;


/**
 * So this abstract class it to make our http request a bit "easier" to implement.
 * 
 * @author Jacob
 *
 * @param <Params>
 * @param <Progress>
 * @param <Result>
 */
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
	
	@Override
	protected void onPreExecute (){
		if(!MainActivity.isNetworkConnected()){
			Log.e(logTag, "Warning: No Internet Connection");
			if(listener != null){
				listener.whenNoInternetConnection();
				abortRequest(false);
			}
		}
	}
	
	/**
	 * General class for doing the GET http request to GitHub. It will automatically add the access_token.
	 * What you have to do is to parse the json string that is returned from the http request.
	 * 
	 * 
	 * @param url
	 * @return the respond (as a json string) or null if the responding message's statuscode wasn't "OK"
	 */
	protected String generalGETRequest(String url) {
		if(!isCancelled()){
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			Log.d(logTag,"generalGETRequest: url -> " + url);
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
				Log.e(logTag, "generalGETRequest: ClientProtocolException");
				e.printStackTrace();
			} catch (IOException e) {
				Log.e(logTag, "generalGETRequest: IOException");
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public void finishedWithRequest(Result result){
		if(result == null){
			Log.e(logTag, "RequestTask: Result is null!");
		}else{
			this.result = result;
			if(listener != null && !isCancelled()){
				listener.requestFinished();
			}
		}
	}
	
	public Result getResult(){
		if(result == null){
			Log.i(logTag, "RequestTask 'getResult' returned null");
		}
		return this.result;
	}
	
	/**
	 * Remove some references. But otherwise it is just like calling cancel(but better).
	 * You're recommended to set mayInterruptIfRunning = false; (if you don't want to, talk to Jacob....
	 * 
	 * @param mayInterruptIfRunning
	 * @return
	 */
	public boolean abortRequest(boolean mayInterruptIfRunning){
		//remove references so the garbagecollector can work....
		this.listener = null;
		this.result = null;
		return super.cancel(mayInterruptIfRunning);
	}
	
	public String getSelectedRepositoryName(){
		String repoName = Preferences.getSelectedRepository().getName();
		if(repoName.matches("[A-z0-9]+/[A-z0-9]+")){
			return repoName;
		}else{
			Log.e(logTag, "RequestBranch: Couldn't get selected repository");
			listener.whenNoSelectedRepository();
			abortRequest(false);
			return "";
			
		}
		
	}

}
