package se.agile.activities;

import se.agile.asynctasks.RequestAccessToken;
import se.agile.asynctasks.RequestListener;
import se.agile.asynctasks.RequestRepositories;
import se.agile.asynctasks.RequestUser;
import se.agile.model.Preferences;
import se.agile.princepolo.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class StartActivity extends Activity implements RequestListener{
	private static String OAUTH_URL = "https://github.com/login/oauth/authorize";
    private static String CALLBACK_URL = "princepolo://oauthresponse";//"http://localhost";
    
	private String logTag;
	
	private RequestAccessToken accessTokenThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browser);
		
		logTag = getResources().getString(R.string.logtag_main);
		Log.d(logTag, "new intent started: testbrowseractivity");
		accessTokenThread = new RequestAccessToken(this);
		// Initializing shared preferences
		Preferences.initializePreferences(this);
		
		if (!Preferences.getAccessToken().equals("") )
		{
			Intent intent = new Intent(StartActivity.this, MainActivity.class);
    	    startActivity(intent);
    	    finish();
		}
		
		else
		{
			String url = OAUTH_URL + "?client_id=" + Preferences.getClientId() + "&redirect_uri=" + CALLBACK_URL;
			
			WebView webview = (WebView)findViewById(R.id.webview);
			webview.getSettings().setJavaScriptEnabled(true);
	        webview.setWebViewClient(new WebViewClient() {
	        	@Override
	            public void onPageFinished(WebView view, String url) {
	        		Log.d(logTag, "onPageFinished url: " + url);
	        		super.onPageFinished(view, url);
	            }
	
	            @Override
	            public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            	Log.d(logTag, "shouldOverrideUrlLoading url: " + url);
	            	return super.shouldOverrideUrlLoading(view, url);
	            }
	            private boolean isPostSent;
	        	@Override
	        	public void onPageStarted(WebView view, String url, Bitmap favicon) {
	                String accessCodeFragment = "code=";
	            	// We hijack the GET request to extract the OAuth parameters
	            	Log.d(logTag, "OnPageStarted url: " + url);
	            	synchronized (this) { //Seems to be threading calling the method since i got several HttpConnections running....
	            		if(url.matches("princepolo://oauthresponse\\?code=[\\dA-z]+") && !isPostSent) {
	    	        		String accessCode = url.split("code=")[1];
	    	        		Log.d(logTag, "accessCode: " + accessCode);
	    	        		accessTokenThread.execute(accessCode);
	                		isPostSent = true;
	    	        	}else{
	    	        		Log.d(logTag, "Url don't match \"princepolo://oauthresponse\\?code=[\\dA-z]+\"");
	    	        	}
					}
	            	if(isPostSent){
	        			Intent intent = new Intent(StartActivity.this, MainActivity.class);
	            	    startActivity(intent);
	            		finish();
	            	}
	        		
	            }});
	        webview.loadUrl(url);
		}
	}

	@Override
	public void requestFinished() {
		new RequestUser().execute();
		new RequestRepositories().execute();
	}
}
